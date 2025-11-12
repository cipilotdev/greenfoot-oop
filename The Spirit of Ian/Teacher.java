import greenfoot.*;
import java.util.List;

public class Teacher extends AnimatedSprites {
    private int oldX, oldY;
    private static final GreenfootImage teacher = new GreenfootImage("Teacher/standing/south.png");
    
    private static final int animationSpeed = 20;
    private static final int walkSpeed = 60;
    
    private boolean popUpOpen = false;
    
    private Dialog radio = new Dialog(10, 12);
    private Dialog kepsek = new Dialog(13, 18, false);
    private Dialog silent = new Dialog(19, 19, false);
    private Dialog kaget = new Dialog(20, 23, false);
    private boolean startFinished = false;
    private boolean radioSpawned = false;
    
    private int difficulty;
    
    public Teacher(int difficulty) {
        this.difficulty = difficulty;
        
        changeSpeed(walkSpeed, animationSpeed);
        setLayer(0, teacher);
        animations.put("walk", Animation.createAnimation(getSpriteSheet(), 9, 4, 9, 64, 64));
        primaryAnimation = animations.get("walk");
        
        loadAnimations();
        setImage(getCurrentFrame());
        
        setCollider(20, 28, 0, 7);
    }    
    
    public void act() {
        handlePopUp();
        
        handleTutorial();
        
        if (!popUpOpen) {
            handleInput();
        }
        
        animate();
        
        checkCollision();
        
        storePosition();
        
        checkChangeMap();
        
        super.teacherAct();
    }
    
    public void checkCollision()
    {
        if (myCollider.checkCollision()) {
            setLocation(oldX, oldY);
        }
    }
    
    public void storePosition() {
        oldX = getX();
        oldY = getY();
    }
    
    public void checkChangeMap() {
        World world = this.getWorld();
        
        if (world.getClass() == CityClass.class) {
            if (getX() > 1045 && getY() < 100 && getX() < 1110) {
                Greenfoot.setWorld(new Home(this));
            }
        }
        if (world.getClass() == Home.class) {
            if (getX() > 1240 && getY() > 270 && getY() < 370) {
                world.stopped();
                Greenfoot.setWorld(new MazePath(this, false));
            }
        }
        if (world.getClass() == MazePath.class) {
            if (getX() > 1232) {
                world.stopped();
                if (!((MazePath)getWorld()).isMaze()) {
                    Greenfoot.setWorld(new MazePath(this, true));
                } else {
                    Greenfoot.setWorld(new School(this));
                }
            }
        }
        if (world.getClass() == School.class) {
            if (getX() > 929 && getY() > 290 && getX() < 950 && getY() < 300) {
                world.stopped();
                Greenfoot.setWorld(new VillageClass(this));
            }
        }
    }
    
    private void handlePopUp() {
        World world = this.getWorld();
        boolean isOpen = true;
        if (world.getClass() == CityClass.class || world.getClass() == VillageClass.class) {
            List<Dialog> dialogs = world.getObjects(Dialog.class);
            List<BoardCollision> boardCollisions = world.getObjects(BoardCollision.class);
            
            if (dialogs != null && !dialogs.isEmpty()) {
                for (Dialog dialog : dialogs) {
                    isOpen = dialog.isOpen();
                }
            } else if (boardCollisions != null && !boardCollisions.isEmpty()) {
                for (BoardCollision boardC : boardCollisions) {
                    isOpen = boardC.isOpen();
                }
            }
        } else {
            List<Dialog> dialogList = world.getObjects(Dialog.class);
            if (dialogList != null && !dialogList.isEmpty()) {
                Dialog dialog = dialogList.get(0);
                if (dialog != null) {
                    isOpen = dialog.isOpen();
                }
            }
        }
        
        if (Greenfoot.isKeyDown("f") || isOpen) {
            popUpOpen = true;
        } else if (!isOpen) {
            popUpOpen = false;
        }
    }
    
    private void handleTutorial() {
        World world = this.getWorld();
        if (!(world.getClass() == CityClass.class)) {
            return;
        }
        CityClass currentWorld = (CityClass)getWorld();
        if (startFinished && !radioSpawned) {
            currentWorld.addObject(radio, 624, 288);
            currentWorld.prologueStop();
            currentWorld.radioStart();
            startFinished = true;
            radioSpawned = true;
        }
        
        if (!radio.isOpen()) {
            currentWorld.addObject(kepsek, 624, 288);
            currentWorld.radioStop();
            currentWorld.kepsekStart();
        }
        
        if (!kepsek.isOpen()) {
            currentWorld.addObject(silent, 624, 288);
            currentWorld.kepsekStop();
        }
        
        if (!silent.isOpen()) {
            currentWorld.addObject(kaget, 624, 288);
            currentWorld.kagetStart();
        }
        
        if (!kaget.isOpen()) {
            currentWorld.stopped();
            setLocation(1046, 99);
        }
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public void setTutorialFinished(boolean f) {
        startFinished = f;
    }
}
