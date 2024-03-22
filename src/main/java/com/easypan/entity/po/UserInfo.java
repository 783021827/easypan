package com.easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lemon
 * @date 2024-03-17 19:01
 * @desc 用户信息
 */
public class UserInfo implements Serializable {
	/**
	 * 用户id
	 */
	public String userId;
	/**
	 * 用户昵称
	 */
	public String nickName;
	/**
	 * 用户邮箱
	 */
	public String email;
	/**
	 * qqOpenId
	 */
	public String qqOpenId;
	/**
	 * qq头像
	 */
	public String qqAvatar;
	/**
	 * qq密码
	 */
	public String password;
	/**
	 * 注册时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date joinTime;
	/**
	 * 最后一次登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date lastLoginTime;
	/**
	 * 用户已经使用的空间
	 */
	public Long useSpace;
	/**
	 * 用户总空间
	 */
	public Long totalSpace;
	/**
	 * 0:禁用 1:启用
	 */
	public Integer status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getQqAvatar() {
		return qqAvatar;
	}

	public void setQqAvatar(String qqAvatar) {
		this.qqAvatar = qqAvatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Long getUseSpace() {
		return useSpace;
	}

	public void setUseSpace(Long useSpace) {
		this.useSpace = useSpace;
	}

	public Long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(Long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString(){
		return "UserInfo{" +
				"userId = " + userId + 
				", nickName = " + nickName + 
				", email = " + email + 
				", qqOpenId = " + qqOpenId + 
				", qqAvatar = " + qqAvatar + 
				", password = " + password + 
				", joinTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(joinTime) + 
				", lastLoginTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastLoginTime) + 
				", useSpace = " + useSpace + 
				", totalSpace = " + totalSpace + 
				", status = " + status + 
				'}';
	}
}