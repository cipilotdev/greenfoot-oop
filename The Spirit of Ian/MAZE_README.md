# Maze Generation System

## Overview
This maze generation system creates procedural mazes for the Greenfoot game "The Spirit of Ian" using the **recursive backtracking algorithm**. The maze uses tree sprites as walls and creates a perfect maze (exactly one path between any two points).

## Components

### 1. MazeGenerator.java
**Purpose**: Generates the maze logic using recursive backtracking algorithm.

**Key Features**:
- Creates a perfect maze (no loops, single path between points)
- Configurable grid size
- Optional random seed for reproducible mazes
- Returns 2D array: `0 = wall`, `1 = path`

**Algorithm**: Recursive Backtracking
1. Start at a cell and mark it as part of the maze
2. Randomly choose an unvisited neighbor
3. Remove the wall between cells
4. Recursively visit that neighbor
5. Backtrack when no unvisited neighbors remain

### 2. Tree.java
**Purpose**: Actor class representing maze walls/obstacles.

**Key Features**:
- Uses tree sprites (32x64 pixels)
- Has collision detection via Collider
- Supports 3 tree variants for visual variety
- Automatically manages collider lifecycle

### 3. MazePath.java (Updated)
**Purpose**: World class that generates and renders the maze.

**Key Configuration**:
```java
World Size: 1248 x 576 pixels
Tree Size: 32 x 64 pixels
Grid Size: 39 x 6 cells
Top Collider: 90 pixels
Bottom Collider: 70 pixels
Maze Area: 416 pixels (576 - 90 - 70)
```

## How It Works

### Grid Calculation
```
GRID_WIDTH = 1248 / 32 = 39 cells
GRID_HEIGHT = (576 - 90 - 70) / 64 = 6 cells
```

### Maze Generation Process
1. **Initialization**: All cells start as walls (0)
2. **Generation**: Recursive backtracking carves paths (1)
3. **Rendering**: Trees placed at wall positions

### Difficulty Levels
- **0 (Easy)**: Random seed (different maze each time)
- **1 (Medium)**: Seed = 1000 (reproducible)
- **2 (Hard)**: Seed = 2000 (reproducible)
- **3 (Expert)**: Seed = 3000 (reproducible)

## Usage

### Creating a Maze World
```java
// Create maze with difficulty level
MazePath mazeWorld = new MazePath(1);  // 1 = medium difficulty
Greenfoot.setWorld(mazeWorld);
```

### Accessing Maze Data
```java
// Get the maze generator
MazeGenerator generator = mazeWorld.getMazeGenerator();

// Check if position is a path
boolean isPath = generator.isPath(x, y);

// Get the full maze grid
int[][] maze = generator.getMaze();

// Print maze to console (debugging)
generator.printMaze();
```

## File Structure
```
The Spirit of Ian/
├── MazeGenerator.java    # Maze generation algorithm
├── Tree.java             # Wall/obstacle actor
├── MazePath.java         # Maze world
├── Teacher.java          # Player character
├── Collider.java         # Collision detection
└── images/
    └── ui/
        └── tree/
            ├── tree_1.png (32x64)
            ├── tree_2.png (32x64)
            └── tree_3.png (32x64)
```

## Collision System

### How Collisions Work
1. Each `Tree` has a `Collider` (32x64 pixels)
2. `Teacher` has a collider that checks intersections
3. When collision detected, teacher returns to previous position
4. Top/bottom boundary colliders prevent leaving maze area

### Collision Flow
```
Teacher moves → 
positionCollider() updates collider location → 
checkCollision() tests for intersection → 
If collision: setLocation(oldX, oldY)
```

## Customization

### Change Grid Size
Modify constants in `MazePath.java`:
```java
private static final int TREE_WIDTH = 32;   // Change wall width
private static final int TREE_HEIGHT = 64;  // Change wall height
```

### Add More Difficulty Levels
In `generateMaze()`:
```java
int seed = switch(difficulty) {
    case 0 -> -1;          // Random
    case 1 -> 1000;        // Medium
    case 2 -> 2000;        // Hard
    case 3 -> 3000;        // Expert
    case 4 -> 4000;        // Custom
    default -> -1;
};
```

### Use Different Wall Images
Modify `Tree.java` constructor:
```java
String imagePath = "ui/wall/wall_" + variant + ".png";
```

## Debugging

### Enable Collider Visualization
In `Collider.java`, uncomment:
```java
setImage(debug);  // Shows red collision boxes
```

### Print Maze to Console
In `MazePath.generateMaze()`, uncomment:
```java
mazeGenerator.printMaze();
```

Output example:
```
██████████████████████
    ██    ██    ██  
██  ██  ██  ██  ██  ██
██      ██      ██    
██████████████████████
```

## Performance Notes

- **Grid Size**: 39 x 6 = 234 cells maximum
- **Average Trees**: ~117 trees (50% walls in perfect maze)
- **Generation Time**: < 50ms for this grid size
- **Memory**: Minimal (2D int array + tree actors)

## Troubleshooting

### Issue: Teacher gets stuck in walls
**Solution**: Check collider sizes match between Teacher and Tree

### Issue: Maze doesn't generate
**Solution**: Verify tree images exist at `ui/tree/tree_1.png`

### Issue: Greenfoot cannot be resolved (compile errors)
**Solution**: These are IDE warnings only - Greenfoot provides these at runtime

### Issue: Teacher spawns in a wall
**Solution**: Adjust starting position to guarantee path cell:
```java
addObject(teacher, TREE_WIDTH, WORLD_HEIGHT / 2);
```

## Algorithm Complexity

- **Time**: O(W × H) where W=width, H=height
- **Space**: O(W × H) for maze array + O(W × H) for recursion stack
- **For 39×6 grid**: ~234 operations, very fast

## Future Enhancements

1. **Exit/Goal**: Add exit tree at maze end
2. **Collectibles**: Place items in path cells
3. **Enemies**: Add patrolling enemies in corridors
4. **Multiple Paths**: Modify algorithm to create loops
5. **Room Generation**: Create larger open areas
6. **Minimap**: Show maze overview in corner

## Credits

- **Algorithm**: Recursive Backtracking (classic maze generation)
- **Implementation**: Custom for Greenfoot framework
- **Tree Assets**: ui/tree/tree_*.png (32x64 pixels)
