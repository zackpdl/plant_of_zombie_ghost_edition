import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.awt.*;


public class Peashooter extends Plant {

    public Timer shootTimer;
    private boolean inBerserkMode = false;

    public Peashooter(GamePanel parent, int x, int y) {
        super(parent, x, y);
        shootTimer = new Timer(getShootInterval(),  (ActionEvent e) -> {
       
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
    
    private void triggerLightning() {
  
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/lightning.gif"));
        JLabel label = new JLabel(icon);

        playLightningSound();

      
        int lightningX = getX() * 100 ;
        int lightningY = getY() * 120 ;

      
        JFrame lightningFrame = new JFrame();
        lightningFrame.setUndecorated(true);
        lightningFrame.setBackground(new Color(0, 0, 0, 0));
        lightningFrame.setLayout(null);
        lightningFrame.setSize(icon.getIconWidth(), icon.getIconHeight());

   
        GraphicsConfiguration gc = lightningFrame.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        int frameX = Math.min(bounds.x + bounds.width - lightningFrame.getWidth(), getGp().getLocationOnScreen().x + lightningX);
        int frameY = Math.min(bounds.y + bounds.height - lightningFrame.getHeight(), getGp().getLocationOnScreen().y + lightningY);
        lightningFrame.setLocation(frameX, frameY);

        // Set label size and position to fill frame
        label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

        // Add label to frame and display
        lightningFrame.getContentPane().add(label);
        lightningFrame.setVisible(true);

        // Dispose of frame after delay
        Timer timer = new Timer(600, (ActionEvent e) -> {
        	lightningFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    } 
    
    private void playLightningSound() {
        try {
            // Replace "path/to/thunder.wav" with the actual path to your sound file
            URL soundUrl = getClass().getResource("images/thunder.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private int getShootInterval() {
        int interval;
        
        // If health is below 400, shoot faster (reduce the interval)
        if (getHealth() < 400) {
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
        // Adjust the shooting interval
        this.shootTimer.setDelay(300);
   
    }
    public void exitBerserkMode() {
        this.inBerserkMode = false;
        this.shootTimer.setDelay(2000); // Revert to normal shooting interval
            }

    public boolean isInBerserkMode() {
        return this.inBerserkMode;
    }
    @Override
    public void setHealth(int health) {
        super.setHealth(health); // Calls the implementation from the Plant class

        // Check if health falls below threshold and not already in berserk mode
        if (getHealth() < 400 && !isInBerserkMode()) {
            enterBerserkMode();
            System.out.println("Peashooter at (" + getX() + "," + getY() + ") is now in BERSERK MODE!");
        } else if (getHealth() >= 400 && isInBerserkMode()) {
            exitBerserkMode();
            System.out.println("Peashooter at (" + getX() + "," + getY() + ") has exited berserk mode.");
        } if (health<=0) {
        	triggerLightning();
        }
    }


    public void explode() { // to use when switching assiggnedPlant and removeSunflower to Peashooter.
    	Zombie[] zombies = getGp().getLaneZombies().get(getY()).toArray(new Zombie[0]); // Get zombies on the current lane
        for (Zombie zombie : zombies) {
            zombie.setHealth(zombie.getHealth() - 500);

        }
    }
}
