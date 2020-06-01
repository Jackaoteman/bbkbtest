package com.liyue.liyuetest;

import java.util.ArrayList;
import java.util.List;

public class StringDemo1 {
    public static void main(String[] args) {

        List<String> list=new ArrayList<>();
        list.add("1");
        show(list);

        for (String s : list) {
            System.out.println(s);
        }


    }

    private static List<String> show(List<String> list) {
        list.add("2");
        return  list;


    }
}
