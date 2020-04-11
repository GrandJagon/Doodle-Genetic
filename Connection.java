package com.company;

public class Connection {
    private int inNode;
    private int outNode;
    private double weight;
    private boolean enabled;
    private int innovation_number;

    public Connection(int inNode, int outNode, double weight, int innovation_number){
        this.inNode = inNode;
        this.outNode = outNode;
        this.weight = weight;
        this.innovation_number = innovation_number;
        this.enabled = true;
    }

    public void enable(){
        if(!enabled){
            enabled = true;
        }
    }

    public void disable(){
        if(enabled){
            enabled = false;
        }
    }

    public void setActivation(boolean activation){
        this.enabled = activation;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public int getInNode(){
        return inNode;
    }

    public int getOutNode(){
        return outNode;
    }

    public void setInNode(int inNode) {
        this.inNode = inNode;
    }

    public void setOutNode(int outNode) {
        this.outNode = outNode;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public double getWeight(){
        return weight;
    }

    public int getInnovation_number(){
        return innovation_number;
    }

    public Connection copy(){
        Connection copy = new Connection(this.inNode, this.outNode, this.weight, this.innovation_number);
        return copy;
    }
}
