import java.awt.Rectangle;

public class Axe extends Pea {
	private static final int damage = 1000;
	
    public Axe(GamePanel parent, int lane, int startX) {
        super(parent, lane, startX); // Call the constructor of the superclass (Pea)
        
    }

    @Override
    public void advance() {
        super.advance(); // Call the advance method of the superclass (Pea)
        for (int i = 0; i < gp.getLaneZombies().get(getMyLane()).size(); i++) {
            Zombie z = gp.getLaneZombies().get(getMyLane()).get(i);
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + getMyLane() * 120, 400, 120);
            if (getPosX() >= z.getPosX() && getPosX() <= z.getPosX() + zRect.width) {
                z.setHealth(z.getHealth() - damage); // Using DAMAGE_AMOUNT constant
                if (z.getHealth() <= 0) {
                    System.out.println("ZOMBIE DIED");
                    gp.getLaneZombies().get(getMyLane()).remove(i);
                    Pea.incrementTotalZombiesKilled();
                    gp.zombieDied();
                    GamePanel.setProgress(10);
                 
                }
                gp.getLanePeas().get(getMyLane()).remove(this);
                return;
            }
        }
    }


}
