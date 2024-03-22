package com.easypan.entity.query;

import java.util.Date;

public class EmailCodeQuery extends BaseParam {
	/**
	 * 邮箱
	 */
	public String email;

	public String emailFuzzy;
	/**
	 * 验证码
	 */
	public String code;

	public String codeFuzzy;
	/**
	 * 创建时间
	 */
	public Date createTime;

	public String createTimeStart;

	public String createTimeEnd;
	/**
	 * 0:未使用 1:已使用
	 */
	public Integer status;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmailFuzzy() {
		return emailFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getCodeFuzzy() {
		return codeFuzzy;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
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

}