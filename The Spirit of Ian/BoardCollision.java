import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BoardCollision here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BoardCollision extends Collider
{
    private Overlay o = new Overlay(30);
    private Board board;
    private Answer generator;
    private TrueButton trueButton;
    private FalseButton falseButton;
    private Question question;
    private Health healthBar;
    private Overlay ekspresi;
    
    private int questionCount = 0;
    private int score = 0;
    
    private int clickRange;
    private boolean isOpen = false;
    private boolean added = false;
    
    private static final double pressCooldown = 250000000.0;   //Cooldown (0,25sec) between pressing a key
    private double lastPressedKeyTime;
    
    private int difficulty;
    
    private GreenfootSound ngajarSound = new GreenfootSound("Ngajar.mp3");
    
    private String ekspresiBasePath = "ui/ekspression/";
    
    public BoardCollision(int xSize, int ySize, int clickRange, int difficulty) {
        super(xSize, ySize, 0, 0);
        this.clickRange = clickRange;
        this.difficulty = difficulty;
        
        board = new Board();
        question = new Question(difficulty);
        healthBar = new Health();
        //ekspresi = new Overlay(new GreenfootImage(ekspresiBasePath + 5 + ".png"));
    }
    
    public void act()
    {
        double i = System.nanoTime();
        if (Greenfoot.isKeyDown("f") && !isOpen) {
            if(!getObjectsInRange(clickRange, Teacher.class).isEmpty()) {
                Teacher t = (Teacher)getObjectsInRange(clickRange, Teacher.class).get(0);
                if(t != null) {
                    if(i - lastPressedKeyTime >= pressCooldown) {
                        getWorld().addObject(o, 624, 288);
                        addToWorld();
                        board.open();
                        question.show();
                        healthBar.show();
                        lastPressedKeyTime = System.nanoTime();
                        isOpen = true;
                        ngajarStart();
                        this.getWorld().stopped();
                    }
                }
            }
        }
        if (isOpen && question.isFinished()) {
            if(!getObjectsInRange(clickRange, Teacher.class).isEmpty()) {
                Teacher t = (Teacher)getObjectsInRange(clickRange, Teacher.class).get(0);
                if(t != null) {
                    if(i - lastPressedKeyTime >= pressCooldown) {
                        lastPressedKeyTime = System.nanoTime();
                        board.close();
                        question.hide();
                        getWorld().removeObject(question);
                        healthBar.hide();
                        getWorld().removeObject(o);
                        isOpen = false;
                        ngajarStop();
                        t.setTutorialFinished(true);
                    }
                }
            }
        }
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    private void addToWorld() {
        World world = this.getWorld();
        world.addObject(board, 519, 263);
        world.addObject(question, 475, 243);
        world.addObject(healthBar, 165, 65);
        //world.addObject(ekspresi, 900, 400);
        
        added = true;
    }
    
    public void ngajarStart() {
        ngajarSound.playLoop();
    }
    
    public void ngajarStop() {
        ngajarSound.stop();
    }
}
