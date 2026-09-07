import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Log here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Log extends Collider
{
    private int clickRange;
    private int state;
    
    private static final double pressCooldown = 250000000.0;   //Cooldown (0,25sec) between pressing a key
    private double lastPressedKeyTime;
    
    private GreenfootImage logImage = new GreenfootImage("ui/tree/tree_1.png");
        
    public Log(int clickRange, int state) {
        super(new GreenfootImage("ui/tree/tree_" + state + ".png"));
        this.clickRange = clickRange;
        this.state = state;
    }
    
    /**
     * Act - do whatever the Log wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        double i = System.nanoTime();
        if (Greenfoot.isKeyDown("space")) {
            if(!getObjectsInRange(clickRange, Teacher.class).isEmpty()) {
                Teacher t = (Teacher)getObjectsInRange(clickRange, Teacher.class).get(0);
                if(t != null) {
                    if(i - lastPressedKeyTime >= pressCooldown) {
                        updateState();
                        lastPressedKeyTime = System.nanoTime();
                    }
                }
            }
        }
    }
    
    private void updateState() {
        state++;
        if (state > 5) {
            getWorld().removeObject(this);
            return;
        }
        GreenfootImage background = new GreenfootImage("ui/tree/tree_" + state + ".png");
        setImage(background);
    }
}
