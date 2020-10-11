import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

public class RoboMain {
	public static JFrame framei, frameg, creditsFrame;
	public static MenuPanel ipanel;
	public static GamePanel gpanel;
	public static CreditsPanel creditsPanel;
	public static Game game;
	public static Cred credits;

	public static void main(String[] args) {

		Dimension dimensionMIN = new Dimension(800, 600);
		Dimension dimension = new Dimension(1024, 768);
		
		framei = new JFrame();
		framei.setMinimumSize(dimensionMIN);
		framei.setPreferredSize(dimension);
		framei.setSize(dimension);
		framei.setLocation(0, 0);
		framei.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ipanel = new MenuPanel();
		ipanel.setSize(dimension);
		ipanel.setPreferredSize(dimension);
		ipanel.setMinimumSize(dimensionMIN);
		
		JButton playButton = new JButton("PLAY");
		playButton.setContentAreaFilled(false);
		playButton.setBorder(null);
		playButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 80));
		playButton.setForeground(Color.yellow);
		
		JButton dbButton = new JButton("SCORES");
		dbButton.setContentAreaFilled(false);
		dbButton.setBorder(null);
		dbButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 80));
		dbButton.setForeground(Color.yellow);
		dbButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me) {
				dbButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 40));
				dbButton.setText("loading...");
				
				creditsFrame.setLocation(framei.getLocation());
				creditsFrame.setSize(framei.getSize());
				creditsPanel.setSize(framei.getSize());
			}
			@Override
			public void mouseReleased(MouseEvent me) {
				credits = new Cred(creditsPanel, creditsFrame, framei);
				credits.setPriority(Thread.NORM_PRIORITY);
				credits.start();
				creditsFrame.setVisible(true);
				framei.setVisible(false);
				dbButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 80));
				dbButton.setText("SCORES");
			}
		});
		
		JButton exitButton = new JButton("EXIT");
		exitButton.setContentAreaFilled(false);
		exitButton.setBorder(null);
		exitButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 80));
		exitButton.setForeground(Color.yellow);
		
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				frameg.setSize(framei.getSize());
				frameg.setLocation(framei.getLocation());
				gpanel.setSize(framei.getSize());
				
				game = new Game(gpanel, frameg, framei);
				gpanel.resetGame();
				game.start();
				
				frameg.setVisible(true);
				framei.setVisible(false);
			}
		});
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				framei.setVisible(false);
				System.exit(0);
			}
		});
		
		ipanel.setLayout(new GridLayout(4, 3));
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(playButton);
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(dbButton);
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ipanel.add(exitButton);
		ipanel.add(Box.createRigidArea(new Dimension(10, 0)));

		Container cont = framei.getContentPane();
		cont.add(ipanel, BorderLayout.CENTER);

		framei.setVisible(true);

		frameg = new JFrame();
		frameg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameg.setMinimumSize(dimensionMIN);
		frameg.setSize(framei.getSize());
		frameg.setLocation(0, 0);

		creditsFrame= new JFrame();
		creditsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		creditsFrame.setMinimumSize(dimension);
		creditsFrame.setSize(framei.getSize());
		creditsFrame.setLocation(0, 0);

		gpanel = new GamePanel(frameg, framei);
		gpanel.setMinimumSize(dimensionMIN);
		gpanel.setSize(framei.getSize());
		
		frameg.setUndecorated(true);
		frameg.setVisible(false);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("img/img.gif");
		Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0),"img");
		cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

		gpanel.setCursor(cursor);
		Container cont2 = frameg.getContentPane();
		cont2.setCursor(cursor);
		cont2.add(gpanel, BorderLayout.CENTER);

		creditsPanel = new CreditsPanel(creditsFrame);
		creditsPanel.setMinimumSize(dimensionMIN);
		creditsPanel.setSize(framei.getSize());
		
		creditsFrame.setVisible(false);
		Cursor cursor2 = new Cursor(Cursor.CROSSHAIR_CURSOR);
		creditsPanel.setCursor(cursor2);
		frameg.setCursor(cursor2);
		Container cont3 = creditsFrame.getContentPane();
		cont3.setCursor(cursor2);
		cont3.add(creditsPanel, BorderLayout.CENTER);
	}

}