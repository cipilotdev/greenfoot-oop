import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class VillageClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VillageClass extends World {
    private static final GreenfootImage villageClassMap = new GreenfootImage("worlds/villageClass.png");
    private static final GreenfootSound kelasSound = new GreenfootSound("KelasD1.mp3");
    private static final GreenfootSound d2Sound = new GreenfootSound("KelasD2.mp3");
    private static final GreenfootSound d3Sound = new GreenfootSound("KelasD3.mp3");
    private static final GreenfootSound preUjianSound = new GreenfootSound("PreUjian.mp3");
    private static final GreenfootSound finalExamSound = new GreenfootSound("Ujian.mp3");
    private static final GreenfootSound finalSound = new GreenfootSound("Final.mp3");
    
    //Center x of each column of student desks
    private static final int[] deskColumnsX = {209, 273, 337, 401, 465, 529, 593, 657, 721};
    
    private Teacher teacher;
    /**
     * Constructor for objects of class VillageClass.
     * 
     */
    public VillageClass(Teacher teacher)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        this.teacher = teacher;
        teacher.stopMoving();
        setBackground(villageClassMap);
        prepare();
    }
    
    private void prepare() {
        kelasSound.play();
        
        int difficulty = teacher.getDifficulty();
        
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        Student student = new Student();
        addObject(student, 210, 300);
        
        Student student2 = new Student();
        addObject(student2, 275, 368);
        
        Student student3 = new Student();
        addObject(student3, 335, 300);
        
        Student student4 = new Student();
        addObject(student4, 465, 300);
        
        Student student5 = new Student();
        addObject(student5, 465, 368);
        
        Student student6 = new Student();
        addObject(student6, 595, 368);
        
        Student student7 = new Student();
        addObject(student7, 720, 300);
        
        //Walls around the floor
        addObject(new Collider(1248, 198, 0, 0), 624, 99);
        addObject(new Collider(32, 576, 0, 0), 16, 288);
        addObject(new Collider(32, 576, 0, 0), 1232, 288);
        addObject(new Collider(1248, 32, 0, 0), 624, 560);
        
        //Teacher desk (the chairs can be walked over)
        addObject(new Collider(71, 32, 0, 0), 664, 240);
        
        //Student desks: each column has two desks, each with a chair below it
        for (int x : deskColumnsX) {
            addObject(new Collider(26, 26, 0, 0), x, 295);
            addObject(new Collider(26, 26, 0, 0), x, 358);
        }
        
        addObject(teacher, 980, 210);
        
        BoardCollision board1 = new BoardCollision(240, 60, 100, difficulty);
        addObject(board1, 488, 170);
        
        ClassEvents classEvents = new ClassEvents();
        addObject(classEvents, 0, 0);
    }
    
    public void kelasStop() {
        kelasSound.stop();
    }

    public void kelasD2Play() {
        d2Sound.play();
    }

    public void kelasD2Stop() {
        d2Sound.stop();
    }

    public void kelasD3Play() {
        d3Sound.play();
    }

    public void kelasD3Stop() {
        d3Sound.stop();
    }

    public void preUjianPlay() {
        preUjianSound.play();
    }

    public void preUjianStop() {
        preUjianSound.stop();
    }

    public void finalExamPlay() {
        finalExamSound.play();
    }

    public void finalExamStop() {
        finalExamSound.stop();
    }

    public void finalPlay() {
        finalSound.play();
    }

    public void finalStop() {
        finalSound.stop();
    }
    
    @Override
    public void stopped() {
        kelasStop();
        kelasD2Stop();
        kelasD3Stop();
        preUjianStop();
        finalExamStop();
        finalStop();
    }
}
