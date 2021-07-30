package com.jake.demo.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Foo {

    private String id;
    private long time;

}
