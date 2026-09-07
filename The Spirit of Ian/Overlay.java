import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Overlay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Overlay extends Actor
{
    private int xSize = 1248;
    private int ySize = 576;
    public String animate;
    private int speed;
    public int counter = 0;
    private World world;
    private boolean play = true;
    private boolean isFinished = false;
    private int tickCounter = 0;
    
    private GreenfootImage transparent;
    public Overlay(int transparency) {       
        transparent = new GreenfootImage(xSize, ySize);
        
        transparent.setColor(Color.BLACK);
        transparent.fill();
        transparent.setTransparency(transparency);
        
        setImage(transparent);
    }
    
    public Overlay() {
        this(90);
    }
    
    public Overlay(String animate, int speed) {
        this();
        this.animate = animate;
        this.speed = speed;
        if ("fadeIn".equals(animate)) {
            counter = 0;
        } else if ("fadeOut".equals(animate)) {
            counter = 90;
        } else if ("full".equals(animate)) {
            counter = 0;
            getImage().setTransparency(0);
        }
    }
    
    public Overlay(World world) {
        this.world = world;
        animate = "badai";
        counter = 0;
    }
    
    public Overlay(GreenfootImage ekspresi, String animate, int speed) {
        setImage(ekspresi);
        this.animate = animate;
        this.speed = speed;
    }
    
    public Overlay(GreenfootImage ekspresi) {
        setImage(ekspresi);
    }
    
    /**
     * Act - do whatever the Overlay wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        animate();
    }
    
    public void animate() {
        if (!play) {
            return ;
        }
        
        if (animate == "badai") {
            if (getWorld() != null && getWorld().getClass() == MainMenu.class) {
                tickCounter++;
                if (tickCounter >= 32) {
                    tickCounter = 0;
                    if (counter == 4) {
                        counter = 0;
                    }
                    GreenfootImage background = new GreenfootImage("worlds/animated/mainMenu/frame_" + counter + ".png");
                    getWorld().setBackground(background);
                    counter++;
                }
            }
            // } else if (getWorld() != null && getWorld().getClass() == Win.class) {
            //     Overlay o = new Overlay(creditFrames[counter]);
            //     getWorld().addObject(o, 624, 288);
            //     counter++;
            // }
        }
        if (animate == "fadeIn") {
            if (counter < 90) {
                getImage().setTransparency(counter);
                counter += speed;
            }
        }
        if (animate == "fadeOut") {
            if (counter > 0) {
                getImage().setTransparency(counter);
                counter -= speed;
            } else {
                counter = 0;
                getImage().setTransparency(counter);
                isFinished = true;
            }
        }
        if (animate == "full") {
            if (counter < 255) {
                getImage().setTransparency(counter);
                counter += speed;
            } else {
                counter = 255;
                getImage().setTransparency(counter);
                isFinished = true;
            }
        }
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
    public void setAnimateOut() {
        animate = "fadeOut";
        counter = 255;
        isFinished = false;
    }
    
    public void setAnimateFull() {
        animate = "full";
        counter = 0;
        isFinished = false;
    }
    
    public void setPlay(boolean f) {
        play = f;
    }
    
    public void setAnimateBadai() {
        animate = "badai";
        counter = 0;
    }
}
