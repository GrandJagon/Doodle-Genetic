package com.company;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.awt.*;
import java.util.LinkedList;

public class Doodle implements  Element {
    private NN NN;
    private Vector position;
    private Vector velocityFinal;
    private Vector motionVelocity;
    private int tempY;
    private int width;
    private int height;
    private int weight;
    private int jumpStrength;
    private double score;
    private boolean onGround;
    private boolean alive;
    private boolean onJump;
    private LinkedList<Doodle> container;
    private Platform ground;
    private World world;


    public Doodle(int x, LinkedList<Doodle> container, World world, GeneticPool genePool) {
        this.position = new Vector(x, Constants.FRAME_HEIGHT - height);
        this.alive = true;
        this.width = 20;
        this.height = 20;
        this.score = 0;
        this.weight = Constants.DOODLE_WEIGHT;
        this.onGround = false;
        this.onJump = false;
        this.velocityFinal = new Vector(0,0);
        this.motionVelocity = new Vector(0,0);
        this.container = container;
        this.container.add(this);
        this.world = world;
        this.NN = new Genome(genePool);
        NN.init();
    }

    public Vector getPosition() {
        return position;
    }

    public double getScore(){
        return score;
    }

    public NN getNN(){
        return NN;
    }

    public void setOnGround(boolean b){
        this.onGround = b;
    }

    public void setContainer(LinkedList<Doodle> container){
        this.container = container;
        this.container.add(this);
    }

    public void removeGround(){
        this.ground = null;
    }

    public void remove(){
        container.remove(this);
    }

    @Override
    public void paint(Graphics2D graphics, Camera c) {
        tempY = position.getY() - c.getTop();
        graphics.setPaint(Color.GREEN);
        graphics.drawOval(position.getX(), tempY, width, height);
        graphics.fillOval(position.getX(), tempY, width, height);
    }

    public void reset(){
        this.alive = true;
        this.score = 0;
        this.onGround = false;
        this.onJump = false;
        this.position = new Vector(Constants.FRAME_WIDTH/2, Constants.FRAME_HEIGHT - height);
        this.velocityFinal = new Vector(0,0);
        this.motionVelocity = new Vector(0,0);
        if(!container.contains(this)){
            container.add(this);
        }
    }

    @Override
    public void update(){
        getEnvironmentData();
        multi_decision();
        updateMotion();
    }


    public void updateMotion() {
        if(onGround){
            if(ground != null && ground.getIndex() > 0 && ground.getIndex() > (score - 0.5)){
                score += 0.0001;
            }
            velocityFinal.updateY(0);
            if (ground != null){
                velocityFinal.updateX(ground.getSpeed() + motionVelocity.getX());
            }else{
                velocityFinal.updateX(motionVelocity.getX());
            }
        }else if(onJump){
            velocityFinal.addY(-jumpStrength);
            jumpStrength = jumpStrength - weight;
            if(jumpStrength <= 0){
                onJump = false;
            }
        }else{
            velocityFinal.updateX(motionVelocity.getX());
            velocityFinal.updateY(Constants.GRAVITY);
        }
     position = position.add(velocityFinal);
    }

    @Override
    public boolean isOnScreen(Camera camera) {
        if (position.getY() < camera.getBottom() && position.getY() >= camera.getTop()) {
            return true;
        } else {
            alive = false;
            return false;
        }
    }

    public boolean collide(Platform p) {
        if(position.getY() + height >= Constants.FRAME_HEIGHT){
            position.setY(Constants.FRAME_HEIGHT - height);
            onGround = true;
            removeGround();
            return true;
        }
        if (position.getX() < (p.getPosition().getX() + p.getWidth()) && position.getX() + width > p.getPosition().getX() && !onJump) {
            if (position.getY() + height >= p.getPosition().getY() && position.getY() + height < (p.getPosition().getY() + p.getHeight()) ) {
                position.setY(p.getPosition().getY() - width);
                onGround = true;
                ground = p;
                return true;
            }
        }
        return false;
    }

    public void wallCollide(){
        if(position.getX() <= 0){
            position.setX(0);
        }else if((position.getX() + width) >= Constants.FRAME_WIDTH){
            position.setX(Constants.FRAME_WIDTH - width);
        }
    }

    public void jump(){
        if(onGround){
            jumpStrength = Constants.JUMP_FORCE;
            onJump = true;
            onGround = false;
        }
    }

    public void right(){
        motionVelocity.setX(Constants.DOODLE_SPEED);
    }

    public void left(){
        motionVelocity.setX(-Constants.DOODLE_SPEED);
    }

    public void stay(){
        motionVelocity.setX(0);
    }

    public void updateScore() {
        if (ground != null && score < ground.getIndex()) {
            score = ground.getIndex();
        }
    }

