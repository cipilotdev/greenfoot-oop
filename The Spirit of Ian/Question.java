import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GenerateQ here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Question extends Board
{
    /**
     * Act - do whatever the GenerateQ wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private int difficulty;
    private String questionText;
    private int correctAnswer;
    private String userInput = "";
    private String currentOp;
    private int missingPosition;
    
    public Question(int difficulty) {
        this.difficulty = difficulty;
        this.questionText = generateQuestion(difficulty);

        GreenfootImage img = new GreenfootImage(questionText, 28, Color.WHITE, new Color(0, 0, 0, 0));
        setImage(img);
    }
    
    public void handleInput(String value) {
        if (value.equals("backspace")) {
            if (userInput.length() > 0)
                userInput = userInput.substring(0, userInput.length() - 1);
        } 
        else if (value.equals("enter")) {
            checkAnswer();
        } 
        else {
            userInput += value;
        }

        updateImage();
    }
    
    private String generateQuestion(int diff) {
        String[] ops = {"+", "-", "*", "/"};
        currentOp = ops[Greenfoot.getRandomNumber(ops.length)];

        int min = 1;
        int max;

        switch (diff) {
            case 0:
            case 1: max = 10; break;
            case 2: max = 50; break;
            case 3: max = 100; break;
            default: max = 10;
        }

        int a = 0, b = 0, result = 0;

        switch (currentOp) {
            case "+":
                a = Greenfoot.getRandomNumber(max - min + 1) + min;
                b = Greenfoot.getRandomNumber(max - min + 1) + min;
                result = a + b;
                break;

            case "-":
                a = Greenfoot.getRandomNumber(max - min + 1) + min;
                b = Greenfoot.getRandomNumber(a - min + 1) + min;
                result = a - b;
                break;

            case "*":
                a = Greenfoot.getRandomNumber(max / 2 - min + 1) + min;
                b = Greenfoot.getRandomNumber(max / 2 - min + 1) + min;
                result = a * b;
                break;

            case "/":
                do {
                    b = Greenfoot.getRandomNumber((max / 2) - min + 1) + min;
                    result = Greenfoot.getRandomNumber((max / 2) - min + 1) + min;
                    a = b * result;
                } while (b == 0 || a > max);
                break;
        }

        missingPosition = Greenfoot.getRandomNumber(3);
        String q = "";

        switch (missingPosition) {
            case 0: 
                correctAnswer = a;
                q = "__ " + currentOp + " " + b + " = " + result; 
                break;
            case 1: 
                correctAnswer = b;
                q = a + " " + currentOp + " __ = " + result; 
                break;
            case 2: 
                correctAnswer = a;
                q = "__ " + currentOp + " __ = " + result; 
                break;
        }

        return q;
    }

    private void checkAnswer() {
        if (userInput.isEmpty()) return;
        
        int userAns = 0;
        try {
            userAns = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            userAns = -999;
        }

        if (userAns == correctAnswer) {
            System.out.println("Beanar");
            World world = getWorld();
            if (world != null) {
                world.addObject(new Question(difficulty), getX(), getY());
                world.removeObject(this);
            }
        } else {
            System.out.println("Salah");
        }

        userInput = "";
    }

    private void updateImage() {
        String display = questionText.replace("__", userInput.isEmpty() ? "__" : userInput);
        GreenfootImage img = new GreenfootImage(display, 28, Color.WHITE, new Color(0, 0, 0, 0));
        setImage(img);
    }

    public void act()
    {
        // Add your action code here.
    }
}