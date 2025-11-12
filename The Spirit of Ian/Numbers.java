import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Numbers here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Numbers extends Actor
{
    private static final String basePath = "ui/numbers/";
    private String num;
    private boolean clickable = true;
    
    public Numbers(String num) {
        GreenfootImage numImage = new GreenfootImage(basePath + num + ".png");
        this.num = num;
        setImage(numImage);
    }
    
    public Numbers(String num, boolean clickable) {
        this(num);
        this.clickable = clickable;
    }
    
    public Numbers(GreenfootImage img) {
        setImage(img);
    }
    
    /**
     * Act - do whatever the Numbers wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this) && clickable) {
            Question q = this.getWorld().getObjects(Question.class).get(0);
            q.addAnswer(num);
        }
    }
}
