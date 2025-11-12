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
    
    private Teacher teacher;
    /**
     * Constructor for objects of class VillageClass.
     * 
     */
    public VillageClass(Teacher teacher)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        this.teacher = teacher;
        setBackground(villageClassMap);
        prepare();
    }
    
    private void prepare() {
        int difficulty = teacher.getDifficulty();
        
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        //Teacher teacher = new Teacher();
        addObject(teacher, 624, 288);
        
        BoardCollision board1 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board1, 600, 200);
    }
}
