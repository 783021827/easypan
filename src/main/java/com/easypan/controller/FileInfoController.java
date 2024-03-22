package com.easypan.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.dto.UserDto;
import com.easypan.entity.vo.FileInfoFolderVo;
import com.easypan.entity.vo.FileInfos;
import com.easypan.myEnum.FileCategoryEnum;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.service.FileInfoService;
import com.easypan.entity.vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lemon
 * @date 2024-03-20 14:46
 * @desc 文件信息
 */

@RestController
@RequestMapping("file")
public class FileInfoController extends ABaseController {

    @Resource
    private FileInfoService fileInfoService;

    /**
     * 根据条件分页查询列表
     */
    @RequestMapping("loadDataList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo loadDataList(@VerifyParam(required = true) String category,
                                   @VerifyParam(required = true) String filePid,
                                   FileInfoQuery query) {
        FileCategoryEnum fileCategoryEnum = FileCategoryEnum.getByCode(category);
        if (fileCategoryEnum != null) {
            query.setFileCategory(fileCategoryEnum.getCategory());
        }

        return getSuccessResponseVo(fileInfoService.findListByPage(query));
    }

    @RequestMapping("uploadFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo upload(HttpSession session,
                             String fileId,
                             MultipartFile file,
                             @VerifyParam(required = true) String fileName,
                             @VerifyParam(required = true) String filePid,
                             @VerifyParam(required = true) String fileMd5,
                             @VerifyParam(required = true) Integer chunkIndex,
                             @VerifyParam(required = true) Integer chunks) {
        UserDto user = (UserDto) session.getAttribute("user");
		UploadResultDto uploadResultDto = fileInfoService.uploadFile(user,fileId,file,fileName,filePid,fileMd5,chunkIndex,chunks);

        return getSuccessResponseVo(uploadResultDto);
    }

    @RequestMapping("getImage/{imageFolder}/{imageName}")
    @GlobalInterceptor(checkParams = true)
    public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder,@PathVariable("imageName") String imageName){
        getImage(response,imageFolder,imageName);
    }

    @RequestMapping("ts/getVideoInfo/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public void getImage(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("fileId") String fileId){
        UserDto user = (UserDto) session.getAttribute("user");
        getFile(response,user.getUserId(),fileId);
    }
    @RequestMapping("getFile/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public void getFile(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("fileId") String fileId){
        UserDto user = (UserDto) session.getAttribute("user");
        super.getFile(response,user.getUserId(),fileId);
    }

    @RequestMapping("newFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo newFolder(HttpSession session,
                                @VerifyParam(required = true) String filePid,
                                @VerifyParam(required = true) String fileName){
        UserDto user = (UserDto) session.getAttribute("user");
        FileInfo fileInfo = fileInfoService.newFolder(filePid,fileName,user.getUserId());
        return getSuccessResponseVo(fileInfo);
    }

    @RequestMapping("getFolderInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo getFolderInfo(HttpSession session,@VerifyParam(required = true) String path){
        UserDto user = (UserDto) session.getAttribute("user");
        List<FileInfoFolderVo> fileInfoFolderVos = fileInfoService.getFolderInfo(path,user.getUserId());
        return getSuccessResponseVo(fileInfoFolderVos);
    }

    @RequestMapping("rename")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo rename(HttpSession session,
                             @VerifyParam(required = true) String fileId,
                             @VerifyParam(required = true) String fileName){
        UserDto user = (UserDto) session.getAttribute("user");
        FileInfo fileInfo = fileInfoService.rename(fileId,fileName,user.getUserId());
        return getSuccessResponseVo(fileInfo);
    }

    @RequestMapping("loadAllFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo loadAllFolder(HttpSession session,
                                    @VerifyParam(required = true) String filePid,
                                    String currentFileIds){
        UserDto user = (UserDto) session.getAttribute("user");
        List<FileInfos> fileInfos = fileInfoService.loadAllFolder(filePid,currentFileIds,user.getUserId());
        return getSuccessResponseVo(fileInfos);
    }

    @RequestMapping("changeFileFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo changeFileFolder(HttpSession session,
                                       @VerifyParam(required = true) String fileIds,
                                       @VerifyParam(required = true) String filePid){
        UserDto user = (UserDto) session.getAttribute("user");
        fileInfoService.changeFileFolder(filePid,fileIds,user.getUserId());
        return getSuccessResponseVo(null);
    }



}