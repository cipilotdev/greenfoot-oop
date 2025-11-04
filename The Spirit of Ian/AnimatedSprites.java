import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;

/**
 * Write a description of class AnimatedSprites here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AnimatedSprites extends Actor {
    //----- Protected variables -----
    protected HashMap<String, Animation> animations; //This HashMap stores the animations
    
    protected Animation primaryAnimation;            //The primary animation is generally movement - this is the base animation
    protected Animation currentAnimation;            //The animation currently playing
    
    protected int direction;                         //Direction: 0 = Right, 1 = Left, 2 = Up, 3 = Down
    
    protected Collider myCollider;                   //Reference to my collider
    
    //----- Protected variables (accessible by subclasses) -----
    protected double framesPerSecond;                  //Animation speed
    protected double secondsPerFrame;                  //Calculated fraction of second per frame
    protected double maxFrameLength = 0.10;            //Used to avoid "jumping" animations if lag
    
    protected int frame = 0;                           //Current frame counter
    protected double xx, yy;                           //Internal, double representation of coordinates
    protected int dirX = 0;                            //Variable used to control direction
    protected int dirY = 0;                            //Variable used to control direction
    protected int prevX, prevY;                        //Previous rounded X and Y values
    protected boolean idle = false;                    //Used to specify idle frame
    protected boolean stopAtEnd = false;               //Is this a TERMINAL animation?
    
    protected double moveSpeed;                        //How many pixels per SECOND
    
    protected static final int maxLayers = 3;          //How many layers can be rendered together to one spriteSheet
    
    protected GreenfootImage[] currentImages;          //Current set of images. This is one dimension of an Animation, and will be cycled through in the animation code.
    protected GreenfootImage[] spriteSheetLayers;      //Array that contains the layers of the spriteSheet
    protected GreenfootImage spriteSheet;              //The final spriteSheet of the character (with all layers that are currenty visible)
    
    protected boolean collisionEnabled = false;
    protected boolean sheet = false;
    
    //Keep animation going at consistent speed
    private long lastFrame;                          //Update of the last animation
    private long current;                            //Time between frames for movement
    private long elapsed;                            //How long a frame was
    
    /**
     * AnimatedCharacter Constructor: Creates a new HashMap for all animations and a new array for the layers of the spritesheet and sets the first lastFrame.
     */ 
    public AnimatedSprites() {
        animations = new HashMap<String, Animation>();

        spriteSheetLayers = new GreenfootImage[maxLayers];

        //Set the initial timestamp for animation timer
        lastFrame = System.nanoTime();
    }
    
    /**
     * Act - do whatever the AnimatedSprites wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        // Add your action code here.
        long lastAct = current;
        //Determine how much time has passed since the last act
        current = System.nanoTime();
        //Find elapsed time since last frame switch in milliseconds (ms) for animation
        elapsed = (long)((current - lastFrame) / 1000000.0); 
        //Find elapsed time since last act, for movement
        long deltaTime = (current - lastAct) / 1000000;

        //----- Animation -----
        if ((dirX == 0 && dirY == 0) && !stopAtEnd) {
            //If not moving, and not playing a terminal animation, switch to idle
            idle = true;
            lastFrame = current;

            //Reset animation timer 
            frame = 0;

            setImage(currentImages[frame]);
        } else {
            if (elapsed > secondsPerFrame * 1000 || idle) {
                //Check if ready to show next frame
                lastFrame = current;
                frame++;

                idle = false;
            }

            if (frame > currentImages.length - 1) //If this is the last frame
            {
                if (stopAtEnd) //If this is a terminal animation
                {
                    //If a terminal animation is finished, return to primary
                    currentAnimation = primaryAnimation;
                    stopAtEnd = false;
                    setCurrentImages(currentAnimation.getDirectionalImages()[direction]);

                    frame = 1;
                } 
                else //If this is no terminal animation, go back to frame 1
                { 
                    frame = 1; //0th frame is the idle frame only, so count 1..last, not 0..last
                }
            }

            //Set the correct image
            setImage(currentImages[frame]);
        }

        //----- Movement -----

        //Calculate delta time - how many seconds have passed since the last act (I.e. 30 fps, dT = 0.0333)
        double dT = (current-lastAct) / 1000000000.0;

        if(dT > maxFrameLength)
        {
            dT = maxFrameLength;
        }

        //Calculate exact new location
        xx += ((double)(dirX) * moveSpeed) * dT;
        yy += ((double)(dirY) * moveSpeed) * dT;

        //Update my location
        super.setLocation((int)Math.round(xx), (int)Math.round(yy));

        //Set the position of my collider to my new location
        if(collisionEnabled)
        {
            positionCollider();
        }
    }
    
    public void teacherAct() {
        if(collisionEnabled)
        {
            long lastAct = current;
            //Determine how much time has passed since the last act
            current = System.nanoTime();
            //Find elapsed time since last frame switch in milliseconds (ms) for animation
            elapsed = (long)((current - lastFrame) / 1000000.0); 
            //Find elapsed time since last act, for movement
            long deltaTime = (current - lastAct) / 1000000;
            //Calculate delta time - how many seconds have passed since the last act (I.e. 30 fps, dT = 0.0333)
            double dT = (current-lastAct) / 1000000000.0;

            if(dT > maxFrameLength)
            {
                dT = maxFrameLength;
            }
            //Calculate exact new location
            xx += ((double)(dirX) * moveSpeed) * dT;
            yy += ((double)(dirY) * moveSpeed) * dT;

            //Update my location
            super.setLocation((int)Math.round(xx), (int)Math.round(yy));
            positionCollider();
        }
    }
    
    /**
     * Method 'addedToWorld': Is called when an animated character is placed in the world.
     * It sets the internal double variables, the currentAnimation, the first character image (based on the starting direction) and calls the 'positionCollider' method, if collision is enabled.
     * 
     * @param 'World w': The world in which the hotbar object will be placed in
     */
    public void addedToWorld(World w)
    {
        //Set the internal double variables 
        xx = getX();
        yy = getY();
        prevX = getX();
        prevY = getY();
        
        if (sheet) {
            currentAnimation = primaryAnimation;
            
            setCurrentImages(currentAnimation.getDirectionalImages()[direction]);
        }
        
        if(collisionEnabled)
        {
            positionCollider();
        }
    }
    
    /**
     * Method 'positionCollider': Is called in the 'addedToWorld' method and in the 'act' method every tick.
     * It spawns a new Collider at the start or when the player changes the map.
     * The Collider will be teleportet to the location of the player every tick.
     */
    public void positionCollider() 
    {
        if(myCollider.getWorld() != getWorld())
        {
            getWorld().addObject(myCollider, 0, 0);
        } 

        myCollider.setLocation(getX() + myCollider.getXOffset(), getY() + myCollider.getYOffset());
    }
    
    /**
     * Method 'setCollider': Is called in every constructor of an animated character subclass that needs a collider.
     * It creates a new Collider and enables the collision.
     * 
     * @param 'width': The width in pixel of the new Collider
     * @param 'height': The height in pixel of the new Collider
     * @param 'xOffset': The offset in pixel of the new Collider in x direction 
     * @param 'yOffset': The offset in pixel of the new Collider in y direction
     */
    public void setCollider(int width, int height, int xOffset, int yOffset)
    {
        myCollider = new Collider(width, height, xOffset, yOffset);
        collisionEnabled = true;
    }
    
    /**
     * Method 'disableCollision': Is called in the 'checkRemove' method in every animated character subclass.
     * It removes the Collider of the character that is dead and disables the collision.
     */
    public void disableCollision() 
    {
        if(myCollider != null && myCollider.getWorld() != null)
        {
            getWorld().removeObject(myCollider);
        }

        collisionEnabled = false;
    }
    
    /**
     * Method 'setLocation': Is called by the 'checkCollision' method in player class.
     * Overrides the default setLocation method because the backing variables xx and yy need to be updated, 
     * otherwise they will immediately move the player back every time it tries to move.
     */
    @Override
    public void setLocation(int x, int y)
    {
        xx = (int)x;
        yy = (int)y;   
        //Once variable have been updated, call the normal method
        super.setLocation((int)Math.round(xx), (int)Math.round(yy));
    }
    
    /**
     * Method 'getSpriteSheet': Is called in every constructor of an animated character subclass that needs to set animations.
     * It returns the current spriteSheet.
     * 
     * @return: The current spriteSheet of the character (ALL LAYERS THAT ARE VISIBLE AT THE TIME) 
     */
    public GreenfootImage getSpriteSheet() 
    {
        return spriteSheet;
    }
    
    /**
     * Method 'setLayer': Is called in every animated character subclass. 
     * It sets the layer for the character image in the spriteSheetLayers array and updates the spriteSheet.
     * 
     * The 0th layer is on the bottom and will be drawn first. The highest layer is on top and will be drawn last. 
     * By default there are 12 layers, but you can add more by changing the maxLayers constant.
     * 
     * @param 'layer': The layer number you want to place this spritesheet at
     * @param 'image': The sprite sheet that you want to place at this layer
     */   
    public void setLayer(int layer, GreenfootImage image)
    {
        spriteSheetLayers[layer] = image;
        spriteSheet = Animation.updateSpriteSheet(spriteSheetLayers);
    }

    /**
     * Method 'refresh': Is called in an animated character subclass, if a layer was changed.
     * It refreshes the animation using the spritesheet built into this AnimatedCharacter. This should be called after making changes to the SpriteSheet, 
     * so that the spriteSheet will be changed in the animation. 
     * 
     * @param 'animation': The Animation object that will be refreshed. This will use the default spriteSheet, which may have just been changed using 'setLayer', or otherwise.
     */
    public void refresh(Animation animation)
    {
        animation.refresh(this.spriteSheet);
        setCurrentImages(currentAnimation.getDirectionalImages()[direction]);
    }

    /**
     * Method 'changeSpeed': Sets the movement speed (in pixels per second) and the animation rate (in frames per second)
     * 
     * @param 'moveSpeed': The new movement speed (in pixels per second) (0 if the character should not move anymore)
     * @param 'framesPerSecond': The animation rate (in frames per second)
     */
    protected void changeSpeed(int moveSpeed, int framesPerSecond)
    {
        //Only run code if something has changed
        if(this.moveSpeed != moveSpeed || this.framesPerSecond != framesPerSecond)
        {
            this.framesPerSecond = framesPerSecond;
            this.moveSpeed = moveSpeed;

            //Figure out how many seconds per frame
            secondsPerFrame = 1.0 / this.framesPerSecond;
            //Reset animation timer
            lastFrame = System.nanoTime();
        }
    }

    /**
     * Method 'moveInDirection': Is called in the 'move' method in all animated character subclasses, that need to move.
     * Makes this AnimatedCharacter move in a specified direction.
     * Subclasses can just set direction once and the animated character will keep moving until stopped or the direction changes.
     * This method does not actually perform movement. It only sets the direction variables. 
     * The 'super.act' call in the subclasses will run last in the subclass 'act' method and perform the actual movement.
     *
     * Intended to receive a 1 or -1 for for ONE of the parameters, and a zero (0) for the other. This method does NOT allow diagonal movement.
     * 
     * @param 'dirX': The direction for x movement. (-1, 0 or 1)
     * @param 'dirY': The direction for y movement. (-1, 0 or 1)
     */
    protected void moveInDirection(int dirX, int dirY)
    {        
        if(this.dirX != dirX || this.dirY != dirY) //If there has been a change in direction
        {
            if(dirX == 0 && dirY == 0)
            {
                idle = true; 
                lastFrame = System.nanoTime(); //Reset animation timer to start fresh
                frame = 0; //0 is the idle frame
                setImage(currentAnimation.getDirectionalImages()[direction][frame]);
            } 
            else 
            {
                idle = false; 

                //Set the facing direction if direction has changed
                if(dirX == 1)
                {
                    direction = 0;
                } 
                else if(dirX == -1)
                {
                    direction = 1;
                }
                else if(dirY == -1)
                {
                    direction = 2;
                }
                else if(dirY == 1)
                {
                    direction = 3;
                }                 
            }
            
            setCurrentImages(currentAnimation.getDirectionalImages()[direction]);

            //Set these variables so that they can be checked next frame for changes
            this.dirX = dirX;
            this.dirY = dirY;
        } 
    }
    
    protected void teacherMoveInDirection(int dirX, int dirY)
    {        
        if(this.dirX != dirX || this.dirY != dirY) //If there has been a change in direction
        {
            if(dirX == 0 && dirY == 0)
            {
                idle = true; 
                lastFrame = System.nanoTime(); //Reset animation timer to start fresh
                frame = 0; //0 is the idle frame
                setImage(currentAnimation.getDirectionalImages()[direction][frame]);
            } 
            else 
            {
                idle = false; 

                //Set the facing direction if direction has changed
                if(dirX == 1)
                {
                    direction = 0;
                } 
                else if(dirX == -1)
                {
                    direction = 1;
                }
                else if(dirY == -1)
                {
                    direction = 2;
                }
                else if(dirY == 1)
                {
                    direction = 3;
                }                 
            }
            
            //Set these variables so that they can be checked next frame for changes
            this.dirX = dirX;
            this.dirY = dirY;
        } 
    }

    /**
     * Method 'stopMoving': Is called in an animated character subclass, if the character wants to stop moving.
     * It sets the direction variables to 0 and resets the animation timer.
     */
    public void stopMoving()
    {
        dirX = 0;
        dirY = 0;

        if(!stopAtEnd)
        {
            lastFrame = System.nanoTime();
        }
    }

    /**
     * Method 'runTerminalAnimation': Is called in an animated character subclass, if the character should do an animation that will have an end.
     * It sets the animation variables and 
     * 
     * @param 'animationName': The name of the Animation (it must have been added to the animations HashMap already)
     * @param 'direction': What direction is this animation currently facing?
     */ 
    public void runTerminalAnimation(String animationName, int direction)
    {
        stopAtEnd = true;
        idle = false;
        frame = 0;

        currentAnimation = animations.get(animationName);
        this.direction = direction;

        if(currentAnimation.isDirectional())
        {
            setCurrentImages(currentAnimation.getDirectionalImages()[direction]);
        } 
        else 
        {          
            setCurrentImages(currentAnimation.getNonDirectionalImages());
        }
    }

    /**
     * Method 'setCurrentImages': Is called in the 'runTerminalAnimation' method, if a terminal animation should be played or in the act method,
     * if the terminal animation is finished and the current animation will set to the primary animation again.
     * It updates the currentImages array. This is one dimension of an Animation, and will be cycled through in the animation code.
     * 
     * @param 'images': The array of GreenfootImages to which the current animation should be set to
     */
    private void setCurrentImages(GreenfootImage[] images)
    {
        currentImages = new GreenfootImage[images.length];
        for(int i = 0; i < images.length; i++)
        {
            currentImages[i] = images[i];
        }
    }
}

