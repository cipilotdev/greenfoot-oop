import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Dialog here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Dialog extends Actor
{
    private boolean tutorialFinished = false;                   //True if the tutorial was finished

    private String textToDisplayNext = "tutorialText1";         //The name of the text which should be displayed next
    private String checkPlayer;                                 //The action the player has to do

    private String changeVariableTo;                            //The name to which the textToDisplayNext String should be set after the cooldown

    private static final double updateCooldown = 3000000000.0;  //Cooldown after the player fullfilled the tutorial message
    private double lastActionTime;                              //Time when the player fullfilled the tutorial message

    private static final double remTutCooldown = 10000000000.0; //Cooldown after the last tutorial message was displayed (Tutorial will be finished after cooldown)
    private double lastMessageDisplayedTime;                    //Time when the last tutorial message was displayed
    
    private boolean killAllEnemysText = false;                  //True if the kill all enemy text is currently being displayed
    private static final double removeCooldown = 5000000000.0;  //Cooldown after the kill all enemy text was displayed
    private double lastKillAllEnemysTextTime;                   //Time when the kill all enemy text was displayed

    //----- Reference -----
    private DialogBox window;                              //Reference to the tutorial window

    /**
     * Tutorial Constructor: Sets the image to null and sets the reference to the window.
     * 
     * @param 'newWindow': Reference to the tutorial window
     * @param 'showTutorial': Only true in the first world to show the tutorial only once.
     */
    public Dialog(DialogBox window, boolean showDialog)
    {
        setImage((GreenfootImage)null);
        
        this.window = window;
        tutorialFinished = !showDialog;
    }
    
    /**
     * Act - do whatever the Dialog wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        update();
    }
    
    private void update() {
        window.fadeIn();
    }
}
