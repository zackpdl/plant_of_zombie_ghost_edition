import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Lightning extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Lightning(int x, int y) {
        setIcon(new ImageIcon(getClass().getResource("images/pea1.png")));
        setBounds(x, y, 50, 50); // Adjust the size and position as needed
    }
}