/**
 * The Animation class stores the animations as a 1d or 2d array of GreenfootImages.
 *  
 * For Animations that are directional, this class will store the animation as a 2d array. 
 * For animations with one direction, this class will store the animation as 1d array.  
 */
class Animation 
{
    private GreenfootImage[][] directionalImages;  //Saves a directional animation as a 2d GreenfootImage array
    private GreenfootImage[] nonDirectionalImages; //Saves a non directional animation as a 1d GreenfootImage array

    private int startRow;                          //The row on which the desired sprites are located
    private int numRows;                           //The number of rows to import, which will correspond with the number of directions (1 or 4 directions)
    private int numFrames;                         //The number of frames in the animation
    private int width;                             //The width of each frame
    private int height;                            //The height of each frame

    private boolean directional;                   //True if an animation is directional (must be 4 directions), false if not
    private int directions;                        //How many directions does this animation have (1 or 4)
    
    /**
     * Animation Constructor for directional animations: Is called in the 'createAnimation' method. Sets the animation variables and saves the animation in the directionalImages array.
     * 
     * @param 'images': 2d array of GreenfootImage with 4 directions and at least one image per direction
     * @param 'startRow': The row on which the desired sprites are located
     * @param 'numRows': The number of rows to import, which will correspond with the number of directions (4 directions)
     * @param 'numFrames': The number of frames in the animation
     * @param 'width': The width of each frame
     * @param 'height': The height of each frame
     */ 
    public Animation(GreenfootImage[][] images, int startRow, int numRows, int numFrames, int width, int height)
    {
        this.startRow = startRow;
        this.numRows = numRows;
        this.width = width;
        this.height = height;
        this.directional = true;
        this.numFrames = numFrames;
        directionalImages = images;

        directions = images.length;
    }

