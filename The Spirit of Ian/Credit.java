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
    
    //Credit sequence phases
    private static final int FADE_IN = 0;
    private static final int HOLD = 1;
    private static final int FADE_OUT = 2;
    private static final int ENDING = 3;
    
    private static final int fadeTicks = 30;
    private static final int holdTicks = 90;
    
    private int phase = FADE_IN;
    private int phaseTimer = 0;
    private int frameIndex = 0;
    
    private GreenfootImage previousBg;
    private GreenfootImage nextBg;
    private Overlay overlay;
    private Overlay blackout;
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
            
            startSequence();
        }

        if (animated) {
            runAnimation();
        }
    }
    
    private void startSequence() {
        animated = true;
        frameIndex = 0;
        previousBg = new GreenfootImage(getWorld().getBackground());
        startFrame();
    }
    
    /**
     * Prepare the crossfade to the next world background and show the next credit frame (invisible at first).
     */
    private void startFrame() {
        phase = FADE_IN;
        phaseTimer = 0;
        nextBg = new GreenfootImage(worlds[frameIndex]);
        
        overlay = new Overlay(creditFrames[frameIndex]);
        overlay.getImage().setTransparency(0);
        getWorld().addObject(overlay, 624, 288);
    }

    private void runAnimation() {
        World w = getWorld();
        if (w == null) {
            return;
        }
        phaseTimer++;
        int alpha = Math.min(255, phaseTimer * 255 / fadeTicks);
        
        switch (phase) {
            case FADE_IN:
                crossfadeBackground(w, alpha);
                overlay.getImage().setTransparency(alpha);
                if (phaseTimer >= fadeTicks) {
                    previousBg = nextBg;
                    nextPhase(HOLD);
                }
                break;
            case HOLD:
                if (phaseTimer >= holdTicks) {
                    nextPhase(FADE_OUT);
                }
                break;
            case FADE_OUT:
                overlay.getImage().setTransparency(255 - alpha);
                if (phaseTimer >= fadeTicks) {
                    w.removeObject(overlay);
                    frameIndex++;
                    if (frameIndex < creditFrames.length) {
                        startFrame();
                    } else {
                        blackout = new Overlay("full", 5);
                        w.addObject(blackout, 624, 288);
                        nextPhase(ENDING);
                    }
                }
                break;
            case ENDING:
                if (blackout.isFinished()) {
                    animated = false;
                    Greenfoot.setWorld(new MainMenu(true));
                }
                break;
        }
    }
    
    private void nextPhase(int p) {
        phase = p;
        phaseTimer = 0;
    }
    
    /**
     * Draw the next background over the previous one with the given alpha.
     */
    private void crossfadeBackground(World w, int alpha) {
        GreenfootImage blended = new GreenfootImage(previousBg);
        nextBg.setTransparency(alpha);
        blended.drawImage(nextBg, 0, 0);
        nextBg.setTransparency(255);
        w.setBackground(blended);
    }
}
