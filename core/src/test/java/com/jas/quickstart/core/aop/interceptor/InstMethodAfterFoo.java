package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstMethodAfterFoo {
    private String name;

    public String hello(String msg) throws Exception {
        return msg;
    }
}