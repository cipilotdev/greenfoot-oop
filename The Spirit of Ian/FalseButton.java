import greenfoot.*;

public class FalseButton extends Actor
{
    private Answer generator;

    public FalseButton(Answer generator) {
        this.generator = generator;
        GreenfootImage img = new GreenfootImage("SALAH", 28, Color.RED, new Color(0, 0, 0, 0));
        setImage(img);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            boolean correct = generator.checkAnswer(false);
            if (correct) {
                System.out.println("Benar");
            } else {
                System.out.println("Salah");
            }

            Greenfoot.delay(10);
            Greenfoot.setWorld(new VillageClass(2));
        }
    }
}