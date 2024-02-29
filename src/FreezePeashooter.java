import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by Armin on 6/25/2016.
 */
public class FreezePeashooter extends Plant {
    private Timer shootTimer;

    public FreezePeashooter(GamePanel parent, int x, int y) {
        super(parent, x, y);
        shootTimer = new Timer(1000, (ActionEvent e) -> {
            // Check if the peashooter is not at the far edges of the lane
            if (getX() > 0 && getX() < 1000) {
                ArrayList<Zombie> zombies = getGp().getLaneZombies().get(y);
                if (!zombies.isEmpty()) {
                    // Get the positions of the first and last zombies in the lane
                    int firstZombieX = zombies.get(0).getPosX();
                    int lastZombieX = zombies.get(zombies.size() - 1).getPosX();

                    // Check if the first zombie is to the left or the last zombie is to the right of the peashooter
                    if (firstZombieX < getX() || lastZombieX > getX()) {
                        // Only create and add FreezePea
                        getGp().getLanePeas().get(y).add(new FreezePea(getGp(), y, 103 + this.getX() * 100));
                    }
                }
            }
        });
        shootTimer.start();
    }

    @Override
    public void stop() {
        shootTimer.stop();
    }
}
