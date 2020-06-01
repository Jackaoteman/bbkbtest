package com.liyue.liyuetest.LetCode;

import java.util.Stack;

/*给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。

        有效字符串需满足：

        左括号必须用相同类型的右括号闭合。
        左括号必须以正确的顺序闭合。

        注意空字符串可被认为是有效字符串。

 */
public class Demo1 {

    public static void main(String[] args) {
        String  str="{[()]}";

        boolean b = method(str);
        System.out.println(b);

    }
    private static boolean method(String str) {
        char[] ch = str.toCharArray();
        Stack<Character> stack=new Stack<>();
        for (char c : ch) {
            if(c=='{'){
                stack.add('}');
            }else if(c=='['){
                stack.add(']');
            }else if(c=='('){
                stack.add(')');
            }else if(stack.isEmpty()||c!=stack.pop()){
                return  false;
            }
        }
        if(stack.isEmpty()){
            return true;
        }else{
            return false;
        }
    }


}
