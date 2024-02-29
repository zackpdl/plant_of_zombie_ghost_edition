import java.awt.*;

public class Pea {

    private static int totalZombiesKilled = 0;
    private int posX;
    protected GamePanel gp;
    private int myLane;

    public Pea(GamePanel parent, int lane, int startX) {
        this.gp = parent;
        this.myLane = lane;
        posX = startX;
    }

    public void advance() {
        Rectangle pRect = new Rectangle(posX, 130 + myLane * 120, 28, 28);
        for (int i = 0; i < gp.getLaneZombies().get(myLane).size(); i++) {
            Zombie z = gp.getLaneZombies().get(myLane).get(i);
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + myLane * 120, 400, 120);
            if (pRect.intersects(zRect)) {
                z.setHealth(z.getHealth() - 500);
     
                if (z.getHealth() < 0) {
                    System.out.println("ZOMBIE DIED");
                    gp.getLaneZombies().get(myLane).remove(i);

                    GamePanel.setProgress(10);
                    totalZombiesKilled++;  // Increment the total count
                    gp.zombieDied();  // Call the method to update total zombie count
             

                }
               
               gp.getLanePeas().get(myLane).remove(this); //remove pea at collision
                return; 
            }
        }
        posX += 15; 
    }


    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getMyLane() {
        return myLane;
    }

    public void setMyLane(int myLane) {
        this.myLane = myLane;
    }

    protected static int getTotalZombiesKilled() {
        return totalZombiesKilled;
    }

    protected static void incrementTotalZombiesKilled() {
        totalZombiesKilled++;
    }
	
}
