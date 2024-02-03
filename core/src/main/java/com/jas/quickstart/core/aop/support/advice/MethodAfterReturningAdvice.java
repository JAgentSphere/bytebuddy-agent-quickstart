package com.jas.quickstart.core.aop.support.advice;

import com.jas.quickstart.core.aop.advice.AfterAdvice;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
public interface MethodAfterReturningAdvice extends AfterAdvice {
    void afterReturning(Object returnValue, Object[] args, Object target) throws Throwable;
}
