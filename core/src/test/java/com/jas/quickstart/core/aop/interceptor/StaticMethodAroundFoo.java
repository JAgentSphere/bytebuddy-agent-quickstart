package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaticMethodAroundFoo {
    private String name;

    public String hello(String msg) {
        return msg;
    }
}