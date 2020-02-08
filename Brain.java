package com.company;

import java.util.Random;

public class Brain implements NN {
    private int hidden_layers;
    private int hidden_layer_size;
    private int input_layer_size;
    private int output_layer_size;
    private double[][] w1;
    private double [][] w2;
    private double[][] X;
    private double[][] Y;
    private double[][] b1;
    private double[][] b2;
    private Random rand;

    public Brain(int hidden_layer_size){
        this.hidden_layer_size = hidden_layer_size;
        this.hidden_layers = 1;
        this.input_layer_size = 7;
        this.output_layer_size = 3;
        this.rand = new Random();
        init();
    }

    public double[][] getW1(){
        return w1;
    }

    public double[][] getW2(){
        return w2;
    }

    public double[][] getB1(){ return b1; }

    public double[][] getB2(){ return b2; }

    public int getHidden(){
        return hidden_layer_size;
    }

    public int getOutput_layer_size(){
        return output_layer_size;
    }

    public int getW1Features(){
        return input_layer_size * hidden_layer_size;
    }

    public int getW2Features(){
        return hidden_layer_size * output_layer_size;
    }

    public int getTotal_features(){
        return (input_layer_size * hidden_layer_size) + (hidden_layer_size * output_layer_size);
    }

    //Random init method for the first generation
    public void init(){
        this.X = new double[1][input_layer_size];
        this.Y = new double[1][output_layer_size];
        this.w1 = Calc.randMatrix(input_layer_size, hidden_layer_size, -1, 1);
        this.w2 = Calc.randMatrix(hidden_layer_size, output_layer_size, -1 , 1);
        this.b1 = Calc.biasMatrix(1,hidden_layer_size, 1);
        this.b2 = Calc.biasMatrix(1, output_layer_size, 1);
    }


    //Init method with external paramters in case of breeding between 2 individuals
    public void init(double[][] w1, double[][] w2, double[][] b1, double[][] b2){
        this.X = new double[1][input_layer_size];
        this.Y = new double[1][output_layer_size];
        this.w1 = w1;
        this.w2 = w2;
        this.b1 = b1;
        this.b2 = b2;

    }

    //Neural network forward propagation method that returns a set of outputs to be used within the doodle class
    public double[] feedForward(){
        double[][] Z1 = Calc.add(Calc.dot(X, w1), b1);
        double[][] A1 = Calc.sigmoid(Z1);

        double[][] Z2 = Calc.add(Calc.dot(A1, w2), b2);
        Y = Calc.sigmoid(Z2);

        return Y[0];
    }

    //Method that feeds all data relative to doodle environment in order to feed the forward propagation
    public void feedInput(double doodleX, double groundX, double groundSpeed, double groundWidth, double upperPlatformX, double upperPlatformSpeed, double upperPlatFormWidth){
        X[0][0] = doodleX;
        X[0][1] = groundX;
        X[0][2] = groundSpeed;
        X[0][3] = groundWidth;
        X[0][4] = upperPlatformX;
        X[0][5] = upperPlatformSpeed;
        X[0][6] = upperPlatFormWidth;
    }

    public void mutate(){

    }





}
