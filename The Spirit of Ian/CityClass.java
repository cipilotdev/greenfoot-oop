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
    private Dialog d = new Dialog(1, 9);
    
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
        
        Collider collider = new Collider(1248, 70, 1, 1);
        addObject(collider, 624, 35);
        
        BoardCollision board1 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board1, 845, 80);
        
        BoardCollision board2 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board2, 335, 80);
        
        Collider collider4 = new Collider(60, 70, 0, 0);
        addObject(collider4, 600, 100);
        
        Collider collider5 = new Collider(1248, 400, 0, 0);
        addObject(collider5, 624, 386);
        
        Student student1 = new Student();
        addObject(student1, 160, 228);
        
        Student student3 = new Student();
        addObject(student3, 383, 228);
        
        Student student5 = new Student();
        addObject(student5, 608, 228);
        
        Student student6 = new Student();
        addObject(student6, 720, 228);
        
        Student student7 = new Student();
        addObject(student7, 850, 228);
        
        Student student8 = new Student();
        addObject(student8, 960, 228);
        
        Student student10 = new Student();
        addObject(student10, 160, 324);
        
        Student student11 = new Student();
        addObject(student11, 273, 324);

        Student student13 = new Student();
        addObject(student13, 495, 324);

        Student student14 = new Student();
        addObject(student14, 608, 324);
        
        Student student15 = new Student();
        addObject(student15, 720, 324);
        
        Student student18 = new Student();
        addObject(student18, 1073, 324);

        Student student19 = new Student();
        addObject(student19, 160, 421);
        
        Student student20 = new Student();
        addObject(student20, 273, 421);
        
        Student student22 = new Student();
        addObject(student22, 495, 421);

        Student student23 = new Student();
        addObject(student23, 608, 421);
        
        Student student24 = new Student();
        addObject(student24, 720, 421);
        
        Student student26 = new Student();
        addObject(student26, 960, 421);
        
        Student student27 = new Student();
        addObject(student27, 1073, 421);
        
        addObject(d, 624, 288);
        
        addObject(teacher, 900, 150);
        
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
    
    public boolean isDialogOpen() {
        return d.isOpen();
    }
}
