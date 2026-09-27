import greenfoot.*;
import java.util.List;

public class Teacher extends AnimatedSprites {
    private int oldX, oldY;
    private static final GreenfootImage teacher = new GreenfootImage("Teacher/standing/south.png");
    
    private static final int animationSpeed = 20;
    private static final int walkSpeed = 60;
    
    //Collider only around the feet, so turning into narrow corridors is forgiving
    public static final int COLLIDER_WIDTH = 18;
    public static final int COLLIDER_HEIGHT = 14;
    public static final int COLLIDER_OFFSET_Y = 13;
    
    private boolean popUpOpen = false;
    
    // Tutorial
    private Dialog tutor = new Dialog(55, 56, true);
    private Dialog radio = new Dialog(10, 12);
    private Dialog kepsek = new Dialog(13, 18, false);
    private Dialog silent = new Dialog(19, 19, false);
    private Dialog kaget = new Dialog(20, 23, false);
    // Main Game
    private Dialog kelas = new Dialog(32, 35, true);
    private Dialog endKelas = new Dialog(36, 36, true);
    private Dialog kelasD2 = new Dialog(37, 39, true);
    private Dialog endKelasD2 = new Dialog(40, 40, true);
    private Dialog kelasD3 = new Dialog(41, 43, true);
    private Dialog endKelasD3 = new Dialog(44, 44, true);
    private Dialog ujian = new Dialog(45, 46, true);
    private Dialog endUjian = new Dialog(47, 50, false);
    
    private boolean startFinished = false;
    private boolean radioSpawned = false;
    
    // Tutorial stages, advanced once each so sounds and dialogs change exactly one time
    private static final int STAGE_TUTOR = 0;
    private static final int STAGE_RADIO = 1;
    private static final int STAGE_KEPSEK = 2;
    private static final int STAGE_SILENT = 3;
    private static final int STAGE_KAGET = 4;
    private static final int STAGE_DONE = 5;
    private int tutorialStage = STAGE_TUTOR;
    private boolean kelasSpawned = false;
    private boolean overlayD2 = false;
    private boolean overlayD3 = false;
    private boolean animateD1 = false;
    private boolean kelasD2Spawned = false;
    private boolean animateD2 = false;
    private boolean animateD3 = false;
    private boolean animateUjian = false;
    private boolean isD1Complete = false;
    private boolean dayTransition = false;   //True while the screen fades to black between days
    
    //Spot in front of the village classroom door, where the teacher starts each day
    private static final int classDoorX = 980;
    private static final int classDoorY = 210;
    private boolean isD2Complete = false;
    private boolean isD3Complete = false;
    private boolean isExamComplete = false;
    
    private Overlay animationOverlay = new Overlay("full", 2);
    private int score;
    
    private int difficulty;
    
    public Teacher(int difficulty) {
        this.difficulty = difficulty;
        
        changeSpeed(walkSpeed, animationSpeed);
        setLayer(0, teacher);
        animations.put("walk", Animation.createAnimation(getSpriteSheet(), 9, 4, 9, 64, 64));
        primaryAnimation = animations.get("walk");
        
        loadAnimations();
        setImage(getCurrentFrame());
        
        setCollider(COLLIDER_WIDTH, COLLIDER_HEIGHT, 0, COLLIDER_OFFSET_Y);
    }    
    
