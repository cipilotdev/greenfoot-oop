import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Easy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Easy extends Difficulty
{
    private static final GreenfootImage easyImage = new GreenfootImage("ui/button/difficulty/easy.png");
    
    public Easy() {
        setImage(easyImage);
    }
    
    /**
     * Act - do whatever the Easy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this)) {
            new Game(1);
            Greenfoot.setWorld(new CityClass(1));
        }
    }
}
