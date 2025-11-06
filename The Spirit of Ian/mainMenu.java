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
        addObject(exit,375,421);
        Settings settings = new Settings();
        addObject(settings,818,362);
        Start start = new Start();
        addObject(start,662,481);
        settings.setLocation(887,402);
        exit.setLocation(287,415);
        start.setLocation(681,464);
        settings.setLocation(1028,452);
        start.setLocation(692,507);
        start.setLocation(673,474);
        settings.setLocation(1159,41);
        exit.setLocation(377,477);
        exit.setLocation(350,467);
        exit.setLocation(352,455);
        exit.setLocation(340,463);
        exit.setLocation(363,441);
        exit.setLocation(346,457);
    }
}
