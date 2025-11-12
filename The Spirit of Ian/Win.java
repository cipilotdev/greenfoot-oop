import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Win here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Win extends World
{
    private static final GreenfootImage winImage = new GreenfootImage("worlds/win.png");        

    public Win(int score) {
        super(1248, 576, 1);

        setBackground(winImage);
        showScore(score);
    }
    
    private void showScore(int score) {
        GreenfootImage text = new GreenfootImage("Skor kamu: " + score, 40, Color.YELLOW, new Color(0,0,0,0));
        getBackground().drawImage(text, getWidth()/2 - text.getWidth()/2, getHeight()/2 + 200);
    }
}
