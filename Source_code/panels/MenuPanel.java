package panels;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image imgBkg;

	public  MenuPanel() {
		super();
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		imgBkg = new ImageIcon(getClass().getResource("img/misc/MenuScreen2.png")).getImage();
		g2.drawImage(imgBkg, 0, 0,getWidth(), getHeight(), null);
	}
}