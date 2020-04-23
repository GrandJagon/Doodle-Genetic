package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class World extends JPanel implements Runnable {
    private boolean running;
    private Thread animator;
    private int timer;
    private Font timerFont;
    private LinkedList<Element> elements;
    private ArrayList<Platform> platforms;
    private GeneticPool genePool;
    private Graphics2D offGraphics;
    private Dashboard dashboard;
    private Image offImage;
    private Camera camera;
    private int ptIndex;
    private int popRatio;
    private Random rand;

    public void init(Dashboard dashboard){
        setPreferredSize(new Dimension(Constants.WORLD_WIDTH, Constants.FRAME_HEIGHT));
        setBackground(Color.WHITE);
        setVisible(true);
        setFocusable(true);
        timer = Constants.TIMER;
        elements = new LinkedList<>();
        platforms = new ArrayList<>();
        camera = new Camera(this);
        ptIndex = 0;
        popRatio = Constants.PLATFORM_POP_RATIO;
        rand = new Random();
        genePool = new GeneticPool(Constants.POOL_SIZE, Constants.SELECTION_RATE,this);
        genePool.populate();
        this.dashboard = dashboard;
        this.dashboard.init(genePool);
        timerFont = Constants.TIMER_FONT;

        init_platforms();
        start();
    }

    public int getPopRatio(){
        return popRatio;
    }

    public Doodle getBestDoodle(){
        return genePool.getBestDoodle();
    }

    public Platform getFirstPlatform(){
        return platforms.get(0);
    }

    public ArrayList<Platform> getPlatforms(){
        return platforms;
    }

    public int getGeneration(){
        return this.genePool.getGeneration();
    }


    public LinkedList<Element> getElements(){
        return elements;
    }

    public void init_platforms(){
        for(int i = Constants.FRAME_HEIGHT - popRatio; i >=  0;  i -= popRatio) {
            int speed = Calc.randomInt(Constants.PLATFORM_MINIMUM_SPEED, Constants.PLATFORM_MAXIMUM_SPEED);
            int width = Calc.randomInt(Constants.PLATFORM_MINIMUM_WIDTH, Constants.PLATFORM_MAXIMUM_WIDTH);
            Platform pt = generate_platform(speed, width, i);
            elements.add(pt);
            platforms.add(pt);
        }
    }

    public Platform generate_platform(int speed, int width, int y){
        ptIndex++;
        int x = rand.nextInt(Constants.WORLD_WIDTH - width);
        System.out.println("Platform generated with index = "+ptIndex+", speed = "+speed+" and width = "+width);
        return new Platform(x, y, width, speed, ptIndex, platforms);
    }

    public void addPlatform(){
        int y = platforms.get(platforms.size() - 1).getPosition().getY();
        Platform pt = generate_platform(Constants.PLATFORM_MINIMUM_SPEED, Constants.PLATFORM_MAXIMUM_WIDTH, y - popRatio);
        elements.add(pt);
        platforms.add(pt);
    }

    public void start(){
       if(animator == null || !running){
           animator = new Thread(this);
           animator.start();
       }
    }

    public void update(){
        if(timer <= 0 || genePool.getAliveDoodles().size() <= 0){
            gameOver();
        }
        for (Element element: elements
             ) {
            element.update();
        }
        for (Doodle d: genePool.getAliveDoodles()
             ) {
            d.wallCollide();
            for (Platform p: platforms
                 ) {
                if(d.collide(p)){
                    break;
                }
                d.setOnGround(false);
            }
            d.updateScore();
        }
        dashboard.updateDashboard();
    }

    public void render(){
        if(offImage == null){
            offImage = this.createImage(Constants.WORLD_WIDTH, Constants.FRAME_HEIGHT);
            offGraphics = (Graphics2D) offImage.getGraphics();
        }
        camera.update(genePool.getHighestDoodle());
        offGraphics.setColor(Color.WHITE);
        offGraphics.fillRect(0,0, Constants.WORLD_WIDTH, Constants.FRAME_HEIGHT);
        for (Element element : elements
             ) {
            if(element.isOnScreen(camera)){
                element.paint(offGraphics, camera);
            }else{
                element.remove();
            }
        }
        if(Constants.AUTOMATIC){
            offGraphics.setColor(Color.BLUE);
            offGraphics.setFont(timerFont);
            offGraphics.drawString(String.valueOf(timer/32), 475, 15);
            timer--;
        }
    }

    public void paint(){
        Graphics2D g = (Graphics2D) this.getGraphics();

        if(offGraphics != null){
            g.drawImage(offImage, 0, 0, null );
        }
    }

    public void gameOver(){
        running = false;
        dashboard.logScore();
        ptIndex = 0;
        camera.reset();
        genePool.sortPopulation();
        genePool.naturalSelection();
        genePool.crossOver();
        System.out.println("Alive doodle after crossover : "+genePool.getAliveDoodles().size());
        genePool.mutatePopulation();
        if(Constants.AUTOMATIC){
            timer = Constants.TIMER;
            launchNextGeneration();
        }
    }

    public void launchNextGeneration(){
        if(running){
            return;
        }
        System.out.println("---------------------------LAUNCH NEW GENERATION----------------------------");
        camera = new Camera(this);
        elements = new LinkedList<>();
        platforms = new ArrayList<>();
        init_platforms();
        for (Doodle d : genePool.getIndividuals()
             ) {
            elements.add(d);
        }
        start();
    }


    @Override
    public void run() {
        long startTime, delay, sleep;

        startTime = System.currentTimeMillis();
        delay = 30;

        if(Constants.AUTOMATIC){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        running = true;

        System.out.println("RUN, delay "+delay);

        while(running){

            update();
            render();
            paint();

            try{
                startTime += delay;
                Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }


    }
}
