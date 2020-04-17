package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class GeneticPool {
    private int size;
    private int generation;
    private InnovationController innovationController;
    private ArrayList<Doodle> individuals;
    private LinkedList<Doodle> aliveDoodles;
    private double populationMutationRate;
    private double mutationRate;
    private double selectionRate;
    private World world;

    public GeneticPool(int size, double selectionRate, World world){
        this.size = size;
        this.generation = 1;
        this.innovationController = new InnovationController();
        this.selectionRate = selectionRate;
        this.world = world;
        this.individuals = new ArrayList<>();
        this.aliveDoodles = new LinkedList<>();
    }

    public void populate(){
        for(int i = 0 ; i < size; i ++){
            Doodle doodle = new Doodle(Constants.FRAME_WIDTH/2, aliveDoodles, world, this, generation);
            doodle.initGenome();
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

    public int getGeneration(){
        return generation;
    }

    public void sortPopulation(){

        Collections.sort(individuals, new Comparator<Doodle>() {
            @Override
            public int compare(Doodle d1, Doodle d2) {
                return (int) d2.getScore() - (int) d1.getScore();
            }
        });

        for(int i = 0; i < individuals.size(); i++){
            individuals.get(i).getGenome().setScore(individuals.get(i).getScore());
        }

    }

    public void naturalSelection(){

        int amountToSave = (int) (individuals.size() * selectionRate);
        System.out.println("Amount to save : "+amountToSave+" as individuals.size : "+individuals.size()+" and selection rate :"+selectionRate);

        ArrayList<Doodle> newGeneration = new ArrayList<>();

        for(int i = 0; i < amountToSave; i++){
            newGeneration.add(individuals.get(i));
        }

        individuals = newGeneration;
        this.aliveDoodles = new LinkedList<>();

        System.out.println(individuals.size()+" left after natural selection");

    }

    public void duplication(){
        int j = 0;
        for(int i = individuals.size(); i < size; i++){
            Doodle copy = individuals.get(j).duplicate();
            individuals.add(copy);
            j++;
        }
    }

    public void newRandomIndividuals(){
        int j = 0;
        for(int i = individuals.size(); i < size; i++){
            Doodle newDoodle = new Doodle(Constants.FRAME_WIDTH/2, aliveDoodles, world, this, generation);
            newDoodle.initGenome();
            individuals.add(newDoodle);
            j++;
        }
        generation++;
        System.out.println(j+" random individuals have been added to the population, now generation "+generation);
    }


    public void mutatePopulation(){
        for (Doodle d: individuals
             ) {
            d.getGenome().mutate();
        }
    }

    public void crossOver(){

            int j = 0;
            ArrayList<Doodle> offsprings = new ArrayList();

            for(int i = individuals.size() - 1; i < size-1; i++){

                int randNumber = Calc.randomIntOut(0, (individuals.size() - 1), j);

                Doodle parent1 = individuals.get(j);
                Doodle parent2 = individuals.get(randNumber);

                offsprings.add(parent1.breed(parent2));

                j++;

                if(j == (individuals.size() - 1)){
                    j = 0;
                }
            }

            generation++;
            System.out.println(offsprings.size()+ " offsprings generated, now generation "+generation);

            individuals.addAll(offsprings);
    }

}
