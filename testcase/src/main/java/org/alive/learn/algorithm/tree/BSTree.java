package org.alive.learn.algorithm.tree;

import java.util.Objects;

/**
 * Java 语言: 二叉查找树
 *
 * @author skywang
 * @date 2013/11/07
 * @see http://www.cnblogs.com/skywang12345/p/3576452.html
 */

public class BSTree<K extends Comparable<K>, V> {

	private BSTNode<K, V> root; // 根结点

	static final class BSTNode<T extends Comparable<T>, V> {
		T key; // 关键字(键值)
		V value; // 存储的实际数据
		BSTNode<T, V> left; // 左孩子
		BSTNode<T, V> right; // 右孩子
		BSTNode<T, V> parent; // 父结点

		public BSTNode(T key, V value, BSTNode<T, V> left, BSTNode<T, V> right, BSTNode<T, V> parent) {
			super();
			this.key = key;
			this.value = value;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}

		public final T getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public String toString() {
			return "key=" + key;
		}

		public final int hashCode() {
			return Objects.hashCode(key) ^ Objects.hashCode(value);
		}

		/**
		 * 参考HashMap.Node实现equals方法
		 * 
		 * @see java.util.HashMap.Node<K, V>
		 */
		public final boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof BSTNode) {
				BSTNode<?, ?> e = (BSTNode<?, ?>) o;
				if (Objects.equals(e.getKey(), key) && Objects.equals(e.getValue(), value)) {
					return true;
				}
			}
			return false;
		}

	}

	public BSTree() {
		root = null;
	}

	/**
	 * 前序遍历"二叉树"
	 */
	private void preOrder(BSTNode<K, V> tree) {
		if (tree != null) {
			System.out.print(tree.key + " ");
			preOrder(tree.left);
			preOrder(tree.right);
		}
	}

	public void preOrder() {
		preOrder(root);
	}

	/**
	 * 中序遍历"二叉树"
	 */
	private void inOrder(BSTNode<K, V> tree) {
		if (tree != null) {
			inOrder(tree.left);
			System.out.print(tree.key + " ");
			inOrder(tree.right);
		}
	}

	public void inOrder() {
		inOrder(root);
	}

	/**
	 * 后序遍历"二叉树"
	 */
	private void postOrder(BSTNode<K, V> tree) {
		if (tree != null) {
			postOrder(tree.left);
			postOrder(tree.right);
			System.out.print(tree.key + " ");
		}
	}

	public void postOrder() {
		postOrder(root);
	}

	/**
	 * (递归实现)查找"二叉树x"中键值为key的节点
	 */
	private BSTNode<K, V> search(BSTNode<K, V> x, K key) {
		if (x == null)
			return x;

		int cmp = key.compareTo(x.key);
		if (cmp < 0)
			return search(x.left, key);
		else if (cmp > 0)
			return search(x.right, key);
		else
			return x;
	}

	public BSTNode<K, V> search(K key) {
		return search(root, key);
	}

	/**
	 * (非递归实现)查找"二叉树x"中键值为key的节点
	 */
	private BSTNode<K, V> iterativeSearch(BSTNode<K, V> x, K key) {
		while (x != null) {
			int cmp = key.compareTo(x.key);

			if (cmp < 0)
				x = x.left;
			else if (cmp > 0)
				x = x.right;
			else
				return x;
		}

		return x;
	}

	public BSTNode<K, V> iterativeSearch(K key) {
		return iterativeSearch(root, key);
	}

	/*
	 * 查找最小结点：返回tree为根结点的二叉树的最小结点。
	 */
	private BSTNode<K, V> minimum(BSTNode<K, V> tree) {
		if (tree == null)
			return null;

		while (tree.left != null)
			tree = tree.left;
		return tree;
	}

	public K minimum() {
		BSTNode<K, V> p = minimum(root);
		if (p != null)
			return p.key;

		return null;
	}

	/*
	 * 查找最大结点：返回tree为根结点的二叉树的最大结点。
	 */
	private BSTNode<K, V> maximum(BSTNode<K, V> tree) {
		if (tree == null)
			return null;

		while (tree.right != null)
			tree = tree.right;
		return tree;
	}

	public K maximum() {
		BSTNode<K, V> p = maximum(root);
		if (p != null)
			return p.key;

		return null;
	}

	/*
	 * 找结点(x)的后继结点。即，查找"二叉树中数据值大于该结点"的"最小结点"。
	 */
	public BSTNode<K, V> successor(BSTNode<K, V> x) {
		// 如果x存在右孩子，则"x的后继结点"为 "以其右孩子为根的子树的最小结点"。
		if (x.right != null)
			return minimum(x.right);

		// 如果x没有右孩子。则x有以下两种可能：
		// (01) x是"一个左孩子"，则"x的后继结点"为 "它的父结点"。
		// (02) x是"一个右孩子"，则查找"x的最低的父结点，并且该父结点要具有左孩子"，找到的这个"最低的父结点"就是"x的后继结点"。
		BSTNode<K, V> y = x.parent;
		while ((y != null) && (x == y.right)) {
			x = y;
			y = y.parent;
		}

		return y;
	}

	/*
	 * 找结点(x)的前驱结点。即，查找"二叉树中数据值小于该结点"的"最大结点"。
	 */
	public BSTNode<K, V> predecessor(BSTNode<K, V> x) {
		// 如果x存在左孩子，则"x的前驱结点"为 "以其左孩子为根的子树的最大结点"。
		if (x.left != null)
			return maximum(x.left);

		// 如果x没有左孩子。则x有以下两种可能：
		// (01) x是"一个右孩子"，则"x的前驱结点"为 "它的父结点"。
		// (01) x是"一个左孩子"，则查找"x的最低的父结点，并且该父结点要具有右孩子"，找到的这个"最低的父结点"就是"x的前驱结点"。
		BSTNode<K, V> y = x.parent;
		while ((y != null) && (x == y.left)) {
			x = y;
			y = y.parent;
		}

		return y;
	}

	/*
	 * 将结点插入到二叉树中
	 *
	 * 参数说明： tree 二叉树的 z 插入的结点
	 */
	private void insert(BSTree<K, V> bst, BSTNode<K, V> z) {
		int cmp;
		BSTNode<K, V> y = null;
		BSTNode<K, V> x = bst.root;

		// 查找z的插入位置
		while (x != null) {
			y = x;
			cmp = z.key.compareTo(x.key);
			if (cmp < 0)
				x = x.left;
			else
				x = x.right;
		}

		z.parent = y;
		if (y == null)
			bst.root = z;
		else {
			cmp = z.key.compareTo(y.key);
			if (cmp < 0)
				y.left = z;
			else
				y.right = z;
		}
	}

	/*
	 * 新建结点(key)，并将其插入到二叉树中
	 *
	 * 参数说明： tree 二叉树的根结点 key 插入结点的键值
	 */
	public void insert(K key, V value) {
		insert(this, new BSTNode<K, V>(key, value, null, null, null));
	}

	/**
	 * <p>
	 * 删除结点(z)，并返回被删除的结点；二叉树的删除分为三种情况：
	 * <ol>
	 * <li>z为叶子节点，直接删除该节点，再修改其父节点的指针（注意分是根节点和不是根节点）；</li>
	 * 
	 * <li>z为单支节点（即只有左子树或右子树）。让z的子树与p的父亲节点相连，删除z即可；（注意分是根节点和不是根节点）；</li>
	 * 
	 * <li>z的左子树和右子树均不空。找到z的后继y，因为y一定没有左子树，所以可以删除y，并让y的父亲节点成为y的右子树的父亲节点，并用y的值代替z的值；或者方法二是找到z的前驱x，x一定没有右子树，所以可以删除x，并让x的父亲节点成为y的左子树的父亲节点；</li>
	 * </ol>
	 * </p>
	 * 
	 * @param bst
	 *            二叉树
	 * 
	 * @param z
	 *            删除的结点
	 * @return
	 */
	private BSTNode<K, V> remove(BSTree<K, V> bst, BSTNode<K, V> z) {
		BSTNode<K, V> x = null;
		BSTNode<K, V> y = null;

		// 找到需要删除的节点(z本身或者z的后继节点)，用y缓存
		if ((z.left == null) || (z.right == null))
			y = z;
		else
			y = successor(z);

		// 用x缓存待删除结点y的子节点，只可能有0个或者1个子节点
		if (y.left != null)
			x = y.left;
		else
			x = y.right;

		// 调整y子节点x，使x.parent=y.parent
		if (x != null)
			x.parent = y.parent;

		// 调整y.parent，使其孩子结点指向x
		// y.parent为null,表示是删除根节点，则用y节点的唯一孩子x作为根节点
		if (y.parent == null)
			bst.root = x;
		else if (y == y.parent.left)
			y.parent.left = x;
		else
			y.parent.right = x;
		
		// FIXME: 上面删除操作完成，下面进行替换操作，如果必要的话

		// y != z，表示删除的节点下面有左右子树，此时应该删除z，并用z的后续节点y代替z，调整y的子节点成y.parent的子节点；
		// 实际上就相当于把后续节点y删除，然后将y的值赋值给z
		if (y != z) {
			z.key = y.key;
			z.value = y.value;
		}

		return y;
	}

	/*
	 * 删除结点(z)，并返回被删除的结点
	 *
	 * 参数说明： tree 二叉树的根结点 z 删除的结点
	 */
	public V remove(K key) {
		BSTNode<K, V> z;
		// BSTNode<T, V> node;

		if ((z = search(root, key)) != null) {
			V value = z.getValue();
			remove(this, z);
			return value;
		}

		return null;
	}

	/*
	 * 销毁二叉树
	 */
	private void destroy(BSTNode<K, V> tree) {
		if (tree == null)
			return;

		if (tree.left != null)
			destroy(tree.left);
		if (tree.right != null)
			destroy(tree.right);

		tree = null;
	}

	public void clear() {
		destroy(root);
		root = null;
	}

	/*
	 * 打印"二叉查找树"
	 *
	 * key -- 节点的键值 direction -- 0，表示该节点是根节点; -1，表示该节点是它的父结点的左孩子;
	 * 1，表示该节点是它的父结点的右孩子。
	 */
	private void print(BSTNode<K, V> tree, K key, int direction) {

		if (tree != null) {

			if (direction == 0) // tree是根节点
				System.out.printf("%2d is root -- value: %6s\n", tree.key, tree.value);
			else // tree是分支节点
				System.out.printf("%2d is %2d's %6s child -- value: %6s\n", tree.key, key,
						direction == 1 ? "right" : "left", tree.value);

			print(tree.left, tree.key, -1);
			print(tree.right, tree.key, 1);
		}
	}

	public void print() {
		if (root != null)
			print(root, root.key, 0);
	}
}
