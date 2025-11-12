import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Settings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Settings extends Button
{
    private static final GreenfootImage settingsImage = new GreenfootImage("ui/button/settings.png");
    
    public Settings()
    {    
        setImage(settingsImage);
    }
    
    /**
     * Act - do whatever the Settings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            Greenfoot.setWorld(null);
        }
    }
}
