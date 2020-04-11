package com.company;

import java.util.ArrayList;

public class Node {
    //List of input, output and hidden nodes connected to it
    private double value;
    private ArrayList<Connection> inConnections;
    private int type;
    private int innovation;

    public Node(int index){
        if(index <= Constants.INPUT_NODES){
            type = 0;
        }else if(index > Constants.INPUT_NODES && index <= (Constants.INPUT_NODES + Constants.OUTPUT_NODES)){
            type = 2;
        }else{
            type = 3;
        }
        this.type = type;
        this.inConnections = new ArrayList<>();
        this.innovation = index;
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

    public int getInnovation(){
        return innovation;
    }

    public void setInnovation(int innovation){
        this.innovation = innovation;
    }

    public Node copy(){
        Node copy = new Node(this.innovation);
        return copy;
    }


}
