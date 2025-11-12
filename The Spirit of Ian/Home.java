import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Home here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Home extends Game
{
    private static final GreenfootImage homeMap = new GreenfootImage("worlds/home.png");
    private static final GreenfootSound homeSound = new GreenfootSound("Home.mp3");
    
    private Teacher teacher;
    
    private Dialog lelah = new Dialog(24, 27, true);
    
    /**
     * Constructor for objects of class Home.
     * 
     */
    public Home(Teacher teacher)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.teacher = teacher;
        setBackground(homeMap);
        prepare();
    }
    
    private void prepare()
    {
        homeSound.playLoop();
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        //Teacher teacher = new Teacher();
        addObject(teacher, 140, 207);

        Collider collider = new Collider(10, 10, 10, 10);
        addObject(collider, 803, 168);
        
        addObject(lelah, 624, 288);
    }
    
    @Override
    public void stopped() {
        homeSound.stop();
    }
}
