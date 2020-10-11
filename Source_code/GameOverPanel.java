import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOverPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GamePanel gp;
	JFrame frameg,gof;
	private float hScale,vScale;
	private long endTime;
	private GOKeyListener listenerKey;
	
	public GameOverPanel(GamePanel gp, JFrame gof, JFrame frameg,int score,long elapsed) {
		super();
		
		this.gp = gp;
		this.gof = gof;
		this.frameg = frameg;
		
		listenerKey = new GOKeyListener();
		gof.addKeyListener(listenerKey);
		
		endTime = (new Date()).getTime();
	}
	
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		
		vScale = (float) gof.getHeight()/768;
		hScale = (float) gof.getWidth()/1024;
		g2.scale(hScale, vScale);
		
		ImageIcon imgover = new ImageIcon(getClass().getResource("img/misc/"+"gameover.png"));
		g2.drawImage(imgover.getImage(), (int) 0, (int) 0, null);
		g2.setFont(new Font("Arial", Font.PLAIN, 50));
		g2.setPaint(Color.red);
		g2.drawString("Time wasted: "+ gp.formatTime(endTime), 500, 500);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.drawString("(You could have been working...)", 500, 550);

	}
	
	private class GOKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				frameg.setLocation(gof.getLocation());
				frameg.setSize(gof.getSize());
				setSize(gof.getSize());
				
				gof.setVisible(false);
				gp.resetGame();
				gp.menuScreen();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

}
