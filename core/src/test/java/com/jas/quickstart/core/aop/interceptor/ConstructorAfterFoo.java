package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;

@Data
public class ConstructorAfterFoo {
    private String name;

    public ConstructorAfterFoo() {
        System.out.println("Foo()");
    }

    public ConstructorAfterFoo(String name) {
        this.name = name;
    }
}