    public void getEnvironmentData(){
        Platform upperPlatform;
        double gx;
        double gw;
        double gs;
        double x = Calc.normalize(Constants.FRAME_WIDTH, 0, position.getX());
        if(ground == null){
            gx = Calc.normalize(Constants.FRAME_WIDTH, 0, 0);
            gw = Calc.normalize(Constants.FRAME_WIDTH, 0, Constants.FRAME_WIDTH);
            gs = 0;
            upperPlatform = world.getFirstPlatform();

        }else{
            gx = Calc.normalize(Constants.PLATFORM_MAXIMUM_WIDTH, Constants.PLATFORM_MINIMUM_WIDTH, ground.getWidth());
            gs = Calc.normalize(Constants.PLATFORM_MAXIMUM_SPEED, Constants.PLATFORM_MINIMUM_SPEED, ground.getSpeed());
            gw = Calc.normalize(Constants.PLATFORM_MAXIMUM_WIDTH, Constants.PLATFORM_MINIMUM_WIDTH, ground.getWidth());
            upperPlatform = ground.getUpperPlatform();
        }
        double upx = Calc.normalize(Constants.FRAME_WIDTH, 0, upperPlatform.getPosition().getX());
        double ups = Calc.normalize(Constants.PLATFORM_MAXIMUM_SPEED, Constants.PLATFORM_MINIMUM_SPEED, upperPlatform.getSpeed());
        double upw = Calc.normalize(Constants.PLATFORM_MAXIMUM_WIDTH, Constants.PLATFORM_MINIMUM_WIDTH, upperPlatform.getWidth());

        NN.feedInput(x, gx, gs, gw, upx, ups, upw);
    }

    //Method that take the outputs of the forward propagation and interpret it in moves within the game, 4 output neurons
    public void decision(){
        int highest_output = Calc.getArrayMaxIndex(NN.feedForward());
        switch(highest_output){
            case 0:
                stay();
                break;
            case 1:
                right();
                break;
            case 2:
                left();
                break;
            case 3:
                jump();
                break;
        }
    }


    //Same method than decision but only takes 3 output neurons and allows the doodle to jump and move at the same time
    public void multi_decision(){
        double[] outputs = NN.feedForward();

        if(outputs[0] > 0.5){
            right();
        }
        if(outputs[1] > 0.5){
            left();
        }
        if(outputs[2] > 0.5){
            jump();
        }
        if(outputs[0] < 0.5 && outputs[1] < 0.5 & outputs[2] < 0.5){
            stay();
        }
    }

    //Returns the exact same doodle from the original one
//    public Doodle duplicate(){
//        Doodle clone = new Doodle(Constants.FRAME_WIDTH/2, container, world, brain.getHidden_layer_size());
//
//        clone.getBrain().init(brain.getW1(), brain.getW2(), brain.getB1(), brain.getB2());
//
//        return clone;
//    }

    //Method that randomly mutates n genes according to a rate by assigning unfirmoly randon new values
//    public void unbiased_weight_mutation(double rate){
//        int amount_mutations = (int) (brain.getTotal_features() * rate);
//
//        int[] genes = new int[amount_mutations];
//
//        for(int i = 0; i < genes.length; i++){
//            genes[i] = Calc.getRand().nextInt(genes.length);
//        }
//
//        for (Integer i: genes
//             ) {
//            if(i < brain.getW1Features()){
//                int m = i / brain.getHidden_layer_size();
//                int n = i - (m * brain.getHidden_layer_size());
//                double previous = brain.getW1()[m][n];
//                brain.getW1()[m][n] = Calc.randomDouble(-1, 1);
//                System.out.println("Mutation of W1["+m+"]["+n+"], previous : "+previous+" new :" + brain.getW1()[m][n]);
//            }else{
//                i = i - brain.getW1Features();
//                int m = i / brain.getOutput_layer_size();
//                int n = i - (m * brain.getOutput_layer_size());
//                double previous = brain.getW2()[m][n];
//                brain.getW2()[m][n] = Calc.randomDouble(-1, 1);
//                System.out.println("Mutation of W1["+m+"]["+n+"], previous : "+previous+" new :" + brain.getW2()[m][n]);
//            }
//        }
//    }
//
//    public void biased_weight_mutation(double rate){
//        int amount_mutations = (int) (brain.getTotal_features() * rate);
//
//        int[] genes = new int[amount_mutations];
//
//        for(int i = 0; i < genes.length; i++){
//            genes[i] = Calc.getRand().nextInt(brain.getTotal_features());
//        }
//
//        for (Integer i: genes
//        ) {
//            if(i < brain.getW1Features()){
//                int m = i / brain.getHidden_layer_size();
//                int n = i - (m * brain.getHidden_layer_size());
//                double previous = brain.getW1()[m][n];
//                brain.getW1()[m][n] += Calc.randomDouble(-1, 1);
//            }else{
//                i = i - brain.getW1Features();
//                int m = i / brain.getOutput_layer_size();
//                int n = i - (m * brain.getOutput_layer_size());
//                double previous = brain.getW2()[m][n];
//                brain.getW2()[m][n] += Calc.randomDouble(-1, 1);
//            }
//        }
//    }

//    public Doodle breed(Doodle d){
//        int hidden_layer_size = d.getBrain().getHidden_layer_size();
//
//        Doodle child = new Doodle()
//
//    }






}
