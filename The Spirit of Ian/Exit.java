import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class exit here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Exit extends Button
{
    private static final GreenfootImage exitImage = new GreenfootImage("ui/button/exit.png");
    
    public Exit()
    {    
        setImage(exitImage);
    }
    
    /**
     * Act - do whatever the exit wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            Greenfoot.stop();
        }
    }
}
