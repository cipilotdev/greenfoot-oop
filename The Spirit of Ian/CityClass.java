import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class cityClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CityClass extends World
{

    /**
     * Constructor for objects of class cityClass.
     * 
     */
    public CityClass()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1); 
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        Teacher teacher = new Teacher();
        addObject(teacher,841,141);
    }
}
