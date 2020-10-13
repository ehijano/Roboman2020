import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class Game extends Thread {
	private GameMouseAdapter adapterMouse;
	private GameMouseMotionListener listenerMouseMotion;
	private GameKeyListener listenerKey;
	private GameMouseWheelListener listenerMouseWheel;
	private static int dir = 1;
	private static GamePanel gpanel;
	private static double xMoonInvader = 760, yMoonInvader = 768 - 50 - 384,tita = 0;
	private boolean MItakeoff = false,isRight = false, isLeft = false;
	private static boolean clicking = false;

	private static String audioFolder = "sounds/events/";
	private volatile boolean exit = false;
	private Clip clipTO;
	private Player player;
	private Vector<Platform> vPlatforms;
	private float hScale,vScale;
	JFrame omniFrame;
	private long timePerFrame,frameTime;
	private int walkingDir = 0;
	
	public Game(GamePanel gpanel, JFrame omniFrame) {
		this.omniFrame = omniFrame;
		
		// Force 60fps (Time here is in Milliseconds)
		timePerFrame = 1000/100;
		
		// Initialize
		Game.gpanel = gpanel;
		
		player = gpanel.getPlayer();
		
		vScale = (float) omniFrame.getHeight()/768;
		hScale = (float) omniFrame.getWidth()/1024;
		
		//Sound clips
		try {
			loadSoundClips();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		
		adapterMouse = new GameMouseAdapter();
		listenerKey = new GameKeyListener();
		listenerMouseMotion = new GameMouseMotionListener();
		listenerMouseWheel = new GameMouseWheelListener();
		gpanel.addKeyListener(listenerKey);
		gpanel.addMouseListener(adapterMouse);
		gpanel.addMouseMotionListener(listenerMouseMotion);
		gpanel.addMouseWheelListener(listenerMouseWheel);
	}
	
	private void loadSoundClips() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		clipTO = gpanel.loadSound(audioFolder+"despegue.au");
	}
	

	
	private void playTakeOff( ) {
		clipTO.setMicrosecondPosition(0);
		clipTO.start();
	}

	
	
	public void stopRun() {
		exit = true;
	}
	
	private void finishStage(int stage) {
		if(stage == 1) {//River 1
			player.setLocation(10,700-50);
		}else if(stage ==12) {// River 2
			player.setLocation(0,650 - 50);
		} else {//Standard
			player.setLocation(10,713);
		}
		
		player.noJump();
		
		if (stage == 17) {// Final stage
			gpanel.finalScreen();
			gpanel.setVisible(false);
			exit = true;
		}else {
			gpanel.changeStage();
		}
	}
	
	public void run() {
		
		long startTime = System.currentTimeMillis();
		gpanel.receiveStartTime(startTime);
		
		frameTime = System.currentTimeMillis();
		
		while ((!exit)) {
			
			if (System.currentTimeMillis()-frameTime>timePerFrame) {
				frameTime = System.currentTimeMillis();
				
				player = gpanel.getPlayer();
				vPlatforms = gpanel.getPlatform();
				
				listenerMouseMotion.settita();
				
				player.setState(isRight,isLeft,dir,walkingDir,clicking);
				
				player.interactPlatforms(vPlatforms);
				
				player.update();
	
				if (MItakeoff) {
					if (yMoonInvader == 768 - 50 - 384) {
						playTakeOff();
					}
					player.setVisible(false);
					yMoonInvader -= (int) 500/(2000.0/timePerFrame);
					gpanel.moonInvader(xMoonInvader, yMoonInvader);
					if (yMoonInvader + 384 < 0) {
						MItakeoff = false;
						player.setVisible(true);
						player.setLocation(100, 713);
						gpanel.changeStage();
					}
	
				} else {
					
					int stage = GamePanel.stage;
					
					if ((player.x > 1024 - 30) && (gpanel.isStageClear())) {// Stage ends
						finishStage(stage);
					}else if ( ((stage ==  2)||(stage==13))&&(player.y>713) ) {// River death
						player.heal(-player.health);
					}else if ((player.x > 729) && (stage == 14)) {// Moon Invader
						player.setVisible(false);
						MItakeoff = true;
					}else {// Otherwise respect screen bounds
						player.fixOutOfBounds();
					}
					
					/*
					try {
						sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					*/
				}
				
				
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
				
				/*
				if((!isLeft)&&(!isRight)) {
					if ((posx - player.x - 15 < 0)) {
						dir = -1;
					} else {
						dir = 1;
					}
				}
				*/
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
			if ((!MItakeoff)&&(!exit)) {
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

			if ((!MItakeoff)&&(player.health > 0)) {
				if (e.getKeyCode() == KeyEvent.VK_D) {
					isRight = true;
					isLeft = false;
					//dir = 1;
					walkingDir = 1;
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					isLeft = true;
					isRight = false;
					//dir = -1;
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