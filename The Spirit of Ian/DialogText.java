import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DialogText here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DialogText extends Actor
{
    private int counter = 0;
    private int speed = 25;
    
    private boolean fade = false;
    
    //----- Object images -----
    private static final GreenfootImage tutorialText1 = new GreenfootImage("ui/tutorialText/tutorialText1.png");
    private static final GreenfootImage tutorialText2 = new GreenfootImage("ui/tutorialText/tutorialText2.png");
    private static final GreenfootImage tutorialText3 = new GreenfootImage("ui/tutorialText/tutorialText3.png");
    private static final GreenfootImage tutorialText4 = new GreenfootImage("ui/tutorialText/tutorialText4.png");
    private static final GreenfootImage tutorialText5 = new GreenfootImage("ui/tutorialText/tutorialText5.png");
    private static final GreenfootImage tutorialText6 = new GreenfootImage("ui/tutorialText/tutorialText6.png");
    private static final GreenfootImage tutorialText7 = new GreenfootImage("ui/tutorialText/tutorialText7.png");
    private static final GreenfootImage tutorialText8 = new GreenfootImage("ui/tutorialText/tutorialText8.png");
    
    private static final GreenfootImage killAllEnemysText = new GreenfootImage("ui/tutorialText/killAllEnemysText.png");
    
    /**
     * TutorialText Constructor: Sets the image to null.
     */
    public DialogText()
    {
        setImage(tutorialText1);
    }
    
    /**
     * Method 'act': Is called every tick or whenever the 'Act' or 'Run' button gets pressed in the environment.
     * It calls the 'fade' method.
     */
    public void act() 
    {
        fade();
    }
    
    /**
     * Method 'fade': Is called every tick by the 'act' method.
     * If the fade boolean is set to true the text will fade in or out (corresponding to the speed variable set in the 'fadeIn' / 'fadeOut' method).
     */
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
            } 
            
            if(counter <= 0)
            {
                fade = false;
                
                DialogBox window = getWorld().getObjects(DialogBox.class).get(0);
                window.fadeOutCalledInTutorialText();
            }
        }
    }
    
    /**
     * Method 'fadeIn': Is called in the TutorialWindow class.
     * The text will start fading in.
     */
    public void fadeIn()
    {        
        speed = 3;       
        
        fade = true;
    }
    
    /**
     * Method 'fadeOut': Is called in the TutorialWindow class.
     * The text will start fading out.
     */
    public void fadeOut()
    {        
        speed = -3;        
        
        fade = true;
    }
    
    /**
     * Method 'updateText': Is called in the TutorialWindow class.
     * The correct image will be set.
     */
    public void updateText(String text)
    {
        if(text == "tutorialText1")
        {
            setImage(tutorialText1);
        }
        else if(text == "tutorialText2")
        {
            setImage(tutorialText2);
        }
        else if(text == "tutorialText3")
        {
            setImage(tutorialText3);
        }
        else if(text == "tutorialText4")
        {
            setImage(tutorialText4);
        }
        else if(text == "tutorialText5")
        {
            setImage(tutorialText5);
        }
        else if(text == "tutorialText6")
        {
            setImage(tutorialText6);
        }
        else if(text == "tutorialText7")
        {
            setImage(tutorialText7);
        }
        else if(text == "tutorialText8")
        {
            setImage(tutorialText8);
        }
        else if(text == "killAllEnemysText")
        {
            setImage(killAllEnemysText);            
        }
        
        getImage().setTransparency(0);
    }
}
