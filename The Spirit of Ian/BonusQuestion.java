import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A small true/false popup question asked by a student during a class event.
 * Shows "a op b = c" and the player answers BENAR (Y) or SALAH (N),
 * by keyboard or by clicking the buttons. (S is not used: it is the walk-down key.) After answering, BETUL/SALAH is shown
 * briefly and the popup reports isFinished().
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BonusQuestion extends Actor
{
    private static final int WIDTH = 560;
    private static final int HEIGHT = 300;
    private static final int BUTTON_W = 200;
    private static final int BUTTON_H = 60;
    private static final int BUTTON_Y = 215;                  // top edge of the buttons (image coordinates)
    private static final int BENAR_X = 70;                    // left edge of BENAR button
    private static final int SALAH_X = WIDTH - 70 - BUTTON_W; // left edge of SALAH button
    private static final long RESULT_DURATION = 1000;         // ms to show BETUL/SALAH

    private static final Color PANEL = new Color(20, 24, 40, 235);
    private static final Color BENAR_COLOR = new Color(40, 150, 70);
    private static final Color SALAH_COLOR = new Color(180, 50, 50);
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private final GreenfootSound correctSound = new GreenfootSound("Correct.mp3");
    private final GreenfootSound wrongSound = new GreenfootSound("Wrong.mp3");

    private String statement;
    private boolean statementTrue;

    private boolean answered = false;
    private boolean finished = false;
    private boolean correct = false;
    private long answeredAt;

    // Previous key states for edge detection
    private boolean prevY, prevN;

    public BonusQuestion(int difficulty) {
        generate(Math.max(1, difficulty));
        drawPanel();
    }

    private void generate(int difficulty) {
        int max = 10 + 10 * difficulty;
        int opCount = difficulty == 1 ? 2 : 3;
        int op = Greenfoot.getRandomNumber(opCount);
        int a, b, result;
        String opText;
        switch (op) {
            case 1:
                a = Greenfoot.getRandomNumber(max) + 1;
                b = Greenfoot.getRandomNumber(a) + 1;
                result = a - b;
                opText = "-";
                break;
            case 2:
                a = Greenfoot.getRandomNumber(4 + 3 * difficulty) + 2;
                b = Greenfoot.getRandomNumber(9) + 2;
                result = a * b;
                opText = "x";
                break;
            default:
                a = Greenfoot.getRandomNumber(max) + 1;
                b = Greenfoot.getRandomNumber(max) + 1;
                result = a + b;
                opText = "+";
                break;
        }

        int shown = result;
        statementTrue = Greenfoot.getRandomNumber(2) == 0;
        if (!statementTrue) {
            int offset = Greenfoot.getRandomNumber(5) + 1;
            if (Greenfoot.getRandomNumber(2) == 0 && result - offset >= 0) {
                shown = result - offset;
            } else {
                shown = result + offset;
            }
        }
        statement = a + " " + opText + " " + b + " = " + shown;
    }

    private void drawPanel() {
        GreenfootImage img = new GreenfootImage(WIDTH, HEIGHT);
        img.setColor(PANEL);
        img.fill();
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        img.drawRect(2, 2, WIDTH - 5, HEIGHT - 5);

        drawCentered(img, new GreenfootImage("Murid bertanya:", 30, new Color(255, 215, 90), TRANSPARENT), 22);
        drawCentered(img, new GreenfootImage(statement, 64, Color.WHITE, TRANSPARENT), 70);

        if (!answered) {
            drawCentered(img, new GreenfootImage("Benar atau salah?  (Y / N)", 24, Color.WHITE, TRANSPARENT), 160);
            drawButton(img, BENAR_X, "BENAR", BENAR_COLOR);
            drawButton(img, SALAH_X, "SALAH", SALAH_COLOR);
        } else {
            GreenfootImage result = correct
                ? new GreenfootImage("BETUL", 90, Color.GREEN, TRANSPARENT)
                : new GreenfootImage("SALAH", 90, Color.RED, TRANSPARENT);
            drawCentered(img, result, 170);
        }
        setImage(img);
    }

    private void drawButton(GreenfootImage img, int x, String text, Color color) {
        img.setColor(color);
        img.fillRect(x, BUTTON_Y, BUTTON_W, BUTTON_H);
        img.setColor(Color.WHITE);
        img.drawRect(x, BUTTON_Y, BUTTON_W - 1, BUTTON_H - 1);
        GreenfootImage label = new GreenfootImage(text, 32, Color.WHITE, TRANSPARENT);
        img.drawImage(label, x + (BUTTON_W - label.getWidth()) / 2, BUTTON_Y + (BUTTON_H - label.getHeight()) / 2);
    }

    private void drawCentered(GreenfootImage img, GreenfootImage text, int y) {
        img.drawImage(text, (WIDTH - text.getWidth()) / 2, y);
    }

    @Override
    protected void addedToWorld(World world) {
        // Prime key states so keys already held when the popup opens do not answer it
        prevY = Greenfoot.isKeyDown("y");
        prevN = Greenfoot.isKeyDown("n");
    }

    /**
     * Act - do whatever the BonusQuestion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (finished) {
            return;
        }
        if (answered) {
            if (System.currentTimeMillis() - answeredAt >= RESULT_DURATION) {
                finished = true;
            }
            return;
        }

        boolean y = Greenfoot.isKeyDown("y");
        boolean n = Greenfoot.isKeyDown("n");
        boolean saysTrue = y && !prevY;
        boolean saysFalse = n && !prevN;
        prevY = y;
        prevN = n;

        if (Greenfoot.mouseClicked(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                int localX = mouse.getX() - (getX() - WIDTH / 2);
                int localY = mouse.getY() - (getY() - HEIGHT / 2);
                if (localY >= BUTTON_Y && localY < BUTTON_Y + BUTTON_H) {
                    if (localX >= BENAR_X && localX < BENAR_X + BUTTON_W) {
                        saysTrue = true;
                    } else if (localX >= SALAH_X && localX < SALAH_X + BUTTON_W) {
                        saysFalse = true;
                    }
                }
            }
        }

        if (saysTrue && !saysFalse) {
            answer(true);
        } else if (saysFalse && !saysTrue) {
            answer(false);
        }
    }

    private void answer(boolean saysTrue) {
        correct = saysTrue == statementTrue;
        answered = true;
        answeredAt = System.currentTimeMillis();
        if (correct) {
            correctSound.play();
        } else {
            wrongSound.play();
        }
        drawPanel();
    }

    /**
     * True while the popup is in the world and not yet finished (the teacher should not move).
     */
    public boolean isOpen() {
        return getWorld() != null && !finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isCorrect() {
        return correct;
    }
}
