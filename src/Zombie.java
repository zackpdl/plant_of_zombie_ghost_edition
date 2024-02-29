import javax.swing.*;

/**
 * Created by Armin on 6/25/2016.
 */
public class Zombie {

    private int health = 5000;
    private int speed = 1;

    private GamePanel gp;

    private int posX = 1000;
    private int myLane;
    private boolean isMoving = true;

    public Zombie(GamePanel parent, int lane) {
        this.gp = parent;
        myLane = lane;
    }
    public void reduceHealth(int amount) {
        health -= amount;
    }

    public void advance() {
        if (isMoving) {
        	 int movementSpeed = speed; // Default speed
             if (health < 200) { // If health is below 50% of the initial value (assuming initial is 1000)
                 movementSpeed *= 5; // Double the speed
             }
            boolean isCollides = false;
            Collider collided = null;
            for (int i = myLane * 9; i < (myLane + 1) * 9; i++) {
                if (gp.getColliders()[i].assignedPlant != null && gp.getColliders()[i].isInsideCollider(posX)) {
                    isCollides = true;
                    collided = gp.getColliders()[i];
                    break;
                }
            }
            if (!isCollides) {
                if (slowInt > 0) {
                    if (slowInt % 2 == 0) {
                        posX--;
                    }
                    slowInt--;
                } else {
                    posX -= movementSpeed;
                }
            } else {
            	Plant collidedPlant = collided.getAssignedPlant();
            	if (collidedPlant instanceof Sunflower) {
            	    Sunflower sunflower = (Sunflower) collidedPlant;
            	    sunflower.setHealth(sunflower.getHealth() - 100);
            	    if (sunflower.getHealth() <= 0) {
            	        gp.removeSunflower(collided.getX(), collided.getY()); 
            	        Zombie collidedZombie = collided.getCollidedZombie(); 
            	        System.out.println("Explosion triggered.");
            	   
            	        if (collidedZombie != null) {
            	            collidedZombie.reduceHealth(500); 
            	        }
            	    }
            	} else {
            	    collidedPlant.setHealth(collidedPlant.getHealth() - 10);
            	    if (collidedPlant.getHealth() <= 0) {
            	        collided.removePlant(); 
            	    }
            	}
}

            }
            if (posX < 0) {
                isMoving = false;
                JOptionPane.showMessageDialog(gp, "ZOMBIES ATE YOUR BRAIN !" + '\n' + "Starting the level again");
                GameWindow.gw.dispose();
                GameWindow.gw = new GameWindow();
            }
        }
    

  


    int slowInt = 0;

    public void slow() {
        slowInt = 1000;
    }

    public static Zombie getZombie(String type, GamePanel parent, int lane) {
        Zombie z = new Zombie(parent, lane);
        switch (type) {
            case "NormalZombie":
                z = new NormalZombie(parent, lane);
                break;
            case "ConeHeadZombie":
                z = new ConeHeadZombie(parent, lane);
                break;
        }
        return z;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public GamePanel getGp() {
        return gp;
    }

    public void setGp(GamePanel gp) {
        this.gp = gp;
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

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getSlowInt() {
        return slowInt;
    }

    public void setSlowInt(int slowInt) {
        this.slowInt = slowInt;
    }
}
