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
    
    /**
     * Constructor for objects of class cityClass.
     * 
     */
    public CityClass()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576, 1);
        setBackground(cityClassMap);
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        /*DialogText text = new DialogText();
        DialogBox window = new DialogBox(text);
        addObject(window, 625, 280);
        addObject(text, 625, 450);
        addObject(new Dialog(window, true), 0, 0);*/

        Teacher teacher = new Teacher();
        addObject(teacher, 900, 167);

        Collider collider = new Collider(10, 10, 10, 10);
        addObject(collider, 803, 168);

        Student student = new Student();
        addObject(student, 720, 228);

        Student student2 = new Student();
        addObject(student2, 849, 228);
    }
}
