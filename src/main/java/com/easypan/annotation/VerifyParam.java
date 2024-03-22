package com.easypan.annotation;

import com.easypan.myEnum.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParam {

    boolean required() default false;

    int max() default -1;

    int min() default -1;

    VerifyRegexEnum regx() default VerifyRegexEnum.NO;
}
// 匹配正则
