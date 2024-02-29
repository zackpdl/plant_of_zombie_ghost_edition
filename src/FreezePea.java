import java.awt.*;

public class FreezePea extends Pea {
    private boolean isDetectZombieToLeft = false;
    private static int totalZombiesKilled = 0; // Flag to track if a zombie is detected to the left

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
                    setDetectZombieToLeft(true); // Set true if a zombie is detected to the left
                    z.setHealth(z.getHealth() - 500);
                    z.slow();

                    if (z.getHealth() <= 0) {
                        System.out.println("ZOMBIE DIE");
                        GamePanel.setProgress(10);
                        gp.getLaneZombies().get(getMyLane()).remove(i);
                        Pea.incrementTotalZombiesKilled();
                        System.out.println("Total zombies killed in FreezePea class: " + Pea.getTotalZombiesKilled());
                        gp.zombieDied();
                    }

                    gp.getLanePeas().get(getMyLane()).remove(this); // Remove the pea at collision
                    return;
                } else {
                    z.setHealth(z.getHealth() - 500); // Damage the zombie on the right side

                    if (z.getHealth() <= 0) {
                        System.out.println("ZOMBIE DIE");
                        GamePanel.setProgress(10);
                        Pea.incrementTotalZombiesKilled(); // Increment the total count
                        gp.getLaneZombies().get(getMyLane()).remove(i);
                        gp.zombieDied();

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
