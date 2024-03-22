package com.easypan.entity.query;

import java.util.Date;

public class FileInfoQuery extends BaseParam {
	/**
	 * 文件id
	 */
	public String fileId;

	public String fileIdFuzzy;
	/**
	 * 用户id
	 */
	public String userId;

	public String userIdFuzzy;
	/**
	 * 文件md5实现秒传
	 */
	public String fileMd5;

	public String fileMd5Fuzzy;
	/**
	 * 文件父id
	 */
	public String filePid;

	public String filePidFuzzy;
	/**
	 * 文件大小
	 */
	public Long fileSize;
	/**
	 * 文件名字
	 */
	public String fileName;

	public String fileNameFuzzy;
	/**
	 * 文件封面
	 */
	public String fileCover;

	public String fileCoverFuzzy;
	/**
	 * 文件路径
	 */
	public String filePath;

	public String filePathFuzzy;
	/**
	 * 创建时间
	 */
	public Date createTime;

	public String createTimeStart;

	public String createTimeEnd;
	/**
	 * 最后更新时间
	 */
	public Date lastUpdateTime;

	public String lastUpdateTimeStart;

	public String lastUpdateTimeEnd;
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
	public Date recoverTime;

	public String recoverTimeStart;

	public String recoverTimeEnd;
	/**
	 * 0:删除 1:回收站 2:正常
	 */
	public Integer delFlag;

	private String[] fileIdArray;

	public String[] getFileIdArray() {
		return fileIdArray;
	}

	public void setFileIdArray(String[] fileIdArray) {
		this.fileIdArray = fileIdArray;
	}

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

	public String getFileIdFuzzy() {
		return fileIdFuzzy;
	}

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getFileMd5Fuzzy() {
		return fileMd5Fuzzy;
	}

	public void setFileMd5Fuzzy(String fileMd5Fuzzy) {
		this.fileMd5Fuzzy = fileMd5Fuzzy;
	}

	public String getFilePidFuzzy() {
		return filePidFuzzy;
	}

	public void setFilePidFuzzy(String filePidFuzzy) {
		this.filePidFuzzy = filePidFuzzy;
	}

	public String getFileNameFuzzy() {
		return fileNameFuzzy;
	}

	public void setFileNameFuzzy(String fileNameFuzzy) {
		this.fileNameFuzzy = fileNameFuzzy;
	}

	public String getFileCoverFuzzy() {
		return fileCoverFuzzy;
	}

	public void setFileCoverFuzzy(String fileCoverFuzzy) {
		this.fileCoverFuzzy = fileCoverFuzzy;
	}

	public String getFilePathFuzzy() {
		return filePathFuzzy;
	}

	public void setFilePathFuzzy(String filePathFuzzy) {
		this.filePathFuzzy = filePathFuzzy;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getLastUpdateTimeStart() {
		return lastUpdateTimeStart;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public String getLastUpdateTimeEnd() {
		return lastUpdateTimeEnd;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

	public String getRecoverTimeStart() {
		return recoverTimeStart;
	}

	public void setRecoverTimeStart(String recoverTimeStart) {
		this.recoverTimeStart = recoverTimeStart;
	}

	public String getRecoverTimeEnd() {
		return recoverTimeEnd;
	}

	public void setRecoverTimeEnd(String recoverTimeEnd) {
		this.recoverTimeEnd = recoverTimeEnd;
	}

}