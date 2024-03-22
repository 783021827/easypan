package com.easypan.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // 程序运行时
@Inherited // 子类可继承
@Documented
public @interface GlobalInterceptor {
    boolean checkLogin() default false;
    boolean checkParams() default false;
    boolean checkAdmin() default true;
}

