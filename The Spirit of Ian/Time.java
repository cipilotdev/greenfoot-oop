import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Time here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Time extends Actor
{
    /**
     * Act - do whatever the Time wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private long startTime;
    private int limit;
    private boolean running;
    private int difficulty;
    private int day = 0;
    
    public Time(int difficulty) {
        this.difficulty = difficulty;
        setLimit();
        setImage(new GreenfootImage(100, 40));
    }
    
    private void setLimit() {
        switch(difficulty) {
            case 0:
                limit = 8;
                break;
            case 1:
                limit = 20;
                break;
            case 2:
                limit = 15;
                break;
            case 3:
                limit = 10;
                break;
            default:
                limit = 15;
                break;
        }
    }
    
    public void reset() {
        startTime = System.currentTimeMillis();
        running = true;
        updateImage();
    }
    
    public void act()
    {
        // Add your action code here.
        if (running) {
            if (getTime() <= 0) {
                timeUp();
            } else {
                updateImage();
            }
        }
    }
    
    private int getElapsedTime() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    private void updateImage() {
        int remaining = Math.max(limit - getElapsedTime(), 0);
        setImage(new GreenfootImage("Time: " + remaining + "s", 24, Color.WHITE, new Color(0, 0, 0, 0)));
    }
    
    private void timeUp() {
        running = false;

        World world = getWorld();
        if (world != null) {
            List<Health> healthList = world.getObjects(Health.class);
            if (!healthList.isEmpty()) {
                Health hp = healthList.get(0);
                hp.reduceHealth();
            }

            reset();
        }
    }
    
    public void start() {
        reset();
    }
    
    public void stop() {
        running = false;
    }
    
    public int getTime() {
        return limit - getElapsedTime();
    }
    
    public int getDay() {
        return day;
    }
    
    public void setDay() {
        day++;
    }
}
