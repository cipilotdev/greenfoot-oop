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
        teacher.stopMoving();
        setBackground(schoolMap);
        prepare();
    }
    
    private void prepare()
    {
        schoolSound.playLoop();
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        addObject(teacher, 20, 278);
        
        addObject(capek, 624, 288);
        
        Collider collider2 = new Collider(1248, 265, 0, 0);
        addObject(collider2, 624, 50);
        
        Collider collider3 = new Collider(1248, 265, 0, 0);
        addObject(collider3, 624, 478);
        
        Collider collider4 = new Collider(40, 40, 0, 0);
        addObject(collider4, 160, 320);
        
        Collider collider5 = new Collider(50, 50, 0, 0);
        addObject(collider5, 415, 310);
        
        Collider collider6 = new Collider(40, 40, 0, 0);
        addObject(collider6, 455, 195);
        
        Collider collider8 = new Collider(150, 120, 0, 0);
        addObject(collider8, 940, 230);
        
        Collider collider9 = new Collider(200, 200, 0, 0);
        addObject(collider9, 1190, 250);
    }
    
    @Override
    public void stopped() {
        schoolSound.stop();
    }
}
