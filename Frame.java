package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.System.exit;

public class Frame extends JFrame {

    public Frame(){
        setResizable(false);
        setVisible(true);

        Container con = getContentPane();
        con.setLayout(new BorderLayout());

        World world = new World();
        Dashboard dashboard = new Dashboard(world);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exit(0);
            }
        });



        add(world, BorderLayout.WEST);
        add(dashboard, BorderLayout.CENTER);
        setSize(Constants.FRAME_WIDTH * 2, Constants.FRAME_HEIGHT + 40);
        world.init(dashboard);
    }
}
