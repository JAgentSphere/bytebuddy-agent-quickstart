package com.jas.quickstart.core.aop.support.interceptor;

import com.jas.quickstart.core.aop.AspectId;
import com.jas.quickstart.spy.Spy;
import net.bytebuddy.asm.Advice;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class ConstructorAfter {

    @Advice.OnMethodExit
    public static void after(@Advice.AllArguments Object[] allArguments,
                             @Advice.This Object target,
                             @Advice.Origin("#t") String className,
                             @AspectId Integer aspectId
    ) {
        Spy.after(aspectId, className, "<init>", allArguments, target, target);
    }
}
