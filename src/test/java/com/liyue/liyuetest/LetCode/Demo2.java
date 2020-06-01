package com.liyue.liyuetest.LetCode;

import java.util.*;

/**
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 *
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 */
public class Demo2 {
    public static void main(String[] args) {

        HashMap<String, List<Character>> map = method("2369");

        for (String s : map.keySet()) {
            System.out.println(s + ":" + map.get(s).toString());
        }
        System.out.println("===============");
        int size = map.size();



    }

    private static HashMap<String, List<Character>> method(String str) {
        HashMap<String, List<Character>> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            List<Character> list = new ArrayList<>();
            if (str.charAt(i) == '2') {
                list.add('a');
                list.add('b');
                list.add('c');
                map.put("2", list);
            } else if (str.charAt(i) == '3') {
                list.add('d');
                list.add('e');
                list.add('f');
                map.put("3", list);
            } else if (str.charAt(i) == '4') {
                list.add('g');
                list.add('h');
                list.add('i');
                map.put("4", list);
            } else if (str.charAt(i) == '5') {
                list.add('j');
                list.add('k');
                list.add('l');
                map.put("5", list);
            } else if (str.charAt(i) == '6') {
                list.add('m');
                list.add('n');
                list.add('o');
                map.put("6", list);
            } else if (str.charAt(i) == '7') {
                list.add('p');
                list.add('q');
                list.add('r');
                list.add('s');
                map.put("7", list);
            } else if (str.charAt(i) == '8') {
                list.add('t');
                list.add('u');
                list.add('v');
                map.put("8", list);
            } else if (str.charAt(i) == '9') {
                list.add('w');
                list.add('x');
                list.add('y');
                list.add('z');
                map.put("9", list);
            }
        }
        return map;
    }
}
