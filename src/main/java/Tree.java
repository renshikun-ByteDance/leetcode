public class Tree {


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {
        }
        TreeNode(int val) {
            this.val = val;
        }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
//        return searchBST(val < root.val ? root.left : root.right, val);
        return root.val < val ? searchBST(root.right, val) : searchBST(root.left, val);

//            if (root == null) {
//                return null;
//            }
//            if (val == root.val) {
//                return root;
//            }
//            return searchBST(val < root.val ? root.left : root.right, val);

    }
}
