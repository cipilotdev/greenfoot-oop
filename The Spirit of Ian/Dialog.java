import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Dialog here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Dialog extends Actor {
    private static final String basePath = "ui/tutorialText/";
    
    private int counter;
    private int maxCounter;
    private int transparency;
    private boolean changeImage = false;
    private boolean animate;
    private boolean animateIn = false;
    private boolean animateOut = false;
    private boolean finished = false;
    private int speed = 10;
    
    private static final double pressCooldown = 300000000.0;   //Cooldown (0,30sec) between pressing a key
    private double lastPressedKeyTime;
    
    public Dialog(int counter, int maxCounter) {
        this(counter, maxCounter, true);
    }
    
    public Dialog(int counter, int maxCounter, boolean animate) {
        this.counter = counter;
        this.maxCounter = maxCounter;
        GreenfootImage d = new GreenfootImage(basePath + counter + ".png");
        setImage(d);
        this.animate = animate;
    }
    
    public void act() {
        handleInput();
        
        if (finished) {
            return;
        }
        
        if (animateOut) {
            fadeOut();
        } else if (changeImage) {
            changeToNextImage();
        } else if (animateIn) {
            fadeIn();
        }
    }
    
    public boolean isOpen() {
        return !finished;
    }
    
    private void handleInput() {
        if (Greenfoot.isKeyDown("enter") && counter != maxCounter + 1) {
            double t = System.nanoTime();
            if (t - lastPressedKeyTime >= pressCooldown) {
                lastPressedKeyTime = t;
                if (animate) {
                    animateOut = true;
                } else {
                    skipToNextImage();
                }
            }
        }
    }

    private void fadeOut() {
        transparency -= 10;
        if (transparency <= 0) {
            transparency = 0;
            animateOut = false;
            if (counter < maxCounter) {
                changeImage = true;
            } else if (counter == maxCounter) {
                changeImage = true;
                finished = true;
            }
        }
        getImage().setTransparency(transparency);
    }

    private void changeToNextImage() {
        counter++;
        setImage(new GreenfootImage(basePath + counter + ".png"));
        getImage().setTransparency(0);
        transparency = 0;
        animateIn = true;
        changeImage = false;
    }

    private void fadeIn() {
        transparency += 10;
        if (transparency >= 255) {
            transparency = 255;
            animateIn = false;
        }
        getImage().setTransparency(transparency);
    }
    
    private void skipToNextImage() {
        counter++;
        if (counter > maxCounter) {
            finished = true;
            return;
        }
        setImage(new GreenfootImage(basePath + counter + ".png"));
    }

}
