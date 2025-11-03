import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Dialog here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DialogBox extends Actor
{
    private String text;
    
    private int counter = 0;
    private int speed = 25;
    
    private boolean fade = false;
    private boolean fadeOutIn = false;
    
    //----- Reference -----
    private DialogText tutorialText;
    
    //----- Object image -----
    private static final GreenfootImage box = new GreenfootImage("ui/dialogBox.png");

    public DialogBox(DialogText text) {
        setImage(box);
        getImage().setTransparency(0);
        
        tutorialText = text;
    }
    
    /**
     * Act - do whatever the Dialog wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        fade();
    }
    
    public void fade()
    {
        if(fade)
        {                        
            if(counter >= 0 && counter <= 255)
            {                
                getImage().setTransparency(counter);
            }    
                
            counter += speed;
            
            if(counter >= 255)
            {                
                fade = false;
                
                tutorialText.updateText(text);
                tutorialText.fadeIn();
            }
            
            if(counter <= 0)
            {
                fade = false;
                
                if(fadeOutIn)
                {
                    speed = 3;
                    
                    fade = true;
                    
                    fadeOutIn = false;
                }                                
            }
        }
    }
    
    /**
     * Method 'fadeIn': Is called in the Tutorial class.
     * The window will start fading in.
     */
    public void fadeIn()
    {                
        speed = 3;        
        
        fade = true;
    }    
    
    /**
     * Method 'fadeOutIn': Is called in the Tutorial class.
     * The window will start fading out and in again.
     */
    public void fadeOutIn()
    {
        tutorialText.fadeOut();
        
        fadeOutIn = true;
    }
    
    /**
     * Method 'fadeOut': Is called in the Tutorial class.
     * The window will start fading out.
     */
    public void fadeOut()
    {                        
        tutorialText.fadeOut();                
    }
    
    /**
     * Method 'fadeOutCalledInTutorialText': Is called in the Tutorial Text class.
     * The window will start fading out and in again.
     */
    public void fadeOutCalledInTutorialText()
    {        
        speed = -3;
        
        fade = true;
    }
    
    /**
     * Method 'updateText': Is called in the Tutorial class.
     * It updates the name of the text that should be displayed next.
     * 
     * @param 'text': The name of the text that should be displayed next.
     */
    public void updateText(String text)
    {
        this.text = text;
    }
}
