import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOverPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hScale,vScale;
	private GOKeyListener listenerKey;
	private long elapsed;
	private CardLayout omniLayout;
	private JPanel omniPanel;
	private JFrame omniFrame;
	private MenuPanel menuPanel;
	
	public GameOverPanel(CardLayout omniLayout,JPanel omniPanel,JFrame omniFrame,MenuPanel menuPanel) {
		super();
		
		this.omniFrame = omniFrame;
		this.omniLayout = omniLayout;
		this.omniPanel = omniPanel;
		this.omniPanel = omniPanel;
		this.menuPanel = menuPanel;
		
		listenerKey = new GOKeyListener();
		
		addKeyListener(listenerKey);
	}
	
	public void receiveScore(int score,long elapsed) {
		//this.score = score;
		this.elapsed = elapsed;
	}
	
	private String formatTime(long t) {
		long elapsed = (t)/1000;
		int seconds = (int) (elapsed%60);
		int minutes = (int) ((elapsed - seconds)/60.0);
		
		String zeroS = "";
		String zeroM = "";
		if (seconds < 10) {zeroS="0";}else {zeroS="";}
		if (minutes < 10) {zeroM="0";}else {zeroM="";}
		
		return zeroM+Integer.toString(minutes)+":"+zeroS+Integer.toString(seconds);
	}
	
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		
		vScale = (float) omniFrame.getHeight()/768;
		hScale = (float) omniFrame.getWidth()/1024;
		g2.scale(hScale, vScale);
		
		ImageIcon imgover = new ImageIcon(getClass().getResource("img/misc/"+"gameover.png"));
		g2.drawImage(imgover.getImage(), (int) 0, (int) 0, null);
		g2.setFont(new Font("Arial", Font.PLAIN, 50));
		g2.setPaint(Color.red);
		g2.drawString("Time wasted: "+ formatTime(elapsed), 500, 500);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.drawString("(You could have been working...)", 500, 550);

	}
	
	private class GOKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				omniLayout.show(omniPanel,"MENU");
				menuPanel.requestFocus();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

}
