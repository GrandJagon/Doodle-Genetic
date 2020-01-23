package com.company;

import java.util.Random;

public class Brain {
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
        this.output_layer_size = 4;
        this.rand = new Random();
        init();
    }

    public double[][] getW1(){
        return w1;
    }

    public double[][] getW2(){
        return w2;
    }

    public int getHidden_layer_size(){
        return hidden_layer_size;
    }

    public void init(){
        this.X = new double[1][input_layer_size];
        this.Y = new double[1][output_layer_size];
        this.w1 = Calc.randMatrix(input_layer_size, hidden_layer_size);
        this.w2 = Calc.randMatrix(hidden_layer_size, output_layer_size);
        this.b1 = Calc.biasMatrix(1,hidden_layer_size, 1);
        this.b2 = Calc.biasMatrix(1, output_layer_size, 0);
    }

    public int feedForward(){
        double[][] Z1 = Calc.add(Calc.dot(X, w1), b1);
        double[][] A1 = Calc.sigmoid(Z1);

        double[][] Z2 = Calc.add(Calc.dot(A1, w2), b2);
        Y = Calc.sigmoid(Z2);

        return Calc.getArrayMaxIndex(Y[0]);
    }

    public void feedInput(double doodleX, double groundX, double groundSpeed, double groundWidth, double upperPlatformX, double upperPlatformSpeed, double upperPlatFormWidth){
        X[0][0] = doodleX;
        X[0][1] = groundX;
        X[0][2] = groundSpeed;
        X[0][3] = groundWidth;
        X[0][4] = upperPlatformX;
        X[0][5] = upperPlatformSpeed;
        X[0][6] = upperPlatFormWidth;
    }





}
