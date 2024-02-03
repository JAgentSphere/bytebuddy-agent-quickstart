package com.jas.quickstart.core.aop;

import com.jas.quickstart.core.aop.advice.Advice;

import java.lang.instrument.ClassFileTransformer;
import java.util.List;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
public interface Aspect {
    Pointcut getPointcut();

    Advice getAdvice();

    String getAspectName();

    boolean isBefore();

    boolean isAfter();

    boolean isAround();

    List<ClassFileTransformer> getTransformers();

    void setTransformers(List<ClassFileTransformer> transformer);

    List<String> getMethodSignatures();

    void setMethodSignatures(List<String> methodSignature);
}
