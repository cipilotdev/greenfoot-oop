import greenfoot.*;

public class TrueButton extends Actor
{
    private Answer generator;

    public TrueButton(Answer generator) {
        this.generator = generator;
        GreenfootImage img = new GreenfootImage("BENAR", 28, Color.GREEN, new Color(0, 0, 0, 0));
        setImage(img);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            boolean correct = generator.checkAnswer(true);
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