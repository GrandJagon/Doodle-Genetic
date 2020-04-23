package com.company;

import java.awt.*;

public interface Constants {
    static final int WORLD_WIDTH = 500;
    static final int DAHSBOARD_WIDTH = 800;
    static final int FRAME_WIDTH = WORLD_WIDTH + DAHSBOARD_WIDTH;
    static final int FRAME_HEIGHT = 700;
    static final int GRAVITY = 7;
    static final int DOODLE_SPEED = 5;
    static final int JUMP_FORCE = 11;
    static final int DOODLE_WEIGHT = 3;
    static final int TIMER = 1000;
    static final int PLATFORM_MINIMUM_WIDTH = 40;
    static final int PLATFORM_MAXIMUM_WIDTH = 80;
    static final int PLATFORM_MINIMUM_SPEED = 2;
    static final int PLATFORM_MAXIMUM_SPEED = 4;
    static final int PLATFORM_POP_RATIO = 60;
    static final int INPUT_NODES = 7;
    static final int OUTPUT_NODES = 3;
    static final int BASE_NODES = INPUT_NODES + OUTPUT_NODES;
    static final int POOL_SIZE = 100;
    static final double SELECTION_RATE = 0.1;
    static final double WEIGHT_CHANGE_CHANCE = 0.8;
    static final double WEIGHT_MUTATION_RATE = 0.3;
    static final double ADDING_NODE_CHANCE = 0.05;
    static final double ADDING_CONNECTION_CHANCE = 0.1;
    static final double DISABLING_CONNECTION_CHANCE = 0.05;
    static final boolean AUTOMATIC = true;
    static final Font TIMER_FONT = new Font("COURIER", Font.BOLD, 14);

}
