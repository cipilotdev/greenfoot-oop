import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GameOver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameOver extends World
{

    /**
     * Constructor for objects of class GameOver.
     * 
     */
    private static final GreenfootImage gameOverImage = new GreenfootImage("worlds/gameOver.png");        

    public GameOver()
    {   
        super(1248, 576, 1);
        setBackground(gameOverImage);
    }
}
