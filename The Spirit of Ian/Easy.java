import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Easy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Easy extends Button
{
    private static final GreenfootImage easyImage = new GreenfootImage("ui/button/difficulty/easy.png");
    
    public Easy() {
        setImage(easyImage);
    }
    
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            stopSound();
            Greenfoot.setWorld(new CityClass(new Teacher(1)));
        }
    }
}
