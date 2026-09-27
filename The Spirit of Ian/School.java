import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class School here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class School extends Game
{
    private static final GreenfootImage schoolMap = new GreenfootImage("worlds/school.png");
    private static final GreenfootSound schoolSound = new GreenfootSound("School.mp3");
    
    private Dialog capek = new Dialog(30, 31, true);
    
    private Teacher teacher;
    
    //Students in front of the school run inside when the teacher arrives
    private static final int[][] studentSpots = {
        {600, 240}, {680, 320}, {745, 215}, {805, 300}, {560, 330}
    };
    private static final int doorX = 935;
    private static final int doorY = 290;
    private static final int laneY = 318;
    private boolean cutscenePlaying = true;
    
    //School map: {centerX, centerY, width, height} of every solid object on the map
    private static final int[][] solidObjects = {
        {1183, 42, 84, 39},
        {896, 51, 42, 54},
        {544, 34, 23, 18},
        {192, 63, 44, 61},
        {383, 64, 47, 52},
        {696, 77, 60, 65},
        {592, 83, 42, 54},
        {471, 88, 32, 37},
        {1122, 98, 53, 56},
        {1206, 105, 70, 71},
        {288, 99, 42, 54},
        {80, 111, 44, 61},
        {397, 136, 32, 43},
        {752, 130, 26, 23},
        {1010, 136, 29, 33},
        {159, 130, 23, 18},
        {624, 160, 47, 52},
        {487, 152, 24, 24},
        {841, 153, 30, 24},
        {207, 166, 24, 41},
        {1179, 226, 91, 152},
        {448, 195, 42, 54},
        {415, 321, 46, 58},
        {160, 335, 44, 61},
        {1130, 353, 47, 48},
        {752, 371, 42, 54},
        {559, 386, 46, 56},
        {704, 370, 26, 23},
        {809, 377, 30, 24},
        {1200, 392, 35, 34},
        {304, 403, 42, 54},
        {633, 393, 30, 24},
        {223, 406, 24, 41},
        {1120, 419, 42, 54},
        {480, 418, 26, 23},
        {80, 447, 44, 61},
        {841, 441, 30, 24},
        {669, 456, 32, 43},
        {895, 466, 46, 56},
        {1191, 474, 100, 72},
        {464, 479, 44, 61},
        {208, 495, 44, 61},
        {367, 497, 46, 58},
        {940, 230, 150, 120},
        {868, 225, 25, 41},
        {962, 123, 14, 22},
        {1058, 139, 14, 22},
        {1074, 171, 14, 22}
    };
    
    /**
     * Constructor for objects of class School.
     * 
     */
    public School(Teacher teacher)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.teacher = teacher;
        teacher.freeze();
        setBackground(schoolMap);
        prepare();
    }
    
    private void prepare()
    {
        schoolSound.playLoop();
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        addObject(teacher, 20, 278);
        
        for (int i = 0; i < studentSpots.length; i++) {
            Student student = new Student(50 + i * 10, doorX, doorY, laneY);
            addObject(student, studentSpots[i][0], studentSpots[i][1]);
        }
        
        //Solid objects (trees, rocks, buildings...) so the teacher can't walk through them
        for (int[] obj : solidObjects) {
            addObject(new Collider(obj[2], obj[3], 0, 0), obj[0], obj[1]);
        }
    }
    
    public void act() {
        if (cutscenePlaying && getObjects(Student.class).isEmpty()) {
            cutscenePlaying = false;
            addObject(capek, 624, 288);
        }
    }
    
    @Override
    public boolean isCutscenePlaying() {
        return cutscenePlaying;
    }
    
    @Override
    public void stopped() {
        schoolSound.stop();
    }
}
