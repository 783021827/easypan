package com.easypan.service;

import java.util.List;

import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.dto.UserDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.vo.FileInfoFolderVo;
import com.easypan.entity.vo.FileInfos;
import com.easypan.entity.vo.PaginationResultVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lemon
 * @date 2024-03-20 14:46
 * @desc 文件信息
 */
public interface FileInfoService {

	/**
	 * 根据条件查询列表
	 */
	public List<FileInfo> findListByQuery(FileInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(FileInfoQuery query);

	/**
	 * 分页查询
	 */
	public PaginationResultVo<FileInfo> findListByPage(FileInfoQuery query);

	/**
	 * 新增
	 */
	public Integer add(FileInfo bean);

	/**
	 * 批量新增对象
	 */
	public Integer addBatch(List<FileInfo> listBean);

	/**
	 * 批量新增/修改对象
	 */
	public Integer addOrUpdateBatch(List<FileInfo> listBean);

	/**
	 * 根据FileIdAndUserId查询对象
	 */
	public FileInfo getFileInfoByFileIdAndUserId(String fileId,String userId);

	/**
	 * 根据FileIdAndUserId更新对象
	 */
	public Integer updateFileInfoByFileIdAndUserId(FileInfo bean,String fileId,String userId);

	/**
	 * 根据FileIdAndUserId删除对象
	 */
	public Integer deleteFileInfoByFileIdAndUserId(String fileId,String userId);

	UploadResultDto uploadFile(UserDto user, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex, Integer chunks);

	FileInfo newFolder(String filePid, String fileName,String user);

	List<FileInfoFolderVo> getFolderInfo(String path,String userId);

	FileInfo rename(String fileId, String fileName, String userId);

	List<FileInfos> loadAllFolder(String filePid, String currentFileIds, String userId);

	void changeFileFolder(String filePid, String fileIds, String userId);
}