package com.easypan.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.config.WebConfig;
import com.easypan.entity.dto.CreateImageCode;
import com.easypan.entity.dto.UserDto;
import com.easypan.entity.dto.UserSpaceDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.vo.ResponseVo;
import com.easypan.exception.BusinessException;
import com.easypan.myEnum.VerifyRegexEnum;
import com.easypan.util.CopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.easypan.service.UserInfoService;

import java.io.IOException;

/**
 * @author lemon
 * @date 2024-03-17 19:01
 * @desc 用户信息
 */

@RestController
public class UserInfoController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private WebConfig webConfig;

    @RequestMapping("checkCode")
    @GlobalInterceptor(checkParams = true)
    public void checkCode(HttpServletResponse response, HttpSession session, @VerifyParam(required = true) Integer type) {
        CreateImageCode cic = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("Expires", "0");
        response.setContentType("image/jpeg");
        String code = cic.getCode();
        if (type == null || type == 0) {
            session.setAttribute("checkCode", code);
        } else {
            session.setAttribute("checkCodeEmail", code);
        }
        try {
            cic.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("sendEmailCode")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo emailCode(HttpSession session,
                                @VerifyParam(required = true) String email,
                                @VerifyParam(required = true) String checkCode,
                                @VerifyParam(required = true) Integer type) {
        try {
            if (session.getAttribute("checkCodeEmail") == null || !checkCode.equals(session.getAttribute("checkCodeEmail"))) {
                throw new BusinessException("验证码不正确");
            }
            userInfoService.sendEmailCode(email, type);
            return getSuccessResponseVo(null);
        } finally {
            session.removeAttribute("checkCodeEmail");
        }


    }

    @RequestMapping("register")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo register(HttpSession session,
                               @VerifyParam(required = true) String email,
                               @VerifyParam(required = true) String nickName,
                               @VerifyParam(required = true, regx = VerifyRegexEnum.PASSWORD) String password,
                               @VerifyParam(required = true) String checkCode,
                               @VerifyParam(required = true) String emailCode) {
        try {
            if (session.getAttribute("checkCode") == null || !checkCode.equals(session.getAttribute("checkCode"))) {
                throw new BusinessException("验证码不正确");
            }
            userInfoService.register(email, nickName, password, emailCode);
            return getSuccessResponseVo(null);
        } finally {
            session.removeAttribute("checkCode");
        }

    }

    @RequestMapping("login")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo login(HttpSession session,
                            @VerifyParam(required = true) String email,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode) {
        try {
            if (session.getAttribute("checkCode") == null || !session.getAttribute("checkCode").equals(checkCode)) {
                throw new BusinessException("验证码不正确");
            }
            UserInfo userInfo = userInfoService.login(email, password);
            UserDto userDto = CopyUtils.copy(userInfo, UserDto.class);
            userDto.setAdmin(webConfig.getAdmin().equals(userInfo.getEmail()));

            session.setAttribute("user", userDto);
            return getSuccessResponseVo(userDto);
        } finally {
            session.removeAttribute("checkCode");
        }
    }

    @RequestMapping("resetPwd")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo resetPwd(HttpSession session,
                               @VerifyParam(required = true) String email,
                               @VerifyParam(required = true) String password,
                               @VerifyParam(required = true) String checkCode,
                               @VerifyParam(required = true) String emailCode) {
        if (session.getAttribute("checkCodeEmail") == null || !session.getAttribute("checkCodeEmail").equals(checkCode)) {
            throw new BusinessException("验证码不正确");
        }
        userInfoService.resetPwd(email, password, emailCode);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("getAvatar/{userId}")
    public void getAvatar(@PathVariable String userId, HttpServletResponse response) {
        readFile(response, webConfig.getFolder()+"avatar/"+ userId + ".jpg");
    }

    @RequestMapping("getUseSpace")
    public ResponseVo getUseSpace(HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user == null) {
            throw new BusinessException("请先登录账号");
        }
        return getSuccessResponseVo(CopyUtils.copy(user, UserSpaceDto.class));
    }

    @RequestMapping("logout")
    public ResponseVo logout(HttpSession session) {
        session.removeAttribute("user");
        return getSuccessResponseVo(null);
    }

    @RequestMapping("updateUserAvatar")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo updateUserAvatar(HttpSession session,
                                       @VerifyParam(required = true) String avatar) {
        UserInfo user = (UserInfo) session.getAttribute("user");
        user.setQqAvatar(avatar);
        userInfoService.updateUserInfoByEmail(user, user.getUserId());
        return getSuccessResponseVo(null);
    }


}