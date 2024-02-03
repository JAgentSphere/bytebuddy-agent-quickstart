package com.jas.quickstart.core.plugins.cmd;

import com.jas.quickstart.core.aop.support.advice.MethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ReaJason
 * @since 2024/1/31
 */
@Slf4j
public class ForkAndExecAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Object[] args, Object target) throws Throwable {
        List<String> cmdList = new ArrayList<>(3);
        cmdList.add(byteToStr((byte[])args[2]));
        cmdList.add(byteToStr((byte[])args[3]));
        cmdList.add(byteToStr((byte[])args[5]));

        log.info("native forkAndExec invoked");
        log.info("command: {}", String.join(" ", cmdList));
    }

    private String byteToStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        String str = new String(bytes).trim().replace("\0", " ");
        if (!str.isEmpty()) {
            return str;
        }
        return "";
    }
}
