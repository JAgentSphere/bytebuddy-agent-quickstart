package com.jas.quickstart.core;

import com.google.common.base.Stopwatch;
import com.jas.quickstart.core.aop.Aspect;
import com.jas.quickstart.core.aop.support.advice.MethodAfterAdvice;
import com.jas.quickstart.core.aop.support.advice.MethodAfterReturningAdvice;
import com.jas.quickstart.core.aop.support.advice.MethodAfterThrowingAdvice;
import com.jas.quickstart.core.aop.support.advice.MethodBeforeAdvice;
import com.jas.quickstart.spy.Spy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
@Setter
@Slf4j
public class DefaultSpyHandler implements Spy.SpyHandler {

    private Map<Integer, Aspect> aspectMap;

    public DefaultSpyHandler(Map<Integer, Aspect> aspectMap) {
        this.aspectMap = aspectMap;
    }

    @Override
    public Spy.Result before(Integer aspectId, String className, String methodName, Object[] args, Object target) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Aspect aspect = aspectMap.get(aspectId);
            if (Objects.nonNull(aspect) && aspect.isBefore()) {
                ((MethodBeforeAdvice)aspect.getAdvice()).before(args, target);
            }
        } catch (Throwable e) {
            log.error("before error", e);
        }
        log.debug("{}#{} before method invoke time -> {}", className, methodName, stopwatch);
        return null;
    }

    @Override
    public Spy.Result afterReturning(Integer aspectId, String className, String methodName, Object[] args,
        Object target, Object ret) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Aspect aspect = aspectMap.get(aspectId);
            if (Objects.nonNull(aspect) && aspect.isAfter()) {
                ((MethodAfterReturningAdvice)aspect.getAdvice()).afterReturning(ret, args, target);
            }
        } catch (Throwable e) {
            log.error("afterReturning error", e);
        }
        log.debug("{}#{} afterReturning method invoke time -> {}", className, methodName, stopwatch);
        return null;
    }

    @Override
    public Spy.Result afterThrowing(Integer aspectId, String className, String methodName, Object[] args, Object target,
        Throwable throwable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Aspect aspect = aspectMap.get(aspectId);
            if (Objects.nonNull(aspect) && aspect.isBefore()) {
                ((MethodAfterThrowingAdvice)aspect.getAdvice()).afterThrowing(args, target, throwable);
            }
        } catch (Throwable e) {
            log.error("afterThrowing error", e);
        }
        log.debug("{}#{} afterThrowing method invoke time -> {}", className, methodName, stopwatch);
        return null;
    }

    @Override
    public Spy.Result after(Integer aspectId, String className, String methodName, Object[] args, Object target,
        Object ret) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Aspect aspect = aspectMap.get(aspectId);
            if (Objects.nonNull(aspect) && aspect.isBefore()) {
                ((MethodAfterAdvice)aspect.getAdvice()).after(ret, args, target);
            }
        } catch (Throwable e) {
            log.error("after error", e);
        }
        log.debug("{}#{} after method invoke time -> {}", className, methodName, stopwatch);
        return null;
    }
}
