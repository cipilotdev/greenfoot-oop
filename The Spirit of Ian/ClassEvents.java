import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Invisible manager for random classroom events during the lesson days (not the exam).
 * After a correct (non-final) answer there is a chance an event starts: the board is
 * suspended, the teacher walks to the student(s) and handles the event, then the board
 * is resumed.
 *
 * Events:
 *  - Sleeping student ("Zzz"): walk near and press SPACE. Timeout: HP -1.
 *  - Raised hand ("?"): walk near and press F to answer a bonus question. Timeout: no penalty.
 *  - Noisy class ("!!"): calm every noisy student with SPACE. Timeout: HP -1.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ClassEvents extends Actor
{
    private static final int TRIGGER_ONE_IN = 3;            // 1 in 3 chance per correct answer
    private static final int MAX_EVENTS_PER_DAY = 2;
    private static final int LAST_LESSON_DAY = 2;           // Time.getDay(): 0..2 = lesson days 1-3, 3 = exam

    // The teacher can walk between the desk columns, so "near" means standing right next to the student
    // (in the aisle beside the desk, or just in front of / behind it).
    private static final int NEAR_DISTANCE = 60;

    private static final long SLEEP_TIMEOUT = 20000; // 20 sec
    private static final long HAND_TIMEOUT = 20000; // 20 sec
    private static final long NOISY_TIMEOUT = 30000; // 30 sec
    private static final long RESULT_DURATION = 1500; // 1.5 sec

    private static final int NONE = -1;
    private static final int SLEEPING = 0;
    private static final int RAISED_HAND = 1;
    private static final int NOISY = 2;

    private static final int BUBBLE_OFFSET_Y = 45;
    private static final Color BUBBLE_BACK = new Color(0, 0, 0, 160);
    private static final Color TEXT_BACK = new Color(0, 0, 0, 160);
    private static final Color GOOD = new Color(120, 255, 120);
    private static final Color BAD = new Color(255, 110, 110);

    private int currentEvent = NONE;
    private boolean resultPhase = false;
    private long eventStart;
    private long resultStart;
    private boolean gameOver = false;

    private int trackedDay = -1;
    private int eventsToday = 0;

    private BoardCollision board;
    private List<Student> targets = new ArrayList<>();
    private List<Overlay> bubbles = new ArrayList<>();     // parallel to targets, null once solved
    private List<int[]> homePositions = new ArrayList<>(); // parallel to targets

    private Overlay instruction;
    private String instructionText = "";
    private int lastShownSeconds = -1;

    private BonusQuestion bonus;

    private boolean prevSpace = false;
    private boolean prevF = false;
    private int bobTick = 0;

    public ClassEvents() {
        setImage(new GreenfootImage(1, 1));
        getImage().setTransparency(0);
    }

    /**
     * Called by Question after a correct answer that is not the last one of the day.
     */
    public void onCorrectAnswer() {
        World world = getWorld();
        if (world == null || currentEvent != NONE || gameOver) {
            return;
        }
        List<BoardCollision> boards = world.getObjects(BoardCollision.class);
        if (boards.isEmpty()) {
            return;
        }
        BoardCollision bc = boards.get(0);
        if (!bc.isOpen() || bc.isSuspended()) {
            return;
        }
        int day = bc.getTimer().getDay();
        if (day < 0 || day > LAST_LESSON_DAY) {
            return;  // No events during the exam
        }
        for (Dialog d : world.getObjects(Dialog.class)) {
            if (d.isOpen()) {
                return;  // Do not interrupt story dialogs
            }
        }
        if (day != trackedDay) {
            trackedDay = day;
            eventsToday = 0;
        }
        if (eventsToday >= MAX_EVENTS_PER_DAY) {
            return;
        }
        if (Greenfoot.getRandomNumber(TRIGGER_ONE_IN) != 0) {
            return;
        }
        if (world.getObjects(Student.class).isEmpty()) {
            return;
        }
        eventsToday++;
        startEvent(bc, Greenfoot.getRandomNumber(3));
    }

    private void startEvent(BoardCollision bc, int type) {
        board = bc;
        board.suspend();

        currentEvent = type;
        resultPhase = false;
        eventStart = System.currentTimeMillis();
        lastShownSeconds = -1;
        bobTick = 0;

        List<Student> students = new ArrayList<>(getWorld().getObjects(Student.class));
        int count = 1;
        String bubbleText;
        switch (type) {
            case SLEEPING:
                bubbleText = "Zzz";
                instructionText = "Bangunkan murid yang tertidur! (SPACE)";
                break;
            case RAISED_HAND:
                bubbleText = "?";
                instructionText = "Ada murid yang bertanya! Hampiri dan tekan F";
                break;
            default:
                bubbleText = "!!";
                instructionText = "Kelas ribut! Tenangkan murid-murid (SPACE)";
                count = 2 + Greenfoot.getRandomNumber(2);
                break;
        }

        count = Math.min(count, students.size());
        for (int k = 0; k < count; k++) {
            Student s = students.remove(Greenfoot.getRandomNumber(students.size()));
            targets.add(s);
            homePositions.add(new int[] {s.getX(), s.getY()});
            Overlay bubble = new Overlay(new GreenfootImage(" " + bubbleText + " ", 28, Color.WHITE, BUBBLE_BACK));
            getWorld().addObject(bubble, s.getX(), s.getY() - BUBBLE_OFFSET_Y);
            bubbles.add(bubble);
        }

        instruction = new Overlay(new GreenfootImage(1, 1));
        getWorld().addObject(instruction, 624, 40);
        updateInstruction(getTimeout());
    }

    /**
     * Act - do whatever the ClassEvents wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        boolean spaceDown = Greenfoot.isKeyDown("space");
        boolean fDown = Greenfoot.isKeyDown("f");
        boolean spacePressed = spaceDown && !prevSpace;
        boolean fPressed = fDown && !prevF;
        prevSpace = spaceDown;
        prevF = fDown;

        if (currentEvent == NONE || gameOver || getWorld() == null) {
            return;
        }
        bobTick++;

        if (resultPhase) {
            if (System.currentTimeMillis() - resultStart >= RESULT_DURATION) {
                finishEvent();
            }
            return;
        }

        switch (currentEvent) {
            case SLEEPING:
                updateSleeping(spacePressed);
                break;
            case RAISED_HAND:
                updateRaisedHand(fPressed);
                break;
            case NOISY:
                updateNoisy(spacePressed);
                break;
        }
    }

    private void updateSleeping(boolean spacePressed) {
        animateBubbles(false);
        if (spacePressed && isTeacherNear(targets.get(0))) {
            solve(0);
            int points = 10 * getDifficulty();
            addBonus(points);
            showResult("Murid sudah bangun! +" + points, GOOD);
            return;
        }
        if (getElapsed() >= SLEEP_TIMEOUT) {
            if (penalize()) {
                return;
            }
            showResult("Murid tertidur terlalu lama! HP -1", BAD);
            return;
        }
        updateInstruction(SLEEP_TIMEOUT);
    }

    private void updateRaisedHand(boolean fPressed) {
        if (bonus != null) {
            if (bonus.isFinished()) {
                boolean ok = bonus.isCorrect();
                if (bonus.getWorld() != null) {
                    bonus.getWorld().removeObject(bonus);
                }
                bonus = null;
                if (ok) {
                    int points = 50 * getDifficulty();
                    addBonus(points);
                    showResult("Jawaban tepat! Bonus +" + points, GOOD);
                } else {
                    showResult("Jawaban kurang tepat.", BAD);
                }
            }
            return;
        }

        animateBubbles(false);
        if (fPressed && isTeacherNear(targets.get(0))) {
            solve(0);
            setInstruction("", Color.WHITE);
            bonus = new BonusQuestion(getDifficulty());
            getWorld().addObject(bonus, 624, 288);
            return;
        }
        if (getElapsed() >= HAND_TIMEOUT) {
            showResult("Murid itu menurunkan tangannya.", Color.WHITE);
            return;
        }
        updateInstruction(HAND_TIMEOUT);
    }

    private void updateNoisy(boolean spacePressed) {
        animateBubbles(true);
        if (spacePressed) {
            Teacher t = getTeacher();
            int nearest = -1;
            int bestDist = Integer.MAX_VALUE;
            for (int k = 0; k < targets.size(); k++) {
                if (bubbles.get(k) == null || !isTeacherNear(targets.get(k))) {
                    continue;
                }
                int[] home = homePositions.get(k);
                int dist = Math.abs(t.getX() - home[0]) + Math.abs(t.getY() - home[1]);
                if (dist < bestDist) {
                    bestDist = dist;
                    nearest = k;
                }
            }
            if (nearest >= 0) {
                solve(nearest);
                if (remainingTargets() == 0) {
                    int points = 10 * getDifficulty();
                    addBonus(points);
                    showResult("Kelas kembali tenang! +" + points, GOOD);
                    return;
                }
            }
        }
        if (getElapsed() >= NOISY_TIMEOUT) {
            if (penalize()) {
                return;
            }
            showResult("Kelas terlalu ribut! HP -1", BAD);
            return;
        }
        updateInstruction(NOISY_TIMEOUT);
    }

    /** Bob the bubbles up and down; optionally jitter the (unsolved) students. */
    private void animateBubbles(boolean jitter) {
        for (int k = 0; k < targets.size(); k++) {
            Overlay bubble = bubbles.get(k);
            if (bubble == null || bubble.getWorld() == null) {
                continue;
            }
            int[] home = homePositions.get(k);
            int bob = (int) Math.round(Math.sin((bobTick + k * 15) * 0.12) * 4);
            bubble.setLocation(home[0], home[1] - BUBBLE_OFFSET_Y + bob);

            Student s = targets.get(k);
            if (jitter && s.getWorld() != null) {
                s.setLocation(home[0] + Greenfoot.getRandomNumber(5) - 2, home[1] + Greenfoot.getRandomNumber(5) - 2);
            }
        }
    }

    /** Mark a target as handled: remove its bubble and restore its position. */
    private void solve(int k) {
        Overlay bubble = bubbles.get(k);
        if (bubble != null && bubble.getWorld() != null) {
            bubble.getWorld().removeObject(bubble);
        }
        bubbles.set(k, null);
        restorePosition(k);
    }

    private void restorePosition(int k) {
        Student s = targets.get(k);
        int[] home = homePositions.get(k);
        if (s.getWorld() != null && (s.getX() != home[0] || s.getY() != home[1])) {
            s.setLocation(home[0], home[1]);
        }
    }

    private int remainingTargets() {
        int count = 0;
        for (Overlay bubble : bubbles) {
            if (bubble != null) {
                count++;
            }
        }
        return count;
    }

    /** Remove all bubbles/restore students and show the outcome text for a moment. */
    private void showResult(String text, Color color) {
        for (int k = 0; k < targets.size(); k++) {
            solve(k);
        }
        setInstruction(text, color);
        resultPhase = true;
        resultStart = System.currentTimeMillis();
    }

    /** Clean up and give control back to the board. */
    private void finishEvent() {
        cleanup();
        currentEvent = NONE;
        resultPhase = false;
        if (board != null && board.getWorld() != null) {
            board.resume();
        }
        board = null;
    }

    private void cleanup() {
        for (int k = 0; k < targets.size(); k++) {
            solve(k);
        }
        targets.clear();
        bubbles.clear();
        homePositions.clear();
        if (instruction != null && instruction.getWorld() != null) {
            instruction.getWorld().removeObject(instruction);
        }
        instruction = null;
        if (bonus != null && bonus.getWorld() != null) {
            bonus.getWorld().removeObject(bonus);
        }
        bonus = null;
    }

    /**
     * Reduce HP by one. Returns true if that ended the game (world switched to GameOver),
     * in which case the caller must stop processing.
     */
    private boolean penalize() {
        World world = getWorld();
        if (world == null) {
            return true;
        }
        List<Health> healthList = world.getObjects(Health.class);
        if (healthList.isEmpty()) {
            return false;
        }
        Health health = healthList.get(0);
        health.reduceHealth();
        if (health.getHp() <= 0) {
            gameOver = true;
            return true;
        }
        return false;
    }

    private boolean isTeacherNear(Student s) {
        Teacher t = getTeacher();
        if (t == null || s == null) {
            return false;
        }
        int[] home = homePositions.get(targets.indexOf(s));
        int dx = t.getX() - home[0];
        int dy = t.getY() - home[1];
        return dx * dx + dy * dy <= NEAR_DISTANCE * NEAR_DISTANCE;
    }

    private Teacher getTeacher() {
        World world = getWorld();
        if (world == null) {
            return null;
        }
        List<Teacher> teachers = world.getObjects(Teacher.class);
        return teachers.isEmpty() ? null : teachers.get(0);
    }

    private int getDifficulty() {
        if (board != null && board.getQuestion() != null) {
            return Math.max(1, board.getQuestion().getDifficulty());
        }
        return 1;
    }

    private void addBonus(int points) {
        if (board != null && board.getQuestion() != null) {
            board.getQuestion().addBonus(points);
        }
    }

    private long getElapsed() {
        return System.currentTimeMillis() - eventStart;
    }

    private long getTimeout() {
        switch (currentEvent) {
            case SLEEPING: return SLEEP_TIMEOUT;
            case RAISED_HAND: return HAND_TIMEOUT;
            default: return NOISY_TIMEOUT;
        }
    }

    /** Instruction text with a countdown; only redrawn when the seconds value changes. */
    private void updateInstruction(long timeout) {
        int seconds = (int) Math.max(0, (timeout - getElapsed() + 999) / 1000);
        if (seconds != lastShownSeconds) {
            lastShownSeconds = seconds;
            setInstruction(instructionText + "  (" + seconds + "s)", Color.WHITE);
        }
    }

    private void setInstruction(String text, Color color) {
        if (instruction == null) {
            return;
        }
        if (text.isEmpty()) {
            instruction.setImage(new GreenfootImage(1, 1));
        } else {
            instruction.setImage(new GreenfootImage(" " + text + " ", 28, color, TEXT_BACK));
        }
    }

    /**
     * True while an event is running (board suspended).
     */
    public boolean isEventActive() {
        return currentEvent != NONE;
    }
}
