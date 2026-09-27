import greenfoot.*;

/**
 * A student. By default the student sits in class.
 * A running student (see the second constructor) stands in front of the school, notices the teacher
 * ("!" bubble), then runs to the school door and disappears inside.
 */
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
    
    //----- Running student states -----
    private static final int SITTING = 0;
    private static final int WAITING = 1;
    private static final int NOTICE = 2;
    private static final int RUN = 3;
    private static final int FADE = 4;
    
    private static final int noticeDuration = 45;
    private static final int arriveRange = 3;
    
    private int state = SITTING;
    private int timer = 0;
    private int noticeDelay;
    private int transparency = 255;
    
    private int[][] waypoints;
    private int waypointIndex = 0;
    
    private Overlay bubble;
    private int bubbleY;
    
    /**
     * A sitting student.
     */
    public Student() {
        //Create spriteSheet
        setLayer(0, students[Greenfoot.getRandomNumber(students.length)]);
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
    
    /**
     * A running student that waits, notices the teacher and runs into the school.
     * 
     * @param noticeDelay ticks to wait before noticing the teacher
     * @param doorX x position of the school door
     * @param doorY y position of the school door
     * @param laneY y position of the lane below the building used to reach the door
     */
    public Student(int noticeDelay, int doorX, int doorY, int laneY) {
        setLayer(0, students[Greenfoot.getRandomNumber(students.length)]);
        //Walking animation rows of the sprite sheet (up, left, down, right)
        animations.put("walk", Animation.createAnimation(getSpriteSheet(), 9, 4, 9, 64, 64));
        
        sheet = true;
        primaryAnimation = animations.get("walk");
        
        //Start facing downward
        direction = 3;
        setImage(primaryAnimation.getOneImage(direction, 0));
        
        //Run speed (pixels per second) and animation speed (frames per second)
        changeSpeed(170, 14);
        
        this.noticeDelay = noticeDelay;
        waypoints = new int[][] { {-1, laneY}, {doorX, laneY}, {doorX, doorY} };
        state = WAITING;
    }
    
    @Override
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        if (waypoints != null) {
            waypoints[0][0] = getX();
        }
    }
    
    public void act() {
        switch (state) {
            case SITTING:
                return;
            case WAITING:
                timer++;
                if (timer >= noticeDelay) {
                    noticeTeacher();
                }
                break;
            case NOTICE:
                timer++;
                bubble.setLocation(bubble.getX(), bubbleY - (timer / 6) % 2 * 3);
                if (timer >= noticeDuration) {
                    getWorld().removeObject(bubble);
                    state = RUN;
                }
                break;
            case RUN:
                run();
                break;
            case FADE:
                transparency -= 15;
                if (transparency <= 0) {
                    getWorld().removeObject(this);
                    return;
                }
                getImage().setTransparency(transparency);
                return;
        }
        
        super.act();
    }
    
    private void noticeTeacher() {
        //Turn left, toward the teacher coming from the left side of the map
        direction = 1;
        refresh(primaryAnimation);
        setImage(currentImages[0]);
        
        GreenfootImage mark = new GreenfootImage(" ! ", 30, Color.RED, Color.WHITE);
        bubble = new Overlay(mark);
        bubbleY = getY() - 45;
        getWorld().addObject(bubble, getX(), bubbleY);
        
        timer = 0;
        state = NOTICE;
    }
    
    private void run() {
        if (waypointIndex >= waypoints.length) {
            moveInDirection(0, 0);
            state = FADE;
            return;
        }
        
        int targetX = waypoints[waypointIndex][0];
        int targetY = waypoints[waypointIndex][1];
        int dx = targetX - getX();
        int dy = targetY - getY();
        
        if (Math.abs(dx) > arriveRange) {
            moveInDirection(Integer.signum(dx), 0);
        } else if (Math.abs(dy) > arriveRange) {
            if (dx != 0) {
                setLocation(targetX, getY());
            }
            moveInDirection(0, Integer.signum(dy));
        } else {
            setLocation(targetX, targetY);
            waypointIndex++;
        }
    }
}
