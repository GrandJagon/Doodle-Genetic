package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Random;

public class World extends JPanel implements Runnable {
    private boolean running;
    private Thread animator;
    private LinkedList<Element> elements;
    private LinkedList<Platform> platforms;
    private GeneticPool genePool;
    private Graphics2D offGraphics;
    private Dashboard gameStats;
    private Image offImage;
    private Camera camera;
    private int ptIndex;
    private int popRatio;
    private Random rand;

    public void init(Dashboard dashboard){
        setPreferredSize(new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));
        setBackground(Color.WHITE);
        setVisible(true);
        setFocusable(true);
        elements = new LinkedList<>();
        platforms = new LinkedList<>();
        camera = new Camera(this);
        ptIndex = 0;
        popRatio = 60;
        rand = new Random();
        genePool = new GeneticPool(100, 0.25, 0.25, 0.25,this);
        genePool.populate();
        gameStats = dashboard;
        gameStats.init(genePool);

        init_platforms();
        start();
    }

    public int getPopRatio(){
        return popRatio;
    }

    public Doodle getBestDoodle(){
        return genePool.getHighestDoodle();
    }

    public Platform getFirstPlatform(){
        return platforms.get(1);
    }


    public LinkedList<Element> getElements(){
        return elements;
    }

    public void init_platforms(){
        for(int i = Constants.FRAME_HEIGHT - popRatio; i >=  0;  i -= popRatio) {
            Platform pt = generate_platform(Constants.PLATFORM_MINIMUM_SPEED, Constants.PLATFORM_MAXIMUM_WIDTH, i);
            elements.add(pt);
            platforms.add(pt);
        }
    }

    public Platform generate_platform(int speed, int width, int y){
        ptIndex++;
        int x = rand.nextInt(Constants.FRAME_WIDTH - width);
        return new Platform(x, y, width, speed, ptIndex, platforms);
    }

    public void addPlatform(){
        int y = platforms.getLast().getPosition().getY();
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
    }

    public void render(){
        if(offImage == null){
            offImage = this.createImage(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
            offGraphics = (Graphics2D) offImage.getGraphics();
        }
        camera.update(genePool.getHighestDoodle());
        offGraphics.setColor(Color.WHITE);
        offGraphics.fillRect(0,0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        for (Element element : elements
             ) {
            if(element.isOnScreen(camera)){
                element.paint(offGraphics, camera);
            }else{
                element.remove();
                if(elements.size() <= 0){
                    gameOver();
                }
            }
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
        camera.reset();
        elements = new LinkedList<Element>();
        genePool.sortPopulation();
        genePool.naturalSelection();
        genePool.duplication();
    }

    public void launchNextGeneration(){
        init_platforms();
        for (Doodle d : genePool.getIndividuals()
             ) {
            d.reset();
            elements.add(d);
        }
        start();
    }


    @Override
    public void run() {
        long startTime, delay, sleep;

        startTime = System.currentTimeMillis();
        delay = 30;

        running = true;

        while(running){

            update();
            render();
            paint();
            gameStats.updateDashboard();

            try{
                startTime += delay;
                Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }


    }
}
