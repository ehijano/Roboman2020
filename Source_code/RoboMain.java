import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import panels.CreditsPanel;
import panels.GameOverPanel;
import panels.GamePanel;
import panels.MenuPanel;
import panels.VictoryPanel;
import threads.Cred;
import threads.Game;

public class RoboMain {
	public static MenuPanel menuPanel;
	public static GamePanel gamePanel;
	public static CreditsPanel creditsPanel;
	public static Game game;
	public static Cred credits;
	static JPanel omniPanel;
	static JFrame omniFrame;
	static CardLayout omniLayout;
	static VictoryPanel vp;
	static GameOverPanel gop;
	
	public static void main(String[] args) {
		Dimension dimensionMIN = new Dimension(800, 600);
		Dimension dimension = new Dimension(1024, 768);
		
		omniFrame = new JFrame("Roboman 2020");
		omniFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		 //loading an image from a file
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        URL imageResource = RoboMain.class.getClassLoader().getResource("img/icon.png");
        Image icon = defaultToolkit.getImage(imageResource);
        Taskbar taskbar = Taskbar.getTaskbar();
        try {
            //set icon for mac os (and other systems which do support this method)
            taskbar.setIconImage(icon);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }
        //set icon for windows os (and other systems which do support this method)
        omniFrame.setIconImage(icon);
		
		
		//omniFrame.setUndecorated(true);
		omniFrame.setSize(dimension);
		omniFrame.setPreferredSize(dimension);
		omniFrame.setMinimumSize(dimensionMIN);
		
		omniLayout = new CardLayout();
		omniPanel = new JPanel(omniLayout);
		
				
		// MENU PANEL
		menuPanel = new MenuPanel();
		
		// VICTORY PANEL
		vp = new VictoryPanel(omniLayout, omniPanel,omniFrame,menuPanel);
		// GAME OVER PANEL
		gop = new GameOverPanel(omniLayout, omniPanel,omniFrame,menuPanel);
		
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
				
				creditsPanel.setSize(omniFrame.getSize());
			}
			@Override
			public void mouseReleased(MouseEvent me) {
				credits = new Cred(creditsPanel, omniLayout, omniPanel);
				credits.setPriority(Thread.NORM_PRIORITY);
				credits.start();
				
				omniLayout.show(omniPanel,"SCORES");
				creditsPanel.requestFocus();
				
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
				gamePanel.setSize(omniFrame.getSize());
				gamePanel.resetGame();
				
				game = new Game(gamePanel, omniFrame);
				
				game.start();
				
				omniLayout.show(omniPanel,"GAME");
				gamePanel.setFocusable(true);
				gamePanel.requestFocus();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		menuPanel.setLayout(new GridLayout(4, 3));
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(playButton);
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(dbButton);
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		menuPanel.add(exitButton);
		menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));



		gamePanel = new GamePanel( omniLayout, omniPanel, omniFrame, vp, gop);
		gamePanel.setMinimumSize(dimensionMIN);
		gamePanel.setSize(omniFrame.getSize());
		

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("img/img.gif");
		Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0),"img");
		cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

		gamePanel.setCursor(cursor);
		Container cont2 = omniFrame.getContentPane();
		cont2.setCursor(cursor);
		cont2.add(gamePanel, BorderLayout.CENTER);

		creditsPanel = new CreditsPanel(omniFrame);
		creditsPanel.setMinimumSize(dimensionMIN);
		creditsPanel.setSize(omniFrame.getSize());
		
				
		Cursor cursor2 = new Cursor(Cursor.CROSSHAIR_CURSOR);
		creditsPanel.setCursor(cursor2);
		gamePanel.setCursor(cursor2);
		
		
		// Setting up OmniPanel
		Container omniContainer = omniFrame.getContentPane();
		omniContainer.add(omniPanel);
		
		omniFrame.setMinimumSize(dimensionMIN);
		omniFrame.setPreferredSize(dimension);
		omniFrame.setSize(dimension);
		omniFrame.setLocation(0, 0);

		omniPanel.setSize(dimension);
		omniPanel.setPreferredSize(dimension);
		omniPanel.setMinimumSize(dimensionMIN);
		
		omniPanel.add(menuPanel, "MENU");
		omniPanel.add(gamePanel, "GAME");
		omniPanel.add(creditsPanel, "SCORES");
		omniPanel.add(vp, "VICTORY");
		omniPanel.add(gop, "GAMEOVER");
		
		
		// Focuseable
		menuPanel.setFocusable(true);
		gamePanel.setFocusable(true);
		creditsPanel.setFocusable(true);
		vp.setFocusable(true);
		gop.setFocusable(true);
		
		//Start Showing menu
		omniLayout.show(omniPanel,"MENU");
		menuPanel.requestFocus();
		
		omniFrame.setVisible(true);
		
	}

}