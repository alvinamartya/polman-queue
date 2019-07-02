package com.example.polmanqueue.HashTable;

public class SLinkedList {
    private Node head;
    private Node tail;
    private int size;

    public void addData(Node node){
        if(!isEmpty()) {
            node.setNextPref(head);
            head = node;
        } else {
            node.setNextPref(null);
            tail = node;
            head = node;
        }
        ++this.size;
    }

    public int getIndex(String serviceNumb){
        Node pointer = head;
        for(int i = 0; i < size;i++){
            if(pointer.getServiceNumb().equals(serviceNumb)) return (i+1);
            pointer = pointer.getNextPref();
        }
        return 0;
    }

    public int getSize(){
        return size;
    }

    public void getData(){
        Node pointer = head;
        for(int i = 0; i < size;i++){
            pointer = pointer.getNextPref();
        }
    }

    public Node getNode(int index){
        Node pointer = head;
        for(int i = 0; i < size;i++){
            if(i == index) return pointer;
            pointer = pointer.getNextPref();
        }
        return null;
    }

    public void deleteData(String serviceNumb)
    {
        if(!isEmpty())
        {
            int getIndex = getIndex(serviceNumb);
            if(getIndex > 0)
            {
                if(getIndex == 1) {
                    if(size > 1){
                        head = head.getNextPref();
                    } else{
                        head = null;
                        tail = null;
                    }
                } else if (getIndex == size) {
                    Node beforeNode = getNode(getIndex - 2);
                    tail = beforeNode;
                } else {
                    Node beforeNode = getNode(getIndex - 2);
                    Node curNode = getNode(getIndex - 1);
                    beforeNode.setNextPref(curNode.getNextPref());
                }

                size--;
            } else {
                System.out.println("Data not found");
            }
        } else {
            System.out.println("Data is empty");
        }
    }

    public boolean isEmpty(){
        return (this.head == null) && (this.tail == null);
    }
}
