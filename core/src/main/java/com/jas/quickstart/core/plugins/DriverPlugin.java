package com.jas.quickstart.core.plugins;

import com.jas.quickstart.core.aop.Pointcut;
import com.jas.quickstart.core.aop.advice.Advice;
import com.jas.quickstart.core.aop.support.AbstractAspect;
import com.jas.quickstart.core.aop.support.advice.MethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
@Slf4j
@Plugin
public class DriverPlugin extends AbstractAspect {

    @Override
    public Pointcut getPointcut() {
        return new Pointcut() {
            @Override
            public ElementMatcher.Junction<MethodDescription> getMethodMatcher() {
                return named("connect");
            }

            @Override
            public ElementMatcher.Junction<TypeDescription> getTypeMatcher() {
                return hasSuperType(named("java.sql.Driver")).and(not(isInterface()));
            }
        };
    }

    @Override
    public Advice getAdvice() {
        return (MethodBeforeAdvice)(args, target) -> {
        };
    }

    @Override
    public String getAspectName() {
        return null;
    }
}
