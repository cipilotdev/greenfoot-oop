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
    
    //{centerX, centerY, width, height} of every student desk (chairs can be walked over)
    private static final int[][] furniture = {
        {158, 209, 54, 48},
        {158, 305, 54, 48},
        {158, 401, 54, 48},
        {270, 209, 54, 48},
        {270, 305, 54, 48},
        {270, 401, 54, 48},
        {382, 209, 54, 48},
        {382, 305, 54, 48},
        {382, 401, 54, 48},
        {494, 209, 54, 48},
        {494, 305, 54, 48},
        {494, 401, 54, 48},
        {606, 209, 54, 48},
        {606, 305, 54, 48},
        {606, 401, 54, 48},
        {718, 209, 54, 48},
        {718, 305, 54, 48},
        {718, 401, 54, 48},
        {846, 209, 54, 48},
        {846, 305, 54, 48},
        {846, 401, 54, 48},
        {958, 209, 54, 48},
        {958, 305, 54, 48},
        {958, 401, 54, 48},
        {1070, 209, 54, 48},
        {1070, 305, 54, 48},
        {1070, 401, 54, 48}
    };
    
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
        
        //Walls around the floor (the door on the top right stays open: the teacher leaves through it)
        addObject(new Collider(1248, 88, 0, 0), 624, 44);
        addObject(new Collider(95, 576, 0, 0), 47, 288);
        addObject(new Collider(98, 576, 0, 0), 1199, 288);
        addObject(new Collider(1248, 94, 0, 0), 624, 529);
        
        //Teacher desk and plant
        addObject(new Collider(58, 64, 0, 0), 601, 125);
        addObject(new Collider(26, 38, 0, 0), 111, 113);
        
        BoardCollision board1 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board1, 845, 80);
        
        BoardCollision board2 = new BoardCollision(130, 60, 80, difficulty);
        addObject(board2, 335, 80);
        
        //Student desks
        for (int[] obj : furniture) {
            addObject(new Collider(obj[2], obj[3], 0, 0), obj[0], obj[1]);
        }
        
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
