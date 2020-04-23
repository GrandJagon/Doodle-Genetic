package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Platform implements Element {
    private Vector position;
    private int tempY;
    private int width;
    private int height;
    private int speed;
    private int index;
    private ArrayList<Platform> container;

    public Platform(int x, int y, int width, int speed, int index, ArrayList<Platform> container) {
        this.position = new Vector(x, y);
        this.width = width;
        this.height = 10;
        this.speed = speed;
        this.index = index;
        this.container = container;
    }

    public Vector getPosition(){
        return position;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getSpeed(){
        return speed;
    }

    public int getIndex() {return index;}

    public Platform getUpperPlatform(){
        int size = container.size() - 1;

        if(index <= size){
            return container.get(index);
        }else{
            return this;
        }
    }

    public void remove(){
        if(container.contains(this)){
        container.remove(this);
        System.out.println("Platform removed, new size of container is "+container.size());
        }
    }


    @Override
    public void paint(Graphics2D graphics, Camera c) {
        tempY = position.getY() - c.getTop();
        graphics.setPaint(Color.BLACK);
        graphics.drawRect(position.getX(), tempY, width, height);
        graphics.fillRect(position.getX(), tempY, width, height);
    }

    @Override
    public void update() {
        this.position.addX(this.speed);
        if ((this.position.getX() + width) >= Constants.WORLD_WIDTH || this.position.getX() <= 0) {
            this.speed = (-this.speed);
        }
    }

    @Override
    public boolean isOnScreen(Camera camera) {
        if (position.getY() <= camera.getBottom() && position.getY() >= camera.getTop()) {
            return true;
        } else {
            return false;
        }
    }

}