    /**
     * Animation Constructor for non directional animations: Is called in the 'createAnimation' method. Sets the animation variables and saves the animation in the nonDirectionalImages array.
     * 
     * @param 'images': 1d array of GreenfootImage with 1 direction and at least one image
     * @param 'startRow': The row on which the desired sprites are located
     * @param 'numRows': The number of rows to import, which will correspond with the number of directions (1 direction)
     * @param 'numFrames': The number of frames in the animation
     * @param 'width': The width of each frame
     * @param 'height': The height of each frame
     */
    public Animation(GreenfootImage[] images, int startRow, int numRows, int numFrames, int width, int height)
    {
        this.startRow = startRow;
        this.numRows = numRows;
        this.width = width;
        this.height = height;
        this.directional = false;
        this.numFrames = numFrames;
        nonDirectionalImages = images;

        directions = images.length;
    }

    /**
     * Method 'isDirectional': Is called in the 'runTerminalAnimation' method in AnimatedCharacter class.
     * 
     * @return: True if the animation is directional (4 - directional), false if the animation is not directional (1 - directional)
     */    
    public boolean isDirectional()
    {
        return this.directional;
    }

    /**
     * Method 'getOneImage': Is called in the constructor of every animated character subclass to get the first starting image with the starting direction.
     * It returns one image of the animation.
     * 
     * @param 'direction': The direction the character is currently facing
     * @param 'frame': The wanted frame of the animation
     * 
     * @return: The image facing in the current direction at the current frame
     */ 
    public GreenfootImage getOneImage(int direction, int frame)
    {
        return directionalImages[direction][frame];
    }

