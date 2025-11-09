import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Start here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Start extends Button
{
    private static final GreenfootImage startImage = new GreenfootImage("ui/button/start.png");
    
    public Start()
    {    
        setImage(startImage);
    }
    
    /**
     * Act - do whatever the Start wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this)) {
            //Greenfoot.setWorld(new MazePath(2));
            World w = ((MainMenu)getWorld());
            
            Overlay o = new Overlay();
            w.addObject(o, 624, 288);
            
            Easy e = new Easy();
            w.addObject(e, 100, 450);
            
            Medium m = new Medium();
            w.addObject(m, 300, 450);
            
            Hard h = new Hard();
            w.addObject(h, 500, 450);
            
            Progressive p = new Progressive();
            w.addObject(p, 700, 450);
            
            w.removeObjects((w.getObjects(Button.class)));
        }
    }
}
