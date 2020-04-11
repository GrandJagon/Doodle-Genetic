package com.company;

import java.util.*;

public class Genome implements NN{
    private TreeMap<Integer, Node> nodes;
    private ArrayList<Connection> connections;
    private Random rand;
    private GeneticPool genePool;
    private double score;

    public Genome(GeneticPool genePool){
        this.nodes = new TreeMap<>();
        this.connections = new ArrayList<>();
        this.genePool = genePool;
        this.rand = new Random();
    }

    public Genome(Genome clone){
        System.out.println("Cloning of an individual....");
        this.nodes = new TreeMap();
        this.connections = new ArrayList<>();

        for (Connection c : clone.getConnections()
             ) {
            this.connections.add(c.copy());
        }

        this.genePool = clone.getGenePool();
        this.rand = new Random();

    }

    public void create(){
        System.out.println("Creation of new individual....");
        int connection_innovation = 1;

        for(int i = 1; i <= Constants.INPUT_NODES; i++){
            for(int j = Constants.INPUT_NODES + 1 ; j <= Constants.BASE_NODES; j++){
                double weight = Calc.randomDouble(-1, 1);
                Connection conn = new Connection(i, j, weight, connection_innovation);
                connections.add(conn);
                System.out.println("New connection created between "+i+" and "+j+ " for new individual with innovation number "+connection_innovation+" with weight = "+weight);
                connection_innovation++;
            }
        }
    }

