import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Home here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Home extends World
{
    private static final GreenfootImage homeMap = new GreenfootImage("worlds/home.png");
    
    /**
     * Constructor for objects of class Home.
     * 
     */
    public Home()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        setBackground(homeMap);
        prepare();
    }
    
    private void prepare()
    {
        Teacher teacher = new Teacher();
        addObject(teacher, 140, 207);

        Collider collider = new Collider(10, 10, 10, 10);
        addObject(collider, 803, 168);
    }
}
