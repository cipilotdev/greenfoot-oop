import greenfoot.*;

public class FalseButton extends Actor
{
    private Answer generator;

    public FalseButton(Answer generator) {
        this.generator = generator;
        GreenfootImage img = new GreenfootImage("SALAH", 58, Color.RED, new Color(0, 0, 0, 0));
        setImage(img);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            handleClick(); // RUBAH: Extract logic ke method terpisah (biar bisa dipanggil dari Teacher punch)
        }
    }
    
    // RUBAH: New public method untuk handle click dari mouse atau punch
    public void handleClick() {
        if (getWorld() instanceof VillageClass) {
            VillageClass world = (VillageClass) getWorld();
            boolean correct = generator.checkAnswer(false);
            
            /*if (correct) {
                world.incrementScore();
                world.getTimer().reset(); // bener reset timer
            } else {
                world.getHealthBar().reduceHealth(); // salah kurangin health
            }
            
            world.incrementQuestionCount();
            
            if (!world.checkWinCondition()) {
                Greenfoot.delay(20); // jeda
                world.generateNewQuestion();
            }*/
        }
    }
}
