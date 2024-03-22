package com.easypan.controller;

import com.easypan.myEnum.ResponseCode;
import com.easypan.entity.vo.ResponseVo;
import com.easypan.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;

@RestControllerAdvice
public class AGlobalExceptionHandlerController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址:{}，错误信息：{}", request.getRequestURI(), e);
        ResponseVo ajaxResponse = new ResponseVo();
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse.setCode(ResponseCode.CODE_404.getCode());
            ajaxResponse.setInfo(ResponseCode.CODE_404.getInfo());
            ajaxResponse.setStatus(STATUS_ERROR);
        } else if (e instanceof BusinessException) {
            BusinessException biz = (BusinessException) e;
            ajaxResponse.setCode(biz.getCode());
            ajaxResponse.setInfo(biz.getMessage());
            ajaxResponse.setStatus(STATUS_ERROR);
        } else if (e instanceof BindException) {
            ajaxResponse.setCode(ResponseCode.CODE_600.getCode());
            ajaxResponse.setInfo(ResponseCode.CODE_600.getInfo());
            ajaxResponse.setStatus(STATUS_ERROR);
        } else {
            ajaxResponse.setCode(ResponseCode.CODE_500.getCode());
            ajaxResponse.setInfo(ResponseCode.CODE_500.getInfo());
            ajaxResponse.setStatus(STATUS_ERROR);
        }

        return ajaxResponse;
    }
}
