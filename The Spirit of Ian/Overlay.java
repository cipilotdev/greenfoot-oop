import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Overlay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Overlay extends Actor
{
    private int xSize = 1248;
    private int ySize = 576;
    
    private GreenfootImage transparent;
    public Overlay() {
        /*this.xSize = xSize;
        this.ySize = ySize;*/
        
        transparent = new GreenfootImage(xSize, ySize);
        
        transparent.setColor(Color.BLACK);
        transparent.fill();
        transparent.setTransparency(90);
        
        setImage(transparent);
    }
    /**
     * Act - do whatever the Overlay wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
