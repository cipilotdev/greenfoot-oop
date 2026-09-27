import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * The MazePath world holds both maze stages on the way to school.
 * Stage 1 (isMaze == false) is a single-screen maze with fallen trees (Logs) blocking some paths.
 * Stage 2 (isMaze == true) is a pure bush maze about two screens wide that scrolls sideways
 * with the teacher and contains moving bush walls.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MazePath extends Game
{
    private static final GreenfootImage mazeMap = new GreenfootImage("worlds/maze.png");
    private static final GreenfootImage block = new GreenfootImage("ui/bush.png");
    private static final GreenfootSound treeSound = new GreenfootSound("TreeMaze.mp3");
    private static final GreenfootSound mazeSound = new GreenfootSound("Maze.mp3");

    //----- Layout -----
    private static final int WORLD_WIDTH = 1248;
    private static final int WORLD_HEIGHT = 576;
    private static final int TILE = 32;              //Pixel distance between two maze cells
    private static final int MAZE_TOP = 96;          //Pixel y of the center of maze row 0
    private static final int MAZE_HEIGHT = 13;       //Rows 0..12, row 0 and row 12 are always walls
    private static final int TREE_WIDTH = 39;        //Stage 1: one screen
    private static final int MAZE_WIDTH = 79;        //Stage 2: about two screens (must be odd)
    private static final int SPAWN_Y_OFFSET = -Teacher.COLLIDER_OFFSET_Y;    //Centers the teacher's feet collider in the corridor

    //----- Side-scrolling (stage 2 only) -----
    private static final int SCROLL_RIGHT = 748;     //Camera follows when the teacher walks right of this x
    private static final int SCROLL_LEFT = 500;      //Camera follows when the teacher walks left of this x
    private List<Actor> scrollActors = new ArrayList<Actor>();
    private int cameraX = 0;
    private int maxCam = 0;
    private GreenfootImage scrollBackground;

    private Dialog fallen = new Dialog(28, 28, true);
    private Dialog puyeng = new Dialog(29, 29, true);

    private Teacher teacher;
    private boolean isMaze;

    /**
     * Constructor for objects of class MazePath.
     * Stage 1 is a bounded single screen, stage 2 is unbounded so the maze can extend beyond the screen.
     */
    public MazePath(Teacher teacher, boolean isMaze)
    {
        super(WORLD_WIDTH, WORLD_HEIGHT, !isMaze);
        this.teacher = teacher;
        this.isMaze = isMaze;
        if (isMaze) {
            scrollBackground = new GreenfootImage(WORLD_WIDTH, WORLD_HEIGHT);
            drawScrollBackground();
            setBackground(scrollBackground);
        } else {
            setBackground(mazeMap);
        }
        prepare();
    }

    private void prepare()
    {
        int difficulty = teacher.getDifficulty();
        if (difficulty == 0) {
            difficulty = Greenfoot.getRandomNumber(2) + 1;
        }

        MazeGenerator mazeGen;
        if (isMaze) {
            mazeSound.playLoop();

            mazeGen = new MazeGenerator(MAZE_WIDTH, MAZE_HEIGHT, difficulty);
            displayMaze(mazeGen.getMaze());
            addMovingWalls(mazeGen, difficulty);

            //Block the entrance so the teacher cannot walk off the left side of the unbounded world
            Collider entranceBlock = new Collider(TILE, WORLD_HEIGHT, 0, 0);
            addObject(entranceBlock, -TILE, WORLD_HEIGHT / 2);
            scrollActors.add(entranceBlock);

            maxCam = MAZE_WIDTH * TILE - WORLD_WIDTH;
        } else {
            treeSound.playLoop();

            mazeGen = new MazeGenerator(TREE_WIDTH, MAZE_HEIGHT, 1);
            modifyMaze(mazeGen.getMaze(), difficulty, mazeGen.getStartY());
            displayMaze(mazeGen.getMaze());
        }

        //Seal the area above row 0 and below the last row (bush tiles are 30px, centered on the row)
        int topEdge = MAZE_TOP - 14;                                      //Slight overlap with row 0
        Collider cTop = new Collider(WORLD_WIDTH, topEdge, 0, 0);
        addObject(cTop, WORLD_WIDTH / 2, topEdge / 2);

        int bottomEdge = MAZE_TOP + (MAZE_HEIGHT - 1) * TILE + 14;       //Slight overlap with the last row
        int bottomSize = WORLD_HEIGHT - bottomEdge;
        Collider cBot = new Collider(WORLD_WIDTH, bottomSize, 0, 0);
        addObject(cBot, WORLD_WIDTH / 2, bottomEdge + bottomSize / 2);

        //Overlay and dialog are added after the tiles so they are painted on top of them
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);

        if (isMaze) {
            addObject(puyeng, 624, 288);
        } else {
            addObject(fallen, 624, 288);
        }

        addObject(teacher, 16, rowToY(mazeGen.getStartY()) + SPAWN_Y_OFFSET);
        teacher.freeze();
    }

    /**
     * Act - scrolls the stage 2 maze so the camera follows the teacher.
     */
    public void act()
    {
        if (!isMaze || teacher.getWorld() != this) {
            return;
        }

        int tx = teacher.getX();
        int shift = 0;
        if (tx > SCROLL_RIGHT && cameraX < maxCam) {
            shift = Math.min(tx - SCROLL_RIGHT, maxCam - cameraX);
        } else if (tx < SCROLL_LEFT && cameraX > 0) {
            shift = -Math.min(SCROLL_LEFT - tx, cameraX);
        }
        if (shift == 0) {
            return;
        }

        //If the teacher is colliding, it reverts to its stored (unscrolled) position during its own act.
        //Scrolling now would make that stored position wrong, so wait until the next tick.
        if (teacher.myCollider != null && teacher.myCollider.getWorld() == this
            && teacher.myCollider.checkCollision()) {
            return;
        }

        cameraX += shift;
        for (Actor a : scrollActors) {
            if (a.getWorld() != this) {
                continue;
            }
            if (a instanceof MovingWall) {
                ((MovingWall)a).shift(-shift);
            } else {
                a.setLocation(a.getX() - shift, a.getY());
            }
        }
        teacher.setLocation(tx - shift, teacher.getY());
        teacher.positionCollider();
        drawScrollBackground();
    }

    /**
     * Tiles the maze background horizontally according to the camera position.
     */
    private void drawScrollBackground()
    {
        int w = mazeMap.getWidth();
        int offset = cameraX % w;
        scrollBackground.clear();
        scrollBackground.drawImage(mazeMap, -offset, 0);
        scrollBackground.drawImage(mazeMap, w - offset, 0);
    }

    private void modifyMaze(int[][] maze, int difficulty, int startY) {
        double density;
        switch (difficulty) {
            case 1: density = 0.05; break; // 5%
            case 2: density = 0.10; break; // 10%
            case 3: density = 0.15; break; // 15%
            default: density = 0.05; break;
        }

        int height = maze.length;
        int width = maze[0].length;

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Keep the cell next to the entrance free, the teacher spawns there
                if (x == 1 && y == startY) {
                    continue;
                }
                // Only modify PATH cells inside the maze
                if (maze[y][x] == 0) {
                    if (Greenfoot.getRandomNumber(100) < density * 100) {
                        maze[y][x] = 2;
                    }
                }
            }
        }
    }

    private void displayMaze(int[][] maze) {
        int y = 0;
        for (int[] row : maze) {
            int x = 0;
            for (int cell : row) {
                if (cell == 1) {
                    Collider c = new Collider(block);
                    addObject(c, x * TILE, rowToY(y));
                    scrollActors.add(c);
                } else if (cell == 2) {
                    Log c = new Log(64, Greenfoot.getRandomNumber(4));
                    addObject(c, x * TILE, rowToY(y));
                    scrollActors.add(c);
                }
                x++;
            }
            y++;
        }
    }

    /**
     * Places moving bush walls on horizontal corridor stretches.
     * A wall can never be walked past inside a one tile wide corridor, so a stretch is only used when:
     * - none of its cells has an opening up or down (the wall never covers a junction or a corner), and
     * - the exit can still be reached from the entrance with every wall stretch treated as solid.
     * That way a wall only ever blocks a detour and the maze can never become unsolvable.
     */
    private void addMovingWalls(MazeGenerator mazeGen, int difficulty) {
        int[][] maze = mazeGen.getMaze();
        int height = maze.length;
        int width = maze[0].length;

        List<int[]> stretches = new ArrayList<int[]>(); // {row, firstX, lastX}
        for (int y = 1; y < height - 1; y += 2) {
            int x = 1;
            while (x < width - 1) {
                if (!isCorridorCell(maze, x, y)) {
                    x++;
                    continue;
                }
                int first = x;
                while (x < width - 1 && isCorridorCell(maze, x, y)) {
                    x++;
                }
                int last = x - 1;
                if (last - first + 1 >= 3) {
                    stretches.add(new int[]{y, first, last});
                }
            }
        }

        Collections.shuffle(stretches, new Random(Greenfoot.getRandomNumber(Integer.MAX_VALUE)));
        boolean[][] blocked = new boolean[height][width];
        int wanted = 3 + difficulty; // 4, 5 or 6 walls
        int placed = 0;
        for (int[] st : stretches) {
            if (placed >= wanted) {
                break;
            }
            for (int x = st[1]; x <= st[2]; x++) blocked[st[0]][x] = true;
            if (!isSolvable(maze, blocked, mazeGen.getStartY(), mazeGen.getEndY())) {
                for (int x = st[1]; x <= st[2]; x++) blocked[st[0]][x] = false;
                continue;
            }

            int left = st[1] * TILE;
            int right = st[2] * TILE;
            int speed = 1 + Greenfoot.getRandomNumber(2);
            int startX = left + Greenfoot.getRandomNumber(right - left + 1);

            MovingWall wall = new MovingWall(block, left, right, speed);
            addObject(wall, startX, rowToY(st[0]));
            scrollActors.add(wall);
            placed++;
        }
    }

    /**
     * A corridor cell is a path cell with walls above and below it (no junction, no corner).
     */
    private boolean isCorridorCell(int[][] maze, int x, int y) {
        return maze[y][x] == 0 && maze[y - 1][x] != 0 && maze[y + 1][x] != 0;
    }

    /**
     * BFS from the entrance (left border) to the exit (right border), not walking on blocked cells.
     */
    private boolean isSolvable(int[][] maze, boolean[][] blocked, int startY, int endY) {
        int height = maze.length;
        int width = maze[0].length;
        boolean[][] seen = new boolean[height][width];
        ArrayDeque<int[]> queue = new ArrayDeque<int[]>();
        queue.add(new int[]{0, startY});
        seen[startY][0] = true;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int[] c = queue.poll();
            if (c[0] == width - 1 && c[1] == endY) {
                return true;
            }
            for (int[] d : dirs) {
                int nx = c[0] + d[0];
                int ny = c[1] + d[1];
                if (nx >= 0 && ny >= 0 && nx < width && ny < height && !seen[ny][nx]
                    && maze[ny][nx] == 0 && !blocked[ny][nx]) {
                    seen[ny][nx] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        return false;
    }

    private int rowToY(int row) {
        return MAZE_TOP + row * TILE;
    }

    /**
     * Method 'atRightEnd': Is called by the teacher before leaving the world on the right side.
     *
     * @return: True in stage 1, in stage 2 only when the camera has scrolled to the end of the maze
     */
    public boolean atRightEnd() {
        return !isMaze || cameraX >= maxCam;
    }

    @Override
    public void stopped() {
        treeSound.stop();
        mazeSound.stop();
    }

    public boolean isMaze() {
        return isMaze;
    }
}

class MazeGenerator {
    private static final int WALL = 1;
    private static final int PATH = 0;

    private int width, height; // maze size including the border walls
    private int[][] maze;      // [height][width]

    private int startX, startY; // entrance on the left border (x == 0)
    private int endX, endY;     // exit on the outer right side (x == width)
    private int difficulty;

    private static final int MAX_ATTEMPTS = 1000;
    private static final int BASE_WIDTH = 39; // thresholds are tuned for a one screen maze

    /**
     * Generates a maze with a random entrance row on the left and a random exit row on the right.
     * One of them lies in the upper half, the other one in the lower half.
     */
    public MazeGenerator(int width, int height, int difficulty) {
        if (width < 5 || height < 5)
            throw new IllegalArgumentException("width and height must be >= 5");

        this.width = width;
        this.height = height;
        this.difficulty = difficulty;

        int upper = randomOddRow(1, (height - 1) / 2 - 1);
        int lower = randomOddRow((height - 1) / 2 + 1, height - 2);
        boolean startTop = Greenfoot.getRandomNumber(2) == 0;

        this.startX = 0;
        this.startY = startTop ? upper : lower;
        this.endX = width;
        this.endY = startTop ? lower : upper;

        generateValidMaze();
    }

    // random odd number in [min, max] (falls back to the nearest odd interior row)
    private int randomOddRow(int min, int max) {
        int lo = min | 1;
        int hi = ((max & 1) == 1) ? max : max - 1;
        if (hi < lo) return Math.max(1, Math.min(height - 2, lo));
        return lo + 2 * Greenfoot.getRandomNumber((hi - lo) / 2 + 1);
    }

    /* --- Public API --- */
    public int[][] getMaze() { return maze; }
    public int getStartY() { return startY; }
    public int getEndY() { return endY; }

    /* --- Main generation loop --- */
    private void generateValidMaze() {
        // wide mazes take longer per attempt, so allow fewer attempts
        int maxAttempts = Math.max(150, MAX_ATTEMPTS * BASE_WIDTH / width);
        int attempts = 0;
        int bestScore = -1;
        int[][] bestMaze = null;

        // carve from an odd interior cell so the carved cells stay on odd coordinates
        int sx = clampOdd((width / 2) | 1, width);
        int sy = clampOdd((height / 2) | 1, height);

        while (attempts++ < maxAttempts) {
            initWalls();
            generateMaze(sx, sy, difficulty);
            openEntranceExit();

            int score = calculateScore();
            if (score < 0) {
                // no path this attempt
                continue;
            }

            if (score > bestScore) {
                bestScore = score;
                bestMaze = deepCopy(maze);
            }

            if (scoreFitsDifficulty(score)) {
                return; // keep current maze
            }
        }

        // fallback: use best attempt if any
        if (bestMaze != null) {
            this.maze = bestMaze;
        } else {
            // last resort: single carve from center
            initWalls();
            generateMaze(sx, sy, difficulty);
            openEntranceExit();
        }
    }

    private int clampOdd(int v, int size) {
        int max = ((size - 2) & 1) == 1 ? size - 2 : size - 3;
        v = Math.max(1, Math.min(max, v));
        return (v & 1) == 1 ? v : v - 1;
    }

    private int[][] deepCopy(int[][] src) {
        int[][] c = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) System.arraycopy(src[i], 0, c[i], 0, src[i].length);
        return c;
    }

    private void initWalls() {
        maze = new int[height][width];
        for (int y = 0; y < height; y++)
            Arrays.fill(maze[y], WALL);
    }

    /* --- Entrance/Exit handling:
       - The entrance is at x == 0: open (0, startY) and its neighbor (1, startY).
       - The exit is on the outer right side (x == width): open (width-1, endY) and (width-2, endY).
    */
    private void openEntranceExit() {
        setOpen(innerX(startX), startY);
        setOpen(innerX(endX), endY);

        if (startX == 0) setOpen(1, startY);
        if (startX == width) setOpen(width - 2, startY);
        if (endX == 0) setOpen(1, endY);
        if (endX == width) setOpen(width - 2, endY);
    }

    private int innerX(int x) {
        return (x == width) ? width - 1 : x;
    }

    private boolean isValidCoord(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void setOpen(int x, int y) {
        if (isValidCoord(x, y)) maze[y][x] = PATH;
    }

    /* --- Maze carving (recursive backtracking stepping by 2) --- */
    private void generateMaze(int x, int y, int diff) {
        maze[y][x] = PATH;

        int[] dirs = {0,1,2,3};
        shuffle(dirs);

        for (int dir : dirs) {
            int dx = (dir == 1 ? 2 : dir == 3 ? -2 : 0);
            int dy = (dir == 0 ? -2 : dir == 2 ? 2 : 0);

            int nx = x + dx;
            int ny = y + dy;

            if (!isInsideInner(nx, ny) || maze[ny][nx] == PATH) continue;

            // carve between
            maze[y + dy/2][x + dx/2] = PATH;

            generateMaze(nx, ny, diff);

            // difficulty-specific extras: extra openings create loops (shortcuts),
            // so harder mazes get fewer of them and the shortest path gets longer
            if (diff <= 1) { // EASY
                if (Greenfoot.getRandomNumber(3) == 0) carveSidePath(nx, ny);
                if (Greenfoot.getRandomNumber(6) == 0) carveExtraForward(nx, ny, dx, dy);
            } else if (diff == 2) { // MEDIUM
                if (Greenfoot.getRandomNumber(5) == 0) carveSidePath(nx, ny);
            } else { // HARD
                if (Greenfoot.getRandomNumber(8) == 0) carveSidePath(nx, ny);
            }
        }
    }

    // only the interior is carved, the outer rows and columns stay walls
    private boolean isInsideInner(int x, int y) {
        return x >= 1 && x < width - 1 && y >= 1 && y < height - 1;
    }

    private void carveSidePath(int nx, int ny) {
        int[][] around = {{1,0},{-1,0},{0,1},{0,-1}};
        int[] s = around[Greenfoot.getRandomNumber(around.length)];
        int sx = nx + s[0], sy = ny + s[1];
        if (isInsideInner(sx, sy)) maze[sy][sx] = PATH;
    }

    private void carveExtraForward(int nx, int ny, int dx, int dy) {
        int bx = nx + dx;
        int by = ny + dy;
        if (isInsideInner(bx, by)) {
            maze[ny + dy/2][nx + dx/2] = PATH;
            maze[by][bx] = PATH;
        }
    }

    private void shuffle(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = Greenfoot.getRandomNumber(i + 1);
            int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        }
    }

    /* --- scoring: shortest path length + turns*5, or -1 if there is no path (BFS) --- */
    private int calculateScore() {
        int sx = innerX(startX), sy = startY;
        int ex = innerX(endX), ey = endY;
        if (!isValidCoord(sx, sy) || !isValidCoord(ex, ey)) return -1;
        if (maze[sy][sx] != PATH || maze[ey][ex] != PATH) return -1;

        int n = width * height;
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] queue = new int[n];
        int head = 0, tail = 0;
        int start = sy * width + sx, end = ey * width + ex;
        parent[start] = start;
        queue[tail++] = start;

        while (head < tail) {
            int cur = queue[head++];
            if (cur == end) break;
            int cx = cur % width, cy = cur / width;
            if (cx + 1 < width) tail = visit(cur, cur + 1, cx + 1, cy, parent, queue, tail);
            if (cx - 1 >= 0) tail = visit(cur, cur - 1, cx - 1, cy, parent, queue, tail);
            if (cy + 1 < height) tail = visit(cur, cur + width, cx, cy + 1, parent, queue, tail);
            if (cy - 1 >= 0) tail = visit(cur, cur - width, cx, cy - 1, parent, queue, tail);
        }
        if (parent[end] == -1) return -1;

        // walk back from the end, counting cells and direction changes
        int len = 1, turns = 0, lastStep = 0;
        for (int cur = end; cur != start; cur = parent[cur]) {
            int step = cur - parent[cur];
            if (lastStep != 0 && step != lastStep) turns++;
            lastStep = step;
            len++;
        }
        return len + turns * 5;
    }

    private int visit(int from, int to, int x, int y, int[] parent, int[] queue, int tail) {
        if (parent[to] == -1 && maze[y][x] == PATH) {
            parent[to] = from;
            queue[tail++] = to;
        }
        return tail;
    }

    // thresholds are for a 39 wide maze and scale with the width
    private boolean scoreFitsDifficulty(int score) {
        double scale = width / (double) BASE_WIDTH;
        switch (difficulty) {
            case 1: return score >= 100 * scale && score < 140 * scale;
            case 2: return score >= 140 * scale && score < 180 * scale;
            case 3: return score >= 180 * scale;
            default: return true;
        }
    }
}
