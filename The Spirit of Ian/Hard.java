import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Hard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hard extends Button
{
    private static final GreenfootImage hardImage = new GreenfootImage("ui/button/difficulty/hard.png");
    
    public Hard() {
        setImage(hardImage);
    }
    
    /**
     * Act - do whatever the Hard wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            stopSound();
            Greenfoot.setWorld(new CityClass(new Teacher(3)));
        }
    }
}
