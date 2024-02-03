package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;

@Data
public class ConstructorBeforeFoo {
    private String name;

    public ConstructorBeforeFoo() {
        System.out.println("Foo()");
    }

    public ConstructorBeforeFoo(String name) {
        this.name = name;
    }
}