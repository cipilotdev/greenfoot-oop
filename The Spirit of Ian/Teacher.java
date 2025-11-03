import greenfoot.*;  
import java.util.HashMap;
import java.util.Map;

public class Teacher extends AnimatedSprites {
    private Map<String, GreenfootImage[]> animations = new HashMap<>();
    private String currentState = "standing";
    private String currentDirection = "south";
    private int frameIndex = 0;
    private int frameDelay = 5;
    private int frameTimer = 0;

    public Teacher() {
        loadAnimations();
        setImage(getCurrentFrame());
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
                        animations.put(key, new GreenfootImage[] { singleFrame });
                    } catch (Exception e) {
                        System.err.println("Missing standing image: " + key);
                    }
                } else {
                    // Other actions use frame_000.png, frame_001.png, etc.
                    GreenfootImage[] frames = loadFrames("Teacher/" + state + "/" + dir + "/");
                    if (frames != null && frames.length > 0) {
                        animations.put(key, frames);
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
    }

    private void handleInput() {
        boolean moving = false;

        if (Greenfoot.isKeyDown("up")) {
            currentDirection = "north";
            currentState = "walking";
            setLocation(getX(), getY() - 3);
            moving = true;
        } else if (Greenfoot.isKeyDown("down")) {
            currentDirection = "south";
            currentState = "walking";
            setLocation(getX(), getY() + 3);
            moving = true;
        } else if (Greenfoot.isKeyDown("left")) {
            currentDirection = "west";
            currentState = "walking";
            setLocation(getX() - 3, getY());
            moving = true;
        } else if (Greenfoot.isKeyDown("right")) {
            currentDirection = "east";
            currentState = "walking";
            setLocation(getX() + 3, getY());
            moving = true;
        } else if (Greenfoot.isKeyDown("space")) {
            currentState = "punch";
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
        return animations.get(currentState + "_" + currentDirection);
    }

    private GreenfootImage getCurrentFrame() {
        GreenfootImage[] frames = getCurrentAnimation();
        if (frames == null || frames.length == 0) return null;
        return frames[0];
    }
}
