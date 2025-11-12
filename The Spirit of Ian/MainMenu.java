import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class mainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainMenu extends World
{
//----- World Background -----
    private static final GreenfootImage mainMenu = new GreenfootImage("worlds/mainMenu.png");
    private static final GreenfootSound mainMenuSound = new GreenfootSound("Opening.mp3");
    
    public MainMenu()
    {
        super(1248, 576, 1);
        setBackground(mainMenu);
        prepare();
    }
    
    private void prepare()
    {
        Exit exit = new Exit();
        addObject(exit, 346, 457);
        //exit.animateOnce(7, "slide");

        Settings settings = new Settings();
        addObject(settings, 252, 323);
        //settings.animateOnce(5, "slide");

        Start start = new Start();
        addObject(start, 673, 474);
        start.animateOnce(11, "drop");
        
        Overlay a = new Overlay(this);
        //addObject(a, 0, 0);
    }
    
    @Override
    public void started() {
        if (!mainMenuSound.isPlaying()) {
            mainMenuSound.playLoop();
        }
    }
    
    @Override
    public void stopped() {
        mainMenuSound.stop();
    }
}
