import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Objects;

/**
 * The Collider class manages collision between characters and objects. Red debug image can be shown for debugging. 
 * Otherwise, colliders have a transparent image (necessary for Greenfoot intersecting detection).
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Collider extends Actor
{
    //----- Collider variables -----
    private int xOffset;
    private int yOffset;
    private int xSize;
    private int ySize; 

    //----- Object image -----
    private GreenfootImage transparent;
    
    //----- Debug image -----
    private GreenfootImage debug;

    /**
     * Collider Constructor: Sets the collider variables and creates the collider image.
     * 
     * @param 'xSize': The width in pixel of the new Collider
     * @param 'ySize': The height in pixel of the new Collider
     * @param 'xOffset': The offset in pixel of the new Collider in x direction 
     * @param 'yOffset': The offset in pixel of the new Collider in y direction
     */ 
    public Collider(int xSize, int ySize, int xOffset, int yOffset)
    {
        this.xSize = xSize;
        this.ySize = ySize;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        transparent = new GreenfootImage(xSize, ySize);
        
        //----- Debug image -----
        debug = new GreenfootImage(xSize, ySize);
        debug.setColor(Color.RED);
        debug.fill();
        debug.setTransparency(75);

        setImage(transparent);
        
        //----- Debug image -----
        //Uncomment the line below
        setImage(debug);
    }
    
    public Collider(GreenfootImage image) {
        transparent = image;
        setImage(transparent);
        
        
    }

    /**
     * Method 'getXOffset': Is called in the 'positionCollider' method in AnimatedCharacter class to get the correct position.
     * 
     * @return: The offset in pixel of the Collider in x direction 
     */
    public int getXOffset() 
    {
        return xOffset;
    }

    /**
     * Method 'getYOffset': Is called in the 'positionCollider' method in AnimatedCharacter class to get the correct position.
     * 
     * @return: The offset in pixel of the Collider in y direction 
     */
    public int getYOffset() 
    {
        return yOffset;
    }

    /**
     * Method 'checkCollision': Is called in every subclass of AnimatedCharacter class that wants to know if they are colliding with another collider or an object.
     * 
     * @return: True if the owner of this collider is intersecting another character or an object, false if not
     */
    public boolean checkCollision()
    {
        if(!getIntersectingObjects(Collider.class).isEmpty() || !getIntersectingObjects(Objects.class).isEmpty())
        {
            return true;            
        }
        else
        {
            return false;
        }
    }
}
