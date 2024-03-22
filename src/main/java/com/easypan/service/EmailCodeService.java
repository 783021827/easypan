package com.easypan.service;

import java.util.List;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.query.EmailCodeQuery;
import com.easypan.entity.vo.PaginationResultVo;
/**
 * @author lemon
 * @date 2024-03-18 13:57
 * @desc 邮箱验证
 */
public interface EmailCodeService {

	/**
	 * 根据条件查询列表
	 */
	public List<EmailCode> findListByQuery(EmailCodeQuery query);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(EmailCodeQuery query);

	/**
	 * 分页查询
	 */
	public PaginationResultVo<EmailCode> findListByPage(EmailCodeQuery query);

	/**
	 * 新增
	 */
	public Integer add(EmailCode bean);

	/**
	 * 批量新增对象
	 */
	public Integer addBatch(List<EmailCode> listBean);

	/**
	 * 批量新增/修改对象
	 */
	public Integer addOrUpdateBatch(List<EmailCode> listBean);

	/**
	 * 根据EmailAndCode查询对象
	 */
	public EmailCode getEmailCodeByEmailAndCode(String email,String code);

	/**
	 * 根据EmailAndCode更新对象
	 */
	public Integer updateEmailCodeByEmailAndCode(EmailCode bean,String email,String code);

	/**
	 * 根据EmailAndCode删除对象
	 */
	public Integer deleteEmailCodeByEmailAndCode(String email,String code);

}