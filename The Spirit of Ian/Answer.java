import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GenerateA here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Answer extends Board
{
    /**
     * Act - do whatever the GenerateA wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    private int difficulty;
    private boolean isCorrect;
    private String op;
    private int a, b;
    private int correctAnswer;
    private int studentAnswer;
    private String questionText;

    public Answer(int difficulty) {
        this.difficulty = difficulty;
        questionText = generateExpression();

        GreenfootImage img = new GreenfootImage(questionText, 28, Color.WHITE, new Color(0, 0, 0, 0));
        setImage(img);
    }

    private String generateExpression() {
        String[] ops = {"+", "-", "*", "/"};
        op = ops[Greenfoot.getRandomNumber(ops.length)];

        int max;
        switch (difficulty) {
            case 0:
            case 1:
                max = 10;
                break;
            case 2:
                max = 50;
                break;
            case 3:
                max = 100;
                break;
            default:
                max = 10;
                break;
        }

        a = Greenfoot.getRandomNumber(max) + 1;
        b = Greenfoot.getRandomNumber(max) + 1;

        switch (op) {
            case "+":
                correctAnswer = a + b;
                break;
            case "-":
                correctAnswer = a - b;
                break;
            case "*":
                correctAnswer = a * b;
                break;
            case "/":
                while (b == 0 || a % b != 0) {
                    a = Greenfoot.getRandomNumber(max) + 1;
                    b = Greenfoot.getRandomNumber(max - 1) + 1;
                }
                correctAnswer = a / b;
                break;
        }

        int studentCorrect = Greenfoot.getRandomNumber(1);

        if (studentCorrect == 1) {
            studentAnswer = correctAnswer;
        } else {
            int offset;
            do {
                offset = Greenfoot.getRandomNumber(3) - 1;
            } while (offset == 0);
            studentAnswer = correctAnswer + offset;
        }

        isCorrect = (studentAnswer == correctAnswer);

        return a + " " + op + " " + b + " = " + studentAnswer;
    }

    public boolean checkAnswer(boolean teacherSaysCorrect) {
        return teacherSaysCorrect == isCorrect;
    }

    public String getQuestion() {
        return questionText;
    }

    public int getResult() {
        return correctAnswer;
    }

    public String getOp() {
        return op;
    }

    public void act()
    {
        // Add your action code here.
    }
}