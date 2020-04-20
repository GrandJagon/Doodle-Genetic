package com.company;

import java.awt.*;

public interface Constants {
    static final int FRAME_WIDTH = 500;
    static final int FRAME_HEIGHT = 700;
    static final int GRAVITY = 7;
    static final int DOODLE_SPEED = 5;
    static final int JUMP_FORCE = 11;
    static final int DOODLE_WEIGHT = 3;
    static final int TIMER = 1000;
    static final int PLATFORM_MINIMUM_WIDTH = 15;
    static final int PLATFORM_MAXIMUM_WIDTH = 80;
    static final int PLATFORM_MINIMUM_SPEED = 2;
    static final int PLATFORM_MAXIMUM_SPEED = 10;
    static final int PLATFORM_POP_RATIO = 60;
    static final int INPUT_NODES = 7;
    static final int OUTPUT_NODES = 3;
    static final int BASE_NODES = INPUT_NODES + OUTPUT_NODES;
    static final double WEIGHT_CHANGE_CHANCE = 0.5;
    static final double WEIGHT_MUTATION_RATE = 0.2;
    static final double ADDING_NODE_CHANCE = 0.2;
    static final double ADDING_CONNECTION_CHANCE = 0.2;
    static final double DISABLING_CONNECTION_CHANCE = 0.2;
    static final boolean AUTOMATIC = true;
    static final Font TIMER_FONT = new Font("COURIER", Font.BOLD, 14);
    static final int DAHSBOARD_WIDTH = 500;
    static final int DASHBOARD_HEIGHT = 700;
}
