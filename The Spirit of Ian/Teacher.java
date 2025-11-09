import greenfoot.*;  
import java.util.HashMap;
import java.util.Map;

public class Teacher extends AnimatedSprites {
    private Map<String, GreenfootImage[]> animations2 = new HashMap<>();
    private String currentState = "standing";
    private String currentDirection = "south";
    private int frameIndex = 0;
    private int frameDelay = 5;
    private int frameTimer = 0;
    
    private int moveX;              //The direction for x movement. (-1, 0 or 1)
    private int moveY;              //The direction for y movement. (-1, 0 or 1)
    private int xOffset, yOffset;   //Direction for attacking
    
    private int oldX, oldY;
    private static final GreenfootImage teacher = new GreenfootImage("Teacher/standing/south.png");
    
    private static final int animationSpeed = 20;
    private static final int walkSpeed = 60;
    
    private int difficulty;
    
    public Teacher() {
        changeSpeed(walkSpeed, animationSpeed);
        setLayer(0, teacher);
        animations.put("walk", Animation.createAnimation(getSpriteSheet(), 9, 4, 9, 64, 64));
        primaryAnimation = animations.get("walk");
        
        loadAnimations();
        setImage(getCurrentFrame());
        
        setCollider(20, 28, 0, 7);
    }

    private void loadAnimations() {
        // Define the action names and directions that match your folder names
        String[] states = {"idle", "walking", "running", "punch", "standing"};
        String[] directions = {"north", "south", "east", "west"};

        for (String state : states) {
            for (String dir : directions) {
                String key = state + "_" + dir;
                
                // Standing uses single image (not frames)
                if (state.equals("standing")) {
                    try {
                        GreenfootImage singleFrame = new GreenfootImage("Teacher/" + state + "/" + dir + ".png");
                        animations2.put(key, new GreenfootImage[] { singleFrame });
                    } catch (Exception e) {
                        System.err.println("Missing standing image: " + key);
                    }
                } else {
                    // Other actions use frame_000.png, frame_001.png, etc.
                    GreenfootImage[] frames = loadFrames("Teacher/" + state + "/" + dir + "/");
                    if (frames != null && frames.length > 0) {
                        animations2.put(key, frames);
                    } else {
                        System.err.println("Missing animation folder: " + key);
                    }
                }
            }
        }
    }

    private GreenfootImage[] loadFrames(String path) {
        // try up to 10 frames just to be safe
        int maxFrames = 4;
        GreenfootImage[] temp = new GreenfootImage[maxFrames];
        int count = 0;

        for (int i = 0; i < maxFrames; i++) {
            String fileName = String.format("%sframe_%03d.png", path, i);
            try {
                temp[count++] = new GreenfootImage(fileName);
            } catch (Exception e) {
                break; // stop if frame not found
            }
        }

        GreenfootImage[] result = new GreenfootImage[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }
    
    
    public void act() {        
        handleInput();

        animate();

        checkCollision();
        
        storePosition();
        
        checkChangeMap();
        
        super.teacherAct();
    }

    private void handleInput() {
        boolean moving = false;
        moveX = 0; 
        moveY = 0;
        if(Greenfoot.isKeyDown("w")) {
            currentDirection = "north";
            currentState = "walking";
            moving = true;
            moveY = -1;
        }
        if(Greenfoot.isKeyDown("s")) {
            currentDirection = "south";
            currentState = "walking";
            moveY = 1;
            moving = true;
        }
        if(Greenfoot.isKeyDown("d")) {
            currentDirection = "east";
            currentState = "walking";
            moveX = 1;
            moving = true;
        }
        if(Greenfoot.isKeyDown("a")) {
            currentDirection = "west";
            currentState = "walking";
            moveX = -1;
            moving = true;
        }
        
        if (moveX != 0 || moveY != 0) {
            //Set directions for attacking. -1, 0 or 1 for each axis to represent which direction the player is facing
            xOffset = moveX;
            yOffset = moveY;
            teacherMoveInDirection(moveX, moveY);
        } else {
            stopMoving();
        }
        
        if (!moving && !Greenfoot.isKeyDown("space")) {
            currentState = "standing";
        }
    }

    private void animate() {
        frameTimer++;
        if (frameTimer >= frameDelay) {
            frameTimer = 0;
            frameIndex++;
            GreenfootImage[] frames = getCurrentAnimation();
            if (frames != null && frames.length > 0) {
                frameIndex %= frames.length;
                setImage(frames[frameIndex]);
            }
        }
    }

    private GreenfootImage[] getCurrentAnimation() {
        return animations2.get(currentState + "_" + currentDirection);
    }

    private GreenfootImage getCurrentFrame() {
        GreenfootImage[] frames = getCurrentAnimation();
        if (frames == null || frames.length == 0) return null;
        return frames[0];
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
    
    /**
     * Method 'checkChangeMap': Is called every tick by the 'act' method.
     * If the player walked to a specific place, change the map, if the player killed all enemys in the current map.
     * If the player did not kill all enemys a tutorial message will appear.
     */
    public void checkChangeMap() {
        if (this.getWorld().getClass() == CityClass.class) {
            difficulty = ((CityClass)getWorld()).getDifficulty();
        } else {
            difficulty = ((Game)getWorld()).getDifficulty();
        }

        if (this.getWorld().getClass() == CityClass.class) {
            if (getX() > 1045 && getY() < 100 && getX() < 1110) {
                Greenfoot.setWorld(new Home(difficulty));
            }
        }
        if (this.getWorld().getClass() == Home.class) {
            if (getX() > 1240 && getY() > 270 && getY() < 370) {
                Greenfoot.setWorld(new MazePath(difficulty));
            }
        }
        if (this.getWorld().getClass() == MazePath.class) {
            if (getX() > 1232) {
                Greenfoot.setWorld(new School(difficulty));
            }
        }
        if (this.getWorld().getClass() == School.class) {
            if (getX() > 929 && getY() > 290 && getX() < 950 && getY() < 300) {
                Greenfoot.setWorld(new VillageClass(difficulty));
            }
        }
    }
}
