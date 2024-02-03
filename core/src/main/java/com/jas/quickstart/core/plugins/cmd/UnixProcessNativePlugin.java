package com.jas.quickstart.core.plugins.cmd;

import com.jas.quickstart.core.aop.Pointcut;
import com.jas.quickstart.core.aop.advice.Advice;
import com.jas.quickstart.core.aop.support.AbstractAspect;
import com.jas.quickstart.core.plugins.Plugin;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author ReaJason
 * @since 2024/1/28
 */
@Slf4j
@Plugin
public class UnixProcessNativePlugin extends AbstractAspect {
    @Override
    public Pointcut getPointcut() {
        return new Pointcut() {
            @Override
            public ElementMatcher.Junction<MethodDescription> getMethodMatcher() {
                return named("forkAndExec");
            }

            @Override
            public ElementMatcher.Junction<TypeDescription> getTypeMatcher() {
                return named("java.lang.UNIXProcess");
            }

            @Override
            public boolean isNative() {
                return true;
            }
        };
    }

    @Override
    public Advice getAdvice() {
        return new ForkAndExecAdvice();
    }

    @Override
    public String getAspectName() {
        return null;
    }
}
