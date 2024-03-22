package com.easypan.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.easypan.component.RedisComponent;
import com.easypan.config.WebConfig;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.dto.UserDto;
import com.easypan.entity.dto.UserSpaceDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.vo.FileInfoFolderVo;
import com.easypan.entity.vo.FileInfos;
import com.easypan.exception.BusinessException;
import com.easypan.mapper.UserInfoMapper;
import com.easypan.myEnum.*;
import com.easypan.util.CopyUtils;
import com.easypan.util.ProcessUtils;
import com.easypan.util.ScaleFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.vo.PaginationResultVo;
import com.easypan.service.FileInfoService;
import com.easypan.mapper.FileInfoMapper;
import com.easypan.entity.query.SimplePage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lemon
 * @date 2024-03-20 14:46
 * @desc 文件信息
 */

@Service("FileInfoService")
public class FileInfoServiceImpl implements FileInfoService {

    @Resource
    private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private WebConfig webConfig;

    @Resource
    @Lazy
    private FileInfoServiceImpl fileInfoService;

    /**
     * 根据条件查询列表
     */
    public List<FileInfo> findListByQuery(FileInfoQuery query) {
        return fileInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByQuery(FileInfoQuery query) {
        return fileInfoMapper.selectCount(query);

    }

    /**
     * 分页查询
     */
    public PaginationResultVo<FileInfo> findListByPage(FileInfoQuery query) {
        int count = this.findCountByQuery(query);
        int pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<FileInfo> list = this.findListByQuery(query);
        PaginationResultVo<FileInfo> result = new PaginationResultVo(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(FileInfo bean) {
        return fileInfoMapper.insert(bean);
    }

    /**
     * 批量新增对象
     */
    public Integer addBatch(List<FileInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return fileInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改对象
     */
    public Integer addOrUpdateBatch(List<FileInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return fileInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据FileIdAndUserId查询对象
     */
    public FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId) {
        return fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
    }

    /**
     * 根据FileIdAndUserId更新对象
     */
    public Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId) {
        return fileInfoMapper.updateByFileIdAndUserId(bean, fileId, userId);
    }

    /**
     * 根据FileIdAndUserId删除对象
     */
    public Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId) {
        return fileInfoMapper.deleteByFileIdAndUserId(fileId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadResultDto uploadFile(UserDto user, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex, Integer chunks) {
        UploadResultDto resultDto = new UploadResultDto();
        File tempFileFolder = null;
        Boolean uploadSuccess = true;
        try {
            if (StringUtils.isEmpty(fileId)) {
                fileId = RandomStringUtils.random(10, false, true);
            }
            resultDto.setFileId(fileId);
            Date curDate = new Date();
            UserSpaceDto spaceDto = redisComponent.getUserSpace(user.getUserId());

            if (chunkIndex == 0) {
                FileInfoQuery infoQuery = new FileInfoQuery();
                infoQuery.setFileMd5(fileMd5);
                infoQuery.setSimplePage(new SimplePage(0, 1));
                infoQuery.setStatus(FileStatusEnum.USING.getStatus());
                List<FileInfo> list = fileInfoMapper.selectList(infoQuery);

                // 秒传
                if (!list.isEmpty()) {
                    FileInfo dbFile = list.get(0);
                    if (dbFile.getFileSize() + spaceDto.getUseSpace() > spaceDto.getTotalSpace()) {
                        throw new BusinessException(ResponseCode.CODE_904);
                    }
                    dbFile.setFileId(fileId);
                    dbFile.setFilePid(filePid);
                    dbFile.setUserId(user.getUserId());
                    dbFile.setCreateTime(curDate);
                    dbFile.setStatus(FileStatusEnum.USING.getStatus());
                    dbFile.setDelFlag(FileDelFlagEnum.USING.getFlag());
                    dbFile.setFileMd5(fileMd5);
                    fileName = autoRename(filePid, user.getUserId(), fileName);
                    dbFile.setFileName(fileName);
                    resultDto.setStatus(UploadStatusEnum.UPLOAD_SECONDS.getCode());
                    fileInfoMapper.insert(dbFile);
                    updateUserSpace(user, dbFile.getFileSize());
                    return resultDto;
                }
            }

            Long currentTempSize = redisComponent.getFileTempSize(user.getUserId(), fileId);

            if (file.getSize() + currentTempSize + spaceDto.getUseSpace() > spaceDto.getTotalSpace()) {
                throw new BusinessException(ResponseCode.CODE_904);
            }
            String tempFolderName = webConfig.getFolder() + "file/temp/";
            String currentUserFolderName = user.getUserId() + fileId;

            tempFileFolder = new File(tempFolderName + currentUserFolderName);
            if (!tempFileFolder.exists()) {
                tempFileFolder.mkdirs();
            }
            File newFile = new File(tempFileFolder.getPath() + "/" + chunkIndex);
            file.transferTo(newFile);
            if (chunkIndex < chunks - 1) {
                resultDto.setStatus(UploadStatusEnum.UPLOADING.getCode());
                redisComponent.saveFileTempSize(user.getUserId(), fileId, file.getSize());
                return resultDto;
            }
            redisComponent.saveFileTempSize(user.getUserId(), fileId, file.getSize());

            String month = DateFormatUtils.format(new Date(), "YYYYMM");
            String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            String realFileName = currentUserFolderName + suffix;
            FileTypeEnum fileTypeEnum = FileTypeEnum.getFileTypeBySuffix(suffix);

            fileName = autoRename(filePid, user.getUserId(), fileName);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setUserId(user.getUserId());
            fileInfo.setFileMd5(fileMd5);
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(month + "/" + realFileName);
            fileInfo.setFilePid(filePid);
            fileInfo.setCreateTime(curDate);
            fileInfo.setLastUpdateTime(curDate);
            fileInfo.setFileCategory(fileTypeEnum.getCategory().getCategory());
            fileInfo.setFileType(FileStatusEnum.TRANSFER.getStatus());
            fileInfo.setFolderType(FileFolderTypeEnum.FILE.getType());
            fileInfo.setDelFlag(FileDelFlagEnum.USING.getFlag());

            fileInfoMapper.insert(fileInfo);

            Long fileTempSize = redisComponent.getFileTempSize(user.getUserId(), fileId);
            updateUserSpace(user, fileTempSize);

            resultDto.setStatus(UploadStatusEnum.UPLOAD_FINISH.getCode());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    fileInfoService.transferFile(fileInfo.getFileId(), user);
                }
            });

            fileInfoService.transferFile(fileId, user);


        } catch (BusinessException e) {
            uploadSuccess = false;
            System.out.println(e);
            throw e;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (!uploadSuccess && tempFileFolder != null) {
                try {
                    FileUtils.deleteDirectory(tempFileFolder);
                } catch (IOException e) {
                    System.out.println("删除临时文件失败");
                }
            }
        }
        return resultDto;
    }

    private String autoRename(String filePid, String userId, String fileName) {
        FileInfoQuery fileInfoQuery = new FileInfoQuery();
        fileInfoQuery.setUserId(userId);
        fileInfoQuery.setFilePid(filePid);
        fileInfoQuery.setDelFlag(FileDelFlagEnum.USING.getFlag());
        fileInfoQuery.setFileName(fileName);

        Integer count = fileInfoMapper.selectCount(fileInfoQuery);
        if (count > 0) {
            fileName = com.easypan.util.StringUtils.rename(fileName);
        }
        return fileName;
    }

    private void updateUserSpace(UserDto userDto, Long useSpace) {
        Integer count = userInfoMapper.updateUserSpace(userDto.getUserId(), useSpace, null);
        if (count == 0) {
            throw new BusinessException(ResponseCode.CODE_904);
        }
        UserSpaceDto userSpace = redisComponent.getUserSpace(userDto.getUserId());
        userSpace.setUseSpace(userSpace.getUseSpace() + useSpace);

        redisComponent.saveUserSpace(userDto.getUserId(), userSpace);
    }

    @Async
    public void transferFile(String fileId, UserDto userDto) {
        Boolean transferSuccess = true;
        String targetFilePath = null;
        String cover = null;
        FileTypeEnum fileTypeEnum = null;
        FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, userDto.getUserId());

        try {
            if (fileInfo == null || !FileStatusEnum.TRANSFER.getStatus().equals(fileInfo.getStatus())) {
                return;
            }
            String tempFolderName = webConfig.getFolder() + "file/temp/";
            String currentUserFolderName = userDto.getUserId() + fileId;
            File fileFolder = new File(tempFolderName + currentUserFolderName);

            String fileName = fileInfo.getFileName();
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            String month = DateFormatUtils.format(fileInfo.getCreateTime(), "YYYYMM");

            String targetFolderName = webConfig.getFolder() + "file/";
            File targetFolder = new File(targetFolderName + month);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            String realFileName = currentUserFolderName + fileSuffix;
            targetFilePath = targetFolder.getPath() + "/" + realFileName;

            // 合并文件
            union(fileInfo.getFilePath(), targetFilePath, fileInfo.getFileName(), true);

            // 视频文件切割
            fileTypeEnum = FileTypeEnum.getFileTypeBySuffix(fileSuffix);
            if (FileTypeEnum.VIDEO == fileTypeEnum) {
                cutFile4Video(fileId, targetFilePath);
                // 视频生成缩略图
                cover = month + "/" + currentUserFolderName + ".png";
                String coverPath = targetFolderName + "/" + cover;

                ScaleFilter.createCover4Video(new File(targetFilePath), 150, new File(coverPath));
            } else if (FileTypeEnum.IMAGE == fileTypeEnum) {
                cover = month + "/" + realFileName.replace(".", "_.");
                String coverPath = targetFolderName + "/" + cover;
                Boolean created = ScaleFilter.createThumbnailWidthFFmpeg(new File(targetFilePath), 150, new File(coverPath), false);
                if (!created) {
                    FileUtils.copyFile(new File(targetFilePath), new File(coverPath));
                }
            }


        } catch (Exception e) {
            transferSuccess = false;
        } finally {
            FileInfo fileInfo1 = new FileInfo();
            fileInfo1.setFileSize(new File(targetFilePath).length());
            fileInfo1.setFileCover(cover);
            fileInfo1.setStatus(transferSuccess ? FileStatusEnum.USING.getStatus() : FileStatusEnum.TRANSFER_FAIL.getStatus());

            fileInfoMapper.updateFileStatusWithOldStatus(fileId, userDto.getUserId(), fileInfo1, FileStatusEnum.TRANSFER.getStatus());
        }
    }

