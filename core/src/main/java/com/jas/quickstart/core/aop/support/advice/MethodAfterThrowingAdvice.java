package com.jas.quickstart.core.aop.support.advice;

import com.jas.quickstart.core.aop.advice.AfterAdvice;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
public interface MethodAfterThrowingAdvice extends AfterAdvice {
    void afterThrowing(Object[] args, Object target, Throwable throwable) throws Throwable;
}
