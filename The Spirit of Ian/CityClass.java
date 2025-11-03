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
        DialogText text = new DialogText();
        DialogBox window = new DialogBox(text);
        
        addObject(window, 625, 280);
        addObject(text, 625, 450);
        addObject(new Dialog(window, true), 0, 0);
    }
}
