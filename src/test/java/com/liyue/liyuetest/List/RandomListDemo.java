package com.liyue.liyuetest.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomListDemo {
    public static void main(String[] args) {

        List<Integer>  list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);

        List<Integer> list2=new ArrayList<>();


        Random random=new Random();

        int curt=4;
        while (true){
            int num = random.nextInt(list.size());
            if(list2.contains(num)){
                continue;
            }else{
                list2.add(num);
            }
            if(list2.size()==curt){
                break;
            }
        }
        for (Integer in : list2) {
            System.out.println(in);
        }





    }
}
