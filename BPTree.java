/**
 * Filename:   BPTree.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import java.util.*;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set.
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;

    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;


    /**
     * Public constructor
     *
     * @param branchingFactor
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
                    "Illegal branching factor: " + branchingFactor);
        }
        this.branchingFactor = branchingFactor;
        // initialized the root
        root = new LeafNode();

    }


    /**
     * insert key and value
     * @param key
     * @param value
     */
    @Override
    public void insert(K key, V value) {
        // call insert of the root
        root.insert(key, value);
    }


    /**
     * rangeSearch nutrients based on the pass in comparator
     *
     * @param key to be searched
     * @param comparator is a string
     * @return recursively call the rangeSearch to reach the correct LeafNode
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") &&
                !comparator.contentEquals("==") &&
                !comparator.contentEquals("<="))
            return new ArrayList<V>();

        // if key is null, it returns a empty list
        if ( key == null){
            return new ArrayList<V>();
        }
        return root.rangeSearch(key, comparator);
    }


    /**
     * Convert the tree into String
     *
     * @return returns a string format of BPTree
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }


    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     *
     * @author sapan
     */
    private abstract class Node {

        // List of keys
        List<K> keys;

        int key_num() {
            return keys.size();
        }

        /**
         * Package constructor
         */
        Node() {
            keys = new ArrayList<>();
        }

        /**
         * Inserts key and value in the appropriate leaf node
         * and balances the tree if required by splitting
         *
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         *
         * @return key
         */
        abstract K getFirstLeafKey();

        /**
         * Gets the new sibling created after splitting the node
         *
         * @return Node
         */
        abstract Node split();

        /**
         * to do the search
         *
         * @param key
         * @param comparator
         * @return
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * to check if the node is overloaded
         *
         * @return boolean value
         */
        abstract boolean isOverflow();

        public String toString() {
            return keys.toString();
        }

    } // End of abstract class Node

    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     *
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;

        /**
         * Package constructor
         */
        InternalNode() {
            super();
            children = new ArrayList<>();
        }

        /**
         * To get the first leaf's key
         *
         * @return the key of the first leaf
         */
        K getFirstLeafKey() {
            return children.get(0).keys.get(0);
        }

        /**
         * To check if the node is full
         *
         * @return return true or false to identify the node is full or not
         */
        boolean isOverflow() {

            if (children.size() > branchingFactor) {
                return true;
            }
            return false;
        }


        /**
         * helper to get the correct child when it is looking for the correct place to do the insert
         *
         * @param key
         * @return the correct child
         */
        private Node getChild(K key) {

            // binarySearch for the correct index
            int correct_place = Collections.binarySearch(keys, key);
            int child_indexing;
            if (correct_place >= 0) {
                child_indexing = correct_place + 1;
            } else {
                child_indexing = -correct_place - 1;
            }

            return children.get(child_indexing);
        }

        /**
         * helper for insert keys into internal node
         *
         * @param key
         * @param child
         */
        private void insertChild(K key, Node child) {

            // binary search for correct index
            int correct_place = Collections.binarySearch(keys, key);
            int child_indexing;

            // to check if the key is already in there and get the correct index after using collections.binarySearch
            if (correct_place >= 0) {
                child_indexing = correct_place + 1;
            } else {
                child_indexing = -correct_place - 1;
            }

            if (correct_place >= 0) {
                keys.add(child_indexing, key);
                children.add(child_indexing, child);
            } else {
                keys.add(child_indexing, key);
                children.add(child_indexing + 1, child);
            }
        }

        /**
         * To insert the correct place for internal node
         *
         * @param key
         * @param value
         */
        void insert(K key, V value) {


            Node child = getChild(key);
            child.insert(key, value);
            // to check if the child is overloaded
            if (child.isOverflow()) {
                Node sibling = child.split();
                insertChild(sibling.getFirstLeafKey(), sibling);
            }

            // if the node is full then it requires to split
            if (root.isOverflow()) {
                Node sibling = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(sibling.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(sibling);
                root = newRoot;
            }

        }

        /**
         * to split the full node
         *
         * @return the newly created sibling after split
         */
        Node split() {

            // to do the split operation
            // to get the correct child to promote to the internal node
            int from = key_num() / 2 + 1, to = key_num();
            InternalNode sibling = new InternalNode();
            sibling.keys.addAll(keys.subList(from, to));
            sibling.children.addAll(children.subList(from, to + 1));

            keys.subList(from - 1, to).clear();
            children.subList(from, to + 1).clear();

            return sibling;
        }

        /**
         * to do the range search
         *
         * @param key
         * @param comparator
         * @return
         */
        List<V> rangeSearch(K key, String comparator) {

            return getChild(key).rangeSearch(key, comparator);
        }

    } // End of class InternalNode


    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     *
     * @author sapan
     */
    private class LeafNode extends Node {

        // List of values
        List<V> values;

        // Reference to the next leaf node
        LeafNode next;

        // Reference to the previous leaf node
        LeafNode previous;

        /**
         * Package constructor
         */
        LeafNode() {
            super();
            values = new ArrayList<>();
        }


        /**
         * to get the first key of the leaf
         *
         * @return
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }

        /**
         * to check if the node is overloaded
         *
         * @return boolean value
         */
        boolean isOverflow() {

            if (values.size() > branchingFactor - 1) {
                return true;
            }
            return false;
        }

        /**
         * to insert the value into correct leafnode
         *
         * @param key
         * @param value
         */
        void insert(K key, V value) {
            // binary search
            int correct_place = Collections.binarySearch(keys, key);
            int indexing;
            if (correct_place >= 0) {
                indexing = correct_place;
            } else {
                indexing = -correct_place - 1;
            }

            if (correct_place >= 0) {
                keys.add(indexing, key);
                values.add(indexing, value);
            } else {
                keys.add(indexing, key);
                values.add(indexing, value);
            }

            // to check if the node is overloaded then split
            if (root.isOverflow()) {
                Node sibling = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(sibling.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(sibling);
                root = newRoot;
            }
        }

        /**
         * to split the leafnode
         *
         * @return the newly created sibling node
         */
        Node split() {
            LeafNode Sibling = new LeafNode();
            int index = (branchingFactor + 1) / 2;
            int to = key_num();
            Sibling.keys.addAll(keys.subList(index, to));
            Sibling.values.addAll(values.subList(index, to));

            keys.subList(index, to).clear();
            values.subList(index, to).clear();


            // to make connections between the old node and the new node
            Sibling.next = next;
            Sibling.previous = this;
            // the current's next may be null so to check on that
            if (next != null) {
                next.previous = Sibling;
            }
            next = Sibling;

            return Sibling;
        }


        /**
         * To do the range search for all the leafnode and look for the values that users want
         *
         * @param key
         * @param comparator
         * @return
         */
        List<V> rangeSearch(K key, String comparator) {

            // linked list for return
            List<V> val = new LinkedList<>();
            LeafNode node_next = this;
            LeafNode node_prev = this.previous;

            // to check the current node's next nodes first
            while (node_next != null) {
                if (comparator.equals("<=")) {
                    if (node_next.getFirstLeafKey().compareTo(key) > 0) {
                        node_next = node_next.next;
                        continue;
                    }else{
                        compare((K) key, comparator, (List<V>) val, node_next);
                        node_next = node_next.next;
                    }
                } else if (comparator.equals(">=")){
                    if (node_next.getFirstLeafKey().compareTo(key) < 0) {
                        node_next = node_next.next;
                        continue;
                    }
                    else{
                        compare((K) key, comparator, (List<V>) val, node_next);
                        node_next = node_next.next;
                    }
                } else if ( comparator.equals("==")){
                    if (node_next.getFirstLeafKey().compareTo(key) > 0){
                        node_next = node_next.next;
                        continue;
                    } else {
                        compare((K) key, comparator, (List<V>) val, node_next);
                        node_next = node_next.next;
                    }
                }
            }

            // to check the previous nodes
            while (node_prev != null) {
                if (comparator.equals("<=")) {
                    if (node_prev.getFirstLeafKey().compareTo(key) > 0) {
                        node_prev = node_prev.previous;
                        continue;
                    }else{
                        compare((K) key, comparator, (List<V>) val, node_prev);
                        node_prev = node_prev.previous;
                    }
                } else if (comparator.equals(">=")){
                    if (node_prev.getFirstLeafKey().compareTo(key) < 0) {
                        node_prev = node_prev.previous;
                        continue;
                    }
                    else{
                        compare((K) key, comparator, (List<V>) val, node_prev);
                        node_prev = node_prev.previous;
                    }
                } else if ( comparator.equals("==")){
                    if (node_prev.getFirstLeafKey().compareTo(key) > 0){
                        node_prev = node_prev.previous;
                        continue;
                    } else {
                        compare((K) key, comparator, (List<V>) val, node_prev);
                        node_prev = node_prev.previous;
                    }
                }


            }

            return val;
        }

        /**
         * a helper method for the search where it compares all the values within a leafnode
         *
         * @param key
         * @param comparator
         * @param val
         * @param node
         */
        private void compare(K key, String comparator, List<V> val, LeafNode node) {
            if (comparator.contentEquals(">=")) {
                // to check every value in the leafnode
                for (V value : node.values) {
                    int index = node.values.indexOf(value);
                    if (key.compareTo(node.keys.get(index)) == 0 ) {
                        val.add(value);
                    } else if (key.compareTo(node.keys.get(index)) < 0){
                        val.add(value);
                    }
                }

            } else if (comparator.contentEquals("==")) {
                for (V value : node.values) {
                    int index = node.values.indexOf(value);
                    if (key.compareTo(node.keys.get(index)) == 0) {
                        val.add(value);
                    }
                }

            } else if (comparator.contentEquals("<=")) {
                for (V value : node.values) {
                    int index = node.values.indexOf(value);
                    if (key.compareTo(node.keys.get(index)) == 0 || key.compareTo(node.keys.get(index)) > 0) {
                        val.add(value);
                    }
                }

            }
        }

    } // End of class LeafNode


    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     *
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, "==");
        System.out.println("Filtered values: " + filteredValues.toString());
        System.out.println("#filetered items: " + (filteredValues.size()));
    }

} // End of class BPTree
