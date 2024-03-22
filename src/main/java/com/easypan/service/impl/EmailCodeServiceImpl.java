package com.easypan.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.query.EmailCodeQuery;
import com.easypan.entity.vo.PaginationResultVo;
import com.easypan.service.EmailCodeService;
import com.easypan.mapper.EmailCodeMapper;
import com.easypan.entity.query.SimplePage;
import com.easypan.myEnum.PageSize;

/**
 * @author lemon
 * @date 2024-03-18 13:57
 * @desc 邮箱验证
 */

@Service("EmailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService{

	@Resource
	private EmailCodeMapper<EmailCode,EmailCodeQuery> emailCodeMapper;
	/**
	 * 根据条件查询列表
	 */
	public List<EmailCode> findListByQuery(EmailCodeQuery query){
		return emailCodeMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(EmailCodeQuery query){
		return emailCodeMapper.selectCount(query);

	}

	/**
	 * 分页查询
	 */
	public PaginationResultVo<EmailCode> findListByPage(EmailCodeQuery query){
		int count = this.findCountByQuery(query);
		int pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();

		SimplePage page = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<EmailCode> list = this.findListByQuery(query);
		PaginationResultVo<EmailCode> result = new PaginationResultVo(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(EmailCode bean){
		return emailCodeMapper.insert(bean);
	}

	/**
	 * 批量新增对象
	 */
	public Integer addBatch(List<EmailCode> listBean){
		if(listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return emailCodeMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改对象
	 */
	public Integer addOrUpdateBatch(List<EmailCode> listBean){
		if(listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return emailCodeMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据EmailAndCode查询对象
	 */
	public EmailCode getEmailCodeByEmailAndCode(String email,String code){
		return emailCodeMapper.selectByEmailAndCode(email, code);
	}

	/**
	 * 根据EmailAndCode更新对象
	 */
	public Integer updateEmailCodeByEmailAndCode(EmailCode bean,String email,String code){
		return emailCodeMapper.updateByEmailAndCode(bean ,email, code);
	}

	/**
	 * 根据EmailAndCode删除对象
	 */
	public Integer deleteEmailCodeByEmailAndCode(String email,String code){
		return emailCodeMapper.deleteByEmailAndCode(email, code);
	}

}