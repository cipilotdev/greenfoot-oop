import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class mainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainMenu extends World
{
//----- World Background -----
    private static final GreenfootImage mainMenu = new GreenfootImage("worlds/mainMenu.png");
    private static final GreenfootSound mainMenuSound = new GreenfootSound("Opening.mp3");
    
    private Overlay a = new Overlay(this);
    private Start start = new Start();
    private Credit credit = new Credit();
    private Controls ctrls = new Controls();
    private Exit exit = new Exit();
    private Overlay title = new Overlay(new GreenfootImage("ui/judul.png"), "full", 1);
    
    private boolean complete = true;
    private Overlay fadeIn;   //Fade from black when returning from the credits
    
    public MainMenu()
    {
        this(false);
    }
    
    /**
     * @param fromCredits true when returning from the credits, the menu then fades in from black
     */
    public MainMenu(boolean fromCredits)
    {
        super(1248, 576, 1);
        setBackground(mainMenu);
        prepare();
        
        if (fromCredits) {
            fadeIn = new Overlay("fadeOut", 4);
            fadeIn.setAnimateOut();
            fadeIn.getImage().setTransparency(255);
            addObject(fadeIn, 624, 288);
        }
    }
    
    private void prepare()
    {
        addObject(title, 654, 270);
        title.getImage().setTransparency(0);
        
        addObject(a, 624, 288);
        a.getImage().setTransparency(0);
        
        //Button images are shared (static) and hide() may have made them invisible, e.g. during the credits
        exit.getImage().setTransparency(255);
        ctrls.getImage().setTransparency(255);
        credit.getImage().setTransparency(255);
        
        addObject(exit, 346, 457);
        
        addObject(ctrls, 250, 350);
        
        addObject(credit, 360, 350);
        
        addObject(start, 673, 474);
        start.animateOnce(11, "drop");
    }
    
    public void act() {
        //The fade overlay covers the whole screen and would catch every mouse click, so remove it once done
        if (fadeIn != null && fadeIn.isFinished()) {
            removeObject(fadeIn);
            fadeIn = null;
        }
    }
    
    @Override
    public void started() {
        if (!mainMenuSound.isPlaying()) {
            mainMenuSound.playLoop();
        }
    }
    
    @Override
    public void stopped() {
        mainMenuSound.stop();
    }
    
    public void stopAnimate() {
        a.setPlay(false);
        complete = false;
    }
    
    public void hide() {
        title.getImage().setTransparency(0);
        exit.getImage().setTransparency(0);
        ctrls.getImage().setTransparency(0);
        credit.getImage().setTransparency(0);
        start.getImage().setTransparency(0);
    }
    
    public void show() {
        title.getImage().setTransparency(255);
        exit.getImage().setTransparency(255);
        ctrls.getImage().setTransparency(255);
        credit.getImage().setTransparency(255);
        start.getImage().setTransparency(255);
        startAnimate();
    }
    
    public void startAnimate() {
        setBackground(mainMenu);
        a.setAnimateBadai();
        a.setPlay(true);
        complete = true;
    }
}
