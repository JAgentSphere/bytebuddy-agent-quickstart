package com.jas.quickstart.core.aop.support.interceptor;

import com.jas.quickstart.core.aop.AspectId;
import com.jas.quickstart.spy.Spy;
import net.bytebuddy.asm.Advice;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class StaticMethodBefore {
    @Advice.OnMethodEnter
    public static void after(@Advice.AllArguments Object[] allArguments,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @AspectId Integer aspectId
    ) {
        Spy.before(aspectId, className, methodName, allArguments, null);
    }
}
