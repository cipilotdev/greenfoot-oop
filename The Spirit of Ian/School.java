import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class School here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class School extends Game
{
    private static final GreenfootImage schoolMap = new GreenfootImage("worlds/school.png");
    private static final GreenfootSound schoolSound = new GreenfootSound("School.mp3");
    
    private Dialog capek = new Dialog(30, 31, true);
    
    private Teacher teacher;
    /**
     * Constructor for objects of class School.
     * 
     */
    public School(Teacher teacher)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.teacher = teacher;
        setBackground(schoolMap);
        prepare();
    }
    
    private void prepare()
    {
        schoolSound.playLoop();
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        //Teacher teacher = new Teacher();
        addObject(teacher, 20, 278);
        
        addObject(capek, 624, 288);
    }
    
    @Override
    public void stopped() {
        schoolSound.stop();
    }
}
