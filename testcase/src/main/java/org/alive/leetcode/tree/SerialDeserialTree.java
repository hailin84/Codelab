package org.alive.leetcode.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 剑指 Offer II 048. 序列化与反序列化二叉树
 */
public class SerialDeserialTree {
    public String serialize(TreeNode root) {
        if (root == null) {
            return "[]";
        }
        List<String> value = new LinkedList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                TreeNode currNode = queue.poll();
                if (currNode != null) {
                    value.add(currNode.val + "");
                    queue.add(currNode.left);
                    queue.add(currNode.right);
                } else {
                    value.add("null");
                }
            }
        }
        return "[" + String.join(",", value) + "]";
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if (data == null || data.length() == 0 || "[]".equals(data)) {
            return null;
        }

        // 去掉两侧中括号
        String[] parts = data.substring(1, data.length() - 1).split(",");
        Queue<TreeNode> queue = new LinkedList<>();
        // 第一个元素自然不会是null
        TreeNode root = new TreeNode(Integer.parseInt(parts[0]));
        queue.add(root);
        int index = 1;
        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            String value = parts[index++];
            if ("null".equals(value)) {
                curr.left = null;
            } else {
                curr.left = new TreeNode(Integer.parseInt(value));
                queue.add(curr.left);
            }

            value = parts[index++];
            if ("null".equals(value)) {
                curr.right = null;
            } else {
                curr.right = new TreeNode(Integer.parseInt(value));
                queue.add(curr.right);
            }
        }

        return root;
    }

    @Test
    public void testSerialize() {
        TreeNode root = new TreeNode(100);
        Assertions.assertEquals("[]", serialize(null));
        Assertions.assertEquals("[100,null,null]", serialize(root));
    }

}


class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}