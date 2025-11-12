import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Health here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Health extends Actor
{
    /**
     * Act - do whatever the Health wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private GreenfootImage[] HealthBar = {
        new GreenfootImage("ui/games/hp/hpBar1.png"),
        new GreenfootImage("ui/games/hp/hpBar2.png"),
        new GreenfootImage("ui/games/hp/hpBar3.png"),
        new GreenfootImage("ui/games/hp/hpBar4.png"),
        new GreenfootImage("ui/games/hp/hpBar5.png")
    };
    
    private int hp = 5;
    
    public Health() {
        updateImage();
        getImage().setTransparency(0);
    }
    
    public void reduceHealth() {
        if (hp > 0) {
            hp--;
            updateImage();
        }
        
        if (hp <= 0) {
            hp = 0;
            Greenfoot.setWorld(new GameOver());
        }
    }
    
    private void updateImage() {
        if (hp > 0) {
            setImage(HealthBar[hp - 1]);
        } else {
            setImage(HealthBar[0]);
        }
    }
    
    public int getHp() {
        return hp;
    }
    
    public void show() {
        getImage().setTransparency(255);
    }
    
    public void hide() {
        getImage().setTransparency(0);
    }
    
    public void act()
    {
        // Add your action code here.        
    }
}
