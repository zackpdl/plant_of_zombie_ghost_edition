import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Armin on 6/25/2016.
 */
public class Peashooter extends Plant {

    public Timer shootTimer;
    private boolean inBerserkMode = false;

    public Peashooter(GamePanel parent, int x, int y) {
        super(parent, x, y);
        shootTimer = new Timer(getShootInterval(),  (ActionEvent e) -> {
            //System.out.println("SHOOT");
            if (getGp().getLaneZombies().get(y).size() > 0) {
                getGp().getLanePeas().get(y).add(new Pea(getGp(), y, 103 + this.getX() * 100));
            }
        });
        shootTimer.start();
    }

    @Override
    public void stop() {
        shootTimer.stop();
    }


    private int getShootInterval() {
        int interval;
        
        // If health is below 400, shoot faster (reduce the interval)
        if (getHealth() < 1000) {
            System.out.println("HEALTH IS LOW BERSERK MODE ON!!!!!!!");
            interval = 100; // Adjust the interval as needed
        } else {
            interval = 2000; // Default interval when health is 400 or above
        }
        System.out.println("Current health: " + getHealth() + ", Shoot Interval: " + interval);
        return interval;

    }


    public void enterBerserkMode() {
        this.inBerserkMode = true;
        // Adjust the shooting interval, damage, or other attributes
        this.shootTimer.setDelay(300); // For example, shoot faster
        this.shootTimer.restart(); // Restart the timer with the new delay
        // Additional changes as needed
    }


    public void exitBerserkMode() {
        this.inBerserkMode = false;
        // Revert any changes made when entering berserk mode
        this.shootTimer.setDelay(2000); // Revert to normal shooting interval
        // Additional reversions as needed
    }

    public boolean isInBerserkMode() {
        return this.inBerserkMode;
    }
    @Override
    public void setHealth(int health) {
        super.setHealth(health); // Calls the implementation from the Plant class

        // Check if health falls below threshold and not already in berserk mode
        if (getHealth() < 1000 && !isInBerserkMode()) {
            enterBerserkMode();
            System.out.println("Peashooter at (" + getX() + "," + getY() + ") is now in BERSERK MODE!");
        } else if (getHealth() >= 1000 && isInBerserkMode()) {
            exitBerserkMode();
            System.out.println("Peashooter at (" + getX() + "," + getY() + ") has exited berserk mode.");
        }
    }


    public void explode() { // to use when switching assiggnedPlant and removeSunflower to Peashooter.
    	Zombie[] zombies = getGp().getLaneZombies().get(getY()).toArray(new Zombie[0]); // Get zombies on the current lane
        for (Zombie zombie : zombies) {
            zombie.setHealth(zombie.getHealth() - 500);
        }
    }
}
