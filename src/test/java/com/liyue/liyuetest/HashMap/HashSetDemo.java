package com.liyue.liyuetest.HashMap;

import java.util.HashSet;

public class HashSetDemo {
    public static void main(String[] args) {
        HashSet<Integer> set=new HashSet<>();

        set.add(1);
        set.add(1);
        set.add(1);
        set.add(2);

        set.stream().forEach(x-> System.out.println(x));
        System.out.println(set.size());



    }
}
