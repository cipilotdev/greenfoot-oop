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
    
    public Board()
    {    
        setImage(papanTulisImage);
        getImage().setTransparency(0);
    }
    
    public void act() {
        
    }
    
    public void open() {
        getImage().setTransparency(255);
    }
    
    public void close() {
        getImage().setTransparency(0);
    }
}
