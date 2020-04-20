package com.company;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JPanel {
    private JLabel score;
    private JLabel individuals;
    private JLabel alive_individuals;
    private JLabel generation;
    private JLabel average_score;
    private JPanel infos;
    private Button startButton;
    private Button stopButton;
    private JPanel buttons;
    private JTable logTable;
    private World world;
    private GeneticPool genePool;

    public Dashboard(World world){
        this.world = world;
        setVisible(true);
        setBorder(new BasicBorders.MarginBorder());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void init(GeneticPool genePool){
        this.genePool = genePool;
        score = new JLabel("Best doodle score : ");
        average_score = new JLabel("Average score : ");
        individuals = new JLabel("Number of individuals in the generation :");
        alive_individuals = new JLabel("Number of individuals alive in the generation");
        generation = new JLabel("GENERATION ");
        generation.setHorizontalAlignment(SwingConstants.LEFT);

        this.infos = new JPanel();
        infos.add(generation);
        infos.add(individuals, LEFT_ALIGNMENT);
        infos.add(alive_individuals, LEFT_ALIGNMENT);
        infos.add(score, LEFT_ALIGNMENT);
        infos.add(average_score, LEFT_ALIGNMENT);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setAlignmentX(SwingConstants.LEFT);


        stopButton = new Button("END GENERATION");
        startButton = new Button("LAUNCH NEW GENERATION");

        buttons = new JPanel();
        buttons.add(stopButton, LEFT_ALIGNMENT);
        buttons.add(startButton, RIGHT_ALIGNMENT);

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.gameOver();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.launchNextGeneration();
            }
        });

        add(infos, LEFT_ALIGNMENT);
        add(buttons);

    }

    public void updateDashboard(){
        generation.setText("GENERATION "+world.getGeneration());
        individuals.setText("Number of individuals in the generation : " + genePool.getIndividuals().size());
        alive_individuals.setText("Number of individuals alive in the generation : " + genePool.getAliveDoodles().size());
        score.setText("Best doodle score : " + String.valueOf(world.getBestDoodle().getScore()));
        average_score = new JLabel("Average score : " + genePool.getAverageScore());
    }

    public void reset(){
       removeAll();
       init(genePool);
    }
}
