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
    
    public void act()
    {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            //Greenfoot.setWorld(new VillageClass(new Teacher(1)));
            World w = ((MainMenu)getWorld());
            
            w.removeObjects((w.getObjects(Button.class)));
            
            Overlay o = new Overlay("fadeIn", 3);
            w.addObject(o, 624, 288);
            
            Easy e = new Easy();
            w.addObject(e, 100, 450);
            e.animateOnce(6, "fadeIn");
            
            Medium m = new Medium();
            w.addObject(m, 300, 450);
            m.animateOnce(5, "fadeIn");
            
            Hard h = new Hard();
            w.addObject(h, 500, 450);
            h.animateOnce(4, "fadeIn");
            
            Progressive p = new Progressive();
            w.addObject(p, 700, 450);
            p.animateOnce(3, "fadeIn");
            //System.out.println(w.getObjects(Button.class));*/
        }
    }
}
