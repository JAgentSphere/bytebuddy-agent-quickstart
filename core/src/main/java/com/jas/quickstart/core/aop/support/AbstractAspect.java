package com.jas.quickstart.core.aop.support;

import com.jas.quickstart.core.aop.Aspect;
import com.jas.quickstart.core.aop.advice.AfterAdvice;
import com.jas.quickstart.core.aop.advice.AroundAdvice;
import com.jas.quickstart.core.aop.advice.BeforeAdvice;
import lombok.Setter;
import lombok.ToString;

import java.lang.instrument.ClassFileTransformer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ReaJason
 * @since 2024/1/28
 */

@Setter
@ToString
public abstract class AbstractAspect implements Aspect {

    private List<ClassFileTransformer> transformers;

    private List<String> methodSignatures = new ArrayList<>();

    @Override
    public boolean isBefore() {
        return getAdvice() instanceof BeforeAdvice;
    }

    @Override
    public boolean isAfter() {
        return getAdvice() instanceof AfterAdvice;
    }

    @Override
    public boolean isAround() {
        return getAdvice() instanceof AroundAdvice;
    }

    @Override
    public List<ClassFileTransformer> getTransformers() {
        return transformers;
    }

    @Override
    public List<String> getMethodSignatures() {
        return methodSignatures;
    }
}
