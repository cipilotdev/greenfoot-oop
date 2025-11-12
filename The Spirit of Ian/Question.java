import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class GenerateQ here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Question extends Board {
    private String question;
    private int difficulty;
    private int answer;
    private int maxQuestion = 5;
    private int questionNum;
    private int score = 0;
    
    private static final double pressCooldown = 500000000.0;   //Cooldown (0,25sec) between pressing a key
    private double lastPressedKeyTime;
    
    private List<Integer> space = new ArrayList<>();
    private String userAnswer;
    
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
    
    private boolean add;
    private boolean correct = false;
    private boolean showingMessage = false;
    private boolean finished = false;
    
    private Color fore = Color.WHITE;
    private Color back = new Color(0, 0, 0, 0);
    
    private Time timer;
    
    public Question(int difficulty) {
        this.difficulty = difficulty;
        setImage(new GreenfootImage(1, 1));
        generateQuestion(difficulty);
        timer = new Time(difficulty);
        hide();
    }
    
    public void display() {
        clearQuestion();
        int i = 0;
        int spaceCounter = 0;
        World w = this.getWorld();
        for (char c : question.toCharArray()) {
            int x = 115 + i * 95 - spaceCounter * 20;
            
            if (c != ' ' && c != '+' && c != '-' && c != '*' && c != '/' && c != '=') {
                Numbers q = new Numbers(String.valueOf(c));
                w.addObject(q, x, 270);
                i++;
            } else if (c == ' ') {
                GreenfootImage q = new GreenfootImage("_", 96, fore, back);
                w.addObject(new Numbers(q), x, 270);
                i++;
                spaceCounter++;
                createSpace(x);
            } else {
                GreenfootImage q = new GreenfootImage(String.valueOf(c), 80, fore, back);
                w.addObject(new Numbers(q), x, 270);
                i++;
                spaceCounter++;
            }
        }
    }
    
    public void clearQuestion() {
        World w = this.getWorld();
        clearUserAnswer();
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
        if (userAnswer != null && userAnswer.length() > 0 && space.size() > 0) {
            for (int i = 0; i < userAnswer.length() && i < space.size(); i++) {
                w.removeObjects(w.getObjectsAt(space.get(i), 270, Numbers.class));
            }
        }
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
        questionNum++;
    }
    
    public void show() {
        getImage().setTransparency(255);
        opt0.getImage().setTransparency(255);
        opt1.getImage().setTransparency(255);
        opt2.getImage().setTransparency(255);
        opt3.getImage().setTransparency(255);
        opt4.getImage().setTransparency(255);
        opt5.getImage().setTransparency(255);
        opt6.getImage().setTransparency(255);
        opt7.getImage().setTransparency(255);
        opt8.getImage().setTransparency(255);
        opt9.getImage().setTransparency(255);
        opt.getImage().setTransparency(255);
        timer.getImage().setTransparency(255);
        timer.start();
        display();
    }
    
    public void hide() {
        // Clear question and user answer from world before hiding
        if (finished) {
            clearQuestion();
            timer.stop();
        }
        
        getImage().setTransparency(0);
        opt0.getImage().setTransparency(0);
        opt1.getImage().setTransparency(0);
        opt2.getImage().setTransparency(0);
        opt3.getImage().setTransparency(0);
        opt4.getImage().setTransparency(0);
        opt5.getImage().setTransparency(0);
        opt6.getImage().setTransparency(0);
        opt7.getImage().setTransparency(0);
        opt8.getImage().setTransparency(0);
        opt9.getImage().setTransparency(0);
        opt.getImage().setTransparency(0);
        timer.getImage().setTransparency(0);
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
        world.addObject(timer, 1150, 55);
        add = true;
    }

    public void act()
    {
        if (!add) {
            addToWorld();
        }
        
        double i = System.nanoTime();
        if (Greenfoot.isKeyDown("enter") && !showingMessage) {
            World w = this.getWorld();
            if (Integer.parseInt(userAnswer) == answer) {
                GreenfootImage correctImage = new GreenfootImage("BETUL", 160, Color.GREEN, back);
                w.addObject(new Overlay(correctImage), 400, 270);
                correct = true;
                score += timer.getTime() * difficulty;
                lastPressedKeyTime = i;
                showingMessage = true;
            } else {
                GreenfootImage correctImage = new GreenfootImage("SALAH", 160, Color.RED, back);
                w.addObject(new Overlay(correctImage), 400, 270);
                score -= 10 * difficulty;
                lastPressedKeyTime = i;
                showingMessage = true;
            }
        }
        
        if(!finished && showingMessage && i - lastPressedKeyTime >= pressCooldown) {
            World w = this.getWorld();
            w.removeObjects(w.getObjectsAt(400, 270, Overlay.class));
            if (correct) {
                updateQuestion();
                timer.reset();
                display();
                correct = false;
            } else {
                clearUserAnswer();
                display();
                userAnswer = "";
            }
            showingMessage = false;
        }
    }
    
    private void updateQuestion() {
        if (questionNum <= maxQuestion) {
            clearQuestion(); // This now properly clears both user answer and question
            space.clear(); // Clear the old space positions
            userAnswer = null; // Reset user answer for new question
            generateQuestion(difficulty);
        } else {
            timer.reset();
            if (timer.getDay() == 6) {
                Greenfoot.setWorld(new Win(score));
            } else {
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
        if (!num.equals("backspace")) {
            if (userAnswer == null) {
                userAnswer = "";
                clearUserAnswer();
            }
            if (userAnswer.length() < space.size()) {
                userAnswer += num;
                Numbers newNum = new Numbers(num, false);
                getWorld().addObject(newNum, space.get(userAnswer.length() - 1), 270);
            }
        } else {
            if (userAnswer != null && userAnswer.length() > 0) {
                List<Numbers> numbersAtPos = getWorld().getObjectsAt(space.get(userAnswer.length() - 1), 270, Numbers.class);
                if (!numbersAtPos.isEmpty()) {
                    getWorld().removeObject(numbersAtPos.get(0));
                    userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
                }
            }
        }
    }
    
    public boolean isFinished() {
        return finished;
    }
}