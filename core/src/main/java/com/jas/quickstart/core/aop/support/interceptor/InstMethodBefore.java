package com.jas.quickstart.core.aop.support.interceptor;

import com.jas.quickstart.core.aop.AspectId;
import com.jas.quickstart.spy.Spy;
import net.bytebuddy.asm.Advice;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class InstMethodBefore {
    @Advice.OnMethodEnter
    public static void interceptor(@Advice.This Object target, @Advice.AllArguments Object[] allArguments,
        @Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName, @Advice.Origin String signature,
        @AspectId Integer aspectId) {
        Spy.before(aspectId, className, methodName, allArguments, target);
    }
}
