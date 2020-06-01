package com.liyue.liyuetest.Array;

import java.util.HashMap;

/**
 * 寻找数组中的最大元素
 */
public class FindMaxInArrays{


    public  static <T extends Comparable<? super T>> T findMax(T[] arr){
    
        int maxIndex=0;

        for (int i = 0; i < arr.length; i++) {
            if(arr[i].compareTo(arr[maxIndex])>0){
                maxIndex=i;
            }
        }
        return  arr[maxIndex];
    }

    public static void main(String[] args) {

        int[] arr={1,23,4,5,6,77,44,53,67};

        HashMap map=new HashMap();
        map.put("1",1);



    }


}
