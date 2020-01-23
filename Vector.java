package com.company;

public class Vector {
    private int x;
    private int y;

    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vector substract(Vector v){
        return new Vector(this.x - v.getX(), this.y - v.getY());
    }

    public Vector add(Vector v){
        return new Vector(this.x + v.getX(), this.y + v.getY());
    }

    public void addX(int x){
        this.x += x;
    }

    public void addY(int y){
        this.y += y;
    }

    public void updateX(int x){
        this.x = x;
    }

    public void updateY(int y){
        this.y = y;
    }
}
