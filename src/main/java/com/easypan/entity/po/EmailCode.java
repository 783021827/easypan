package com.easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lemon
 * @date 2024-03-18 13:57
 * @desc 邮箱验证
 */
public class EmailCode implements Serializable {
	/**
	 * 邮箱
	 */
	public String email;
	/**
	 * 验证码
	 */
	public String code;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date createTime;
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

	@Override
	public String toString(){
		return "EmailCode{" +
				"email = " + email + 
				", code = " + code + 
				", createTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime) + 
				", status = " + status + 
				'}';
	}
}