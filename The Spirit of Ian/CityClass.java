import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class cityClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CityClass extends World
{
    //----- World Background -----
    private static final GreenfootImage cityClassMap = new GreenfootImage("worlds/cityClass.png");
    private static final GreenfootSound prologueSound = new GreenfootSound("Tutorial.mp3");
    private static final GreenfootSound radioSound = new GreenfootSound("Radio.mp3");
    private static final GreenfootSound kepsekSound = new GreenfootSound("Kepsek.mp3");
    private static final GreenfootSound kagetSound = new GreenfootSound("Kaget.mp3");
    
    private Teacher teacher;
    
    /**
     * Constructor for objects of class cityClass.
     * 
     */
    public CityClass(Teacher teacher)
    {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        this.teacher = teacher;
        setBackground(cityClassMap);
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        int difficulty = teacher.getDifficulty();
        
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        Dialog d = new Dialog(1, 9);
        addObject(d, 624, 288);

        Collider collider = new Collider(1248, 70, 1, 1);
        addObject(collider, 624, 35);

        BoardCollision board1 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board1, 845, 80);
        
        BoardCollision board2 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board2, 335, 80);
        
        Collider collider4 = new Collider(60, 70, 0, 0);
        addObject(collider4, 600, 100);
        
        Student student = new Student();
        addObject(student, 720, 228);

        Student student2 = new Student();
        addObject(student2, 849, 228);
        
        addObject(teacher, 900, 167);
        
        prologueSound.playLoop();
    }
    
    public void prologueStop() {
        prologueSound.stop();
    }
    
    public void radioStart() {
        radioSound.playLoop();
    }
    
    public void radioStop() {
        radioSound.stop();
    }
    
    public void kepsekStart() {
        kepsekSound.playLoop();
    }
    
    public void kepsekStop() {
        kepsekSound.stop();
    }
    
    public void kagetStart() {
        kagetSound.playLoop();
    }
    
    public void kagetStop() {
        kagetSound.stop();
    }
    
    @Override
    public void stopped() {
        prologueStop();
        radioStop();
        kepsekStop();
        kagetStop();
    }
}
