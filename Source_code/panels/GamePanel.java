package panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import environment.Platform;
import loot.Loot;
import loot.MoneyLoot;
import monster.FlyingMonster;
import monster.Monster;
import player.Player;
import projectiles.Bullet;
import stages.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int stageINI = 0; //Cheat here
	private static ImageIcon imgLI, imgLI2;
	public static int stage;
	private Vector<int[]> bats;
	private Vector<Platform> vPlatform;
	private Vector<Monster> vMonsters;
	private Vector<Loot> vLoot;
	private Vector<Bullet> vBulletShots;
	private boolean  cstage = true;
	boolean gameRun = true;
	public static String  audioFolder = "sounds/events/";
	public static String miscImgFolder="img/misc/";
	private long startTime, stageTime, stageStartTime;
	public Player player;
	private float hScale,vScale;
	private CardLayout omniLayout;
	private JPanel omniPanel;
	private JFrame omniFrame;
	private VictoryPanel vp;
	private GameOverPanel gop;
	public Vector<Stage> vStage;
	public static Stage currentStage;
	
	public GamePanel(CardLayout omniLayout, JPanel omniPanel, JFrame omniFrame, VictoryPanel vp, GameOverPanel gop) {
		this.omniLayout = omniLayout;
		this.omniPanel = omniPanel;
		this.omniFrame = omniFrame;
		this.vp = vp;
		this.gop = gop;

        // Images
		imgLI = new ImageIcon(getClass().getResource(miscImgFolder+"light1.png"));
		imgLI2 = new ImageIcon(getClass().getResource(miscImgFolder+"light2.png"));

		// Starting variables
		stage = stageINI;
		vStage = new Vector<Stage>();
		
		// Player
		player = new Player(this);
		
		// Lists
		vMonsters = new Vector<Monster>();
		vLoot = new Vector<Loot>();

		vBulletShots = new Vector<Bullet>();
		vPlatform = new Vector<Platform>();
		
		bats = new Vector<int[]>();

		repaint();
	}
	
	public boolean getgameRun() {
		return gameRun;
	}
	
	public void setGameRun(boolean b) {
		gameRun = b;
	}
	
	// static Sounds
	public Clip loadSound(String s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		
		InputStream in = getClass().getResourceAsStream(s);
		InputStream bufferedIn = new BufferedInputStream(in);
		AudioInputStream sourceAudioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        AudioInputStream targetAudioInputStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, sourceAudioInputStream);
        AudioFormat targetFormat = new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 16000, 16, 1, 2, 8000, false);
        AudioInputStream targetAudioInputStream1 = AudioSystem.getAudioInputStream(targetFormat, targetAudioInputStream);
        DataLine.Info info = new DataLine.Info(Clip.class, targetAudioInputStream1.getFormat());
        Clip clipNew = (Clip) AudioSystem.getLine(info);
        
        clipNew.addLineListener(new LineListener(){
            public void update(LineEvent e){
                if(e.getType() == LineEvent.Type.STOP){
                    ((Clip) e.getLine()).flush();
                }
            }
        });
        
        clipNew.open(targetAudioInputStream1);
        
        in.close();
        targetAudioInputStream.close();
        targetAudioInputStream1.close();
        sourceAudioInputStream.close();
        
		return clipNew;
	}
	
	// temp sounds
	protected Clip loadTempSound(String s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		
		InputStream in = getClass().getResourceAsStream(s);
		InputStream bufferedIn = new BufferedInputStream(in);
		AudioInputStream sourceAudioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        AudioInputStream targetAudioInputStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, sourceAudioInputStream);
        AudioFormat targetFormat = new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 16000, 16, 1, 2, 8000, false);
        AudioInputStream targetAudioInputStream1 = AudioSystem.getAudioInputStream(targetFormat, targetAudioInputStream);
        DataLine.Info info = new DataLine.Info(Clip.class, targetAudioInputStream1.getFormat());
        Clip clipNew = (Clip) AudioSystem.getLine(info);
        
        clipNew.addLineListener(new LineListener(){
            public void update(LineEvent e){
                if(e.getType() == LineEvent.Type.STOP){
                    ((Clip) e.getLine()).close();
                }
            }
        });
        
        clipNew.open(targetAudioInputStream1);
        
        in.close();
        bufferedIn.close();
        targetAudioInputStream.close();
        targetAudioInputStream1.close();
        sourceAudioInputStream.close();
        
		return clipNew;
	}
	
	public void playSound(Clip c) {
		/*
		if(c.isRunning()) {
	        c.stop();
		}
		*/
	    c.setFramePosition(0);
	    c.start();
	}
	
	public void playNewSound(String s) {
		try {
			Clip c = loadTempSound(s);
			c.setFramePosition(0);
		    c.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public double getGravity() {
		return currentStage.gravity();
	}
	public double getDrag() {
		return currentStage.drag();
	}
	
	public void resetGame() {
		// booleans
		gameRun = true;
		
		// Starting variables
		stage = stageINI;
		stageTime = System.currentTimeMillis();
		
		player = new Player(this);
		
		startTime = System.currentTimeMillis();
		
		vMonsters = new Vector<Monster>();
		vLoot = new Vector<Loot>();
		vPlatform = new Vector<Platform>();
		bats = new Vector<int[]>();
		vBulletShots = new Vector<Bullet>();

		cstage = true;
		chooseStage();
	}
	
	public void receiveStartTime(long st) {
		startTime = st;
	}

	public int randomize() {
		double l = 2 * Math.random();
		if (l > 1) {
			return 1;
		} else {
			return -1;
		}
	}

	public void resetstage() {
		stage = stageINI;
	}

	public void changeStage() {
		stage += 1;
		stageTime = System.currentTimeMillis();
		cstage = true;
	}
	
	public Vector<Platform> getPlatform(){
		return vPlatform;
	}
	
	public String formatTime(long t) {
		long elapsed = (t - startTime)/1000;
		int seconds = (int) (elapsed%60);
		int minutes = (int) ((elapsed - seconds)/60.0);
		
		String zeroS = "";
		String zeroM = "";
		if (seconds < 10) {zeroS="0";}else {zeroS="";}
		if (minutes < 10) {zeroM="0";}else {zeroM="";}
		
		return zeroM+Integer.toString(minutes)+":"+zeroS+Integer.toString(seconds);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void addBullet(int x, int y, int dir, double tita, int code, int power) {
		// speed = 20
		vBulletShots.add(new Bullet(this, (double) (x + 15), (double) (y + 30 - 2) , 20, tita, dir, power, false, code));
	}
	
	private void clearArrays() {
		vMonsters.clear();
		bats.clear();
		vLoot.clear();
		vBulletShots.clear();
		vPlatform.clear();
	}
	
	public void monsterShoot(double xS, double yS, int speed, double tita, double dir, int damage, int code) {
		vBulletShots.add(new Bullet(this, (double) (xS), (double) (yS) , speed, tita, dir, damage , true, code));
	}
	public void dropCurrency(int x, int y, int code) {
		vLoot.add(new MoneyLoot(this,player,code,x,y) );
	}
	public void spawnMonster(Monster monster) {
		vMonsters.add(monster);
	}
	
	public void chooseStage() {
		if (cstage) {
			
			stageStartTime = System.currentTimeMillis();
			clearArrays();
			cstage = false;
			vStage.clear();
			
			switch(stage){
			case 0:vStage.add( new Stage0(this,vPlatform,vMonsters,vLoot, player));break;
			case 1:vStage.add( new Stage1(this,vPlatform,vMonsters,vLoot, player));break;
			case 2:vStage.add( new Stage2(this,vPlatform,vMonsters,vLoot, player));break;
			case 3:vStage.add( new Stage3(this,vPlatform,vMonsters,vLoot, player));break;
			case 4:vStage.add( new Stage4(this,vPlatform,vMonsters,vLoot, player));break;
			case 5:vStage.add( new Stage5(this,vPlatform,vMonsters,vLoot, player));break;
			case 6:vStage.add( new Stage6(this,vPlatform,vMonsters,vLoot, player));break;
			case 7:vStage.add( new Stage7(this,vPlatform,vMonsters,vLoot, player));break;
			case 8:vStage.add( new Stage8(this,vPlatform,vMonsters,vLoot, player));break;
			case 9:vStage.add( new Stage9(this,vPlatform,vMonsters,vLoot, player));break;
			case 10:vStage.add( new Stage10(this,vPlatform,vMonsters,vLoot, player));break;
			case 11:vStage.add( new Stage11(this,vPlatform,vMonsters,vLoot, player));break;
			case 12:vStage.add( new Stage12(this,vPlatform,vMonsters,vLoot, player));break;
			case 13:vStage.add( new Stage13(this,vPlatform,vMonsters,vLoot, player));break;
			case 14:vStage.add( new Stage14(this,vPlatform,vMonsters,vLoot, player));break;
			case 15:vStage.add( new Stage15(this,vPlatform,vMonsters,vLoot, player));break;
			case 16:vStage.add( new Stage16(this,vPlatform,vMonsters,vLoot, player));break;
			case 17:vStage.add( new Stage17(this,vPlatform,vMonsters,vLoot, player));
			//break;
			//case 20:vStage.add( new StageRandom(this,vPlatform,vMonsters,vLoot, player,0.9));

			}
		}
	}
	
	public void menuScreen() {
		omniLayout.show(omniPanel,"MENU");
	}
	
	public void finalScreen() {
		clearArrays();
		
		long elapsed = System.currentTimeMillis()- startTime;
		
		vp.receiveScore(player.score,elapsed);
		
		vp.setSize(omniFrame.getSize());
		
		omniLayout.show(omniPanel,"VICTORY");
		vp.requestFocus();
		
		resetGame();
	}
	
	public void gameOverScreen() {
		long elapsed = (System.currentTimeMillis() - startTime);
		
		gop.receiveScore(player.score,elapsed);
		
		gop.setSize(omniFrame.getSize());
		
		
		omniLayout.show(omniPanel,"GAMEOVER");
		gop.requestFocus();
	}

	public boolean isStageClear() {
		if (vMonsters.size()==0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void drawPlatforms(Graphics2D g2) {
		for (int b = 0; b < vPlatform.size(); b++) {
			Platform plat = vPlatform.elementAt(b);
			g2.setPaint(plat.color);
			Rectangle2D rec = new Rectangle2D.Double(plat.xp, plat.yp, plat.wp, plat.hp);
			g2.fill(rec);
			g2.draw(rec);
		}
	}
	
	private void drawHUD(Graphics2D g2) {
		if (player.visible) {
			player.drawHUD(g2,0,0);
			
			// time 
			g2.setFont(new Font("Arial", Font.PLAIN, 18));
			g2.setPaint(new Color(147, 8, 201));
			g2.drawString("TIME: "+formatTime(System.currentTimeMillis()), 140, 88);
		}
	}

	
	public void paintComponent(Graphics g) {
		chooseStage();
		
		currentStage = vStage.elementAt(0);
		
		Graphics2D g2 = (Graphics2D) g;
		
		Insets decoration = omniFrame.getInsets();
		vScale = (float) (omniFrame.getHeight()- decoration.top - decoration.bottom)/768;
		hScale = (float) (omniFrame.getWidth()- decoration.left - decoration.right)/1024;
		g2.scale(hScale, vScale);
		
		// stageTime += 1;
		stageTime = (int) (System.currentTimeMillis() - stageStartTime);
		currentStage.update();
		
		// Background image
		currentStage.draw(g2);

		// Platforms
		drawPlatforms(g2);

		// HUD
		drawHUD(g2);
		
		// Loot
		int lootIndex = 0;
		while (lootIndex < vLoot.size()) {
			Loot loot = vLoot.elementAt(lootIndex);
			if (loot.touchesPlayer(player.x,player.y)) {
				loot.pickUp();
				vLoot.remove(lootIndex);
			}else {
				g.drawImage(loot.lootImage().getImage(), loot.x, loot.y , null);
				lootIndex += 1;
				loot.update();
			}
		}

		

		// Bullets
		int bulletIndex=0;
		while(bulletIndex<vBulletShots.size()) {
			Bullet bullet = vBulletShots.elementAt(bulletIndex);
			boolean eliminated = false;
			double cortex = (double) bullet.x;
			double cortey = (double) bullet.y;
			// Out of bounds
			if (bullet.isOutOfBounds()) {
				eliminated = true;
			}
			// Floor
			if (bullet.y>740) {
				cortey = 740;
			}
			
			// Platform collision
			int platformInd = 0;
			while ((!eliminated) && (platformInd < vPlatform.size())) {
				Platform plat = vPlatform.elementAt(platformInd);
				if (bullet.hitsPlat(plat)) {
					cortex = (bullet.y - plat.yp) / (Math.tan(bullet.tita)) + bullet.x;
					cortey = plat.yp+plat.hp/2;
					// Eliminate bullet
					eliminated = true;
				}
				platformInd += 1;
			}
			
			
			// Player collision
			if ((bullet.enemy) && (!eliminated)  && (bullet.hitsPlayer(player.x,player.y)) ){
				player.reduceHP(bullet.damage);
				eliminated = true;
			}
			
			// Monster collision
			int monsterIndex = 0;
			while ((!bullet.enemy)&&(!eliminated) && (monsterIndex < vMonsters.size())) {
				Monster monster = vMonsters.elementAt(monsterIndex);
				if ((monster.alive) && (stageTime>monster.tDelay) && (monster.isShot(bullet.x,bullet.y))) {
					monster.setHP(bullet.damage);
					vMonsters.remove(monsterIndex);
					if (monster.health==0) {
						monster.die();
						monsterIndex -= 1;
					} else {
						vMonsters.insertElementAt(monster,monsterIndex);
					}
					eliminated = true;
				}
				monsterIndex += 1;
			}
			
			// Bat collision
			bats = currentStage.getBats();
			int batIndex = 0;
			while ((!eliminated)&&(batIndex<bats.size())) {
				int mur[] = (int[]) bats.elementAt(batIndex);
				if (bullet.hitsBat(mur)) {
					bats.remove(batIndex);
					eliminated = true;
					int random = randomize();
					vMonsters.add(new FlyingMonster(this,"Bat",mur[0], mur[1],0, 1024,random,0));
				}
				batIndex += 1;
			}
			
			// Update 
			if(!eliminated) {
				bullet.drawBullet(g2);
				bullet.propagate();
				vBulletShots.remove(bulletIndex);
				vBulletShots.insertElementAt(bullet, bulletIndex);
				bulletIndex += 1;
			} else {
				vBulletShots.remove(bulletIndex);
				bullet.drawExplosion(g2, cortex, cortey);
			}
			
		}
		
		// Player
		if (player.visible) {
			g.drawImage(player.getBodyImage().getImage(), player.x, player.y - 2, null);
			g.drawImage(player.getHelmetImage().getImage(), player.x - 1, player.y - 4, null);
			// Weapon
			int posx;
			int posy;
			AffineTransform rotation = AffineTransform.getRotateInstance(-player.tita, player.x + 15, player.y + 30);
			if (Player.dir == 1) {
				posx = player.x;
				posy = player.y;
			} else {
				posx = player.x - 102;
				posy = player.y;
			}
			rotation.translate(posx, posy + 20);
			// Light
			if ((stage == 8) || (stage == 9) || (stage == 10)) {
				if (Player.dir == 1) {
					g2.drawImage(imgLI.getImage(), rotation, null);
				} else {
					g2.drawImage(imgLI2.getImage(), rotation, null);
				}
			}
			g2.drawImage(player.getWeaponImage().getImage(), rotation, null);
		}


		// Monsters
		for(int monsterIndex=0; monsterIndex < vMonsters.size(); monsterIndex++) {
			Monster monster = vMonsters.elementAt(monsterIndex);
			if ((monster.alive)&&(stageTime>monster.tDelay)) {
				if (monster.touchesPlayer(player.x,player.y)) { // Contact damage
					player.reduceHP(1);
				}
				monster.moveMonster(player.x,player.y); // Movement
				monster.shoot(); // Shoot
				monster.drawExtras(g2);
				g.drawImage(monster.monsterImage().getImage(), monster.xm, monster.ym , null);
				vMonsters.remove(monsterIndex);
				vMonsters.insertElementAt(monster,monsterIndex);
			}
		}
		
		currentStage.drawExtras(g2);
		
		
	}

}