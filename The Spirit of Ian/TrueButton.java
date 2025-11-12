import greenfoot.*;

public class TrueButton extends Actor
{
    private Answer generator;

    public TrueButton(Answer generator) {
        this.generator = generator;
        
        GreenfootImage img = new GreenfootImage("BENAR", 58, Color.GREEN, new Color(0, 0, 0, 0));
        setImage(img);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            handleClick(); // RUBAH: Extract logic ke method terpisah (biar bisa dipanggil dari Teacher punch)
        }
    }
    
    // RUBAH: New public method untuk handle click dari mouse atau punch
    public void handleClick() {
        boolean correct = generator.checkAnswer(true);

        /*if (getWorld() instanceof VillageClass) {
            VillageClass world = (VillageClass) getWorld();

            if (correct) {
                world.incrementScore();
                world.getTimer().reset();
            } else {
                world.getHealthBar().reduceHealth();
            }

            world.incrementQuestionCount();

            Greenfoot.delay(20);

            if (!world.checkWinCondition()) {
                world.generateNewQuestion();
            }
        }*/
    }
}