    public void generateNN(){
        System.out.println("Generation of NN....");
        for (Connection c : connections
             ) {

            if(!nodes.containsKey(c.getInNode())){
                nodes.put(c.getInNode(), new Node(c.getInNode()));
                System.out.println("Node "+c.getInNode()+" created and added to NN");
            }

            if(!nodes.containsKey(c.getOutNode())){
                nodes.put(c.getOutNode(), new Node(c.getOutNode()));
                System.out.println("Node "+c.getOutNode()+" created and added to NN");
            }

            nodes.get(c.getOutNode()).addInConnection(c);
        }
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public GeneticPool getGenePool() {
        return genePool;
    }

    public int getHidden(){
        return nodes.size();
    }

    public double getScore(){
        return score;
    }

    public void setScore(double score){
        this.score = score;
    }

    //Method that feeds all data relative to doodle environment in order to feed the forward propagation
    public void feedInput(double doodleX, double groundX, double groundSpeed, double groundWidth, double upperPlatformX, double upperPlatformSpeed, double upperPlatFormWidth){
        nodes.get(1).setValue(doodleX);
        nodes.get(2).setValue(groundX);
        nodes.get(3).setValue(groundSpeed);
        nodes.get(4).setValue(groundWidth);
        nodes.get(5).setValue(upperPlatformX);
        nodes.get(6).setValue(upperPlatformSpeed);
        nodes.get(7).setValue(upperPlatFormWidth);
    }

    public double[] feedForward() {
        Node inNode;

        for(int i = nodes.size() ; i > Constants.INPUT_NODES; i--){
            double total = 0;

            for (Connection conn: nodes.get(i).getInConnections()
                 ) {
                inNode = nodes.get(conn.getInNode());

               if(conn.isEnabled() && inNode.getValue() > 0){
                   total += (inNode.getValue() * conn.getWeight());
               }
            }
            nodes.get(i).setValue(Calc.sigmoid(total));
            System.out.println("Value of node "+i+" = "+Calc.sigmoid(total));
        }

        double[] Y = new double[Constants.OUTPUT_NODES];

        for(int i = 0; i < Constants.OUTPUT_NODES; i++){
            Y[i] = nodes.get(Constants.INPUT_NODES + i + 1).getValue();
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
            changeRandomWeight(Constants.WEIGHT_MUTATION_RATE);
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

    public void changeRandomWeight(double weight_mutation_rate){
        for (Connection c: connections
             ) {
            if(rand.nextInt(100) < weight_mutation_rate){
                c.setWeight(c.getWeight() + Calc.randomDouble(-0.3, 0.3));
            }
        }

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
            if(conn.getInNode() == n1.getInnovation()){
                System.out.println("Adding new weight not possible as there is already an existing one for those two nodes");
                return;
            }
        }

        int connectionInnovationNumber = genePool.getInnovationController().assignConnectionNumber(n1.getInnovation(), n2.getInnovation());

        Connection newConn = new Connection(n1.getInnovation(), n2.getInnovation(), Calc.randomDouble(-1, 1), connectionInnovationNumber);
        connections.add(newConn);
        n2.addInConnection(newConn);
        System.out.println("New connection added from node " + i + " to node " + j);
    }

    public void addNode(){
        int i = rand.nextInt(connections.size() - 1);

        Connection conn = connections.get(i);
        Node input = nodes.get(conn.getInNode());
        Node output = nodes.get(conn.getOutNode());

        if((input.getType() == 0 && output.getType() == 0) || (input.getType() == 2 && output.getType() == 2) || (input.getType() > output.getType())){
            System.out.println("Adding node impossible as input type = "+input.getType()+" and output type = "+output.getType());
            return;
        }

        int nodeInnovationNumber = genePool.getInnovationController().assignNodeNumber(input.getInnovation(), output.getInnovation());

        Node newNode = new Node(nodeInnovationNumber);

        int connectionInnovationNumber = genePool.getInnovationController().assignConnectionNumber(input.getInnovation(), newNode.getInnovation());

        Connection newConn = new Connection(input.getInnovation(), output.getInnovation(), 1, connectionInnovationNumber);
        newNode.addInConnection(newConn);

        nodes.put(newNode.getInnovation(), newNode);
        connections.add(newConn);

        System.out.println("New node added between node " + input.getInnovation() + " and node " + output.getInnovation());
    }

    public Genome clone(){
        Genome child = new Genome(this);
        return child;
    }

    public void crossBaseConnections(Genome genome2, Genome child){
        for(int i = 1; i <= child.getConnections().size(); i++){
            int dice = rand.nextInt(100);

            Connection parentConnection ;

            if(dice < 50){
                parentConnection = this.getConnections().get(i);
            }else{
                parentConnection = genome2.getConnections().get(i);
            }

            child.getConnections().get(i).setWeight(parentConnection.getWeight());
            child.getConnections().get(i).setActivation(parentConnection.isEnabled());
            System.out.println("Connection number "+i+" inherited from parent "+dice+" with weight :"+parentConnection.getWeight()+" and enabled ="+parentConnection.isEnabled());
        }
    }

//    public void crossExtraGenes(Genome genome2, Genome child){
//        int extraNodesParent1 = this.getNodes().size() - Constants.BASE_NODES;
//        int extraNodesParent2 = genome2.getNodes().size() - Constants.BASE_NODES;
//        Genome mostEvolved;
//        Genome fittest = null;
//
//        if(this.getScore() > genome2.getScore()){
//            fittest = this;
//        }else if(this.getScore() < genome2.getScore()){
//            fittest = genome2;
//        }
//
//        if(extraNodesParent1 == 0 && extraNodesParent2 == 0){
//            System.out.println("Neither of both parent have evolved new nodes through evolution");
//            return;
//        }
//
//        if(extraNodesParent1 > extraNodesParent2){
//            mostEvolved = this;
//        }else{
//            mostEvolved = genome2;
//        }
//
//        if (extraNodesParent1 == 0 || extraNodesParent2 == 0){
//            System.out.println("One of the genomes has not evolved new nodes through evolution");
//            if(mostEvolved == fittest){
//                System.out.println("Fittest genome is also the most evolved, starting to pass nodes to offspring");
//                for(int i = Constants.BASE_NODES + 1; i < mostEvolved.getNodes().size(); i++){
//                    Node parentNode = mostEvolved.getNodes().get(i);
//                    Node inheritedNode = new Node(1, mostEvolved.getNodes().get(i).getInnovation());
//                    child.getNodes().add(inheritedNode);
//
//                }
//            }
//        }
//
//    }

//    public Genome crossOver(Genome genome2){
//        Genome child = new Genome(genePool);
//        child.create();
//
//        crossBaseConnections(genome2, child);
//
//
//
//
//
//    }



}