    /**
     * Method 'getDirectionalImages': Is called in every method of the animated character class to get the 2d array of the directional animation.
     * 
     * @return: The directional animation as a 2d GreenfootImage array
     */ 
    public GreenfootImage[][] getDirectionalImages()
    {
        return directionalImages;
    }

    /**
     * Method 'getNonDirectionalImages': Is called in every method of the animated character class to get the 1d array of the non directional animation.
     * 
     * @return: The non directional animation as a 1d GreenfootImage array
     */
    public GreenfootImage[] getNonDirectionalImages()
    {
        return nonDirectionalImages;
    }

    /** 
     * Method 'createAnimation': Called in every constructor of an animated character subclass to create an animation from a spriteSheet.
     *  
     * @param 'spriteSheet': The Spritesheet to pull frames from
     * @param 'startRow': The row on which the desired sprites are located
     * @param 'numRows': The number of rows to import, which will correspond with the number of directions (1 or 4 directions)
     * @param 'numFrames': The number of frames in the animation
     * @param 'width': The width of each frame
     * @param 'height': The height of each frame
     *  
     * @return: The Animation object that is either 1 directional or 4 directional
     */
    protected static Animation createAnimation(GreenfootImage spriteSheet, int startRow, int numRows, int numFrames, int width, int height)
    {
        GreenfootImage[][] images = processImages(spriteSheet, startRow, numRows, numFrames, width, height);

        if(numRows == 1) //If this only has one dimension, create a 1 dimension Animation
        { 
            GreenfootImage[] img1d = new GreenfootImage[images[0].length];

            for(int i = 0; i < img1d.length; i++)
            {
                img1d[i] = images[0][i];
            }

            Animation animation = new Animation(img1d, startRow, numRows, numFrames, width, height);
            return animation;
        } 
        else //If this is directional
        { 
            Animation animation = new Animation(images, startRow, numRows, numFrames, width, height);
            return animation;
        }
    }

