package com.easypan.aspect;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.dto.UserDto;
import com.easypan.exception.BusinessException;
import com.easypan.myEnum.ResponseCode;
import com.easypan.util.VerifyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class OperationAspect {

    private static final String[] TYPE_BASE = {"java.lang.String", "java.lang.Integer", "java.lang.Long"};

    @Pointcut("@annotation(com.easypan.annotation.GlobalInterceptor)")
    private void requestInterceptor() {

    }

    @Around("requestInterceptor()")
    public Object InterceptorDo(ProceedingJoinPoint point) {
        try {
            Object target = point.getTarget();
            Object[] args = point.getArgs();
            String methodName = point.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (interceptor == null) {
                return null;
            }
            if (interceptor.checkLogin()) {
                checkLogin(interceptor.checkAdmin());
            }
            if (interceptor.checkParams()) {
                validateParams(method, args);
            }
            // 环绕通知=前置+目标方法执行+后置通知，proceed方法就是用于启动目标方法执行的.
            // 环绕通知 ProceedingJoinPoint 执行proceed方法的作用是让目标方法执行，
            // 这也是环绕通知和前置、后置通知方法的一个最大区别。
            return point.proceed();// 放行
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.CODE_500);
        } catch (Throwable e) {
            throw new BusinessException(ResponseCode.CODE_500);
        }

    }

    private void checkLogin(boolean checkAdmin) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            throw new BusinessException(ResponseCode.CODE_901);
        }
        if (checkAdmin && !user.getAdmin()) {
            throw new BusinessException("您不具有管理员的权利");
        }
    }

    public void validateParams(Method m, Object[] arguments) {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if (verifyParam == null) {
                continue;
            }
            if (ArrayUtils.contains(TYPE_BASE, parameter.getParameterizedType().getTypeName())) {
                checkValue(value, verifyParam);
            } else {
                checkObjectValue(parameter, value);
            }

        }
    }

    private void checkValue(Object value, VerifyParam verifyParam) {
        Boolean isEmpty = value == null || StringUtils.isEmpty(value.toString());
        Integer length = value == null ? 0 : value.toString().length();
        /**
         * 校验空
         */
        if (isEmpty && verifyParam.required()) {
            throw new BusinessException(ResponseCode.CODE_600);
        }
        /**
         * 校验长度
         */
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() >= length)) {
            throw new BusinessException(ResponseCode.CODE_600);
        }
        /**
         * 检验正则
         */
        if (!StringUtils.isEmpty(verifyParam.regx().getRegex()) && VerifyUtils.Verify(verifyParam.regx(), String.valueOf(value))) {
            throw new BusinessException(ResponseCode.CODE_500);
        }

    }

    private void checkObjectValue(Parameter parameter, Object obj) {
        try {
            String typeName = parameter.getParameterizedType().getTypeName();
            Class clazz = Class.forName(typeName);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam fieldAnnotation = field.getAnnotation(VerifyParam.class);
                if (fieldAnnotation == null) {
                    continue;
                }
                field.setAccessible(true);
                Object o = field.get(obj);
                checkValue(o, fieldAnnotation);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.CODE_500);
        }
    }
}
