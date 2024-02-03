package com.jas.quickstart.core.aop.support.advice;

import com.jas.quickstart.core.aop.advice.AroundAdvice;

/**
 * @author ReaJason
 * @since 2024/1/28
 */
public interface MethodAroundAdvice extends AroundAdvice, MethodBeforeAdvice,
        MethodAfterAdvice, MethodAfterReturningAdvice, MethodAfterThrowingAdvice {
}