    /** 
     * Method 'processImages': Called in the 'createAnimation' method to cut the spriteSheet into images, that will be saved into a 2d GreenfootImage array.
     *  
     * @param 'spriteSheet': The Spritesheet to pull frames from
     * @param 'startRow': The row on which the desired sprites are located
     * @param 'numRows': The number of rows to import, which will correspond with the number of directions (1 or 4 directions)
     * @param 'numFrames': The number of frames in the animation
     * @param 'width': The width of each frame
     * @param 'height': The height of each frame
     *  
     * @return: The 2d GreenfootImage array
     */
    private static GreenfootImage[][] processImages(GreenfootImage spriteSheet, int startRow, int numRows, int numFrames, int width, int height)
    {
        GreenfootImage[][] images = new GreenfootImage[numRows][numFrames];

        for(int row = 0; row < numRows; row++)
        {
            int dir = -1;

            if(numRows == 1)
            { 
                dir = 0;
            } 
            else 
            { 
                switch(row) 
                { 
                    //Translate between Direction values and the order in which the frames are organized in spriteSheets
                    case 0: dir = 2;  break;
                    case 1: dir = 1;  break;
                    case 2: dir = 3;  break;
                    case 3: dir = 0;  break; 
                }
            }

            if(dir == -1) 
            {
                return null;
            }

            for(int frame = 0; frame < numFrames; frame++)
            {
                images[dir][frame] = new GreenfootImage(getSlice(spriteSheet, frame * width, (startRow + row - 1) * height, width, height));
            }
        }
        return images;
    }

