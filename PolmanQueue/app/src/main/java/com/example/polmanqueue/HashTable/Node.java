package com.example.polmanqueue.HashTable;

public class Node {
    private String serviceNumb;
    private Node nextPref;

    public Node(String serviceNumb) {
        this.serviceNumb = serviceNumb;
    }

    public String getServiceNumb() {
        return serviceNumb;
    }

    public void setServiceNumb(String serviceNumb) {
        this.serviceNumb = serviceNumb;
    }

    public Node getNextPref() {
        return nextPref;
    }

    public void setNextPref(Node nextPref) {
        this.nextPref = nextPref;
    }
}
