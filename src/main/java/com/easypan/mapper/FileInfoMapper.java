package com.easypan.mapper;

import org.apache.ibatis.annotations.Param;
/**
 * @author lemon
 * @date 2024-03-20 14:46
 * @desc 文件信息
 */
public interface FileInfoMapper<T, P> extends BaseMapper {
	/**
	 * 根据FileIdAndUserId查询
	 */
	T selectByFileIdAndUserId(@Param("fileId") String fileId,@Param("userId") String userId);

	/**
	 * 根据FileIdAndUserId更新
	 */
	Integer updateByFileIdAndUserId(@Param("bean") T t, @Param("fileId") String fileId,@Param("userId") String userId);

	/**
	 * 根据FileIdAndUserId删除
	 */
	Integer deleteByFileIdAndUserId(@Param("fileId") String fileId,@Param("userId") String userId);

    void updateFileStatusWithOldStatus(String fileId, String userId, @Param("bean") T fileInfo1, Integer status);
}