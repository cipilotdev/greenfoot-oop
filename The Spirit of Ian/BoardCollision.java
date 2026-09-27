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
    private Question question;
    private Health healthBar;
    private Overlay ekspresi;
    private Time timer;
    
    private int questionCount = 0;
    private int score = 0;
    
    private int clickRange;
    private boolean isOpen = false;
    private boolean added = false;
    private boolean suspended = false;     // Board temporarily hidden for a class event
    
    private static final double pressCooldown = 250000000.0;   //Cooldown (0,25sec) between pressing a key
    private double lastPressedKeyTime;
    
    private int difficulty;
    
    private GreenfootSound ngajarSound = new GreenfootSound("Ngajar.mp3");
    
    private String ekspresiBasePath = "ui/ekspression/";
    
    private static final GreenfootImage arrow = new GreenfootImage("ui/button/continue.png");
    private Overlay continueBtn = new Overlay(arrow);
    private boolean btnSpawned = false;
    private int btnTransparency = 0;
    
    public BoardCollision(int xSize, int ySize, int clickRange, int difficulty) {
        super(xSize, ySize, 0, 0);
        this.clickRange = clickRange;
        this.difficulty = difficulty;
        
        board = new Board();
        if (difficulty == 0) {
            difficulty = 1;
        }
        question = new Question(difficulty);
        healthBar = new Health();
        timer = new Time(difficulty);
        ekspresi = new Overlay(new GreenfootImage(ekspresiBasePath + 5 + ".png"));
    }
    
    public void act()
    {
        if (getWorld() != null && !btnSpawned) {
            getWorld().addObject(continueBtn, getX(), getY() -50);
            btnSpawned = true;
        }
        
        if (btnSpawned) {
            animateBtn();
        }
        
        double i = System.nanoTime();
        if (Greenfoot.isKeyDown("f") && !isOpen && !suspended) {
            if(!getObjectsInRange(clickRange, Teacher.class).isEmpty()) {
                Teacher t = (Teacher)getObjectsInRange(clickRange, Teacher.class).get(0);
                if(t != null) {
                    if(i - lastPressedKeyTime >= pressCooldown) {
                        lastPressedKeyTime = System.nanoTime();
                        openBoard();
                    }
                }
            }
        }
        if (isOpen && question.isFinished()) {
            // The teacher may have walked away from the board during a class event,
            // so fall back to the teacher anywhere in the world.
            Teacher t = findTeacher();
            if (t != null) {
                if(i - lastPressedKeyTime >= pressCooldown) {
                    lastPressedKeyTime = System.nanoTime();
                        
                    closeBoard();
                    t.setTutorialFinished(true);
                        
                    if (timer.getDay() == 1 && t.getIsKelasDone()) {
                        t.setIsD1Complete(true);
                        question.setFinished(false);
                            
                        if (t.getDifficulty() == 0) {
                            question.setDifficulty(2);
                        }
                    } else if (timer.getDay() == 2) {
                        t.setIsD2Complete(true);
                        question.setFinished(false);
                            
                        if (t.getDifficulty() == 0) {
                            question.setDifficulty(2);
                        }
                    } else if (timer.getDay() == 3) {
                        t.setIsD3Complete(true);
                        question.setFinished(false);
                            
                        if (t.getDifficulty() == 0) {
                            question.setDifficulty(3);
                        }
                    } else if (timer.getDay() == 4) {
                        t.setIsExamComplete(true);
                        t.setScore(question.getScore());
                    }
                }
            }
        }
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    private Teacher findTeacher() {
        if (!getObjectsInRange(clickRange, Teacher.class).isEmpty()) {
            return (Teacher)getObjectsInRange(clickRange, Teacher.class).get(0);
        }
        java.util.List<Teacher> teachers = getWorld().getObjects(Teacher.class);
        return teachers.isEmpty() ? null : teachers.get(0);
    }
    
    private void openBoard() {
        if (!added) {
            addToWorld();
        }
        
        o.getImage().setTransparency(30);
        board.open();
        question.show();
        healthBar.show();
        timer.getImage().setTransparency(255);
        timer.start();
        ekspresi.getImage().setTransparency(255);
        
        isOpen = true;
        ngajarStart();
        this.getWorld().stopped();
    }
    
    private void closeBoard() {
        board.close();
        question.hide();
        healthBar.hide();
        
        o.getImage().setTransparency(0);
        timer.getImage().setTransparency(0);
        timer.stop();
        ekspresi.getImage().setTransparency(0);
        
        isOpen = false;
        ngajarStop();
    }
    
    /**
     * Temporarily hide the board (e.g. for a class event) so the teacher can walk.
     * The health bar stays visible. The current question is kept and shown again on resume().
     */
    public void suspend() {
        if (!isOpen || suspended) {
            return;
        }
        board.close();
        question.hide();
        question.clearQuestion();
        
        o.getImage().setTransparency(0);
        timer.stop();
        timer.getImage().setTransparency(0);
        ekspresi.getImage().setTransparency(0);
        
        suspended = true;
        isOpen = false;
    }
    
    /**
     * Reopen the board after suspend(): redisplay the current question and restart the timer.
     */
    public void resume() {
        if (!suspended) {
            return;
        }
        suspended = false;
        
        o.getImage().setTransparency(30);
        board.open();
        question.show();
        healthBar.show();
        timer.getImage().setTransparency(255);
        timer.start();
        ekspresi.getImage().setTransparency(255);
        
        isOpen = true;
    }
    
    public boolean isSuspended() {
        return suspended;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    private void addToWorld() {
        World world = this.getWorld();
        world.addObject(board, 519, 263);
        world.addObject(question, 475, 243);
        world.addObject(healthBar, 165, 65);
        world.addObject(timer, 1150, 55);
        world.addObject(o, 624, 288);
        world.addObject(ekspresi, 1100, 270);
        
        added = true;
    }
    
    public void ngajarStart() {
        ngajarSound.playLoop();
    }
    
    public void ngajarStop() {
        ngajarSound.stop();
    }
    
    public Health getHealthBar() {
        return healthBar;
    }
    
    public Time getTimer() {
        return timer;
    }
    
    public void setEkspresi(int num) {
        ekspresi.setImage(ekspresiBasePath + num + ".png");
        if (!isOpen) {
            // Keep the expression hidden while the board is closed/suspended (e.g. HP lost during a class event)
            ekspresi.getImage().setTransparency(0);
        }
    }
    
    private void animateBtn() {
        if (btnTransparency <= 255) {
            btnTransparency += 5;
            if (btnTransparency > 255) {
                btnTransparency = 0;
            }
        } else {
            btnTransparency -= 5;
            if (btnTransparency < 0) {
                btnTransparency = 255;
            }
        }
        continueBtn.getImage().setTransparency(btnTransparency);
    }
}
