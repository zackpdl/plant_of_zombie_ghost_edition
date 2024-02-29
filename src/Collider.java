import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Armin on 6/25/2016.
 */
public class Collider extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
	private ActionListener al;

    public Collider() {
        //setBorder(new LineBorder(Color.RED));
        setOpaque(false);
        addMouseListener(this);
        //setBackground(Color.green);
        setSize(100, 120);
    }

    public Plant assignedPlant;
    public Zombie collidedZombie;
    
    public Zombie getCollidedZombie() {
		return collidedZombie;
	}

	public void setCollidedZombie(Zombie collidedZombie) {
		this.collidedZombie = collidedZombie;
	}

	public Plant getAssignedPlant() {
    	return assignedPlant;
    }

    public void setPlant(Plant p) {
        assignedPlant = p;
    }

    public void removePlant() {
        assignedPlant.stop();
        assignedPlant = null;
    }

    public boolean isInsideCollider(int tx) {
        return (tx > getLocation().x) && (tx < getLocation().x + 100);
    }

    public void setAction(ActionListener al) {
        this.al = al;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (al != null) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
