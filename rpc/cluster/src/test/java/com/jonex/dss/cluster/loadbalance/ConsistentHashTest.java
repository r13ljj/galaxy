package com.jonex.dss.cluster.loadbalance;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <pre>
 *
 *  File: ConsistentHashTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/19				lijunjun				Initial.
 *
 * </pre>
 */
public class ConsistentHashTest {

    public static void main(String[] args) {

        //NodeArray nodeArray = new CommonHashNodeArray();
        NodeArray nodeArray = new ConsistentHashNodeArray();

        Node[] nodes = {
                new Node("Node--> 1"),
                new Node("Node--> 2"),
                new Node("Node--> 3")
        };

        for (Node node : nodes) {
            nodeArray.addNode(node);
        }

        Obj[] objs = {
                new Obj("1"),
                new Obj("2"),
                new Obj("3"),
                new Obj("4"),
                new Obj("5")
        };

        for (Obj obj : objs) {
            nodeArray.put(obj);
        }

        validate(nodeArray, objs);
    }

    private static void validate(NodeArray nodeArray, Obj[] objs) {
        for (Obj obj : objs) {
            System.out.println(nodeArray.get(obj));
        }

        nodeArray.addNode(new Node("anything1"));
        nodeArray.addNode(new Node("anything2"));

        System.out.println("========== after  =============");

        for (Obj obj : objs) {
            System.out.println(nodeArray.get(obj));
        }
    }


    static class Obj {
        String key;
        Obj(String key) {
            this.key = key;
        }
        @Override
        public int hashCode() {
            return key.hashCode();
        }
        @Override
        public String toString() {
            return "Obj{" +
                    "key='" + key + '\'' +
                    '}';
        }
    }

    static class Node {

        Map<Integer, Obj> node = new HashMap<>();
        String name;

        Node(String name) {
            this.name = name;
        }

        public void putObj(Obj obj) {
            node.put(obj.hashCode(), obj);
        }

        Obj getObj(Obj obj) {
            return node.get(obj.hashCode());
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    interface NodeArray{
        void addNode(Node node);

        Obj get(Obj obj);

        void put(Obj obj);
    }

    static class CommonHashNodeArray implements NodeArray{

        Node[] nodes = new Node[1024];
        int size = 0;

        public void addNode(Node node) {
            nodes[size++] = node;
        }

        public Obj get(Obj obj) {
            int index = obj.hashCode() % size;
            return nodes[index].getObj(obj);
        }

        public void put(Obj obj) {
            int index = obj.hashCode() % size;
            nodes[index].putObj(obj);
        }
    }

    static class ConsistentHashNodeArray implements NodeArray{

        /** 按照 键 排序*/
        TreeMap<Integer, Node> nodes = new TreeMap<>();

        public void addNode(Node node) {
            nodes.put(node.hashCode(), node);
        }

        public void put(Obj obj) {
            //可以用1.加密算法MD5 CRC32 SHA-1 2.murmur 算hash
            int objHashcode = obj.hashCode();
            Node node = nodes.get(objHashcode);
            if (node != null) {
                node.putObj(obj);
                return;
            }

            // 找到比给定 key 大的集合
            SortedMap<Integer, Node> tailMap = nodes.tailMap(objHashcode);
            // 找到最小的节点
            int nodeHashcode = tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey();
            nodes.get(nodeHashcode).putObj(obj);

        }

        public Obj get(Obj obj) {
            Node node = nodes.get(obj.hashCode());
            if (node != null) {
                return node.getObj(obj);
            }

            // 找到比给定 key 大的集合
            SortedMap<Integer, Node> tailMap = nodes.tailMap(obj.hashCode());
            // 找到最小的节点
            int nodeHashcode = tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey();
            return nodes.get(nodeHashcode).getObj(obj);
        }
    }



}



