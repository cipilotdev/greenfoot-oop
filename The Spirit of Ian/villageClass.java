import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class VillageClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VillageClass extends Game
{
    private static final GreenfootImage villageClassMap = new GreenfootImage("worlds/villageClass.png");
    
    private int difficulty;
    /**
     * Constructor for objects of class VillageClass.
     * 
     */
    public VillageClass(int difficulty)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.difficulty = difficulty;
        setBackground(villageClassMap);
        prepare();
    }
    
    private void prepare() {
        Board board = new Board();
        addObject(board, 624, 288);
    }
}
