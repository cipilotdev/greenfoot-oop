import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Log here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Log extends Collider
{
    private int clickRange;
    
    private static final int CELL = 32;          //Size of a maze cell
    private static final int REACH = 10;         //How far (in pixel) from the cell edge the teacher can punch
    private static final int MIN_OVERLAP = 10;   //How much the teacher must be lined up with the cell
    private int state;
    
    private static final double pressCooldown = 250000000.0;   //Cooldown (0,25sec) between pressing a key
    private double lastPressedKeyTime;
    
    private GreenfootImage logImage = new GreenfootImage("ui/tree/tree_1.png");
        
    public Log(int clickRange, int state) {
        super(new GreenfootImage("ui/tree/tree_" + state + ".png"));
        this.clickRange = clickRange;
        this.state = state;
    }
    
    /**
     * Act - do whatever the Log wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        double i = System.nanoTime();
        if (Greenfoot.isKeyDown("space")) {
            if(!getObjectsInRange(clickRange, Teacher.class).isEmpty()) {
                Teacher t = (Teacher)getObjectsInRange(clickRange, Teacher.class).get(0);
                if(t != null && canBeHitBy(t)) {
                    if(i - lastPressedKeyTime >= pressCooldown) {
                        updateState();
                        lastPressedKeyTime = System.nanoTime();
                    }
                }
            }
        }
    }
    
    /**
     * The teacher can only punch the tree when standing right next to its maze cell and facing it,
     * so there is never a bush between them.
     */
    private boolean canBeHitBy(Teacher t) {
        int half = CELL / 2;
        int cellLeft = getX() - half;
        int cellRight = getX() + half;
        int cellTop = getY() - half;
        int cellBottom = getY() + half;
        
        //Teacher collider box (around his feet)
        int boxLeft = t.getX() - Teacher.COLLIDER_WIDTH / 2;
        int boxRight = boxLeft + Teacher.COLLIDER_WIDTH;
        int boxTop = t.getY() + Teacher.COLLIDER_OFFSET_Y - Teacher.COLLIDER_HEIGHT / 2;
        int boxBottom = boxTop + Teacher.COLLIDER_HEIGHT;
        
        int overlapX = Math.min(cellRight, boxRight) - Math.max(cellLeft, boxLeft);
        int overlapY = Math.min(cellBottom, boxBottom) - Math.max(cellTop, boxTop);
        String facing = t.getFacing();
        
        if (overlapY >= MIN_OVERLAP) {
            if (boxRight <= cellLeft + REACH && boxRight >= cellLeft - REACH && facing.equals("east")) {
                return true;
            }
            if (boxLeft >= cellRight - REACH && boxLeft <= cellRight + REACH && facing.equals("west")) {
                return true;
            }
        }
        if (overlapX >= MIN_OVERLAP) {
            if (boxBottom <= cellTop + REACH && boxBottom >= cellTop - REACH && facing.equals("south")) {
                return true;
            }
            if (boxTop >= cellBottom - REACH && boxTop <= cellBottom + REACH && facing.equals("north")) {
                return true;
            }
        }
        return false;
    }
    
    private void updateState() {
        state++;
        if (state > 5) {
            getWorld().removeObject(this);
            return;
        }
        GreenfootImage background = new GreenfootImage("ui/tree/tree_" + state + ".png");
        setImage(background);
    }
}
