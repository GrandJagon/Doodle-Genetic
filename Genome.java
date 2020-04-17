package com.company;

import java.util.*;

public class Genome {
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
        int connection_innovation = 1;

        for(int i = 1; i <= Constants.INPUT_NODES; i++){
            for(int j = Constants.INPUT_NODES + 1 ; j <= Constants.BASE_NODES; j++){
                double weight = Calc.randomDouble(-1, 1);
                Connection conn = new Connection(i, j, weight, connection_innovation);
                connections.add(conn);
                connection_innovation++;
            }
        }
    }

    public void generateNN(){
        nodes.clear();
        for (Connection c : connections
             ) {

            if(!nodes.containsKey(c.getInNode())){
                nodes.put(c.getInNode(), new Node(c.getInNode()));
            }

            if(!nodes.containsKey(c.getOutNode())){
                nodes.put(c.getOutNode(), new Node(c.getOutNode()));
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

        for(Map.Entry<Integer, Node> nodeEntry : nodes.entrySet()){
            double total = 0;

            if(nodeEntry.getKey() > Constants.INPUT_NODES){
                for(Connection conn : nodeEntry.getValue().getInConnections()){
                    inNode = nodes.get(conn.getInNode());

                    if(conn.isEnabled()){
                        total += (inNode.getValue() * conn.getWeight());
                    }
                }
                nodeEntry.getValue().setValue(Calc.sigmoid(total));
            }

        }

        double[] Y = new double[Constants.OUTPUT_NODES];

        for(int i = 0; i < Constants.OUTPUT_NODES; i++){
            Y[i] = nodes.get(Constants.INPUT_NODES + i + 1).getValue();
        }

        return Y;
    }

    public void mutate(){
        System.out.println("---------------MUTATION "+this+" GENERATION -----------------");
        System.out.println("Number of connections before mutation :"+connections.size());
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

        System.out.println("Number of connections after mutation :"+connections.size());
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

        Set nodeSet = nodes.navigableKeySet();

        int i = rand.nextInt(nodeSet.size() - 1);
        int j = rand.nextInt(nodeSet.size() - 1);

        int n1i  = (int) nodeSet.toArray()[i];
        int n2i = (int) nodeSet.toArray()[j];

        Node n1 = nodes.get(n1i);
        Node n2 = nodes.get(n2i);


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

        int inConnectionInnovationNumber = genePool.getInnovationController().assignConnectionNumber(input.getInnovation(), newNode.getInnovation());
        int outConnectionInnovationNumber = genePool.getInnovationController().assignConnectionNumber(newNode.getInnovation(), output.getInnovation());

        Connection newConnIn = new Connection(input.getInnovation(), newNode.getInnovation(), 1, inConnectionInnovationNumber);
        Connection newConnOut = new Connection(newNode.getInnovation(), output.getInnovation(), Calc.randomDouble(-1,1), outConnectionInnovationNumber);

        newNode.addInConnection(newConnIn);
        output.addInConnection(newConnOut);
        conn.disable();

        nodes.put(newNode.getInnovation(), newNode);
        connections.add(newConnIn);
        connections.add(newConnOut);

        System.out.println("New node "+newNode.getInnovation()+" added between node " + input.getInnovation() + " and node " + output.getInnovation() +
                " as well as connection "+conn.getInnovation_number()+" replaced by connections " + newConnIn.getInnovation_number()+ " and " +newConnOut.getInnovation_number());
    }

    public Genome clone(){
        Genome child = new Genome(this);
        return child;
    }


   public Genome crossOver(Genome parent2){

        Genome child = new Genome(genePool);
        
        TreeMap<Integer, Connection> DNA1 = new TreeMap<>();
        TreeMap<Integer, Connection> DNA2 = new TreeMap<>();

       for (Connection conn: this.connections
            ) {
           if(!DNA1.containsValue(conn.getInnovation_number())){
               DNA1.put(conn.getInnovation_number(), conn);
           }
       }

       for (Connection conn :parent2.getConnections()
            ) {
           if(!DNA2.containsKey(conn.getInnovation_number())){
               DNA2.put(conn.getInnovation_number(), conn);
           }
       }

       Set<Integer> allGenes = new HashSet<>(DNA1.keySet());
       allGenes.addAll(DNA2.keySet());

       for (Integer gene : allGenes
            ) {
           Connection inheritedGene = null;

           if(DNA1.containsKey(gene) && DNA2.containsKey(gene)){
               if(rand.nextBoolean()){
                   inheritedGene = DNA1.get(gene).copy();
               }else{
                   inheritedGene = DNA2.get(gene).copy();
               }
           }else{
               if(this.getScore() == parent2.getScore()){
                   if(DNA1.containsKey(gene)){
                       inheritedGene = DNA1.get(gene).copy();
                   }else{
                       inheritedGene = DNA2.get(gene).copy();
                   }
               }else{
                   if(DNA1.containsKey(gene) && this.getScore() > parent2.getScore()){
                       inheritedGene = DNA1.get(gene).copy();
                   }else if(DNA2.containsKey(gene) && parent2.getScore() > this.getScore()){
                       inheritedGene = DNA2.get(gene).copy();
                   }
               }
           }
           if(inheritedGene != null){
               child.connections.add(inheritedGene);
           }
       }

       System.out.println("Child successfully created with "+child.getConnections().size()+" connections from p1 with "+connections.size()+ " and p2 with "+parent2.connections.size());

       return child;

   }


}
