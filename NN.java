package com.company;

public interface NN {

    abstract void feedInput(double doodleX, double groundX, double groundSpeed, double groundWidth, double upperPlatformX, double upperPlatformSpeed, double upperPlatFormWidth);
    abstract double[] feedForward();
    abstract void init();
    abstract int getHidden();
    abstract void mutate();
}
