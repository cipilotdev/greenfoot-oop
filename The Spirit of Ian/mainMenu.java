import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class mainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class mainMenu extends World
{
//----- World Background -----
    private static final GreenfootImage mainMenu = new GreenfootImage("worlds/mainMenu.png");
    
    /**
     * Constructor for objects of class cityClass.
     * 
     */
    public mainMenu()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        setBackground(mainMenu);
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        /*DialogText text = new DialogText();
        DialogBox window = new DialogBox(text);
        addObject(window, 625, 280);
        addObject(text, 625, 450);
        addObject(new Dialog(window, true), 0, 0);*/
        Exit exit = new Exit();
        addObject(exit,346,457);

        Settings settings = new Settings();
        addObject(settings,252,323);

        Start start = new Start();
        addObject(start,673,474);
    }
}
