import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CreditsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int t = 0, topThreshold = 10;
	private ResultSet rs;
	private float hScale,vScale;
	private JFrame omniFrame;
	
	public CreditsPanel(JFrame omniFrame) {
		super();
		this.omniFrame = omniFrame;
	}
	
	public void refresh(int tt) {
		t = tt;
		repaint();
	}
	
	public void sendData( ResultSet rs ) {
		this.rs = rs;
	}
    
	private String getFinalTime(int elapsedMILI) {
		long elapsed = elapsedMILI/1000;
		int seconds = (int) (elapsed%60);
		int minutes = (int) ((elapsed - seconds)/60.0);
		
		String zeroS = "";
		String zeroM = "";
		if (seconds < 10) {zeroS="0";}else {zeroS="";}
		if (minutes < 10) {zeroM="0";}else {zeroM="";}
		
		return zeroM+Integer.toString(minutes)+":"+zeroS+Integer.toString(seconds);
	}
	
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;

		vScale = (float) omniFrame.getHeight()/768;
		hScale = (float) omniFrame.getWidth()/1024;
		g2.scale(hScale, vScale);
		
		// White background
		Rectangle2D r = new Rectangle2D.Double(0, 0, 1024, 768);
		g2.setPaint(Color.white);
		g2.fill(r);
		
		// Print Score-board
		g2.setPaint(Color.black);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));

		int dbCounter = 1;
		try {
			while ((rs.next()) && (dbCounter<=topThreshold)) {
				g2.setFont(new Font("Arial", Font.PLAIN, 40));
				g2.drawString("TOP ROBOMAN PLAYERS IN THE UNIVERSE", 100 , (768 - t) + 30*1 );

				g2.setFont(new Font("Arial", Font.PLAIN, 20));
				g2.drawString(rs.getString(1), 1024 / 2 - 400, (768 -t)  + 30*(dbCounter + 1));
				g2.drawString("Time: "+getFinalTime(rs.getInt(3)), 1024 / 2 - 400 +100   ,   (768 -t)  + 30*(dbCounter + 1)  );
				g2.drawString("Score: "+Integer.toString(rs.getInt(2))+"$", 1024 / 2 - 400 +300   ,   (768 -t)  + 30*(dbCounter + 1)  );
				g2.drawString("Date: "+rs.getDate(4), 1024 / 2 - 400 +500   ,   (768 -t)  + 30*(dbCounter + 1)  );
				
	            dbCounter += 1;
			}
			rs.beforeFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}