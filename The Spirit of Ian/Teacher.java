import greenfoot.*;
import java.util.List;

public class Teacher extends AnimatedSprites {
    private int oldX, oldY;
    private static final GreenfootImage teacher = new GreenfootImage("Teacher/standing/south.png");
    
    private static final int animationSpeed = 20;
    private static final int walkSpeed = 60;
    
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
    private boolean kelasSpawned = false;
    private boolean overlayD2 = false;
    private boolean overlayD3 = false;
    private boolean animateD1 = false;
    private boolean kelasD2Spawned = false;
    private boolean animateD2 = false;
    private boolean animateD3 = false;
    private boolean animateUjian = false;
    private boolean isD1Complete = false;
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
        
        setCollider(20, 28, 0, 7);
    }    
    
    public void act() {
        handlePopUp();
        
        handleTutorial();
        
        handleGame();
        
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
        if (!currentWorld.isDialogOpen()) {
            currentWorld.addObject(tutor, 624, 288);            
        }
        
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
        }
        
        if (animateD1 && !kelasD2Spawned) {
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
        }
        
        if (animateD2 && !overlayD2) {
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
        }
        
        if (animateD3 && !overlayD3 && animationOverlay.isFinished()) {
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