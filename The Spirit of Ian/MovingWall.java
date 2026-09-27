import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A MovingWall is a bush that slides back and forth along a horizontal corridor in the stage 2 maze.
 * It turns around before it touches the teacher, so it never traps him.
 * The teacher can kick it (SPACE while standing next to it and facing it): the bush then shoots away along its corridor,
 * crashes at the end and breaks apart, opening the way.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MovingWall extends Collider
{
    //Minimum free space (in pixel) kept between the wall and the teacher's collider while patrolling.
    //Larger than the teacher's maximum step per tick, so the teacher's last position is always free.
    private static final int MARGIN = 8;
    //How close (in pixel) the teacher has to stand to kick the wall
    private static final int KICK_RANGE = 14;
    private static final int KICK_SPEED = 9;
    private static final int BREAK_SPEED = 20;

    private static final int PATROL = 0;
    private static final int KICKED = 1;
    private static final int BREAKING = 2;

    private int leftBound;
    private int rightBound;
    private int speed;
    private int direction;
    private int state = PATROL;
    private int transparency = 255;
    private boolean spaceWasDown = true;   //Ignore a SPACE that was already held when the wall appeared

    /**
     * MovingWall Constructor
     *
     * @param 'image': The image of the wall (also its collision size), copied so it can fade on its own
     * @param 'leftBound': The smallest x (in pixel) the center of the wall may reach
     * @param 'rightBound': The largest x (in pixel) the center of the wall may reach
     * @param 'speed': How many pixel the wall moves per tick
     */
    public MovingWall(GreenfootImage image, int leftBound, int rightBound, int speed)
    {
        super(new GreenfootImage(image));
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = Math.max(1, speed);
        this.direction = Greenfoot.getRandomNumber(2) == 0 ? 1 : -1;
    }

    /**
     * Act - patrol, fly after a kick, or break apart.
     */
    public void act()
    {
        switch (state) {
            case PATROL:
                checkKick();
                if (state == PATROL) {
                    patrol();
                }
                break;
            case KICKED:
                fly();
                break;
            case BREAKING:
                transparency -= BREAK_SPEED;
                if (transparency <= 0) {
                    getWorld().removeObject(this);
                    return;
                }
                getImage().setTransparency(transparency);
                break;
        }
    }

    /**
     * Method 'shift': Is called by the MazePath world when the camera scrolls.
     *
     * @param 'dx': The distance in pixel the wall and its bounds move in x direction
     */
    public void shift(int dx)
    {
        leftBound += dx;
        rightBound += dx;
        setLocation(getX() + dx, getY());
    }

    private void patrol()
    {
        int x = getX();
        int nextX = Math.max(leftBound, Math.min(rightBound, x + direction * speed));

        if (nextX == x || tooCloseToTeacher(x, nextX)) {
            direction = -direction;
            return;
        }

        setLocation(nextX, getY());

        if (nextX == leftBound || nextX == rightBound) {
            direction = -direction;
        }
    }

    /**
     * A new SPACE press while the teacher stands right next to the wall (same corridor) kicks it away from him.
     */
    private void checkKick()
    {
        boolean spaceDown = Greenfoot.isKeyDown("space");
        boolean pressed = spaceDown && !spaceWasDown;
        spaceWasDown = spaceDown;
        if (!pressed) {
            return;
        }

        Teacher teacher = findTeacher();
        if (teacher == null) {
            return;
        }
        int[] box = teacherBox(teacher);
        int[] wall = wallBox(getX(), 0);

        //At least half of the teacher's collider height overlaps the wall row
        int overlap = Math.min(box[3], wall[3]) - Math.max(box[2], wall[2]);
        boolean sameCorridor = overlap >= (box[3] - box[2]) / 2;
        int gapRight = box[0] - wall[1];   //Teacher right of the wall
        int gapLeft = wall[0] - box[1];    //Teacher left of the wall
        if (!sameCorridor) {
            return;
        }
        String facing = teacher.getFacing();
        if (gapLeft >= 0 && gapLeft <= KICK_RANGE && facing.equals("east")) {
            direction = 1;
            state = KICKED;
        } else if (gapRight >= 0 && gapRight <= KICK_RANGE && facing.equals("west")) {
            direction = -1;
            state = KICKED;
        }
    }

    /**
     * After a kick the wall shoots along its corridor until it crashes at the end, then breaks apart.
     */
    private void fly()
    {
        int nextX = Math.max(leftBound, Math.min(rightBound, getX() + direction * KICK_SPEED));
        setLocation(nextX, getY());
        if (nextX == leftBound || nextX == rightBound) {
            state = BREAKING;
        }
    }

    /**
     * Checks if moving from x to nextX would bring the wall closer than MARGIN to the teacher's collider.
     * Moving away from the teacher is always allowed.
     */
    private boolean tooCloseToTeacher(int x, int nextX)
    {
        Teacher teacher = findTeacher();
        if (teacher == null) {
            return false;
        }
        int[] box = teacherBox(teacher);
        int[] wall = wallBox(nextX, MARGIN);
        boolean near = wall[0] < box[1] && wall[1] > box[0] && wall[2] < box[3] && wall[3] > box[2];
        int cx = (box[0] + box[1]) / 2;
        return near && Math.abs(nextX - cx) <= Math.abs(x - cx);
    }

    private Teacher findTeacher()
    {
        World world = getWorld();
        if (world == null || world.getObjects(Teacher.class).isEmpty()) {
            return null;
        }
        return world.getObjects(Teacher.class).get(0);
    }

    /**
     * @return {left, right, top, bottom} of the teacher's collider (computed from its known size if not placed yet)
     */
    private int[] teacherBox(Teacher teacher)
    {
        int cx, cy, cw, ch;
        Collider c = teacher.myCollider;
        if (c != null && c.getWorld() == getWorld()) {
            cx = c.getX();
            cy = c.getY();
            cw = c.getImage().getWidth();
            ch = c.getImage().getHeight();
        } else {
            cx = teacher.getX();
            cy = teacher.getY() + Teacher.COLLIDER_OFFSET_Y;
            cw = Teacher.COLLIDER_WIDTH;
            ch = Teacher.COLLIDER_HEIGHT;
        }
        int left = cx - cw / 2;
        int top = cy - ch / 2;
        return new int[]{left, left + cw, top, top + ch};
    }

    /**
     * @return {left, right, top, bottom} of the wall centered at wallX, expanded by margin
     */
    private int[] wallBox(int wallX, int margin)
    {
        int w = getImage().getWidth();
        int h = getImage().getHeight();
        int left = wallX - w / 2 - margin;
        int top = getY() - h / 2 - margin;
        return new int[]{left, left + w + 2 * margin, top, top + h + 2 * margin};
    }
}
