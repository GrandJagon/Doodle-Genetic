package com.company;

import java.awt.*;

public class Camera {
    private int position;
    private int oldPosition;
    private int newPosition;
    private int distToNextPlatForm = Constants.PLATFORM_POP_RATIO;
    private World world;
    private int total_distance;
    private float tempX;
    private boolean lock;


    public Camera(World world){
        position = Constants.FRAME_HEIGHT;
        tempX = 0;
        total_distance = 0;
        this.world = world;
        lock = false;
    }

    public int getBottom(){
        return position;
    }

    public int getTop(){
        return position - Constants.FRAME_HEIGHT;
    }

    public void update(Doodle d) {
        if (d.getPosition().getY() >= Constants.FRAME_HEIGHT / 2 && !lock) {
            position = Constants.FRAME_HEIGHT;
        } else {
            if(!lock){lock = true;}
            newPosition = d.getPosition().getY() + Constants.FRAME_HEIGHT / 2;
            if (position <= newPosition) {
                return;
            } else {
                oldPosition = position;
                position = newPosition;
                total_distance = total_distance + (oldPosition - newPosition);
                distToNextPlatForm -= (oldPosition - newPosition);
                if (distToNextPlatForm <= 0) {
                    System.out.println("Calling addPlatform()");
                    world.addPlatform();
                    distToNextPlatForm = Constants.PLATFORM_POP_RATIO;
                }
            }
        }
    }

    public void reset(){
        position = Constants.FRAME_HEIGHT;
        tempX = 0;
        total_distance = 0;
        lock = false;
    }


}
