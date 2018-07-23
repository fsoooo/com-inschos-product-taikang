package com.inschos.product.taikang.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAopConfig {

    public void before() {
        System.out.println("(配置文件)前置增强......");
    }

}
