package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaticMethodBeforeFoo {
    private String name;

    public static String hello(String msg) {
        return msg;
    }
}