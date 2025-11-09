import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Progressive here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Progressive extends Difficulty
{
    private static final GreenfootImage progressiveImage = new GreenfootImage("ui/button/difficulty/progressive.png");
    
    public Progressive() {
        setImage(progressiveImage);
    }
    
    /**
     * Act - do whatever the Progressive wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this)) {
            new Game(0);
            Greenfoot.setWorld(new CityClass(0));
        }
    }
}
