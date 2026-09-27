import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class GenerateQ here.
 *
 * Answers can be entered by clicking the on-screen keypad or by typing on the
 * keyboard (digits, backspace, enter). Some questions are multiple choice and are
 * answered by clicking an AnswerChoice (or pressing 1/2/3).
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Question extends Board {
    private String question;
    private int difficulty;
    private int answer;
    private int maxQuestion = 4;
    private static final int EXAM_QUESTIONS = 10;              // Questions on the last day (exam)
    private static final int EXAM_DAY = 3;                     // Time.getDay() during the exam
    private int questionNum;
    private int score = 0;
    
    private static final double pressCooldown = 500000000.0;   //Cooldown (0,25sec) between pressing a key
    private double lastPressedKeyTime;
    
    private static final int MULTIPLE_CHOICE_CHANCE = 30;      // % chance a question is multiple choice
    private static final int[] CHOICE_X = {300, 624, 948};
    private static final int CHOICE_Y = 482;
    
    private List<Integer> space = new ArrayList<>();
    private String userAnswer;
    private List<Numbers> typedNumbers = new ArrayList<>();
    
    private Numbers opt0 = new Numbers("0");
    private Numbers opt1 = new Numbers("1");
    private Numbers opt2 = new Numbers("2");
    private Numbers opt3 = new Numbers("3");
    private Numbers opt4 = new Numbers("4");
    private Numbers opt5 = new Numbers("5");
    private Numbers opt6 = new Numbers("6");
    private Numbers opt7 = new Numbers("7");
    private Numbers opt8 = new Numbers("8");
    private Numbers opt9 = new Numbers("9");
    private Numbers opt = new Numbers("backspace");
    private Numbers[] keypad = {opt0, opt1, opt2, opt3, opt4, opt5, opt6, opt7, opt8, opt9, opt};
    
    private boolean add;
    private boolean correct = false;
    private boolean showingMessage = false;
    private boolean finished = false;
    private boolean shown = false;
    
    // Multiple choice
    private boolean multipleChoice = false;
    private List<AnswerChoice> choices = new ArrayList<>();
    
    // Keyboard edge detection (previous key states)
    private boolean[] digitDown = new boolean[10];
    private boolean backspaceDown = false;
    private boolean enterDown = false;
    
    private Color fore = Color.WHITE;
    private Color back = new Color(0, 0, 0, 0);
    
    private static final GreenfootSound correctSound = new GreenfootSound("Correct.mp3");
    private static final GreenfootSound wrongSound = new GreenfootSound("Wrong.mp3");
    private static final GreenfootImage wrongImage = new GreenfootImage("SALAH", 160, Color.RED, new Color(0, 0, 0, 0));
    private static final GreenfootImage correctImage = new GreenfootImage("BETUL", 160, Color.GREEN, new Color(0, 0, 0, 0));
    
    private Overlay correctOverlay = new Overlay(correctImage);
    private Overlay wrongOverlay = new Overlay(wrongImage);
    
    public Question(int difficulty) {
        this.difficulty = difficulty;
        setImage(new GreenfootImage(1, 1));
        generateQuestion(difficulty);
        hide();
    }
    
    public void display() {
        clearQuestion();
        space.clear();
        userAnswer = "";
        int i = 0;
        int spaceCounter = 0;
        World w = this.getWorld();
        for (char c : question.toCharArray()) {
            int x = 115 + i * 95 - spaceCounter * 20;
            Numbers q = new Numbers(String.valueOf(c), false);
            i++;
            if (c == ' ') {
                w.addObject(q, x, 300);
                createSpace(x);
            } else {
                w.addObject(q, x, 270);
            }
            
            if (!(c != ' ' && c != '+' && c != '-' && c != '*' && c != '/' && c != '=')) {
                spaceCounter++;
            }
        }
        
        setKeypadVisible(!multipleChoice);
        if (multipleChoice) {
            spawnChoices();
        }
    }
    
    public void clearQuestion() {
        World w = this.getWorld();
        if (w == null) {
            return;
        }
        clearUserAnswer();
        removeChoices();
        int j = 0;
        int spaceCounter = 0;
        for (char c : question.toCharArray()) {
            int x = 115 + j * 95 - spaceCounter * 20;
            w.removeObjects(w.getObjectsAt(x, 270, Numbers.class));
            if (c != ' ' && c != '+' && c != '-' && c != '*' && c != '/' && c != '=') {
                j++;
            } else if (c == ' ') {
                j++;
                spaceCounter++;
            } else {
                j++;
                spaceCounter++;
            }
        }
    }
    
    public void clearUserAnswer() {
        World w = this.getWorld();
        if (w == null) {
            return;
        }
        if (userAnswer != null && userAnswer.length() > 0 && space.size() > 0) {
            for (int i = 0; i < userAnswer.length() && i < space.size(); i++) {
                w.removeObjects(w.getObjectsAt(space.get(i), 270, Numbers.class));
            }
        }
        w.removeObjects(typedNumbers);
        typedNumbers.clear();
    }
    
    private void generateQuestion(int difficulty) {
        String[] operators = {"+", "-", "*", "/"};
        int limit;
        int max;
        int min;
        switch (difficulty) {
            case 1:
                min = 1; max = 20; limit = 2;
                break;
            case 2:
                min = 15; max = 50; limit = 3;
                break;
            case 3:
                min = 30; max = 90; limit = 4;
                break;
            default:
                min = 1; max = 90; limit = 4;
        }
        String currentOperator = operators[Greenfoot.getRandomNumber(limit)];
        
        int a = 0, b = 0, result = 0;
        switch (currentOperator) {
            case "+":
                a = Greenfoot.getRandomNumber(max - min) + min;
                b = Greenfoot.getRandomNumber(max - min) + min;
                result = a + b;
                break;
            case "-":
                a = Greenfoot.getRandomNumber(max - min) + max;
                b = Greenfoot.getRandomNumber(max - min);
                result = a - b;
                break;
            case "*":
                a = Greenfoot.getRandomNumber(max / 2 - min) + min;
                b = Greenfoot.getRandomNumber(max / 2 - min) + min;
                result = a * b;
                break;
            case "/":
                result = Greenfoot.getRandomNumber(max / 5 - min / 5) + min / 5;
                b = Greenfoot.getRandomNumber(max / 5 - min / 5) + min / 5;
                a = b * result;
                break;
        }
        
        int missingPosition = Greenfoot.getRandomNumber(3);
        String q = "";
        int correctAnswer = 0;
        switch (missingPosition) {
            case 0:
                correctAnswer = a;
                q = " ".repeat(String.valueOf(a).length()) + currentOperator + b + "=" + result;
                break;
            case 1:
                correctAnswer = b;
                q = a + currentOperator + " ".repeat(String.valueOf(b).length()) + "=" + result;
                break;
            case 2:
                correctAnswer = result;
                q = a + currentOperator + b + "=" + " ".repeat(String.valueOf(result).length());
                break;
        }
        answer = correctAnswer;
        question = q;
        multipleChoice = Greenfoot.getRandomNumber(100) < MULTIPLE_CHOICE_CHANCE;
    }
    
    public void show() {
        shown = true;
        // Make sure the keypad is added before any AnswerChoice so the choices
        // are painted (and clicked) on top of the hidden keypad.
        if (!add && getWorld() != null) {
            addToWorld();
        }
        getImage().setTransparency(255);
        display();
    }
    
    public void hide() {
        // Clear question and user answer from world before hiding
        if (finished) {
            clearQuestion();
        }
        
        shown = false;
        getImage().setTransparency(0);
        setKeypadVisible(false);
        removeChoices();
        correctOverlay.getImage().setTransparency(0);
        wrongOverlay.getImage().setTransparency(0);
    }
    
    public void addToWorld() {
        World world = this.getWorld();
        world.addObject(opt0, 95, 482);
        world.addObject(opt1, 195, 482);
        world.addObject(opt2, 295, 482);
        world.addObject(opt3, 395, 482);
        world.addObject(opt4, 495, 482);
        world.addObject(opt5, 595, 482);
        world.addObject(opt6, 695, 482);
        world.addObject(opt7, 795, 482);
        world.addObject(opt8, 895, 482);
        world.addObject(opt9, 995, 482);
        world.addObject(opt, 1095, 482);
        world.addObject(correctOverlay, 400, 170);
        world.addObject(wrongOverlay, 400, 170);
        add = true;
    }
    
    public void act()
    {
        if (!add) {
            addToWorld();
        }
        
        double i = System.nanoTime();
        handleKeyboard();
        
        if(!finished && showingMessage && i - lastPressedKeyTime >= pressCooldown) {
            World w = this.getWorld();
            correctOverlay.getImage().setTransparency(0);
            wrongOverlay.getImage().setTransparency(0);
            showingMessage = false;
            if (correct) {
                questionNum++;
                updateQuestion();
                Time timer = getTimer();
                if (timer != null) {
                    timer.reset();
                }
                display();
                correct = false;
                if (!finished) {
                    notifyClassEvents();
                }
            } else {
                clearUserAnswer();
                display();
                userAnswer = "";
            }
        }
    }
    
    /**
     * Check a submitted answer (from the Enter key or an AnswerChoice).
     */
    public void checkAnswer(int value) {
        if (!shown || showingMessage || finished) {
            return;
        }
        double i = System.nanoTime();
        if (value == answer) {
            correctOverlay.getImage().setTransparency(255);
            correct = true;
            Time timer = getTimer();
            if (timer != null) {
                score += timer.getTime() * difficulty;
            }
            lastPressedKeyTime = i;
            showingMessage = true;
            correctSound.play();
        } else {
            wrongOverlay.getImage().setTransparency(255);
            score -= 10 * difficulty;
            lastPressedKeyTime = i;
            showingMessage = true;
            wrongSound.play();
        }
    }
    
    /**
     * Keyboard input with per-key edge detection, so holding a key only
     * counts once. Key states are tracked every act so that keys held while
     * the question is hidden do not fire when it becomes visible.
     */
    private void handleKeyboard() {
        for (int d = 0; d <= 9; d++) {
            boolean down = Greenfoot.isKeyDown(String.valueOf(d));
            if (down && !digitDown[d] && isInputActive()) {
                if (multipleChoice) {
                    if (d >= 1 && d <= choices.size()) {
                        checkAnswer(choices.get(d - 1).getValue());
                    }
                } else {
                    addAnswer(String.valueOf(d));
                }
            }
            digitDown[d] = down;
        }
        
        boolean backDown = Greenfoot.isKeyDown("backspace");
        if (backDown && !backspaceDown && isInputActive() && !multipleChoice) {
            removeLastDigit();
        }
        backspaceDown = backDown;
        
        boolean entDown = Greenfoot.isKeyDown("enter");
        if (entDown && !enterDown && isInputActive() && !multipleChoice) {
            if (userAnswer != null && userAnswer.length() > 0) {
                try {
                    checkAnswer(Integer.parseInt(userAnswer));
                } catch (NumberFormatException e) {
                    // Should not happen (only digits are typed); ignore invalid input
                }
            }
        }
        enterDown = entDown;
    }
    
    private boolean isInputActive() {
        return shown && !showingMessage && !finished && getWorld() != null;
    }
    
    /**
     * Lesson days end after maxQuestion + 1 correct answers, the exam after EXAM_QUESTIONS.
     */
    private boolean hasMoreQuestions() {
        Time timer = getTimer();
        if (timer != null && timer.getDay() == EXAM_DAY) {
            return questionNum < EXAM_QUESTIONS;
        }
        return questionNum <= maxQuestion;
    }
    
    private void updateQuestion() {
        if (hasMoreQuestions()) {
            clearQuestion();
            space.clear();
            userAnswer = null;
            generateQuestion(difficulty);
        } else {
            Time timer = getTimer();
            if (timer != null) {
                timer.reset();
                timer.stop();
                clearQuestion();
                space.clear();
                userAnswer = null;
                timer.setDay();
                finished = true;
            }
        }
    }
    
    private void createSpace(int x) {
        space.add(x);
    }
    
    public void addAnswer(String num) {
        if (!isInputActive() || multipleChoice) {
            return;
        }
        if (!num.equals("backspace")) {
            if (num.length() != 1 || !Character.isDigit(num.charAt(0))) {
                return;
            }
            if (userAnswer == null) {
                userAnswer = "";
                clearUserAnswer();
            }
            if (userAnswer.length() < space.size()) {
                userAnswer += num;
                Numbers newNum = new Numbers(num, false);
                getWorld().addObject(newNum, space.get(userAnswer.length() - 1), 270);
                typedNumbers.add(newNum);
            }
        } else {
            removeLastDigit();
        }
    }
    
    /**
     * Remove only the last typed digit (keyboard or keypad backspace).
     */
    private void removeLastDigit() {
        if (userAnswer == null || userAnswer.length() == 0 || typedNumbers.isEmpty()) {
            return;
        }
        Numbers last = typedNumbers.remove(typedNumbers.size() - 1);
        World w = getWorld();
        if (w != null) {
            w.removeObject(last);
        }
        userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
    }
    
    private void setKeypadVisible(boolean visible) {
        for (Numbers key : keypad) {
            key.getImage().setTransparency(visible ? 255 : 0);
            key.setClickable(visible);
        }
    }
    
    private void spawnChoices() {
        World w = getWorld();
        if (w == null) {
            return;
        }
        removeChoices();
        
        List<Integer> values = new ArrayList<>();
        values.add(answer);
        int spread = Math.max(5, answer / 5);
        int attempts = 0;
        while (values.size() < 3 && attempts < 50) {
            attempts++;
            int offset = Greenfoot.getRandomNumber(spread) + 1;
            int candidate = Greenfoot.getRandomNumber(2) == 0 ? answer + offset : answer - offset;
            if (candidate >= 0 && !values.contains(candidate)) {
                values.add(candidate);
            }
        }
        int fallback = answer + 1;
        while (values.size() < 3) {
            if (!values.contains(fallback)) {
                values.add(fallback);
            }
            fallback++;
        }
        
        // Shuffle using Greenfoot's random generator
        for (int k = values.size() - 1; k > 0; k--) {
            int r = Greenfoot.getRandomNumber(k + 1);
            int tmp = values.get(k);
            values.set(k, values.get(r));
            values.set(r, tmp);
        }
        
        for (int k = 0; k < values.size(); k++) {
            AnswerChoice choice = new AnswerChoice(values.get(k), k + 1, this);
            choices.add(choice);
            w.addObject(choice, CHOICE_X[k], CHOICE_Y);
        }
    }
    
    private void removeChoices() {
        World w = getWorld();
        if (w != null) {
            w.removeObjects(choices);
        }
        choices.clear();
    }
    
    private void notifyClassEvents() {
        World w = getWorld();
        if (w == null) {
            return;
        }
        List<ClassEvents> managers = w.getObjects(ClassEvents.class);
        if (!managers.isEmpty()) {
            managers.get(0).onCorrectAnswer();
        }
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public boolean isShown() {
        return shown;
    }
    
    public boolean isMultipleChoice() {
        return multipleChoice;
    }
    
    public void setFinished(boolean f) {
        this.finished = f;
        // Reset questionNum when resetting for next day
        if (!f) {
            questionNum = 0;
        }
    }
    
    private Time getTimer() {
        World world = getWorld();
        if (world != null) {
            List<BoardCollision> boards = world.getObjects(BoardCollision.class);
            if (!boards.isEmpty()) {
                return boards.get(0).getTimer();
            }
        }
        return null;
    }
    
    public int getScore() {
        return score;
    }
    
    /**
     * Add bonus points (e.g. from class events) to the score.
     */
    public void addBonus(int points) {
        score += points;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        questionNum = 0;
        generateQuestion(difficulty);
    }
}
