package com.jake.demo;

import io.reactivex.Observable;

public class Test {

    public static void main(String[] args) {
        Observable.just(3, 2, 1).subscribe(System.out::println);
    }
}
