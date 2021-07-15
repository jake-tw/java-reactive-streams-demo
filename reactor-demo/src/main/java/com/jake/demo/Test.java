package com.jake.demo;

import reactor.core.publisher.Flux;

public class Test {

    public static void main(String[] args) {
        Flux.just(1,2,3).subscribe(System.out::println);
    }
}
