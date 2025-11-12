import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class MazePath here.
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
    
    private Dialog fallen = new Dialog(28, 28, true);
    private Dialog puyeng = new Dialog(29, 29, true);
    
    private Teacher teacher;
    private boolean isMaze;
    
    /**
     * Constructor for objects of class MazePath.
     * 
     */
    public MazePath(Teacher teacher, boolean isMaze)
    {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1248, 576);
        this.teacher = teacher;
        this.isMaze = isMaze;
        setBackground(mazeMap);
        prepare();
    }
    
    private void prepare()
    {
        Overlay o = new Overlay("fadeOut", 2);
        addObject(o, 624, 288);
        
        Collider cTop = new Collider(1248, 70, 0, 0);
        addObject(cTop, 624, 35);
        
        Collider cBot = new Collider(1248, 70, 0, 0);
        addObject(cBot, 624, 541);
        
        if (isMaze) {
            mazeSound.playLoop();
            
            MazeGenerator mazeGen = new MazeGenerator(39, 14, 0, 4, 39, 4, teacher.getDifficulty());
            displayMaze(mazeGen.getMaze());
            
            addObject(puyeng, 624, 288);
        } else {
            treeSound.playLoop();
            
            MazeGenerator mazeGen = new MazeGenerator(39, 14, 0, 4, 39, 4, 1);
            modifyMaze(mazeGen.getMaze(), teacher.getDifficulty());
            displayMaze(mazeGen.getMaze());
            
            addObject(fallen, 624, 288);
        }
        
        //Teacher teacher = new Teacher();
        addObject(teacher, 16, 216);
    }
    
    private void modifyMaze(int[][] maze, int difficulty) {
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
                    addObject(c, x * 32, 96 + y * 32);
                } else if (cell == 2) {
                    Log c = new Log(64, Greenfoot.getRandomNumber(4));
                    addObject(c, x * 32, 96 + y * 32);
                }
                x++;
            }
            y++;
        }
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

    private int width, height; // inner maze size
    private int[][] maze;      // [height][width]

    private int startX, startY; // may be 0..width  or 0..height (value==width means outer-right)
    private int endX, endY;
    private int difficulty;

    private static final int MAX_ATTEMPTS = 1000;

    public MazeGenerator(int width, int height,
                         int startX, int startY,
                         int endX, int endY,
                         int difficulty) {

        // use width/height as provided (inner maze). force min size
        if (width < 3 || height < 3)
            throw new IllegalArgumentException("width and height must be >= 3");

        // keep width/height as-is (no automatic outer border)
        this.width = width;
        this.height = height;
        this.difficulty = difficulty;
        
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        generateValidMaze();
    }

    // check that (x,y) is on the conceptual border: either x==0 or x==width or y==0 or y==height
    private boolean isValidBorderCoord(int x, int y) {
        boolean xb = (x == 0 || x == width);
        boolean yb = (y == 0 || y == height);
        // must be on one of the four borders (outer side allowed via width/height)
        return (x >= 0 && x <= width && y >= 0 && y <= height) && (xb || yb);
    }

    /* --- Public API --- */
    public int[][] getMaze() { return maze; }

    /* --- Main generation loop --- */
    private void generateValidMaze() {
        int attempts = 0;
        int bestScore = -1;
        int[][] bestMaze = null;

        while (attempts++ < MAX_ATTEMPTS) {
            initWalls();
            // start carving from an interior coordinate (one step inside if the start is border)
            int sx = Math.max(1, Math.min(width - 2, width / 2));
            int sy = Math.max(1, Math.min(height - 2, height / 2));
            
            // clamp to valid interior
            sx = Math.max(1, Math.min(width - 2, sx));
            sy = Math.max(1, Math.min(height - 2, sy));

            generateMaze(sx, sy, difficulty);

            // open entrance/exit tiles (treat outer coords by opening their inner neighbor)
            openEntranceExit();

            List<int[]> path = findShortestPath();
            if (path == null) {
                // no path this attempt
                continue;
            }

            int score = calculateScore(path);
            if (score > bestScore) {
                bestScore = score;
                bestMaze = deepCopy(maze);
            }

            if (scoreFitsDifficulty(score)) {
                this.maze = maze; // keep current maze
                //System.out.println("Generated maze in " + attempts + " attempts. Score=" + score);
                return;
            }
        }

        // fallback: use best attempt if any
        if (bestMaze != null) {
            this.maze = bestMaze;
            System.out.println("Fallback: used best of " + (attempts-1) + " attempts. BestScore=" + bestScore);
        } else {
            // last resort: single carve from center
            initWalls();
            generateMaze(width/2, height/2, difficulty);
            openEntranceExit();
            this.maze = maze;
            //System.out.println("Fallback final maze (no good attempts).");
        }
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
       - If coordinate equals width/height (outer side), we do not write outside array;
         instead we open the adjacent inner cell (width-1 or height-1).
       - If coordinate is 0, we open index 0 and its neighbor 1 so the entrance connects inward.
    */
    private void openEntranceExit() {
        // Resolve start inner coords to use for BFS start
        int sxInner = (startX == width) ? width - 1 : startX;
        int syInner = (startY == height) ? height - 1 : startY;
        int exInner = (endX == width) ? width - 1 : endX;
        int eyInner = (endY == height) ? height - 1 : endY;

        // Open the border tile inside the array
        if (isValidCoord(sxInner, syInner)) maze[syInner][sxInner] = PATH;
        if (isValidCoord(exInner, eyInner)) maze[eyInner][exInner] = PATH;

        // Also open the adjacent inward neighbor so the 'outer opening' actually connects
        // Left outer: startX == 0 -> open (1, startY)
        if (startX == 0 && isValidCoord(1, syInner)) setOpen(1, syInner);
        // Right outer: startX == width -> open (width-2, ...)
        if (startX == width && isValidCoord(width - 2, syInner)) setOpen(width - 2, syInner);
        // Top outer: startY == 0 -> open (startX,1)
        if (startY == 0 && isValidCoord(sxInner, 1)) setOpen(sxInner, 1);
        // Bottom outer: startY == height -> open (startX,height-2)
        if (startY == height && isValidCoord(sxInner, height - 2)) setOpen(sxInner, height - 2);

        // Same for exit
        if (endX == 0 && isValidCoord(1, eyInner)) setOpen(1, eyInner);
        if (endX == width && isValidCoord(width - 2, eyInner)) setOpen(width - 2, eyInner);
        if (endY == 0 && isValidCoord(exInner, 1)) setOpen(exInner, 1);
        if (endY == height && isValidCoord(exInner, height - 2)) setOpen(exInner, height - 2);
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
            int bx = x + dx/2;
            int by = y + dy/2;
            if (isValidCoord(bx, by)) maze[by][bx] = PATH;

            generateMaze(nx, ny, diff);

            // difficulty-specific extras (preserve your earlier logic)
            if (diff == 1) {
                if (Greenfoot.getRandomNumber(5) == 0) carveSidePath(nx, ny);
            } else if (diff == 2) {
                if (Greenfoot.getRandomNumber(3) == 0) carveSidePath(nx, ny);
                if (Greenfoot.getRandomNumber(6) == 0) carveExtraForward(nx, ny, dx, dy);
            } else { // HARD
                if (Greenfoot.getRandomNumber(2) == 0) carveSidePath(nx, ny);
                if (Greenfoot.getRandomNumber(3) == 0) carveExtraForward(nx, ny, dx, dy);
            }
        }
    }

    private boolean isInsideInner(int x, int y) {
        // allow full inner area [0..width-1], [0..height-1], but when stepping by 2 we want to stay inside
        return x >= 0 && x < width && y >= 0 && y < height;
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
            int betweenX = nx + dx/2;
            int betweenY = ny + dy/2;
            if (isValidCoord(betweenX, betweenY)) maze[betweenY][betweenX] = PATH;
            maze[by][bx] = PATH;
        }
    }

    private void shuffle(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = Greenfoot.getRandomNumber(i + 1);
            int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        }
    }

    /* --- BFS shortest path (standard, returns path list or null) --- */
    private List<int[]> findShortestPath() {
        // map requested border coords to inner indices for BFS start/end
        int sx = (startX == width) ? width - 1 : startX;
        int sy = (startY == height) ? height - 1 : startY;
        int ex = (endX == width) ? width - 1 : endX;
        int ey = (endY == height) ? height - 1 : endY;

        if (!isValidCoord(sx, sy) || !isValidCoord(ex, ey)) return null;
        if (maze[sy][sx] == WALL || maze[ey][ex] == WALL) return null;

        Queue<int[]> q = new LinkedList<>();
        boolean[][] visited = new boolean[height][width];
        Map<String,String> parent = new HashMap<>();

        q.add(new int[]{sx, sy});
        visited[sy][sx] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cx = cur[0], cy = cur[1];
            if (cx == ex && cy == ey) return buildPath(parent, cur);

            for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height
                    && maze[ny][nx] == PATH && !visited[ny][nx]) {
                    visited[ny][nx] = true;
                    parent.put(nx + "," + ny, cx + "," + cy);
                    q.add(new int[]{nx, ny});
                }
            }
        }
        return null;
    }

    private List<int[]> buildPath(Map<String,String> parent, int[] end) {
        List<int[]> path = new ArrayList<>();
        String key = end[0] + "," + end[1];
        while (key != null) {
            String[] p = key.split(",");
            path.add(new int[]{Integer.parseInt(p[0]), Integer.parseInt(p[1])});
            if (!parent.containsKey(key)) break;
            key = parent.get(key);
        }
        Collections.reverse(path);
        if (path.size() < 2) return null;
        return path;
    }

    /* --- scoring: steps + turns*2 --- */
    private int calculateScore(List<int[]> path) {
        int turns = 0;
        for (int i = 2; i < path.size(); i++) {
            int[] a = path.get(i-2), b = path.get(i-1), c = path.get(i);
            int dx1 = b[0]-a[0], dy1 = b[1]-a[1];
            int dx2 = c[0]-b[0], dy2 = c[1]-b[1];
            if (dx1 != dx2 || dy1 != dy2) turns++;
        }
        return path.size() + turns * 5;
    }

    private boolean scoreFitsDifficulty(int score) {
        switch (difficulty) {
            case 1: return score >= 60 && score < 90;
            case 2: return score >= 100 && score < 140;
            case 3: return score >= 140;
            default: return true;
        }
    }
}
