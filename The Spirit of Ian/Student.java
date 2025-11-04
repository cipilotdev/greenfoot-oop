import greenfoot.*;

public class Student extends AnimatedSprites {
    //----- Animation -----  
    private static final int animationSpeed = 20;              //Number of animation frames per second
    
    //----- Movement -----
    private static final int walkSpeed = 60;                   //Move 60 pixel per second
    private int moveX;                                         //The direction for x movement. (-1, 0 or 1)
    private int moveY;                                         //The direction for y movement. (-1, 0 or 1)
    private int xOffset, yOffset;                              //Direction for attacking
    
    //----- Collision -----
    private int oldX;                                          //Stores the x position from the last tick. Used for collision
    private int oldY;                                          //Stores the y position from the last tick. Used for collision
    
    //----- Layer images -----
    private static final GreenfootImage student = new GreenfootImage("Student/student.png");

    public Student() {
        //Set the speed
        changeSpeed(walkSpeed, animationSpeed);

        //Create spriteSheet
        setLayer(0, student);
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
        //move();
        
        checkCollision();
        
        storePosition();
        
        //Call superclass act() to perform animations and movement
        super.act();
    }
    
    /**
     * Method 'move': Is called every tick by the 'act' method.
     * It sets the move variables if 'w', 's', 'd' or 'a' is pressed.
     * If an item was used, it checks if the use cooldown of that item has expired, because the character is not able to move while using an item / attacking.
     */    
    public void move()
    {
        //Each tick, movement is reset
        moveX = 0; 
        moveY = 0;
        if(Greenfoot.isKeyDown("w")) {
            moveY = -1;
        } else if(Greenfoot.isKeyDown("s")) {
            moveY = 1;
        } else if(Greenfoot.isKeyDown("d")) {
            moveX = 1;
        } else if(Greenfoot.isKeyDown("a")) {
            moveX = -1;
        }

        if (moveX != 0 || moveY != 0) {
            //Set directions for attacking. -1, 0 or 1 for each axis to represent which direction the player is facing
            xOffset = moveX;
            yOffset = moveY;
            moveInDirection(moveX, moveY);
        } else {
            stopMoving();
        }
    }

    /**
     * Method 'checkCollision': Is called every tick by the 'act' method.
     * If the collder of the player intersects another collider or an object, teleports the player back to his position of the last tick.
     */
    public void checkCollision()
    {
        if (myCollider.checkCollision()) {
            setLocation(oldX, oldY);
        }
    }
    
    /**
     * Method 'storePosition': Is called every tick by the 'act' method.
     * It stores the current position of the player, so that information can be used next tick in the 'checkCollision' method, if the player collides with something.
     */
    public void storePosition() {
        oldX = getX();
        oldY = getY();
    }
}
