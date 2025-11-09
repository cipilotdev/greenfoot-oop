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
    
    private int difficulty;
    /**
     * Constructor for objects of class School.
     * 
     */
    public School(int difficulty)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.difficulty = difficulty;
        setBackground(schoolMap);
        prepare();
    }
    
    private void prepare()
    {
        Teacher teacher = new Teacher();
        addObject(teacher, 20, 278);
    }
}
