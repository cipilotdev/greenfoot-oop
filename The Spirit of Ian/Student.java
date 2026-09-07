import greenfoot.*;

public class Student extends AnimatedSprites {
    //----- Layer images -----
    private static final GreenfootImage[] students = {
        new GreenfootImage("Student/student.png"),
        new GreenfootImage("Student/student1.png"),
        new GreenfootImage("Student/student2.png"),
        new GreenfootImage("Student/student3.png"),
        new GreenfootImage("Student/student4.png"),
        new GreenfootImage("Student/student5.png"),
        new GreenfootImage("Student/student6.png"),
        new GreenfootImage("Student/student7.png"),
        new GreenfootImage("Student/student8.png"),
        new GreenfootImage("Student/student9.png"),
        new GreenfootImage("Student/student10.png")
    };
    
    public Student() {
        //Create spriteSheet
        setLayer(0, students[Greenfoot.getRandomNumber(11)]);
        //Build sitting animation (primary animation)
        animations.put("sitting", Animation.createAnimation(getSpriteSheet(), 30, 4, 3, 64, 64));                
        
        //Set primary animation (default animation)
        sheet = true;
        primaryAnimation = animations.get("sitting");

        //Start: facing downward
        direction = 1;

        //For the starting image, grab the 0th frame from the current facing dirction
        setImage(primaryAnimation.getOneImage(direction, 0));

        //Spawn new Collider
        setCollider(28, 55, 0, 4);
    }

    public void act() {
    }
}
