package com.easypan.service.impl;

import java.util.Date;
import java.util.List;

import com.easypan.component.RedisComponent;
import com.easypan.config.WebConfig;
import com.easypan.entity.dto.UserSpaceDto;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.query.EmailCodeQuery;
import com.easypan.exception.BusinessException;
import com.easypan.mapper.EmailCodeMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.vo.PaginationResultVo;
import com.easypan.service.UserInfoService;
import com.easypan.mapper.UserInfoMapper;
import com.easypan.entity.query.SimplePage;
import com.easypan.myEnum.PageSize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;

/**
 * @author lemon
 * @date 2024-03-17 19:01
 * @desc 用户信息
 */

@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    // 先注入 javaMailSender
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private WebConfig webConfig;

    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    public List<UserInfo> findListByQuery(UserInfoQuery query) {
        return userInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByQuery(UserInfoQuery query) {
        return userInfoMapper.selectCount(query);

    }

    /**
     * 分页查询
     */
    public PaginationResultVo<UserInfo> findListByPage(UserInfoQuery query) {
        int count = this.findCountByQuery(query);
        int pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserInfo> list = this.findListByQuery(query);
        PaginationResultVo<UserInfo> result = new PaginationResultVo(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserInfo bean) {
        return userInfoMapper.insert(bean);
    }

    /**
     * 批量新增对象
     */
    public Integer addBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return userInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改对象
     */
    public Integer addOrUpdateBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return userInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据UserId查询对象
     */
    public UserInfo getUserInfoByUserId(String userId) {
        return userInfoMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId更新对象
     */
    public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
        return userInfoMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除对象
     */
    public Integer deleteUserInfoByUserId(String userId) {
        return userInfoMapper.deleteByUserId(userId);
    }


    /**
     * 根据Email查询对象
     */
    public UserInfo getUserInfoByEmail(String email) {
        return userInfoMapper.selectByEmail(email);
    }

    /**
     * 根据Email更新对象
     */
    public Integer updateUserInfoByEmail(UserInfo bean, String email) {
        return userInfoMapper.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除对象
     */
    public Integer deleteUserInfoByEmail(String email) {
        return userInfoMapper.deleteByEmail(email);
    }


    /**
     * 根据QqOpenId查询对象
     */
    public UserInfo getUserInfoByQqOpenId(String qqOpenId) {
        return userInfoMapper.selectByQqOpenId(qqOpenId);
    }

    /**
     * 根据QqOpenId更新对象
     */
    public Integer updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId) {
        return userInfoMapper.updateByQqOpenId(bean, qqOpenId);
    }

    /**
     * 根据QqOpenId删除对象
     */
    public Integer deleteUserInfoByQqOpenId(String qqOpenId) {
        return userInfoMapper.deleteByQqOpenId(qqOpenId);
    }


    /**
     * 根据NickName查询对象
     */
    public UserInfo getUserInfoByNickName(String nickName) {
        return userInfoMapper.selectByNickName(nickName);
    }

    /**
     * 根据NickName更新对象
     */
    public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
        return userInfoMapper.updateByNickName(bean, nickName);
    }

    /**
     * 根据NickName删除对象
     */
    public Integer deleteUserInfoByNickName(String nickName) {
        return userInfoMapper.deleteByNickName(nickName);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {

        if (type == 0) {
            UserInfo userInfo = userInfoMapper.selectByEmail(email);

            if (userInfo != null) {
                throw new BusinessException("邮箱已存在");
            }
        }
        String code = RandomStringUtils.random(5, false, true);

        emailCodeMapper.disableEmailCode(email);

        sendEmailDo(email, code);

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(0);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }

    public void sendEmailDo(String toEmail, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(webConfig.getUserName());
            helper.setTo(toEmail);

            helper.setSubject(redisComponent.getSysSettingDto().getRegisterMailTitle());
            helper.setText(String.format(redisComponent.getSysSettingDto().getRegisterEmailContent(),code));
            helper.setSentDate(new Date());

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BusinessException("发送邮件失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String nickName, String password, String emailCode) {
        if (userInfoMapper.selectByEmail(email) != null) {
            throw new BusinessException("用户已存在");
        }
        EmailCode code = emailCodeMapper.selectByEmailAndCode(email, emailCode);
        if (code == null) {
            throw new BusinessException("邮箱验证码不正确");
        }
        emailCodeMapper.disableEmailCode(email);

        UserInfo user = new UserInfo();


        user.setUserId(RandomStringUtils.random(15, true, true));
        user.setEmail(email);
        user.setNickName(nickName);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        Date date = new Date();
        user.setJoinTime(date);
        user.setLastLoginTime(date);
        user.setUseSpace(0l);
        user.setTotalSpace(redisComponent.getSysSettingDto().getUserInitUseSpace());
        user.setStatus(1);

        userInfoMapper.insert(user);



    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo login(String email, String password) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if(userInfo==null){
            throw new BusinessException("用户不存在");
        }
        UserInfo user = userInfoMapper.varifyPwd(email, DigestUtils.md5DigestAsHex(password.getBytes()));
        if(user==null){
            throw new BusinessException("用户密码错误或者已被禁用");
        }
        user.setLastLoginTime(new Date());
        userInfoMapper.updateByEmail(user,email);

        UserSpaceDto userSpaceDto = new UserSpaceDto();
        userSpaceDto.setUseSpace(user.getUseSpace());
        userSpaceDto.setTotalSpace(user.getTotalSpace());

        redisComponent.getSysSettingDto();

        redisComponent.saveUserSpace(user.getUserId(), userSpaceDto);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if(userInfo==null){
            throw new BusinessException("用户不存在");
        }
        String code = RandomStringUtils.random(5, false, true);
        emailCodeMapper.disableEmailCode(email);

        sendEmailDo(email,code);
        EmailCode emailCode1 = new EmailCode();
        emailCode1.setCode(code);
        emailCode1.setEmail(email);
        emailCode1.setCreateTime(new Date());
        emailCode1.setStatus(0);

        emailCodeMapper.insert(emailCode1);

        EmailCode emailCode2 = emailCodeMapper.selectByEmailAndCode(email, code);
        if(emailCode2==null){
            throw new BusinessException("验证码不正确或者邮箱错误");
        }
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfoMapper.updateByEmail(userInfo,email);

    }

}