package com.easypan.service;

import java.util.List;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.vo.PaginationResultVo;
/**
 * @author lemon
 * @date 2024-03-17 19:01
 * @desc 用户信息
 */
public interface UserInfoService {

	/**
	 * 根据条件查询列表
	 */
	public List<UserInfo> findListByQuery(UserInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(UserInfoQuery query);

	/**
	 * 分页查询
	 */
	public PaginationResultVo<UserInfo> findListByPage(UserInfoQuery query);

	/**
	 * 新增
	 */
	public Integer add(UserInfo bean);

	/**
	 * 批量新增对象
	 */
	public Integer addBatch(List<UserInfo> listBean);

	/**
	 * 批量新增/修改对象
	 */
	public Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 * 根据UserId查询对象
	 */
	public UserInfo getUserInfoByUserId(String userId);

	/**
	 * 根据UserId更新对象
	 */
	public Integer updateUserInfoByUserId(UserInfo bean, String userId);

	/**
	 * 根据UserId删除对象
	 */
	public Integer deleteUserInfoByUserId(String userId);


	/**
	 * 根据Email查询对象
	 */
	public UserInfo getUserInfoByEmail(String email);

	/**
	 * 根据Email更新对象
	 */
	public Integer updateUserInfoByEmail(UserInfo bean, String email);

	/**
	 * 根据Email删除对象
	 */
	public Integer deleteUserInfoByEmail(String email);


	/**
	 * 根据QqOpenId查询对象
	 */
	public UserInfo getUserInfoByQqOpenId(String qqOpenId);

	/**
	 * 根据QqOpenId更新对象
	 */
	public Integer updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId);

	/**
	 * 根据QqOpenId删除对象
	 */
	public Integer deleteUserInfoByQqOpenId(String qqOpenId);


	/**
	 * 根据NickName查询对象
	 */
	public UserInfo getUserInfoByNickName(String nickName);

	/**
	 * 根据NickName更新对象
	 */
	public Integer updateUserInfoByNickName(UserInfo bean, String nickName);

	/**
	 * 根据NickName删除对象
	 */
	public Integer deleteUserInfoByNickName(String nickName);

	public void sendEmailCode(String email,Integer type);


	void register(String email, String nickName, String password, String emailCode);

	UserInfo login(String email, String password);

    void resetPwd(String email, String password, String emailCode);


}