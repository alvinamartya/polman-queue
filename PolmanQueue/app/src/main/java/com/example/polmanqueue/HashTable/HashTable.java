package com.example.polmanqueue.HashTable;

public class HashTable {
    private static final int defaultHashTableSize = 3;
    private SLinkedList linkedList[];
    private int size;

    public HashTable() {
        linkedList = new SLinkedList[defaultHashTableSize];
    }

    private int hash(String role) {
        int key = 0;
        for(int i = 0; i < role.length(); i++) {
            key += (int)role.charAt(i);
        }

        return key%3;
    }

    public String getLastData(String role) {
        if(role.equals("Reguler")) role = "";
        int key = hash(role);
        int Numb = 1;
        if(linkedList[key] != null) {
            String lastIndex = linkedList[key].getNode(0).getServiceNumb();
            Numb = Integer.parseInt(lastIndex) + 1;
        }

        return role + "000".substring(String.valueOf(Numb).length()) + String.valueOf(Numb);
    }

    public void insert(String serviceNumber) {
        String id = serviceNumber.substring(serviceNumber.length() - 3);
        String role = serviceNumber.replace(id,"");
        int key = hash(role);
        if(linkedList[key] == null) linkedList[key] = new SLinkedList();
        linkedList[key].addData(new Node(id));
        ++this.size;
    }

    public void remove(String serviceNumber) {
        String id = serviceNumber.substring(serviceNumber.length() - 3);
        String role = serviceNumber.replace(id,"");
        int key = hash(role);
        if(linkedList[key] != null) linkedList[key].deleteData(id);
        else return;
        --this.size;
    }
}
