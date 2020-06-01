package com.liyue.liyuetest.Tree;

import javax.swing.tree.TreeNode;

/**
 * 手写二叉树
 */
public class TreeNodeDemo {

    private  TreeNodeDemo leftNode;
    private  TreeNodeDemo rightNode;
    private  int value;

    public TreeNodeDemo() {
    }

    public TreeNodeDemo(int value) {
        this.value = value;
    }

    public TreeNodeDemo getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(TreeNodeDemo leftNode) {
        this.leftNode = leftNode;
    }

    public TreeNodeDemo getRightNode() {
        return rightNode;
    }

    public void setRightNode(TreeNodeDemo rightNode) {
        this.rightNode = rightNode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    /**
     * 先序遍历
     * @param rootTreeNode  根节点
     */
    public static void preTraverseBTree(TreeNodeDemo rootTreeNode) {

        if (rootTreeNode != null) {

            //访问根节点
            System.out.println(rootTreeNode.getValue());

            //访问左节点
            preTraverseBTree(rootTreeNode.getLeftNode());

            //访问右节点
            preTraverseBTree(rootTreeNode.getRightNode());
        }
    }

    /**
     * 中序遍历
     * @param rootTreeNode  根节点
     */
    public static void inTraverseBTree(TreeNodeDemo rootTreeNode) {

        if (rootTreeNode != null) {

            //访问左节点
            inTraverseBTree(rootTreeNode.getLeftNode());

            //访问根节点
            System.out.println(rootTreeNode.getValue());

            //访问右节点
            inTraverseBTree(rootTreeNode.getRightNode());
        }
    }

    /**
     * 查找树的深度
     * @param treeNode
     * @return
     */
    public static int getHeight(TreeNodeDemo treeNode) {

        if (treeNode == null) {
            return 0;
        } else {

            //左边的子树深度
            int left = getHeight(treeNode.getLeftNode());

            //右边的子树深度
            int right = getHeight(treeNode.getRightNode());


            int max = left;

            if (right > max) {
                max = right;
            }
            return max + 1;
        }
    }
}
