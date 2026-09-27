import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Home here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Home extends Game
{
    private static final GreenfootImage homeMap = new GreenfootImage("worlds/home.png");
    private static final GreenfootSound homeSound = new GreenfootSound("Home.mp3");
    
    private Teacher teacher;
    
    private Dialog lelah = new Dialog(24, 27, true);
    
    //Home map: {centerX, centerY, width, height} of every solid object on the map
    private static final int[][] solidObjects = {
        {768, 34, 44, 56},
        {1088, 34, 44, 56},
        {496, 35, 42, 54},
        {1200, 71, 76, 109},
        {109, 40, 32, 43},
        {447, 32, 21, 20},
        {704, 32, 23, 20},
        {39, 40, 24, 24},
        {201, 69, 58, 82},
        {848, 63, 44, 61},
        {416, 49, 14, 12},
        {640, 79, 44, 61},
        {1008, 79, 44, 61},
        {367, 63, 23, 22},
        {544, 66, 26, 23},
        {432, 95, 44, 61},
        {912, 111, 44, 61},
        {255, 96, 21, 20},
        {735, 118, 24, 41},
        {1077, 135, 65, 70},
        {351, 145, 46, 58},
        {527, 145, 46, 58},
        {848, 159, 44, 61},
        {1120, 191, 44, 61},
        {1200, 207, 76, 93},
        {975, 182, 24, 41},
        {15, 176, 21, 20},
        {256, 195, 42, 54},
        {654, 179, 23, 14},
        {768, 207, 44, 61},
        {559, 194, 23, 18},
        {15, 208, 21, 20},
        {346, 227, 30, 14},
        {639, 256, 21, 20},
        {704, 258, 23, 18},
        {1072, 285, 44, 65},
        {154, 291, 30, 14},
        {79, 321, 46, 58},
        {960, 306, 23, 18},
        {1139, 334, 50, 63},
        {1008, 335, 44, 61},
        {191, 319, 19, 26},
        {768, 345, 44, 73},
        {879, 320, 21, 20},
        {496, 339, 42, 54},
        {143, 335, 19, 26},
        {911, 342, 24, 41},
        {383, 353, 46, 58},
        {560, 355, 42, 54},
        {1219, 366, 50, 64},
        {175, 367, 19, 26},
        {646, 371, 30, 14},
        {221, 392, 32, 43},
        {63, 401, 46, 58},
        {832, 415, 44, 61},
        {976, 415, 44, 61},
        {416, 427, 48, 62},
        {583, 409, 26, 26},
        {621, 424, 32, 43},
        {1103, 433, 46, 58},
        {1188, 479, 68, 125},
        {281, 441, 26, 26},
        {510, 446, 21, 28},
        {335, 465, 46, 58},
        {151, 456, 32, 37},
        {752, 479, 44, 61},
        {912, 479, 44, 61},
        {1039, 486, 24, 41},
        {95, 496, 47, 52},
        {447, 498, 46, 56},
        {199, 488, 24, 24},
        {580, 514, 66, 55},
        {159, 515, 23, 20},
        {528, 544, 47, 52},
        {681, 537, 26, 26},
        {359, 552, 24, 24},
        {46, 94, 49, 64},
        {111, 138, 134, 121},
        {117, 226, 23, 33},
        {33, 256, 51, 58},
        {259, 122, 16, 22}
    };
    
    /**
     * Constructor for objects of class Home.
     * 
     */
    public Home(Teacher teacher)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.teacher = teacher;
        
        setBackground(homeMap);
        prepare();
    }
    
    private void prepare()
    {
        homeSound.playLoop();
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        addObject(teacher, 140, 207);
        teacher.freeze();
        
        //Solid objects (trees, rocks, buildings...) so the teacher can't walk through them
        for (int[] obj : solidObjects) {
            addObject(new Collider(obj[2], obj[3], 0, 0), obj[0], obj[1]);
        }
        addObject(lelah, 624, 288);
    }
    
    @Override
    public void stopped() {
        homeSound.stop();
    }
}
