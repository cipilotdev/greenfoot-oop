import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MazePath here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MazePath extends World
{
    private static final GreenfootImage mazeMap = new GreenfootImage("worlds/maze.png");
    
    /**
     * Constructor for objects of class MazePath.
     * 
     */
    public MazePath()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        setBackground(mazeMap);
        prepare();
    }
    
    private void prepare()
    {
        Teacher teacher = new Teacher();
        addObject(teacher, 140, 207);
    }
}
