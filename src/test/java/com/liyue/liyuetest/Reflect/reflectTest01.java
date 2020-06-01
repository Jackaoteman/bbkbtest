package com.liyue.liyuetest.Reflect;

import org.junit.platform.commons.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class reflectTest01 {

    public static void main(String[] args) {

    Person person=new Person();
    person.setName("哈哈");
    person.setAge("20");
    person.setAddress("杭州市");
    person.setPhone("1231231231");

        List<String> list=new ArrayList<>();
        list.add("name");
        list.add("age");
        list.add("address");
        list.add("phone");
      //  list.stream().sorted(x-> Comparator.comparing(String:))
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return   o1.compareTo(o2) ;
            }
        });
        list.stream().forEach(x-> System.out.println(x));



    }
}
