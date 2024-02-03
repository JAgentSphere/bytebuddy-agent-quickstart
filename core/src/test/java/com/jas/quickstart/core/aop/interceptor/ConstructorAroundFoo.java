package com.jas.quickstart.core.aop.interceptor;

import lombok.Data;

@Data
public class ConstructorAroundFoo {
    private String name;

    public ConstructorAroundFoo() {
        System.out.println("Foo()");
    }

    public ConstructorAroundFoo(String name) {
        this.name = name;
    }
}
