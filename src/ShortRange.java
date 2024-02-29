import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShortRange extends Plant {
	private Timer shootTimer;
private static final int SHOOT_RANGE = 400; // The range until the defender detects attacker
private int health;


	public ShortRange(GamePanel parent, int x, int y) {
		super(parent, x, y);
        this.health = 5000; // Set initial health value
        shootTimer = new Timer(200, (ActionEvent e) -> {
       
            for (Zombie zombie : getGp().getLaneZombies().get(y)) {
                int zombieDistance = zombie.getPosX() - getX();
                if (zombieDistance > 0 && zombieDistance < SHOOT_RANGE) {
                    getGp().getLanePeas().get(y).add(new Axe(getGp(), y, 103 + this.getX() * 100));
                    break; // Only shoot one pea for the closest zombie
                }
            }
        });
        shootTimer.start();
    }

    @Override
    public void stop() {
        shootTimer.stop();
    }

    // Getter for health
    public int getHealth() {
        return health;
    }

    // Setter for health
    public void setHealth(int health) {
        this.health = health;
    }

	}


