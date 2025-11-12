import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Medium here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Medium extends Button
{
    private static final GreenfootImage mediumImage = new GreenfootImage("ui/button/difficulty/medium.png");
    
    public Medium() {
        setImage(mediumImage);
    }
    
    /**
     * Act - do whatever the Medium wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            stopSound();
            Greenfoot.setWorld(new CityClass(new Teacher(2)));
        }
    }
}
