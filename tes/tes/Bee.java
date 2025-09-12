import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bee here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bee extends Actor
{
    /**
     * Act - do whatever the Bee wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        turn(-5);
        int speed = 3;
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY() - speed); // Move up
        }
        if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY() + speed); // Move down
        }
        if (Greenfoot.isKeyDown("left")) {
            setLocation(getX() - speed, getY()); // Move left
        }
        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX() + speed, getY()); // Move right
        }
    }
}
