package com.company;

import java.util.ArrayList;

public class InnovationController {
    private int node_innovation_number;
    private int connection_innovation_number;
    private ArrayList<Innovation> node_innovation_pool;
    private ArrayList<Innovation> connection_innovation_pool;

    public InnovationController(){
        this.node_innovation_number = Constants.INPUT_NODES + Constants.OUTPUT_NODES;
        this.connection_innovation_number = Constants.INPUT_NODES * Constants.OUTPUT_NODES;
        this.node_innovation_pool = new ArrayList<>();
        this.connection_innovation_pool = new ArrayList<>();
    }

    public int get_node_innovation_number(){
        return node_innovation_number;
    }

    public int get_connection_innovation_number(){
        return connection_innovation_number;
    }

    public int new_node_innovation(){
        node_innovation_number++;
        return node_innovation_number;
    }

    public int new_connection_innovation(){
        connection_innovation_number++;
        return connection_innovation_number;
    }

    public int assignNodeNumber(int in, int out){
        if(node_innovation_pool.isEmpty()){
            Innovation innovation = new Innovation(new_node_innovation(), in, out);
            node_innovation_pool.add(innovation);
            System.out.println("First node innovation created, number "+node_innovation_number+" assigned for a node placed between nodes "+in+" and "+out);
            return node_innovation_number;
        }

        for (Innovation innovation : node_innovation_pool
             ) {
            if(innovation.getInNodeNumber() == in && innovation.getOutNodeNumber() == out){
                System.out.println("Innovation already existing within the pool with the number "+innovation.getInnovation_number());
                return innovation.getInnovation_number();
            }
        }

        Innovation innovation = new Innovation(new_node_innovation(), in, out);
        node_innovation_pool.add(innovation);
        System.out.println("No similar innovation found within the pool, new number assigned : "+node_innovation_number);
        return node_innovation_number;
    }

    public int assignConnectionNumber(int in, int out){
        if(connection_innovation_pool.isEmpty()){
            Innovation innovation = new Innovation(new_connection_innovation(), in, out);
            connection_innovation_pool.add(innovation);
            System.out.println("First connection innovation created, number "+connection_innovation_number+" assigned for a node placed between nodes "+in+" and "+out);
            return connection_innovation_number;
        }

        for (Innovation innovation : connection_innovation_pool
        ) {
            if(innovation.getInNodeNumber() == in && innovation.getOutNodeNumber() == out){
                System.out.println("Innovation already existing within the pool with the number "+innovation.getInnovation_number());
                return innovation.getInnovation_number();
            }
        }

        Innovation innovation = new Innovation(new_connection_innovation(), in, out);
        connection_innovation_pool.add(innovation);
        System.out.println("No similar innovation found within the pool, new number assigned : "+connection_innovation_number);
        return connection_innovation_number;
    }


}
