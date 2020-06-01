package com.liyue.liyuetest.Tree;

import java.math.BigDecimal;

public class Test1 {
    public static void main(String[] args) {

        BigDecimal  bigDecimal=new BigDecimal("79.50");
        BigDecimal bigDecimal1 = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
        System.out.println(bigDecimal1);


    }
}
