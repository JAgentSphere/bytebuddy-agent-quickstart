package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstMethodBeforeFoo {
    private String name;

    public String hello(String msg) {
        return msg;
    }
}