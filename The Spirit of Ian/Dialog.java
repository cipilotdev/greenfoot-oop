import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Dialog here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Dialog extends Actor {
    private static final String basePath = "ui/tutorialText/";
    
    private static final GreenfootImage arrow = new GreenfootImage("ui/button/continue.png");
    
    private int counter;
    private int maxCounter;
    private int transparency;
    private boolean changeImage = false;
    private boolean animate;
    private boolean animateIn = false;
    private boolean animateOut = false;
    private boolean finished = false;
    private int speed = 10;
    
    private static final double pressCooldown = 300000000.0;   //Cooldown (0,30sec) between pressing a key
    private static double lastPressedKeyTime;                   //Shared so a new dialog can't be skipped by the key that closed the previous one
    private boolean waitRelease = true;                         //Enter must be released once before this dialog accepts it
    private static final int spawnFadeSpeed = 25;
    private int fadeInSpeed = speed;
    
    private Overlay continueBtn = new Overlay(arrow);
    private boolean btnSpawned = false;
    private int btnTransparency = 0;
    
    public Dialog(int counter, int maxCounter) {
        this(counter, maxCounter, true);
    }
    
    public Dialog(int counter, int maxCounter, boolean animate) {
        this.counter = counter;
        this.maxCounter = maxCounter;
        GreenfootImage d = new GreenfootImage(basePath + counter + ".png");
        setImage(d);
        this.animate = animate;
        transparency = 255;
    }
    
    /**
     * Fade the dialog in when it appears instead of popping in.
     * Full-screen (opaque) dialogs appear at once, otherwise the map would show through during the fade.
     */
    @Override
    protected void addedToWorld(World world) {
        if (finished || isOpaque(getImage())) {
            return;
        }
        transparency = 0;
        getImage().setTransparency(0);
        fadeInSpeed = spawnFadeSpeed;
        animateIn = true;
    }
    
    public void act() {
        handleInput();
        
        if (finished) {
            removeContinueBtn();
            return;
        }
        
        if (getWorld() != null && !btnSpawned) {
            getWorld().addObject(continueBtn, 1000, 480);
            btnSpawned = true;
        }
        
        if (btnSpawned && !animateOut) {
            animateBtn();
        }
        
        if (animateOut) {
            continueBtn.getImage().setTransparency(0);
            fadeOut();
        } else if (changeImage) {
            changeToNextImage();
        } else if (animateIn) {
            fadeIn();
        }
    }
    
    public boolean isOpen() {
        return !finished;
    }
    
    private void handleInput() {
        boolean enterDown = Greenfoot.isKeyDown("enter");
        if (waitRelease) {
            if (!enterDown) {
                waitRelease = false;
            }
            return;
        }
        if (animateIn || animateOut || changeImage) {
            return;
        }
        if (enterDown && counter != maxCounter + 1 && !finished) {
            double t = System.nanoTime();
            if (t - lastPressedKeyTime >= pressCooldown) {
                lastPressedKeyTime = t;
                waitRelease = true;
                if (animate) {
                    animateOut = true;
                } else {
                    skipToNextImage();
                }
            }
        }
    }

    private void fadeOut() {
        transparency -= 10;
        if (transparency <= 0) {
            transparency = 0;
            animateOut = false;
            if (counter < maxCounter) {
                changeImage = true;
            } else if (counter == maxCounter) {
                finish();
            }
        }
        getImage().setTransparency(transparency);
    }

    private void changeToNextImage() {
        counter++;
        setImage(new GreenfootImage(basePath + counter + ".png"));
        getImage().setTransparency(0);
        transparency = 0;
        fadeInSpeed = speed;
        animateIn = true;
        changeImage = false;
    }

    private void fadeIn() {
        transparency += fadeInSpeed;
        if (transparency >= 255) {
            transparency = 255;
            animateIn = false;
        }
        getImage().setTransparency(transparency);
    }
    
    private void skipToNextImage() {
        counter++;
        if (counter > maxCounter) {
            finish();
            return;
        }
        setImage(new GreenfootImage(basePath + counter + ".png"));
    }
    
    private void finish() {
        finished = true;
        //Animated dialogs have already faded out. Non-animated ones keep their last page visible,
        //so a full-screen scene stays on screen until the next dialog or world replaces it.
        if (animate) {
            getImage().setTransparency(0);
        }
        removeContinueBtn();
    }
    
    /**
     * Checks a grid of sample points to see if the image covers the whole screen without transparent parts.
     */
    private static boolean isOpaque(GreenfootImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 8; j++) {
                int x = (w - 1) * i / 15;
                int y = (h - 1) * j / 7;
                if (img.getColorAt(x, y).getAlpha() < 255) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void removeContinueBtn() {
        if (continueBtn.getWorld() != null) {
            continueBtn.getWorld().removeObject(continueBtn);
        }
    }
    
    private void animateBtn() {
        if (btnTransparency <= 255) {
            btnTransparency += 5;
            if (btnTransparency > 255) {
                btnTransparency = 0;
            }
        } else {
            btnTransparency -= 5;
            if (btnTransparency < 0) {
                btnTransparency = 255;
            }
        }
        continueBtn.getImage().setTransparency(btnTransparency);
    }
}
