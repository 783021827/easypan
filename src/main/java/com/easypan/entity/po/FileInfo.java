package com.easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lemon
 * @date 2024-03-20 14:46
 * @desc 文件信息
 */
public class FileInfo implements Serializable {
	/**
	 * 文件id
	 */
	public String fileId;
	/**
	 * 用户id
	 */
	public String userId;
	/**
	 * 文件md5实现秒传
	 */
	public String fileMd5;
	/**
	 * 文件父id
	 */
	public String filePid;
	/**
	 * 文件大小
	 */
	public Long fileSize;
	/**
	 * 文件名字
	 */
	public String fileName;
	/**
	 * 文件封面
	 */
	public String fileCover;
	/**
	 * 文件路径
	 */
	public String filePath;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date createTime;
	/**
	 * 最后更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date lastUpdateTime;
	/**
	 * 0:文件 1:目录
	 */
	public Integer folderType;
	/**
	 * 1:视频 2:音频 3:图片 4:文档 5:其他
	 */
	public Integer fileCategory;
	/**
	 * 1:视频 2:音频 3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他
	 */
	public Integer fileType;
	/**
	 * 0:转码中 1:转码失败 2:转码成功
	 */
	public Integer status;
	/**
	 * 进入回收站的时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date recoverTime;
	/**
	 * 0:删除 1:回收站 2:正常
	 */
	public Integer delFlag;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getFilePid() {
		return filePid;
	}

	public void setFilePid(String filePid) {
		this.filePid = filePid;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileCover() {
		return fileCover;
	}

	public void setFileCover(String fileCover) {
		this.fileCover = fileCover;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getFolderType() {
		return folderType;
	}

	public void setFolderType(Integer folderType) {
		this.folderType = folderType;
	}

	public Integer getFileCategory() {
		return fileCategory;
	}

	public void setFileCategory(Integer fileCategory) {
		this.fileCategory = fileCategory;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString(){
		return "FileInfo{" +
				"fileId = " + fileId + 
				", userId = " + userId + 
				", fileMd5 = " + fileMd5 + 
				", filePid = " + filePid + 
				", fileSize = " + fileSize + 
				", fileName = " + fileName + 
				", fileCover = " + fileCover + 
				", filePath = " + filePath + 
				", createTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime) + 
				", lastUpdateTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdateTime) + 
				", folderType = " + folderType + 
				", fileCategory = " + fileCategory + 
				", fileType = " + fileType + 
				", status = " + status + 
				", recoverTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recoverTime) + 
				", delFlag = " + delFlag + 
				'}';
	}
}