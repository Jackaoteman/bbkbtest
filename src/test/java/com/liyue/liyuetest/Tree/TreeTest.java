package com.liyue.liyuetest.Tree;

/**
 * 值得说明的是：二叉树遍历有三种方式
 *
 *     先序遍历
 *         先访问根节点，然后访问左节点，最后访问右节点(根->左->右)
 *     中序遍历
 *         先访问左节点，然后访问根节点，最后访问右节点(左->根->右)
 *     后序遍历
 *         先访问左节点，然后访问右节点，最后访问根节点(左->右->根)
 */
public class TreeTest {
    public static void main(String[] args) {
        TreeNodeDemo demo1=new TreeNodeDemo(10);
        TreeNodeDemo demo2=new TreeNodeDemo(20);
        TreeNodeDemo demo3=new TreeNodeDemo(30);
        TreeNodeDemo demo4=new TreeNodeDemo(25);
        TreeNodeDemo demo5=new TreeNodeDemo(15);
        TreeNodeDemo demo6=new TreeNodeDemo(8);

        demo2.setLeftNode(demo5);
        demo2.setRightNode(demo4);
        demo5.setLeftNode(demo1);
        demo1.setLeftNode(demo6);
        demo4.setRightNode(demo3);

        TreeNodeDemo.preTraverseBTree(demo2);
        System.out.println("");
        System.out.println("================");
        TreeNodeDemo.inTraverseBTree(demo2);
    }


}
