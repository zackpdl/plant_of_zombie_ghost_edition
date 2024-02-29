import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Sunflower extends Plant {

    private Timer sunProduceTimer;

    public Sunflower(GamePanel parent, int x, int y) {
        super(parent, x, y);
        sunProduceTimer = new Timer(15000, (ActionEvent e) -> {
            Sun sta = new Sun(getGp(), 60 + x * 100, 110 + y * 120, 130 + y * 120);
            getGp().getActiveSuns().add(sta);
            getGp().add(sta, 1);
        });
        sunProduceTimer.start();
    }

    public void explode() {
        displayJumpscare();

        Zombie[] zombies = getGp().getLaneZombies().get(getY()).toArray(new Zombie[0]);
        for (Zombie zombie : zombies) {
            zombie.setHealth(zombie.getHealth() - 100);
        }
    }

    private void displayJumpscare() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/scare.gif"));
        JLabel label = new JLabel(icon);

        int jumpscareX = getX() * 100;
        int jumpscareY = getY() * 120;

        JFrame jumpscareFrame = new JFrame();
        jumpscareFrame.setUndecorated(true);
        jumpscareFrame.setBackground(new Color(0, 0, 0, 0));
        
        // Set layout to null to allow manual positioning and sizing
        jumpscareFrame.setLayout(null);

        // Set the size of the frame to match the size of the image
        jumpscareFrame.setSize(icon.getIconWidth(), icon.getIconHeight());

        // Get the screen bounds
        GraphicsConfiguration gc = jumpscareFrame.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();

        // Calculate the desired position within the screen bounds
        int frameX = Math.min(bounds.x + bounds.width - jumpscareFrame.getWidth(), getGp().getLocationOnScreen().x + jumpscareX);
        int frameY = Math.min(bounds.y + bounds.height - jumpscareFrame.getHeight(), getGp().getLocationOnScreen().y + jumpscareY);

        // Set the location of the jumpscare frame
        jumpscareFrame.setLocation(frameX, frameY);

        // Set the size and position of the label to fill the frame
        label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

        jumpscareFrame.getContentPane().add(label);
        jumpscareFrame.setVisible(true);

        Timer timer = new Timer(600, (ActionEvent e) -> {
            jumpscareFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }


}