    private void union(String dirPath, String toFilePath, String fileName, Boolean delSource) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new BusinessException("目录不存在");
        }
        File[] files = dir.listFiles();
        File targetFile = new File(toFilePath);

        RandomAccessFile writeFile = null;
        try {
            writeFile = new RandomAccessFile(targetFile, "rw");
            byte[] b = new byte[1024 * 10];
            for (int i = 0; i < files.length; i++) {
                int len = -1;
                File chunkFile = new File(dirPath + "/" + i);
                RandomAccessFile readFile = null;
                try {
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(b)) != -1) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    throw new BusinessException("合并分片失败");
                } finally {
                    readFile.close();
                }
            }
        } catch (Exception e) {
            throw new BusinessException("合并文件失败");
        } finally {
            if (writeFile != null) {
                try {
                    writeFile.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (delSource && dir.exists()) {
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void cutFile4Video(String fileId, String videoFilePath) {
        // 创建同名切片目录
        File tsFolder = new File(videoFilePath.substring(0, videoFilePath.lastIndexOf(".")));
        if (!tsFolder.exists()) {
            tsFolder.mkdirs();
        }
        final String CMD_TRANSFER_2TS = "ffmpeg -y -i %s -vcodec copy -acoder copy h264_mp4toannexb %s";
        final String CMD_CUT_TS = "ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 30 %s/%s_%%4d.ts";
        String tsPath = tsFolder + "/" + "index.ts";
        //生成.ts文件
        String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
        ProcessUtils.executeCommand(cmd, false);
        // 生成索引文件.m3u8和切片.ts
        cmd = String.format(CMD_CUT_TS, tsPath, tsFolder.getPath() + "/" + "index.m3u8", tsFolder.getPath(), fileId);

        ProcessUtils.executeCommand(cmd, false);
        //删除index.tx
        new File(tsPath).delete();
    }

    @Override
    public FileInfo newFolder(String filePid, String fileName, String userId) {
        checkFileName(filePid, userId, fileName, FileFolderTypeEnum.FOLDER.getType());
        FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(filePid, userId);
        if (fileInfo == null) {
            throw new BusinessException("父文件不存在");
        }
        FileInfo newFileFolder = new FileInfo();
        newFileFolder.setFilePid(RandomStringUtils.random(10, false, true));
        newFileFolder.setFileName(fileName);
        newFileFolder.setUserId(userId);
        newFileFolder.setFolderType(FileFolderTypeEnum.FOLDER.getType());
        newFileFolder.setFilePid(filePid);
        newFileFolder.setCreateTime(new Date());
        newFileFolder.setStatus(FileStatusEnum.USING.getStatus());
        newFileFolder.setDelFlag(FileDelFlagEnum.USING.getFlag());

        fileInfoMapper.insert(newFileFolder);

        return fileInfo;
    }

    private void checkFileName(String filePid, String userId, String fileName, Integer folderType) {
        FileInfoQuery fileInfoQuery = new FileInfoQuery();
        fileInfoQuery.setFilePid(filePid);
        fileInfoQuery.setUserId(userId);
        fileInfoQuery.setFileName(fileName);
        fileInfoQuery.setFileType(folderType);

        Integer count = fileInfoMapper.selectCount(fileInfoQuery);
        if (count > 0) {
            throw new BusinessException("此目录下已存在同名目录");
        }
    }

    @Override
    public List<FileInfoFolderVo> getFolderInfo(String path, String userId) {

        String[] pathSplit = path.split("/");

        FileInfoQuery fileInfoQuery = new FileInfoQuery();
        fileInfoQuery.setFileIdArray(pathSplit);
        fileInfoQuery.setUserId(userId);
        fileInfoQuery.setFileType(FileFolderTypeEnum.FOLDER.getType());
        String orderBy = "field(file_id,\"" + StringUtils.join(pathSplit, "\",\"") + "\")";

        fileInfoQuery.setOrderBy(orderBy);

        List<FileInfo> list = fileInfoMapper.selectList(fileInfoQuery);

        return CopyUtils.copyList(list, FileInfoFolderVo.class);

//        for (String s : pathSplit) {
//            FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(s, userId);
//            if (fileInfo == null) {
//                throw new BusinessException("文件不存在");
//            }
//            FileInfoFolderVo folderVo = CopyUtils.copy(fileInfo, FileInfoFolderVo.class);
//            folderVos.add(folderVo);
//        }
//        return folderVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo rename(String fileId, String fileName, String userId) {
        FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
        if (fileInfo == null) {
            throw new BusinessException("文件不存在");
        }
        checkFileName(fileInfo.getFilePid(), userId, fileName, fileInfo.getFolderType());
        if (fileInfo.getFileType().equals(FileFolderTypeEnum.FILE.getType())) {
            fileName = fileName + fileInfo.getFileName().substring(0, fileInfo.getFileName().lastIndexOf("."));
        }

        fileInfoMapper.updateByFileIdAndUserId(fileInfo, fileId, userId);

        FileInfoQuery fileInfoQuery = new FileInfoQuery();
        fileInfoQuery.setFilePid(fileInfo.getFilePid());
        fileInfoQuery.setUserId(userId);
        fileInfoQuery.setFileName(fileName);
        Integer count = fileInfoMapper.selectCount(fileInfoQuery);
        if(count>1){
            throw new BusinessException("文件名"+fileName+"已经存在");
        }

        fileInfo.setLastUpdateTime(new Date());
        fileInfo.setFileName(fileName);
        return fileInfo;
    }

    @Override
    public List<FileInfos> loadAllFolder(String filePid, String currentFileIds, String userId) {
        List<FileInfos> fileInfos = new ArrayList<>();
        FileInfoQuery query = new FileInfoQuery();
        query.setFilePid(filePid);
        query.setUserId(userId);
        List<FileInfo> list = fileInfoMapper.selectList(query);

        if (list.size() == 0) {
            throw new BusinessException("没有此子文件");
        }
        for (FileInfo fileInfo : list) {
            fileInfos.add(CopyUtils.copy(fileInfo, FileInfos.class));
        }
        return fileInfos;
    }

    @Override
    public void changeFileFolder(String filePid, String fileIds, String userId) {
        String[] fileIdS = fileIds.split(",");
        for (String fileId : fileIdS) {
            FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
            if (fileInfo == null) {
                throw new BusinessException("文件不存在");
            }
            if (fileInfo.getStatus().equals(FileStatusEnum.DEL.getStatus())) {
                throw new BusinessException("文件已删除");
            }
            fileInfo.setFilePid(filePid);

            fileInfoMapper.updateByFileIdAndUserId(fileInfo, fileId, userId);
        }
    }
}