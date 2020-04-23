package com.company;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Dashboard extends JPanel {
    private JLabel score;
    private JLabel individuals;
    private JLabel alive_individuals;
    private JLabel generation;
    private JLabel average_score;
    private JLabel best_doodle_neurons;
    private JLabel average_neurons;
    private JLabel selection_rate;
    private Border labelBorder;
    private Border elementBorder;
    private JPanel infos;
    private Button startButton;
    private Button stopButton;
    private JPanel buttons;
    private JTable logTable;
    private World world;
    private GeneticPool genePool;
    private DecimalFormat d3f = new DecimalFormat("#.###");
    private DecimalFormat d2f = new DecimalFormat("#.##");

    public Dashboard(World world){
        this.world = world;
        setSize(Constants.DAHSBOARD_WIDTH, Constants.FRAME_HEIGHT);
        setVisible(true);
        setBorder(new BasicBorders.MarginBorder());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.labelBorder = new EmptyBorder(0,0,5,0);
        this.elementBorder = new EmptyBorder(0,0,10,0);
    }

    public void init(GeneticPool genePool){
        this.genePool = genePool;

        generation = new JLabel("GENERATION ");
        generation.setBorder(new EmptyBorder(0,0,20,0));
        generation.setAlignmentX(Constants.DAHSBOARD_WIDTH/2 - generation.getWidth());
        score = new JLabel("Best doodle score : ");
        score.setBorder(labelBorder);
        average_score = new JLabel("Generation average score : ");
        average_score.setBorder(labelBorder);
        individuals = new JLabel("Number of individuals in the generation : ");
        individuals.setBorder(labelBorder);
        alive_individuals = new JLabel("Number of individuals alive in the generation  :");
        alive_individuals.setBorder(labelBorder);
        best_doodle_neurons = new JLabel("Best doodle number of neurons : ");
        best_doodle_neurons.setBorder(labelBorder);
        average_neurons = new JLabel("Generation average number of neurons : ");
        average_neurons.setBorder(labelBorder);
        selection_rate = new JLabel("Selection rate : "+Constants.SELECTION_RATE);
        selection_rate.setBorder(labelBorder);

        this.infos = new JPanel();
        infos.add(individuals);
        infos.add(alive_individuals);
        infos.add(selection_rate);
        infos.add(score);
        infos.add(average_score);
        infos.add(best_doodle_neurons);
        infos.add(average_neurons);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setAlignmentX(SwingConstants.LEFT);
        infos.setBorder(labelBorder);


        stopButton = new Button("END GENERATION");
        startButton = new Button("LAUNCH NEW GENERATION");

        buttons = new JPanel();
        buttons.add(stopButton, LEFT_ALIGNMENT);
        buttons.add(startButton, RIGHT_ALIGNMENT);

        String[] columns = {"GENERATION", "SURVIVORS", "BEST SCORE", "AVERAGE SCORE", "BEST DOODLE NEURONS", "AVERAGE NEURONS"};
        String[][] data = {};

        logTable = new JTable(new DefaultTableModel(data, columns));

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

        add(generation);
        add(infos);
        add(buttons);
        add(new JScrollPane(logTable));

    }

    public void updateDashboard(){
        generation.setText("GENERATION "+world.getGeneration());
        individuals.setText("Number of individuals in the generation : " + genePool.getIndividuals().size());
        alive_individuals.setText("Number of individuals alive in the generation : " + genePool.getAliveDoodles().size());
        score.setText("Best doodle score : " + d3f.format(genePool.getBestDoodle().getScore()));
        average_score.setText("Generation average score : " + d3f.format(genePool.getAverageScore()));
        best_doodle_neurons.setText("Best doodle number of neurons : "+genePool.getBestDoodle().getGenome().getNodes());
        average_neurons.setText("Generation average number of neurons : "+d2f.format(genePool.getAverageNeurons()));
    }

    public void logScore(){
        String gen = String.valueOf(world.getGeneration());
        String survivors = String.valueOf(genePool.getAliveDoodles().size());
        String bestScore = String.valueOf(d3f.format(genePool.getBestDoodle().getScore()));
        String averageScore = String.valueOf(d3f.format(genePool.getAverageScore()));
        String bestNeurons = String.valueOf(genePool.getBestDoodle().getGenome().getNodes());
        String averageNeurons = String.valueOf(genePool.getAverageNeurons());

        System.out.println("Logging score");

        DefaultTableModel tableModel = (DefaultTableModel) logTable.getModel();
        tableModel.insertRow(0, new String[]{gen, survivors, bestScore, averageScore, bestNeurons, averageNeurons});

    }

    public void reset(){
       removeAll();
       init(genePool);
    }
}
