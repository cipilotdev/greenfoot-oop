import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Hard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hard extends Difficulty
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
        // Add your action code here.
        if (Greenfoot.mouseClicked(this)) {
            new Game(3);
            Greenfoot.setWorld(new CityClass(3));
        }
    }
}
