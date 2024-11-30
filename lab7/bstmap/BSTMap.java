package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    /**
     * the root of tree
     */
    private BSTNode root;

    public BSTMap() {
        root = null;
    }
    private int size;
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }

        if (root.get(key) != null) {
            return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        BSTNode node = root.get(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value, null, null);
        } else {
            BSTNode node = root;
            while (node != null) {
                int cmp = key.compareTo(node.key);
                if (cmp < 0) {
                    if (node.left == null) {
                        node.left = new BSTNode(key, value, null, null);
                        break;
                    } else {
                        node = node.left;
                    }
                } else if (cmp > 0) {
                    if (node.right == null) {
                        node.right = new BSTNode(key, value, null, null);
                        break;
                    } else {
                        node = node.right;
                    }
                } else {
                    node.value = value;
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        printBSTNode(root);
    }

    private void printBSTNode(BSTNode node) {
        if (node != null) {
            return;
        }
        printBSTNode(node.left);
        System.out.println(node.key);
        printBSTNode(node.right);
    }

    private class BSTNode {
        BSTNode left, right;
        K key;
        V value;
        BSTNode(K key, V value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        BSTNode get(K key) {
            if (key.equals(this.key)) {
                return this;
            }
            /**
             * means key < this.key
             */
            if (key.compareTo(this.key) < 0) {
                return left.get(key);
            } else {
                return right.get(key);
            }
        }
    }
}
