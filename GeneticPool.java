package com.company;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class GeneticPool {
    private int size;
    private LinkedList<Doodle> individuals;
    private LinkedList<Doodle> aliveDoodles;
    private double populationMutationRate;
    private double mutationRate;
    private double selectionRate;
    private World world;

    public GeneticPool(int size, double populationMutationRate, double mutationRate, double selectionRate, World world){
        this.size = size;
        this.populationMutationRate = populationMutationRate;
        this.mutationRate = mutationRate;
        this.selectionRate = selectionRate;
        this.world = world;
        this.individuals = new LinkedList<>();
        this.aliveDoodles = new LinkedList<>();
    }

    public void populate(){
        for(int i = 0 ; i < size; i ++){
            Doodle doodle = new Doodle(Constants.FRAME_WIDTH/2, aliveDoodles, world, Calc.randomInt(1, 20));
            individuals.add(doodle);
            world.getElements().add(doodle);
        }
    }

    public LinkedList<Doodle> getAliveDoodles(){
        return aliveDoodles;
    }

    public Doodle getHighestDoodle(){
        Doodle bestDoodle = aliveDoodles.get(0);
        for (Doodle d : aliveDoodles
             ) {
            if(d.getScore() > bestDoodle.getScore()){
                bestDoodle = d;
            }
        }
        return bestDoodle;
    }

    public LinkedList<Doodle> getIndividuals(){
        return individuals;
    }

    public void sortPopulation(){

        Collections.sort(individuals, new Comparator<Doodle>() {
            @Override
            public int compare(Doodle d1, Doodle d2) {
                return (int) d2.getScore() - (int) d1.getScore();
            }
        });

    }

    public void naturalSelection(){
        int amountToSave = (int) (individuals.size() * selectionRate);

        LinkedList<Doodle> newGeneration = new LinkedList<>();

        for(int i = 0; i < amountToSave; i++){
            newGeneration.add(individuals.get(i));
            System.out.println(individuals.get(i).getScore());
        }

        individuals = newGeneration;
    }

    public void duplication(){
        int j = 0;
        for(int i = individuals.size(); i < size; i++){
            Doodle individual = individuals.get(j);
            individuals.add(individual.duplicate());
            j++;
        }
    }

    public void mutateAll(){
        for (Doodle d:individuals
             ) {
            d.unbiased_weight_mutation(mutationRate);
        }
    }

}
