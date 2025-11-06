import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class VillageClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VillageClass extends World
{
    private static final GreenfootImage villageClassMap = new GreenfootImage("worlds/villageClass.png");
    
    /**
     * Constructor for objects of class VillageClass.
     * 
     */
    public VillageClass()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        setBackground(villageClassMap);
        prepare();
    }
    
    private void prepare() {
        
    }
}
