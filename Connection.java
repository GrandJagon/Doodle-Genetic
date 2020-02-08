package com.company;

public class Connection {
    private Node input;
    private Node output;
    private double weight;
    private boolean enabled;
    private int innovation_number;

    public Connection(Node input, Node output, double weight, int innovation_number){
        this.input = input;
        this.output = output;
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

    public boolean isEnabled(){
        return enabled;
    }

    public Node getInput(){
        return input;
    }

    public void setInput(Node input){
        this.input = input;
    }

    public Node getOutput(){
        return output;
    }

    public void setOutput(Node output){
        this.output = output;
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
}
