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

    private static final GreenfootSound gameOverSound = new GreenfootSound("Defeat.mp3");
    
    public GameOver()
    {   
        super(1248, 576, 1);
        setBackground(gameOverImage);
        prepare();
    }
    
    private void prepare() {
        Return r = new Return();
        addObject(r, 624, 400);

        gameOverSound.playLoop();
    }
    
    @Override
    public void stopped() {
        gameOverSound.stop();
    }
}
