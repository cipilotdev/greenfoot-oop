import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A clickable answer button for multiple-choice questions.
 * Clicking it (or pressing its number key, handled by Question) submits its value.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class AnswerChoice extends Actor
{
    private static final int WIDTH = 240;
    private static final int HEIGHT = 80;
    private static final Color BACKGROUND = new Color(30, 60, 110, 230);
    private static final Color BORDER = Color.WHITE;
    private static final Color INDEX_COLOR = new Color(255, 215, 90);

    private int value;
    private int index;
    private Question question;

    public AnswerChoice(int value, int index, Question question) {
        this.value = value;
        this.index = index;
        this.question = question;
        setImage(createImage());
    }

    private GreenfootImage createImage() {
        GreenfootImage img = new GreenfootImage(WIDTH, HEIGHT);
        img.setColor(BACKGROUND);
        img.fill();
        img.setColor(BORDER);
        img.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        img.drawRect(1, 1, WIDTH - 3, HEIGHT - 3);

        GreenfootImage indexText = new GreenfootImage(index + ")", 28, INDEX_COLOR, new Color(0, 0, 0, 0));
        img.drawImage(indexText, 14, (HEIGHT - indexText.getHeight()) / 2);

        GreenfootImage valueText = new GreenfootImage(String.valueOf(value), 48, Color.WHITE, new Color(0, 0, 0, 0));
        img.drawImage(valueText, (WIDTH - valueText.getWidth()) / 2 + 12, (HEIGHT - valueText.getHeight()) / 2);
        return img;
    }

    /**
     * Act - do whatever the AnswerChoice wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this) && question != null) {
            question.checkAnswer(value);
        }
    }

    public int getValue() {
        return value;
    }
}
