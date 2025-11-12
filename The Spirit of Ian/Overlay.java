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
    private String animate;
    private int speed;
    private int counter = 0;
    private World world;
    
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
        if (animate == "fadeIn") {
            counter = 0;
        } else if (animate == "fadeOut") {
            counter = 90;
        }
    }
    
    public Overlay(World world) {
        this.world = world;
        animate = "badai";
        counter = 0;
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
        if (animate == "badai") {
            if (world.getClass() == MainMenu.class) {
                if (counter == 4) {
                    counter = 0;
                }
                GreenfootImage background = new GreenfootImage("worlds/animated/mainMenu/frame_" + counter + ".png");
                world.setBackground(background);
                counter++;
            }
        } else if (animate == "fadeIn") {
            if (counter < 90) {
                getImage().setTransparency(counter);
                counter += speed;
            }
        } else if (animate == "fadeOut") {
            if (counter > 0) {
                getImage().setTransparency(counter);
                counter -= speed;
            }
        }
    }
}
