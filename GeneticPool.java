package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class GeneticPool {
    private int size;
    private InnovationController innovationController;
    private ArrayList<Doodle> individuals;
    private LinkedList<Doodle> aliveDoodles;
    private double populationMutationRate;
    private double mutationRate;
    private double selectionRate;
    private World world;

    public GeneticPool(int size, double selectionRate, World world){
        this.size = size;
        this.innovationController = new InnovationController();
        this.selectionRate = selectionRate;
        this.world = world;
        this.individuals = new ArrayList<>();
        this.aliveDoodles = new LinkedList<>();
    }

    public void populate(){
        for(int i = 0 ; i < size; i ++){
            Doodle doodle = new Doodle(Constants.FRAME_WIDTH/2, aliveDoodles, world, this);
            individuals.add(doodle);
            world.getElements().add(doodle);
        }
    }

    public LinkedList<Doodle> getAliveDoodles(){
        return aliveDoodles;
    }

    public InnovationController getInnovationController() {
        return innovationController;
    }

    public int getAverageScore(){
        int total = 0;
        for (Doodle d: individuals
             ) {
            total += d.getScore();
        }

        total = total / individuals.size();

        return total;
    }


    public Doodle getHighestDoodle(){
        Doodle highestDoodle = aliveDoodles.get(0);
        for (Doodle d : aliveDoodles
             ) {
            if(d.getScore() > highestDoodle.getScore()){
                highestDoodle = d;
            }
        }
        return highestDoodle;
    }

    public Doodle getBestDoodle(){
        Doodle bestDoodle = individuals.get(0);
        for (Doodle d : individuals
        ) {
            if(d.getScore() > bestDoodle.getScore()){
                bestDoodle = d;
            }
        }
        return bestDoodle;
    }

    public ArrayList<Doodle> getIndividuals(){
        return individuals;
    }

    public void sortPopulation(){

        Collections.sort(individuals, new Comparator<Doodle>() {
            @Override
            public int compare(Doodle d1, Doodle d2) {
                return (int) d2.getScore() - (int) d1.getScore();
            }
        });

        for(int i = 0; i < individuals.size(); i++){
            System.out.println("Doodle number "+ (i+1) + " score : "+individuals.get(i).getScore());
            individuals.get(i).getNN().setScore(individuals.get(i).getScore());
        }

    }

    public void naturalSelection(){

        int amountToSave = (int) (individuals.size() * selectionRate);

        ArrayList<Doodle> newGeneration = new ArrayList<>();

        for(int i = 0; i < amountToSave; i++){
            newGeneration.add(individuals.get(i));
        }

        individuals = newGeneration;
        this.aliveDoodles = new LinkedList<>();

    }

    public void duplication(){
        int j = 0;
        for(int i = individuals.size(); i < size; i++){
            Doodle individual = individuals.get(j);
            individuals.add(individual.duplicate());
            j++;
        }
    }

    public void newRandomIndividuals(){
        int j = 0;
        for(int i = individuals.size(); i < size; i++){
            Doodle individual = individuals.get(j);
            individuals.add(new Doodle(Constants.FRAME_WIDTH/2, aliveDoodles, world, this));
            j++;
        }
        System.out.println(j+" random individuals have been added to the population");
    }


    public void mutatePopulation(){
        for (Doodle d: individuals
             ) {
            d.getNN().mutate();
        }
    }

}
