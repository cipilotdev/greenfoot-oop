import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Settings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Credit extends Button
{
    private static final GreenfootImage creditsImage = new GreenfootImage("ui/button/credits.png");
    

    private GreenfootImage[] creditFrames = {
        new GreenfootImage("ui/creditFrames/Austin.png"),
        new GreenfootImage("ui/creditFrames/David.png"),
        new GreenfootImage("ui/creditFrames/Michael.png"),
        new GreenfootImage("ui/creditFrames/Triemas.png")
    };

    private GreenfootImage[] worlds = {
        new GreenfootImage("worlds/cityClass.png"),
        new GreenfootImage("worlds/home.png"),
        new GreenfootImage("worlds/maze.png"),
        new GreenfootImage("worlds/school.png"),
        new GreenfootImage("worlds/villageClass.png")
    };
    
    private int counter = 0;
    private int frameIndex = 0;
    private int bgIndex = 0;
    private int frameTimer = 0;
    private int bgTimer = 0;
    private int frameDuration = 92;
    private int bgDuration = 86;

    private Overlay overlay;
    private boolean animated = false;

    public Credit()
    {    
        setImage(creditsImage);
    }
    
    /**
     * Act - do whatever the Settings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this) && !animated) {
            World w = this.getWorld();
            if (w != null && w.getClass() == MainMenu.class) {
                MainMenu main = (MainMenu)getWorld();
                main.stopAnimate();
                main.hide();
            }
            
            animated = true;
        }

        if (animated) {
            runAnimation();
        }
    }

    private void runAnimation() {
        if (frameTimer > frameDuration) {
            frameTimer = 0;
            frameIndex++;
            if (frameIndex > 4) {
                frameIndex = 0;
                animated = false;
                Greenfoot.setWorld(new MainMenu());
                World w = getWorld();
                if (w != null && w.getClass() == MainMenu.class) {
                    MainMenu main = (MainMenu)getWorld();
                    main.startAnimate();
                    main.show();
                }
            }
        }
        
        if (bgTimer > bgDuration) {
            bgTimer = 0;
            bgIndex++;
            if (bgIndex > 4) {
                bgIndex = 0;
            }
        }
        
        World w = getWorld();
        if (w != null && frameTimer == frameDuration) {
            w.removeObject(overlay);
            
            if (frameIndex != 4) {
                overlay = new Overlay(creditFrames[frameIndex]);
                w.addObject(overlay, 624, 288);
            }
        }
        if (w != null && bgTimer == bgDuration) {
            w.setBackground(worlds[bgIndex]);
        }
        
        frameTimer++;
        bgTimer++;
    }
}
