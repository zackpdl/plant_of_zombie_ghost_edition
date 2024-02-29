import java.awt.*;

/**
 * Created by Armin on 6/28/2016.
 */
public class FreezePea extends Pea {
    private boolean isDetectZombieToLeft = false;
    private static int totalZombiesKilled = 0;// Flag to track if a zombie is detected to the left

    public FreezePea(GamePanel parent, int lane, int startX) {
        super(parent, lane, startX);
    }

    @Override
    public void advance() {
        Rectangle pRect = new Rectangle(getPosX(), 130 + getMyLane() * 120, 28, 28);

        for (int i = 0; i < gp.getLaneZombies().get(getMyLane()).size(); i++) {
            Zombie z = gp.getLaneZombies().get(getMyLane()).get(i);
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + getMyLane() * 120, 400, 120);
            if (pRect.intersects(zRect)) {
                // Check if the zombie is to the left
                if (z.getPosX() < getPosX()) {
                    setDetectZombieToLeft(true); // Set flag to true if a zombie is detected to the left
                    z.setHealth(z.getHealth() - 500);
                    z.slow();
                   
                    if (z.getHealth() <= 0) {
                        System.out.println("ZOMBIE DIE");
                        GamePanel.setProgress(10);
                        gp.getLaneZombies().get(getMyLane()).remove(i);
                        totalZombiesKilled++; 
                        gp.zombieDied();  // Call the method to update total zombie count
// Increment the total count

                      

            
                    }
                    gp.getLanePeas().get(getMyLane()).remove(this); // Remove the pea at collision
                    return;
                } else {
                    z.setHealth(z.getHealth() - 100); // Damage the zombie even if it's on the right side
                    if (z.getHealth() <= 0) {
                        System.out.println("ZOMBIE DIE");
                        GamePanel.setProgress(10);
                        gp.getLaneZombies().get(getMyLane()).remove(i);
                        
                    }
                    gp.getLanePeas().get(getMyLane()).remove(this); // Remove the pea at collision
                    return;
                }
            }
        }

        if (isDetectZombieToLeft()) {
            setPosX(getPosX() - 15); // Move the pea to the left
        } else {
            setPosX(getPosX() + 15); // Move the pea to the right
        }
    }

    public boolean isDetectZombieToLeft() {
        return isDetectZombieToLeft;
    }

    public void setDetectZombieToLeft(boolean detectZombieToLeft) {
        this.isDetectZombieToLeft = detectZombieToLeft;
    }

    public static int getTotalZombiesKilled() {
        return totalZombiesKilled;
    }

}

    

