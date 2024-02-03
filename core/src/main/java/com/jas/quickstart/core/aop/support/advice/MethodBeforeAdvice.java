package com.jas.quickstart.core.aop.support.advice;

import com.jas.quickstart.core.aop.advice.BeforeAdvice;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
public interface MethodBeforeAdvice extends BeforeAdvice {
    void before(Object[] args, Object target) throws Throwable;
}
