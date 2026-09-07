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
        
        addObject(teacher, 140, 207);
        teacher.stopMoving();
        
        Collider collider2 = new Collider(1248, 265, 0, 0);
        addObject(collider2, 624, 35);
        
        Collider collider3 = new Collider(1248, 265, 0, 0);
        addObject(collider3, 624, 528);
        
        Collider collider4 = new Collider(50, 50, 0, 0);
        addObject(collider4, 770, 226);
        
        Collider collider5 = new Collider(50, 50, 0, 0);
        addObject(collider5, 769, 357);
        
        Collider collider6 = new Collider(50, 50, 0, 0);
        addObject(collider6, 565, 360);
        
        Collider collider7 = new Collider(50, 50, 0, 0);
        addObject(collider7, 383, 368);
        
        Collider collider8 = new Collider(50, 50, 0, 0);
        addObject(collider8, 77, 328);
        
        Collider collider9 = new Collider(50, 50, 0, 0);
        addObject(collider9, 39, 265);
        
        Collider collider10 = new Collider(50, 50, 0, 0);
        addObject(collider10, 258, 188);
        
        Collider collider11 = new Collider(50, 50, 0, 0);
        addObject(collider11, 498, 343);
        
        Collider collider12 = new Collider(50, 50, 0, 0);
        addObject(collider12, 1002, 352);
        
        Collider collider13 = new Collider(50, 50, 0, 0);
        addObject(collider13, 1072, 304);
        
        Collider collider14 = new Collider(50, 50, 0, 0);
        addObject(collider14, 1217, 371);
        
        Collider collider15 = new Collider(50, 50, 0, 0);
        addObject(collider15, 1139, 346);
        
        Collider collider16 = new Collider(50, 50, 0, 0);
        addObject(collider16, 1139, 346);
        
        Collider collider17 = new Collider(50, 50, 0, 0);
        addObject(collider17, 1120, 206);
        
        addObject(lelah, 624, 288);
    }
    
    @Override
    public void stopped() {
        homeSound.stop();
    }
}
