package com.company;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Genome implements NN{
    private ArrayList<Node> nodes;
    private ArrayList<Connection> connections;
    private Random rand;
    private int innovation;
    private int index;
    private int input;
    private int hidden;
    private int output;
    private GeneticPool genePool;

    public Genome(GeneticPool genePool){
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.innovation = 1;
        this.index = 1;
        this.input = 0;
        this.hidden = 0;
        this.output = 0;
        this.genePool = genePool;
        this.rand = new Random();
    }

    public void init(){
        for(int i = 0; i < Constants.INPUT_NODES; i++){
            nodes.add(new Node(0, index));
            input++;
            index++;
        }

        for(int j = 0; j < Constants.OUTPUT_NODES; j++){
            nodes.add(new Node(2, index));
            output++;
            index++;
        }

        for(int i = 0; i < Constants.INPUT_NODES; i++){
            for(int j = 0; j < Constants.OUTPUT_NODES; j++){
                Node input = nodes.get(i);
                Node output = nodes.get((nodes.size() - j - 1));
                Connection conn = new Connection(input, output, Calc.randomDouble(-1 , 1), innovation);
                output.addInConnection(conn);
                connections.add(conn);
                innovation++;
                genePool.setInnovation_number(innovation);
            }
        }
    }

    public int getHidden(){
        return hidden;
    }

    //Method that feeds all data relative to doodle environment in order to feed the forward propagation
    public void feedInput(double doodleX, double groundX, double groundSpeed, double groundWidth, double upperPlatformX, double upperPlatformSpeed, double upperPlatFormWidth){
        nodes.get(0).setValue(doodleX);
        nodes.get(1).setValue(groundX);
        nodes.get(2).setValue(groundSpeed);
        nodes.get(3).setValue(groundWidth);
        nodes.get(4).setValue(upperPlatformX);
        nodes.get(5).setValue(upperPlatformSpeed);
        nodes.get(6).setValue(upperPlatFormWidth);
    }

    public double[] feedForward() {

        for(int i = (nodes.size() - 1); i > Constants.INPUT_NODES; i--){
            double total = 0;

            for (Connection conn: nodes.get(i).getInConnections()
                 ) {
               if(conn.isEnabled() && conn.getInput().getValue() > 0){
                   total += (conn.getInput().getValue() * conn.getWeight());
               }
            }
            nodes.get(i).setValue(Calc.sigmoid(total));
        }

        double[] Y = new double[Constants.OUTPUT_NODES];

        for(int i = 0; i < Constants.OUTPUT_NODES; i++){
            Y[i] = nodes.get(7 + i).getValue();
        }

        return Y;
    }

    public void mutate(){
        int dice = rand.nextInt(100);

        double a = Constants.WEIGHT_CHANGE_CHANCE * 100;
        double b = Constants.DISABLING_CONNECTION_CHANCE * 100;
        double c = Constants.ADDING_CONNECTION_CHANCE * 100;
        double d = Constants.ADDING_NODE_CHANCE * 100;

        if(dice < a){
            changeRandomWeight();
        }

        if(dice > a && dice < (a + b)){
          disableRandomWeight();
        }

        if(dice > (a + b) && dice < (a + b + c)){
            addRandomConnection();
        }

        if(dice > (a + b + c) && dice <= (a + b + c + d)){
            addNode();
        }
    }

    public void changeRandomWeight(){
        int i = rand.nextInt(connections.size() - 1);

        connections.get(i).setWeight(connections.get(i).getWeight() + Calc.randomDouble(-1 , 1));

        System.out.println("Weight changed");
    }

    public void disableRandomWeight(){
        int i = rand.nextInt(connections.size() - 1);

        connections.get(i).disable();

        System.out.println("Connection disabled");

    }

    public void addRandomConnection(){
        int i = rand.nextInt(nodes.size() - 1);
        int j = rand.nextInt(nodes.size() - 1);

        Node n1 = nodes.get(i);
        Node n2 = nodes.get(j);

        if(n2.getType() == 0 || n1.getType() == 2 || (n1.getType() == 0 && n2.getType() == 0) || (n1.getType() == 2 && n2.getType() == 2)){
            System.out.println("Adding new weight not possible as node 1 = " + n1.getType() + " and node 2 = " + n2.getType());
            return;
        }

        for (Connection conn : n2.getInConnections()
             ) {
            if(conn.getInput() == n1){
                System.out.println("Adding new weight not possible as there is already an existing one for those two nodes");
                return;
            }
        }

        Connection newConn = new Connection(n1, n2, Calc.randomDouble(-1, 1), genePool.getInnovation_number());
        connections.add(newConn);
        n2.addInConnection(newConn);
        System.out.println("New connection added from node " + i + " to node " + j);
    }

    public void addNode(){
        int i = rand.nextInt(connections.size() - 1);

        Connection conn = connections.get(i);
        Node input = conn.getInput();
        Node output = conn.getOutput();

        if((input.getType() == 0 && output.getType() == 0) || (input.getType() == 2 && output.getType() == 2)){
            System.out.println("Adding node impossible between two input or output nodes");
            return;
        }

        Node newNode = new Node(1, index);
        Connection newConn = new Connection(input, newNode, 1, genePool.getInnovation_number());
        newNode.addInConnection(newConn);
        conn.setInput(newNode);

        nodes.add(newNode);
        connections.add(newConn);

        index++;

        System.out.println("New node added between node " + input.getIndex() + " and node " + output.getIndex());
    }


}
