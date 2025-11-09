import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PapanTulis here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board extends Actor
{
    private static final GreenfootImage papanTulisImage = new GreenfootImage("ui/board.png");
    
    private boolean isOpen = false;
    /**
     * Constructor for objects of class PapanTulis.
     * 
     */
    public Board()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        setImage(papanTulisImage);
        getImage().setTransparency(0);
    }
    
    public void act() {
        if (!isOpen) {
            if (Greenfoot.isKeyDown("f")) {
                getImage().setTransparency(255);
                isOpen = true;
            }
        } else {
            if (Greenfoot.isKeyDown("f")) {
                getImage().setTransparency(0);
                isOpen = false;
            }
        }
    }
}
