import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Game here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game extends World
{
    private int difficulty;
    
    /**
     * Constructor for objects of class Game.
     * 
     */
    public Game(int difficulty)
    {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        this.difficulty = difficulty;
    }
    
    public Game(int x, int y) {
        super(x, y, 1);
    }
    
    public Game(int x, int y, boolean bounded) {
        super(x, y, 1, bounded);
    }
    
    /**
     * Worlds with a cutscene override this so the teacher stays frozen while it plays.
     */
    public boolean isCutscenePlaying() {
        return false;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
}
