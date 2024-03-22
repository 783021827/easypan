package com.easypan.mapper;

import org.apache.ibatis.annotations.Param;
/**
 * @author lemon
 * @date 2024-03-18 13:57
 * @desc 邮箱验证
 */
public interface EmailCodeMapper<T, P> extends BaseMapper {
	/**
	 * 根据EmailAndCode查询
	 */
	T selectByEmailAndCode(@Param("email") String email,@Param("code") String code);

	/**
	 * 根据EmailAndCode更新
	 */
	Integer updateByEmailAndCode(@Param("bean") T t, @Param("email") String email,@Param("code") String code);

	/**
	 * 根据EmailAndCode删除
	 */
	Integer deleteByEmailAndCode(@Param("email") String email,@Param("code") String code);

    void disableEmailCode(String email);
}