    /**
     * Method 'getSlice': Is called by the 'processImages' method.
     * It grabs a part of a sprite sheet and returns it as a new GreenfootImage. (The spriteSheet must be larger than the resulting image.)
     * 
     * @param 'spriteSheet': The larger spriteSheet to pull images from
     * @param 'xPos': The x position (of the left) of the desired spot to draw from
     * @param 'yPos': The y position (of the top) of the desired spot to draw from
     * @param 'frameWidth': The horizontal tile size
     * @param 'frameHeight': The vertical tile size
     * 
     * @return: The resulting image
     */
    private static GreenfootImage getSlice(GreenfootImage spriteSheet, int xPos, int yPos, int frameWidth, int frameHeight)
    {
        if(frameWidth > spriteSheet.getWidth() || frameHeight > spriteSheet.getHeight()) //If the resulting image is larger than the spriteSheet
        {
            return null;
        }

        GreenfootImage small = new GreenfootImage(frameWidth, frameHeight);
        small.drawImage(spriteSheet, -xPos, -yPos);
        return small;
    }

    /**
     * Method 'updateSpriteSheet': Is called by the 'setLayer' method in AnimatedCharacter class, if a layer was changed and the spriteSheet needs to be refreshed.
     * It draws the layers ontop each others to create the new spriteSheet.
     * 
     * @param 'layers': The array that contains all layers
     * 
     * @return: The resulting spriteSheet
     */
    public static GreenfootImage updateSpriteSheet(GreenfootImage[] layers)
    {
        GreenfootImage spriteSheet = new GreenfootImage(layers[0].getWidth(), layers[0].getHeight());

        for(int i = 0; i < layers.length; i++)
        {
            if(layers[i] != null)
            {
                spriteSheet.drawImage(layers[i], 0, 0);
            }
        }

        return spriteSheet;
    }
    
    /**
     * Method 'refresh': Is called in the 'refresh' method in AnimatedCharacter class, if a layer was changed and the animation needs to be refreshed.
     * It processes a new array with the new spriteSheet that was updated by the 'updateSpriteSheet' method.
     * 
     * @param 'spriteSheet': The updated spriteSheet
     */
    public void refresh(GreenfootImage spriteSheet) 
    {
        if(directional)
        {
            GreenfootImage[][] temp = processImages(spriteSheet, startRow, numRows, numFrames, width, height);
            directionalImages = temp;            
        } 
        else 
        {
            GreenfootImage[] temp = processImages(spriteSheet, startRow, numRows, numFrames, width, height)[0];
            nonDirectionalImages = temp;            
        }
    }
}