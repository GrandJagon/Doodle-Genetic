package com.company;

import java.awt.*;

public interface Element {
    abstract void paint(Graphics2D g, Camera c);
    abstract void update();
    abstract boolean isOnScreen(Camera camera);
    abstract void remove();
}
