package org.alive.test.testcase;

import org.alive.learn.algorithm.tree.AVLTree;
import org.alive.learn.algorithm.tree.BSTree;
import org.alive.learn.algorithm.tree.RBTree;
import org.alive.learn.algorithm.tree.SplayTree;
import org.alive.test.core.TestCase;

public class AlgorithmTestCase extends TestCase {

	public AlgorithmTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testBSTree() {
		int arr[] = { 1, 5, 4, 3, 2, 6 };
		// int arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int i, ilen;
		BSTree<Integer, String> tree = new BSTree<Integer, String>();

		System.out.print("== 依次添加: ");
		ilen = arr.length;
		for (i = 0; i < ilen; i++) {
			System.out.print(arr[i] + " ");
			tree.insert(arr[i], arr[i] + "v");
		}

		System.out.print("\n== 前序遍历: ");
		tree.preOrder();

		System.out.print("\n== 中序遍历: ");
		tree.inOrder();

		System.out.print("\n== 后序遍历: ");
		tree.postOrder();
		System.out.println();

		System.out.println("== 最小值: " + tree.minimum());
		System.out.println("== 最大值: " + tree.maximum());
		System.out.println("== 树的详细信息: ");
		tree.print();

		System.out.print("\n== 删除节点: " + arr[3]);
		tree.remove(arr[3]);
		
		System.out.print("\n== 中序遍历: ");
		tree.inOrder();
		System.out.println();
		tree.print();
		System.out.println();
		

		System.out.print("\n== 删除节点: " + arr[1]);
		tree.remove(arr[1]);
		System.out.print("\n== 中序遍历: ");
		tree.inOrder();
		System.out.println();
		tree.print();

		// 销毁二叉树
		tree.clear();
	}

	public void mtestAVLTree() {
		int arr[] = { 3, 2, 1, 4, 5, 6, 7, 16, 15, 14, 13, 12, 11, 10, 8, 9 };
		int i;
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();

		System.out.printf("== 依次添加: ");
		for (i = 0; i < arr.length; i++) {
			System.out.printf("%d ", arr[i]);
			tree.insert(arr[i]);
		}

		System.out.printf("\n== 前序遍历: ");
		tree.preOrder();

		System.out.printf("\n== 中序遍历: ");
		tree.inOrder();

		System.out.printf("\n== 后序遍历: ");
		tree.postOrder();
		System.out.printf("\n");

		System.out.printf("== 高度: %d\n", tree.height());
		System.out.printf("== 最小值: %d\n", tree.minimum());
		System.out.printf("== 最大值: %d\n", tree.maximum());
		System.out.printf("== 树的详细信息: \n");
		tree.print();

		i = 8;
		System.out.printf("\n== 删除根节点: %d", i);
		tree.remove(i);

		System.out.printf("\n== 高度: %d", tree.height());
		System.out.printf("\n== 中序遍历: ");
		tree.inOrder();
		System.out.printf("\n== 树的详细信息: \n");
		tree.print();

		// 销毁二叉树
		tree.destroy();
	}

	public void mtestSplayTree() {
		int arr[] = { 10, 50, 40, 30, 20, 60 };
		int i, ilen;
		SplayTree<Integer> tree = new SplayTree<Integer>();

		System.out.print("== 依次添加: ");
		ilen = arr.length;
		for (i = 0; i < ilen; i++) {
			System.out.print(arr[i] + " ");
			tree.insert(arr[i]);
		}

		System.out.print("\n== 前序遍历: ");
		tree.preOrder();

		System.out.print("\n== 中序遍历: ");
		tree.inOrder();

		System.out.print("\n== 后序遍历: ");
		tree.postOrder();
		System.out.println();

		System.out.println("== 最小值: " + tree.minimum());
		System.out.println("== 最大值: " + tree.maximum());
		System.out.println("== 树的详细信息: ");
		tree.print();

		i = 30;
		System.out.printf("\n== 旋转节点(%d)为根节点\n", i);
		tree.splay(i);
		System.out.printf("== 树的详细信息: \n");
		tree.print();

		// 销毁二叉树
		tree.clear();
	}

	public void mtestRBTree() {
		int a[] = { 10, 40, 30, 60, 90, 70, 20, 50, 80 };
		boolean mDebugInsert = false; // "插入"动作的检测开关(false，关闭；true，打开)
		boolean mDebugDelete = false; // "删除"动作的检测开关(false，关闭；true，打开)
		int i, ilen = a.length;
		RBTree<Integer> tree = new RBTree<Integer>();

		System.out.printf("== 原始数据: ");
		for (i = 0; i < ilen; i++)
			System.out.printf("%d ", a[i]);
		System.out.printf("\n");

		for (i = 0; i < ilen; i++) {
			tree.insert(a[i]);
			// 设置mDebugInsert=true,测试"添加函数"
			if (mDebugInsert) {
				System.out.printf("== 添加节点: %d\n", a[i]);
				System.out.printf("== 树的详细信息: \n");
				tree.print();
				System.out.printf("\n");
			}
		}

		System.out.printf("== 前序遍历: ");
		tree.preOrder();

		System.out.printf("\n== 中序遍历: ");
		tree.inOrder();

		System.out.printf("\n== 后序遍历: ");
		tree.postOrder();
		System.out.printf("\n");

		System.out.printf("== 最小值: %s\n", tree.minimum());
		System.out.printf("== 最大值: %s\n", tree.maximum());
		System.out.printf("== 树的详细信息: \n");
		tree.print();
		System.out.printf("\n");

		// 设置mDebugDelete=true,测试"删除函数"
		if (mDebugDelete) {
			for (i = 0; i < ilen; i++) {
				tree.remove(a[i]);

				System.out.printf("== 删除节点: %d\n", a[i]);
				System.out.printf("== 树的详细信息: \n");
				tree.print();
				System.out.printf("\n");
			}
		}

		// 销毁二叉树
		tree.clear();
	}
}