    public void act() {
        handlePopUp();
        
        handleTutorial();
        
        handleGame();
        
        if (!popUpOpen) {
            handleInput();
        } else {
            freeze();
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
            if (getX() > 1045 && getY() < 100 && getX() < 1110 && radioSpawned) {
                world.stopped();
                //Greenfoot.setWorld(new VillageClass(this));
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
                MazePath maze = (MazePath)getWorld();
                if (!maze.isMaze()) {
                    world.stopped();
                    Greenfoot.setWorld(new MazePath(this, true));
                } else if (maze.atRightEnd()) {
                    world.stopped();
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
        boolean isOpen = false;
        
        for (Dialog dialog : world.getObjects(Dialog.class)) {
            if (dialog.isOpen()) {
                isOpen = true;
            }
        }
        for (BoardCollision boardC : world.getObjects(BoardCollision.class)) {
            if (boardC.isOpen()) {
                isOpen = true;
            }
        }
        for (BonusQuestion bonus : world.getObjects(BonusQuestion.class)) {
            if (bonus.isOpen()) {
                isOpen = true;
            }
        }
        if (world instanceof Game && ((Game)world).isCutscenePlaying()) {
            isOpen = true;
        }
        if (dayTransition) {
            isOpen = true;
        }
        
        popUpOpen = Greenfoot.isKeyDown("f") || isOpen;
    }
    
    private void handleTutorial() {
        World world = this.getWorld();
        if (!(world.getClass() == CityClass.class)) {
            return;
        }
        CityClass currentWorld = (CityClass)getWorld();
        
        switch (tutorialStage) {
            case STAGE_TUTOR:
                if (!currentWorld.isDialogOpen()) {
                    currentWorld.addObject(tutor, 624, 288);
                }
                if (startFinished && !radioSpawned) {
                    currentWorld.addObject(radio, 624, 288);
                    currentWorld.prologueStop();
                    currentWorld.radioStart();
                    radioSpawned = true;
                    tutorialStage = STAGE_RADIO;
                }
                break;
            case STAGE_RADIO:
                if (!radio.isOpen()) {
                    currentWorld.removeObject(radio);
                    currentWorld.radioStop();
                    currentWorld.kepsekStart();
                    currentWorld.addObject(kepsek, 624, 288);
                    tutorialStage = STAGE_KEPSEK;
                }
                break;
            case STAGE_KEPSEK:
                if (!kepsek.isOpen()) {
                    currentWorld.removeObject(kepsek);
                    currentWorld.kepsekStop();
                    currentWorld.addObject(silent, 624, 288);
                    tutorialStage = STAGE_SILENT;
                }
                break;
            case STAGE_SILENT:
                if (!silent.isOpen()) {
                    currentWorld.removeObject(silent);
                    currentWorld.kagetStart();
                    currentWorld.addObject(kaget, 624, 288);
                    tutorialStage = STAGE_KAGET;
                }
                break;
            case STAGE_KAGET:
                if (!kaget.isOpen()) {
                    currentWorld.stopped();
                    setLocation(1046, 99);
                    tutorialStage = STAGE_DONE;
                }
                break;
            default:
                break;
        }
    }
    
    private void handleGame() {
        World world = this.getWorld();
        if (!(world.getClass() == VillageClass.class)) {
            return;
        }
        VillageClass currentWorld = (VillageClass)getWorld();
        
        if (!kelasSpawned) {
            currentWorld.addObject(animationOverlay, 624, 288);
            animationOverlay.setPlay(false);
            currentWorld.addObject(kelas, 624, 288);
            kelasSpawned = true;
        }
        
        if (isD1Complete) {
            currentWorld.removeObject(kelas);
            currentWorld.addObject(endKelas, 624, 288);
            isD1Complete = false;
        }

        if (!endKelas.isOpen() && !animateD1) {
            currentWorld.kelasStop();
            currentWorld.kelasD2Play();
            currentWorld.removeObject(endKelas);
            animationOverlay.setAnimateFull();
            animationOverlay.setPlay(true);
            animateD1 = true;
            dayTransition = true;
        }
        
        if (animateD1 && !kelasD2Spawned && animationOverlay.isFinished()) {
            returnToClassDoor();
            animationOverlay.setAnimateOut();
            currentWorld.addObject(kelasD2, 624, 288);
            kelasD2Spawned = true;
        }
        
        if (!kelasD2.isOpen()) {
            currentWorld.removeObject(kelasD2);
        }
        
        if (isD2Complete) {
            currentWorld.addObject(endKelasD2, 624, 288);
            isD2Complete = false;
        }
        
        if (!endKelasD2.isOpen() && !animateD2) {
            currentWorld.kelasD2Stop();
            currentWorld.kelasD3Play();
            currentWorld.removeObject(endKelasD2);
            animationOverlay.setAnimateFull();
            animateD2 = true;
            dayTransition = true;
        }
        
        if (animateD2 && !overlayD2 && animationOverlay.isFinished()) {
            returnToClassDoor();
            animationOverlay.setAnimateOut();
            currentWorld.addObject(kelasD3, 624, 288);
            overlayD2 = true;
        }

        if (!kelasD3.isOpen()) {
            currentWorld.removeObject(kelasD3);
        }

        if (isD3Complete) {
            currentWorld.addObject(endKelasD3, 624, 288);
            isD3Complete = false;
        }

        if (!endKelasD3.isOpen() && !animateD3) {
            currentWorld.kelasD3Stop();
            currentWorld.preUjianPlay();
            currentWorld.removeObject(endKelasD3);
            animationOverlay.setAnimateFull();
            animateD3 = true;
            dayTransition = true;
        }
        
        if (animateD3 && !overlayD3 && animationOverlay.isFinished()) {
            returnToClassDoor();
            animationOverlay.setAnimateOut();
            currentWorld.addObject(ujian, 624, 288);
            overlayD3 = true;
        }
        
        if (!ujian.isOpen()) {
            currentWorld.removeObject(ujian);
        }

        if (isExamComplete) {
            currentWorld.addObject(endUjian, 624, 288);
            isExamComplete = false;
            currentWorld.finalPlay();
        }

        if (!endUjian.isOpen() && !animateUjian) {
            animationOverlay.setAnimateFull();
            animateUjian = true;
        }
        
        if (animateUjian && animationOverlay.isFinished()) {
            Greenfoot.setWorld(new Win(score));
        }
    }
    
    /**
     * Puts the teacher back in front of the classroom door for the next day (called while the screen is black).
     */
    private void returnToClassDoor() {
        setLocation(classDoorX, classDoorY);
        storePosition();
        freeze();
        dayTransition = false;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public void setTutorialFinished(boolean f) {
        startFinished = f;
    }
    
    public void setPopUpOpen(boolean f) {
        this.popUpOpen = f;
    }

    public void setIsD1Complete(boolean isD1Complete) {
        this.isD1Complete = isD1Complete;
    }

    public void setIsD2Complete(boolean isD2Complete) {
        this.isD2Complete = isD2Complete;
    }

    public void setIsD3Complete(boolean isD3Complete) {
        this.isD3Complete = isD3Complete;
    }

    public void setIsExamComplete(boolean isExamComplete) {
        this.isExamComplete = isExamComplete;
    }
    
    public boolean getIsKelasDone() {
        return !kelas.isOpen();
    }
    
    public void setScore(int score) {
        this.score = score;
    }
}