import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class button here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Button extends Actor
{
    private int targetX;
    private int targetY;
    private int speed;
    private String version;
    private boolean animate = false;
    private int counter = 0;
    
    public void animateOnce(int speed, String version) {
        this.speed = speed;
        this.version = version;
        targetX = getX();
        targetY = getY();
        
        if (version == "slide") {
            setLocation(0, targetY);
        } else if (version == "drop") {
            setLocation(targetX, 0);
            getImage().setTransparency(0);
        } else if (version == "fadeIn") {
            getImage().setTransparency(0);
        }
        animate = true;
    }
    
    private void animate() {
        if (version == "slide") {
            if (getX() < targetX) {
                setLocation(getX() + speed, getY());
            } else {
                setLocation(targetX, targetY);
                animate = false;
            }
        } else if (version == "drop") {
            if (getY() < targetY) {
                setLocation(targetX, getY() + speed);
                int t = getImage().getTransparency();
                if (t <= 255 && (targetY / 12) < getY()) {
                    if (t + speed > 255) {
                        t = 255 - speed;
                    }
                    getImage().setTransparency(t + speed);
                }
            } else {
                setLocation(targetX, targetY);
                animate = false;
            }
        } else if (version == "fadeIn") {
            if (counter <= 255) {
                counter = counter + speed;
                if (counter > 255) {
                    counter = 255;
                }
                getImage().setTransparency(counter);
            } else {
                animate = false;
            }
        }
    }
    
    public void act()
    {
        if (animate) {
            animate();
        }
    }
    
    public void stopSound() {
        MainMenu menu = (MainMenu)getWorld();
        menu.stopped();
    }
}
