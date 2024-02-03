package com.jas.quickstart.spy;

import java.util.Objects;

/**
 * Spy class loaded with BootStrapClassLoader, so it can be used to intercept all class, especially java.*.
 *
 * @author ReaJason
 * @since 2024/1/24
 */
public class Spy {
    private static SpyHandler handler;
    private static final Result NOP = new Result();

    public static void setSpyHandler(SpyHandler spyHandler) {
        handler = spyHandler;
    }

    public static SpyHandler getSpyHandler() {
        return handler;
    }

    public static void clean() {
        handler = null;
    }

    /**
     * @param className  class name
     * @param methodName method name
     * @param args       method args
     * @param target     method target
     */
    public static Result before(Integer aspectId, String className, String methodName, Object[] args, Object target) {
        if (Objects.nonNull(handler)) {
            return handler.before(aspectId, className, methodName, args, target);
        }
        return NOP;
    }

    /**
     * @param className  class name
     * @param methodName method name
     * @param args       method args
     * @param target     method target
     * @param ret        method return value
     */
    public static Result afterReturning(Integer aspectId, String className, String methodName, Object[] args, Object target, Object ret) {
        if (Objects.nonNull(handler)) {
            return handler.afterReturning(aspectId, className, methodName, args, target, ret);
        }
        return NOP;
    }

    /**
     * @param className  class name
     * @param methodName method name
     * @param args       method args
     * @param target     method target
     * @param throwable  method throwable
     */
    public static Result afterThrowing(Integer aspectId, String className, String methodName, Object[] args, Object target, Throwable throwable) {
        if (Objects.nonNull(handler)) {
            return handler.afterThrowing(aspectId, className, methodName, args, target, throwable);
        }
        return NOP;
    }

    /**
     * @param className  class name
     * @param methodName method name
     * @param args       method args
     * @param target     method target
     * @param ret        method return value
     */
    public static Result after(Integer aspectId, String className, String methodName, Object[] args, Object target, Object ret) {
        if (Objects.nonNull(handler)) {
            return handler.after(aspectId, className, methodName, args, target, ret);
        }
        return NOP;
    }

    public static class Result {
        private Object[] args;
        private Object ret;

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

        public Object getRet() {
            return ret;
        }

        public void setRet(Object ret) {
            this.ret = ret;
        }
    }

    public interface SpyHandler {
        Result before(Integer aspectId, String className, String methodName, Object[] args, Object target);

        Result afterReturning(Integer aspectId, String className, String methodName, Object[] args, Object target, Object ret);

        Result afterThrowing(Integer aspectId, String className, String methodName, Object[] args, Object target, Throwable throwable);

        Result after(Integer aspectId, String className, String methodName, Object[] args, Object target, Object ret);
    }
}
