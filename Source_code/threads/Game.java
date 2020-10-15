package threads;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;
import javax.swing.JFrame;
import environment.Platform;
import panels.GamePanel;
import player.Player;
import stages.Stage;

public class Game extends Thread {
	private GameMouseAdapter adapterMouse;
	private GameMouseMotionListener listenerMouseMotion;
	private GameKeyListener listenerKey;
	private GameMouseWheelListener listenerMouseWheel;
	private static int dir = 1;
	private static GamePanel gpanel;
	private static double tita = 0;
	private boolean isRight = false, isLeft = false;
	private static boolean clicking = false;
	private volatile boolean exit = false;
	private Player player;
	private Vector<Platform> vPlatforms;
	private float hScale,vScale;
	JFrame omniFrame;
	private long timePerFrame,frameTime;
	private int walkingDir = 0;
	private Stage currentStage;
	
	public Game(GamePanel gpanel, JFrame omniFrame) {
		this.omniFrame = omniFrame;
		
		// Force 60fps (Time here is in Milliseconds)
		timePerFrame = 1000/100;
		
		// Initialize
		Game.gpanel = gpanel;
		
		player = gpanel.getPlayer();
		
		vScale = (float) omniFrame.getHeight()/768;
		hScale = (float) omniFrame.getWidth()/1024;
		
		adapterMouse = new GameMouseAdapter();
		listenerKey = new GameKeyListener();
		listenerMouseMotion = new GameMouseMotionListener();
		listenerMouseWheel = new GameMouseWheelListener();
		gpanel.addKeyListener(listenerKey);
		gpanel.addMouseListener(adapterMouse);
		gpanel.addMouseMotionListener(listenerMouseMotion);
		gpanel.addMouseWheelListener(listenerMouseWheel);
	}

	public void stopRun() {
		exit = true;
	}
	
	
	public void run() {
		
		long startTime = System.currentTimeMillis();
		gpanel.receiveStartTime(startTime);
		
		frameTime = System.currentTimeMillis();
		
		while ((!exit)) {
			
			//Only apply evolution of game if a given cap of time has passed. In this case, 1/100 of a second. Enforces 100fps maximum
			if (System.currentTimeMillis()-frameTime>timePerFrame) { 
				frameTime = System.currentTimeMillis();
				
				// Get information about the state of the game
				player = gpanel.getPlayer();
				vPlatforms = gpanel.getPlatform();
				currentStage = GamePanel.currentStage;
				
				// Update the cursor
				listenerMouseMotion.settita();
				
				// Update player state using input feedback
				player.setState(isRight,isLeft,dir,walkingDir,clicking);
				// Interact with geometry
				player.interactPlatforms(vPlatforms);
				// update player given the current state
				player.update();
				
				// Enforce stage rules. End stage/play movie/drown, etc.
				if (!currentStage.playingMovie()) {
					currentStage.enforceBounds();
				}
				
				// Paint
				gpanel.repaint();
				
				// Game over
				if (player.health == 0) {
					stopRun();
					gpanel.gameOverScreen();
				}
				
				// Did game end?
				if (!gpanel.getgameRun()) {
					stopRun();
				}
			}
		}

	}

	private class GameMouseMotionListener implements MouseMotionListener {
		MouseEvent egeneral;

		public void mouseMoved(MouseEvent e) {
			Insets decoration = omniFrame.getInsets();
			vScale = (float) (omniFrame.getHeight()- decoration.top - decoration.bottom)/768;
			hScale = (float) (omniFrame.getWidth()- decoration.left - decoration.right)/1024;
			double posx = e.getX()/hScale;
			double posy = e.getY()/vScale;
			double tita = Math.atan((player.y + 25 - posy) / (posx - player.x - 15));
			player.changeAngle(tita);
			egeneral = e;
		}

		public void mouseDragged(MouseEvent e) {
			Insets decoration = omniFrame.getInsets();
			vScale = (float) (omniFrame.getHeight()- decoration.top - decoration.bottom)/768;
			hScale = (float) (omniFrame.getWidth()- decoration.left - decoration.right)/1024;
			double posx = e.getX()/hScale;
			double posy = e.getY()/vScale;
			double tita = Math.atan((player.y + 25 - posy) / (posx - player.x - 15));
			player.changeAngle(tita);
			egeneral = e;
		}

		public void settita() {
			if (egeneral != null) {
				Insets decoration = omniFrame.getInsets();
				vScale = (float) (omniFrame.getHeight()- decoration.top - decoration.bottom)/768;
				hScale = (float) (omniFrame.getWidth()- decoration.left - decoration.right)/1024;
				double posx = egeneral.getX()/hScale;
				double posy = egeneral.getY()/vScale;
				tita = Math.atan((player.y + 25 - posy) / (posx - player.x - 15));
				
				player.changeAngle(tita);

				if ((posx - player.x - 15 < 0)) {
					dir = -1;
				} else {
					dir = 1;
				}
			}
		}
	}

	private class GameMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if ((!currentStage.playingMovie())&&(!exit)) {
				clicking = true;
				player.shootWeapon();
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			clicking = false;
		}
	}
	
	private class GameKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {

			if ((!currentStage.playingMovie())&&(player.health > 0)) {
				if (e.getKeyCode() == KeyEvent.VK_D) {
					isRight = true;
					isLeft = false;
					walkingDir = 1;
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					isLeft = true;
					isRight = false;
					walkingDir = -1;
				}
				if ((e.getKeyCode() == KeyEvent.VK_SPACE)||(e.getKeyCode() == KeyEvent.VK_W)) {
					player.jump();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gpanel.resetGame();
				gpanel.menuScreen();
				stopRun();
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_D) {
				isRight = false;
				walkingDir = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				isLeft = false;
				walkingDir = 0;
			}
		}

		public void keyTyped(KeyEvent e) {
		}
	}
	
	private class GameMouseWheelListener implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			Vector<Integer> availableWeapons = player.getAvailableWeapons();
			int currentPosition = availableWeapons.indexOf(player.currentWeapon);
			int newPosition = (currentPosition+e.getWheelRotation())% availableWeapons.size();
			if (newPosition<0) {newPosition+=availableWeapons.size();}
			player.changeWeapon(availableWeapons.elementAt(newPosition));

		}
	}
	

}