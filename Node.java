package com.company;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
    //List of input, output and hidden nodes connected to it
    private double value;
    private ArrayList<Connection> inConnections;
    private int type;
    private int index;

    public Node(int type, int index){
        this.type = type;
        this.inConnections = new ArrayList<>();
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Connection> getInConnections() {
        return inConnections;
    }

    public void addInConnection(Connection connection){
        this.inConnections.add(connection);
    }

    public int getIndex(){
        return index;
    }
}
