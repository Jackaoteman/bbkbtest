package com.liyue.liyuetest.Tree;

/**
 * 动态创建二叉树
 */
public class TreeRoot {

    private TreeNodeDemo treeRoot;

    public TreeNodeDemo getTreeRoot() {
        return treeRoot;
    }

    public void setTreeRoot(TreeNodeDemo treeRoot) {
        this.treeRoot = treeRoot;
    }


    /**
     * 动态创建二叉查找树
     *
     * @param treeRoot 根节点
     * @param value    节点的值
     */
    public static void createTree(TreeRoot treeRoot, int value) {


        //如果树根为空(第一次访问)，将第一个值作为根节点
        if (treeRoot.getTreeRoot() == null) {
            TreeNodeDemo treeNode = new TreeNodeDemo(value);
            treeRoot.setTreeRoot(treeNode);

        } else {
            //当前树根
            TreeNodeDemo tempRoot = treeRoot.getTreeRoot();
            while (tempRoot != null) {
                //当前值大于根值，往右边走
                if (value > tempRoot.getValue()) {

                    //右边没有树根，那就直接插入
                    if (tempRoot.getRightNode() == null) {
                        tempRoot.setRightNode(new TreeNodeDemo(value));
                        return;
                    } else {
                        //如果右边有树根，到右边的树根去
                        tempRoot = tempRoot.getRightNode();
                    }
                } else {
                    //左没有树根，那就直接插入
                    if (tempRoot.getLeftNode() == null) {
                        tempRoot.setLeftNode(new TreeNodeDemo(value));
                        return;
                    } else {
                        //如果左有树根，到左边的树根去
                        tempRoot = tempRoot.getLeftNode();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {20, 10, 14, 13, 15, 25, 26, 22};

        TreeRoot treeRoot = new TreeRoot();
        for (int i : arr) {
            createTree(treeRoot, i);
        }
            //先序遍历树
            //TreeNodeDemo.inTraverseBTree(treeRoot.getTreeRoot())
            //中序遍历树
           TreeNodeDemo.preTraverseBTree(treeRoot.getTreeRoot());


     }
}
