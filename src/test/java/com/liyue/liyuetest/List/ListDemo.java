package com.liyue.liyuetest.List;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListDemo {
    public static void main(String[] args) throws InterruptedException {
        List<Integer>  list=new ArrayList<>();
        Collections.unmodifiableCollection(list);
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    list.add(i);
                }
            }
        };
        thread1.start();
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    list.add(0, 1);
                }
            }
        };
        thread.start();

        thread1.join();
        thread.join();

     list.stream().forEach(x-> System.out.println(x));



    }
}
