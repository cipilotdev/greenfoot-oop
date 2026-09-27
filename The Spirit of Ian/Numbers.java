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
    private String[] operators = {"+", "-", "*", "/", "_", "="};
    
    public Numbers(String num) {
        switch (num) {
            case "+": num = "plus"; break;
            case "-": num = "minus"; break;
            case "*": num = "times"; break;
            case "/": num = "divide"; break;
            case " ": num = "answer"; break;
            case "=": num = "equal"; break;
            default: num = num;
        }
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
     * Enable or disable mouse clicks on this number (used to lock the keypad
     * while it is hidden, e.g. during multiple-choice questions or class events).
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
    
    /**
     * Act - do whatever the Numbers wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this) && clickable) {
            java.util.List<Question> questions = this.getWorld().getObjects(Question.class);
            for (Question q : questions) {
                if (q.isShown()) {
                    q.addAnswer(num);
                    break;
                }
            }
        }
    }
}
