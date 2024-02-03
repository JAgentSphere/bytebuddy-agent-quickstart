package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaticMethodAfterFoo {
    private String name;

    public static String hello(String msg) throws Exception {
        return msg;
    }
}