import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Settings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Controls extends Button
{
    private static final GreenfootImage controlsImage = new GreenfootImage("ui/button/controls.png");
    private static final GreenfootImage menuImg = new GreenfootImage("ui/controlMenu.png");
    
    private Overlay menu = new Overlay(menuImg);
    public Controls()
    {
        setImage(controlsImage);
    }
    
    /**
     * Act - do whatever the Settings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            getWorld().addObject(menu, 624, 288);
            getWorld().addObject(new Return(), 70, 70);
        }
    }
}
