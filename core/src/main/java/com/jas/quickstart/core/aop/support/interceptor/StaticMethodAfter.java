package com.jas.quickstart.core.aop.support.interceptor;

import com.jas.quickstart.core.aop.AspectId;
import com.jas.quickstart.spy.Spy;
import net.bytebuddy.asm.Advice;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class StaticMethodAfter {
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void after(@Advice.AllArguments Object[] allArguments,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.Return Object result,
                             @Advice.Thrown Throwable throwable,
                             @AspectId Integer aspectId
    ) {
        if (throwable != null) {
            Spy.afterThrowing(aspectId, className, methodName, allArguments, null, throwable);
        } else {
            Spy.afterReturning(aspectId, className, methodName, allArguments, null, result);
        }
        Spy.after(aspectId, className, methodName, allArguments, null, result);
    }
}
