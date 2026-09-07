import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Return here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Return extends Button
{
    /**
     * Act - do whatever the Return wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    private static final GreenfootImage returnImage = new GreenfootImage("ui/button/return.png");
    private static final GreenfootImage classImage = new GreenfootImage("ui/class.png");
    
    private boolean animated = false;
    private int frameTimer = 0;
    private int frameDuration = 120;
    private int frameIndex = 0;
    private Overlay overlay;
    
    private GreenfootImage[] creditFrames = {
        new GreenfootImage("ui/creditFrames/Austin.png"),
        new GreenfootImage("ui/creditFrames/David.png"),
        new GreenfootImage("ui/creditFrames/Michael.png"),
        new GreenfootImage("ui/creditFrames/Triemas.png")
    };
    
    public Return() {
        setImage(returnImage);
    }

    public void act()
    {
        // Add your action code here.
        super.act();
        if (Greenfoot.mouseClicked(this) && getWorld().getClass() != Win.class) {
            Greenfoot.setWorld(new MainMenu());
        } else if (Greenfoot.mouseClicked(this) && getWorld().getClass() == Win.class) {
            getWorld().setBackground(classImage);
            getImage().setTransparency(0);
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
                if (getWorld().getClass() == VillageClass.class) {
                    ((VillageClass)getWorld()).stopped();
                }
                Greenfoot.setWorld(new MainMenu());
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
        
        frameTimer++;
    }
}