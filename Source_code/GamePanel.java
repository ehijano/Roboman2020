import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int stageINI = 0; //Cheat here
	private static Image imgBackground;
	private static ImageIcon imgLI, imgmur, imgLI2, imgMoonInvader, imgMoonInvader2, imgMoonInvader3, imgTIBU, imgNextStage, imgHallOfFame;
	private static int stage, MIanimationCounter = 0, dripCounter = 0;
	private Vector<int[]>   vPlatforms, bats, drips;
	private Vector<Monster> vMonsters;
	private Vector<Loot> vLoot;
	private Vector<Bullet> vBulletShots;
	private static double xn = 760, yn = 768 - 50 - 384,random = Math.random();
	private boolean  cstage = true, gameRun = true;
	private static String  audioFolder = "sounds/events/", stagesFolder = "img/stages/", miscImgFolder="img/misc/";
	private long startTime, stageTime, stageStartTime;
	private Clip clipDrip;
	public Player player;
	private float hScale,vScale;
	private CardLayout omniLayout;
	private JPanel omniPanel;
	private JFrame omniFrame;
	private VictoryPanel vp;
	private GameOverPanel gop;
	
	public GamePanel(CardLayout omniLayout, JPanel omniPanel, JFrame omniFrame, VictoryPanel vp, GameOverPanel gop) {
		this.omniLayout = omniLayout;
		this.omniPanel = omniPanel;
		this.omniFrame = omniFrame;
		this.vp = vp;
		this.gop = gop;
		
		//Audio
		try {
			clipDrip=loadSound(audioFolder+"goti.au");
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		
        // Images
		imgNextStage = new ImageIcon(getClass().getResource(miscImgFolder+"flecha.png"));
		imgHallOfFame = new ImageIcon(getClass().getResource(miscImgFolder+"hof.png"));
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+"0p.png")).getImage();
		imgmur = new ImageIcon(getClass().getResource(miscImgFolder+"mur.png"));
		imgMoonInvader = new ImageIcon(getClass().getResource(miscImgFolder+"MoonInvader.png"));
		imgMoonInvader2 = new ImageIcon(getClass().getResource(miscImgFolder+"MoonInvader2.png"));
		imgMoonInvader3 = new ImageIcon(getClass().getResource(miscImgFolder+"MoonInvader3.png"));
		imgLI = new ImageIcon(getClass().getResource(miscImgFolder+"light1.png"));
		imgLI2 = new ImageIcon(getClass().getResource(miscImgFolder+"light2.png"));
		imgTIBU = new ImageIcon(getClass().getResource(miscImgFolder+"tibu.png"));
		
		// Starting variables
		stage = stageINI;
		
		// Player
		player = new Player(this);
		
		// Lists
		vMonsters = new Vector<Monster>();
		vLoot = new Vector<Loot>();
		vPlatforms = new Vector<int[]>();
		vBulletShots = new Vector<Bullet>();
		
		bats = new Vector<int[]>();
		drips = new Vector<int[]>();

		repaint();
	}
	
	protected Clip loadSound(String s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		
		InputStream in = getClass().getResourceAsStream(s);
		InputStream bufferedIn = new BufferedInputStream(in);
		AudioInputStream sourceAudioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        AudioInputStream targetAudioInputStream=AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, sourceAudioInputStream);
        AudioFormat targetFormat = new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 16000, 16, 1, 2, 8000, false);
        AudioInputStream targetAudioInputStream1 = AudioSystem.getAudioInputStream(targetFormat, targetAudioInputStream);
        DataLine.Info info = new DataLine.Info(Clip.class, targetAudioInputStream1.getFormat());
        Clip clipNew = (Clip) AudioSystem.getLine(info);
        clipNew.addLineListener(event -> {
            if(LineEvent.Type.STOP.equals(event.getType())) {
            	clipNew.flush();
            }
        });
        clipNew.open(targetAudioInputStream1);
        
		return clipNew;
	}
	
	public void playSound(Clip c) {
		if(c.isRunning()) {
	        c.stop();
		}
	    c.setFramePosition(0);
	    c.start();
	}
	
	public double getGravity() {
		if (stage > 14) {
			return 0.4;
		} else {
			return 0.7;
		}
	}
	public double getDrag() {
		if (stage > 14) {
			return 0.1 * 0.4 * Math.pow(0.7, -1);
		} else {
			return 0.1;
		}
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
		vPlatforms = new Vector<int[]>();
		bats = new Vector<int[]>();
		drips = new Vector<int[]>();
		vBulletShots = new Vector<Bullet>();
		
		// Misc
		xn = 760;
		yn = 768 - 50 - 384;

		cstage = true;
		chooseStage();
	}
	
	public void moonInvader(double xMoonInvader, double yMoonInvader) {
		xn = xMoonInvader;
		yn = yMoonInvader;
		repaint();
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

	public Vector<int[]> getPlatforms() {
		return vPlatforms;
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

	public int getstage() {
		return stage;
	}

	public void updatePlatforms(Vector<int[]> rp) {
		vPlatforms = rp;
	}
	
	private void clearArrays() {
		vMonsters.clear();
		bats.clear();
		vLoot.clear();
		vBulletShots.clear();
		vPlatforms.clear();
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
			imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(stage)+"p.png")).getImage();
			// Each stage generated by adding monster arrays
			switch(stage){
			case 0:
				vPlatforms.add(new int[] { 700, 650, 120, 15, 2, 1 });
				
				vMonsters.add( new WalkingMonster(this,"Slug", 700, 650 - 60, 700, 820 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug", 810, 768 - 8 - 60, 1, 1024, -1, 0) );
				break;
			case 1:
				vMonsters.add( new WalkingMonster(this,"Slug",700, 650 - 60, 700, 820 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug", 810, 768 - 8 - 60,  0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",  200, 768 - 8 - 60,  0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug", 280, 768 - 8 - 60,  0,1024 , -1, 0) );
				
				vMonsters.add( new FlyingMonster(this,"Bee",1, 100,  0, 1024 , -1, 400) );
				vMonsters.add( new FlyingMonster(this,"Bee",1024, 100,  0, 1024 , -1, 400) );
				vMonsters.add( new FlyingMonster(this,"Bee", 300, 100,  0, 1024 , 1, 0) );
				vMonsters.add( new FlyingMonster(this,"Bee",1, 500,  0, 1024 , -1, 700) );
				
				vMonsters.add( new WalkingMonster(this,"Worm",1023, 692 ,  0, 1024  , -1, 600) );
				vMonsters.add( new WalkingMonster(this,"Worm",1023, 692 ,  0, 1024  , -1, 400) );
				vMonsters.add( new WalkingMonster(this,"Worm",1023, 692 ,  0, 1024  , -1, 100) );
				vMonsters.add( new WalkingMonster(this,"Worm",1023, 692 ,  0, 1024  , -1, 0) );

				
				vLoot.add(new WeaponLoot(this,player,1,300,739));

				vLoot.add(new ShieldLoot(this,player,1,100, 729 ));

				vPlatforms.add(new int[] { 700, 650, 120, 15, 2, 1 });
				vPlatforms.add(new int[] { 570, 550, 120, 15, 2, 1 });
				vPlatforms.add(new int[] { 370, 350, 120, 15, 2, 1 });

				for(int i=0; i<3; i++) {
					vLoot.add(new MoneyLoot(this,player, 1, 600+40*i, 739 ));
				}
				
				break;
			case 2:
				imgTIBU = new ImageIcon(getClass().getResource(miscImgFolder+"tibu.png"));
				
				vMonsters.add( new WalkingMonster(this,"Slug",300, 640 - 60 , 300, 300 + 120 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",900 + 120, 670 - 60 , 900, 900 + 120, -1, 0) );

				vMonsters.add( new FlyingMonster(this,"Bee",1024, 200,  0, 1024 , -1, 700) );
				vMonsters.add( new FlyingMonster(this,"Bee", 1, 200,  0, 1024 , 1, 0) );
				vMonsters.add( new FlyingMonster(this,"Bee", 1, 200,  0, 1024 , -1, 200) );
				vMonsters.add( new FlyingMonster(this,"Bee", 1, 400,  0, 1024 , 1, 700) );

				vPlatforms.add(new int[] { 0, 700, 120, 15, 1, 1 });
				vPlatforms.add(new int[] { 200, 670, 50, 15, 2, 1 });
				vPlatforms.add(new int[] { 300, 640, 120, 15, 2, 1 });
				vPlatforms.add(new int[] { 500, 600, 120, 15, 2, 1 });
				vPlatforms.add(new int[] { 650, 640, 180, 15, 2, 1 });
				vPlatforms.add(new int[] { 900, 670, 124, 15, 2, 1 });


				vLoot.add(new AmmoLoot(this,player,1,210, 670 - 23));
				
			break;
			case 3:
				vMonsters.add( new WalkingMonster(this,"Skelleton",713, 768 - 10 - 56, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Skelleton",413, 768 - 10 - 56, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Skelleton",1013, 768 - 10 - 56, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 513, 768 - 10 - 56, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 913, 768 - 10 - 56, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 953, 768 - 10 - 56, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 933, 768 - 10 - 56, 0,1024 , -1, 0) );
				
				vMonsters.add( new WalkingMonster(this,"Skelleton", 1024, 768 - 10 - 56, 0,1024 , -1, 600) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 1024, 768 - 10 - 56, 0,1024 , -1, 620) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 1024, 768 - 10 - 56, 0,1024 , -1, 640) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 1024, 768 - 10 - 56, 0,1024 , -1, 660) );
				vMonsters.add( new WalkingMonster(this,"Skelleton", 1024, 768 - 10 - 56, 0,1024 , -1, 680) );
				
				vMonsters.add( new FlyingMonster(this,"Bat",1024, 500,  0, 1024 , -1, 700) );
				vMonsters.add( new FlyingMonster(this,"Bat",1024, 500,  0, 1024 , -1, 800) );
				vMonsters.add( new FlyingMonster(this,"Bat", 1024, 400,  0, 1024 , -1, 900) );
				vMonsters.add( new FlyingMonster(this,"Bat", 1024, 350,  0, 1024 , -1, 950) );
				vMonsters.add( new FlyingMonster(this,"Bat", 1024, 520,  0, 1024 , -1, 400) );

				
				vLoot.add(new AmmoLoot(this,player,1, 210, 768 - 10 - 23));
				vLoot.add(new AmmoLoot(this,player,1, 810, 768 - 10 - 23));
				break;
			case 4:
				vLoot.add(new AmmoLoot(this,player,1, 210, 768 - 10 - 23));

				vLoot.add(new WeaponLoot(this,player,2,840, 739));
				
				vLoot.add(new HPLoot(this,player, 1,710, 768 - 10 - 33));

				vMonsters.add( new ChasingMonster(this,"SK", 700 - 100, 768 - 8 - 121 ,  0, 1024 , -1, 0) );
				break;
			case 5:
				vLoot.add(new AmmoLoot(this,player,1, 220, 600 - 23));

				vLoot.add(new HPLoot(this,player, 1,470, 400 - 33));

				vPlatforms.add(new int[] { 200, 600, 120, 15, 1, 3 });
				vMonsters.add( new ShootingMonster(this,"Soldier", 200, 600 - 58,  200,200 + 120 , 1, 0) );
				
				vPlatforms.add(new int[] { 450, 400, 100, 15, 2, 3 });
				vMonsters.add( new ShootingMonster(this,"Soldier", 450, 400 - 58, 450,450 + 100 , 1, 0) );

				vPlatforms.add(new int[]  { 600, 600, 120, 15, 2, 3 });
				vMonsters.add( new ShootingMonster(this,"Soldier",600 + 100, 600 - 58, 600, 600 + 100, -1, 0) );

				vMonsters.add( new ShootingMonster(this,"Soldier",1023, 768 - 8 - 58, 0, 1024, -1, 600) );
				vMonsters.add( new WalkingMonster(this,"SUV", 1023, 692,  0, 1024 , -1, 750) );
				vMonsters.add( new ShootingMonster(this,"Soldier",1023, 768 - 8 - 58, 0, 1024, -1, 900) );

				vMonsters.add( new FlyingMonster(this,"Helicopter",1023, 100,  0, 1024 , -1, 30) );
				vMonsters.add( new FlyingMonster(this,"Helicopter",1023, 200,  0, 1024 , -1, 200) );
				
				vMonsters.add( new SpamMonster(this,"Tank",1024, 768 - 80 - 7,  0, 1024 , -1, 1100) );
				break;
			case 6:
				vLoot.add(new HPLoot(this,player, 1,100, 768 - 10 - 33));

				vLoot.add(new ShieldLoot(this,player,1,150, 729 ));

				vPlatforms.add(new int[] { 158, 679, 236, 15, 1, 3 });
				vPlatforms.add(new int[] { 434, 573, 128, 15, 1, 3 });
				vPlatforms.add(new int[] { 603, 335, 61, 15, 1, 3 });

				vLoot.add(new AmmoLoot(this,player,1, 614, 335 - 23));

				vPlatforms.add(new int[]  { 692, 335, 61, 15, 1, 3 });

				vLoot.add(new AmmoLoot(this,player,1, 704, 335 - 23));

				vMonsters.add( new ShootingMonster(this,"Soldier",158, 679 - 58,158,158 + 236, 1, 0) );
				vMonsters.add( new ShootingMonster(this,"Soldier",434, 573 - 58, 434,434 + 128, 1, 0) );
				
				for( int i=0; i<4;i++) {
					vMonsters.add( new ShootingMonster(this,"Soldier",1024, 768 - 8 - 58 ,0, 1024 , -1, 410+40*i) );
				}
				break;
			case 7:
				vLoot.add(new WeaponLoot(this,player,3,200, 739));

				vPlatforms.add(new int[] { 341, 515, 398 - 341, 10, 1, 3 });
				vPlatforms.add(new int[] { 398, 580, 509 - 398, 10, 1, 3 });
				vPlatforms.add(new int[] { 509, 289, 613 - 509, 10, 1, 3 });
				vPlatforms.add(new int[] { 613, 580, 726 - 613, 10, 1, 3 });
				vPlatforms.add(new int[] { 726, 515, 785 - 726, 10, 1, 3 });

				vMonsters.add( new ShootingMonster(this,"Soldier", 398, 580 - 58,398, 509, 1, 0) );
				vMonsters.add( new ShootingMonster(this,"Soldier", 726, 580 - 58, 613,726, -1, 0) );

				vLoot.add(new ShieldLoot(this,player,2,529, 289 - 31 ));

				vMonsters.add( new FlyingMonster(this,"Helicopter",1023, 200,  0, 1024 , -1, 200) );
				
				vMonsters.add( new FlyingMonster(this,"Helicopter",-100, 300,  0, 1024 , 1, 500) );
				
				vMonsters.add( new SpamMonster(this,"Tank",1024, 768 - 80 - 7,0, 1024, -1, 1100) );
				
				cstage = false;
				break;
			case 8:
				vMonsters.add( new WalkingMonster(this,"Slug",810, 768 - 8 - 60, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",200, 768 - 8 - 60, 0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",512, 553 - 60, 512, 626 , 1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",280, 768 - 8 - 60,0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Zombie",612, 768 - 8 - 60,0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Zombie",712, 768 - 8 - 60,0,1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Zombie",912, 768 - 8 - 60,0,1024 , -1, 0) );
				
				vMonsters.add( new FlyingMonster(this,"Bat",1024, 450, 0, 1024 , -1, 400) );
				vMonsters.add( new FlyingMonster(this,"Bat",1024, 400, 0, 1024 , -1, 100) );
				vMonsters.add( new FlyingMonster(this,"Bat", 1024, 300, 0, 1024 , -1, 400) );
				vMonsters.add( new FlyingMonster(this,"Bat",1024, 250, 0, 1024 , -1, 100) );
				
				vPlatforms.add(new int[] { 512, 553, 626 - 512, 14, 1, 3 });
				break;
			case 9:
				for(int i = 0; i<3; i++) {
					vMonsters.add( new WalkingMonster(this,"Zombie",557, 768 - 8 - 60, 0,1024 , -1, 40*i) );
					vMonsters.add( new WalkingMonster(this,"Zombie",557, 768 - 8 - 60, 0,1024 , 1, 40*i) );
				}
				for(int i = 0; i<2; i++) {
					vMonsters.add( new FlyingMonster(this,"Bat",0, 400, 0,1024 , 1, 200*i) );
					vMonsters.add( new FlyingMonster(this,"Bat",1023, 450, 0,1024 , -1, 200*i) );
				}

				bats.add(new int[] { 395, 326 });
				bats.add(new int[] { 695, 326 });
				bats.add(new int[] { 813, 219 });
				break;
			case 10:
				vMonsters.add( new WalkingMonster(this,"Zombie", 500, 768 - 8 - 60,0,834, 1, 0) );

				bats.add(new int[] { 295, 326 });
				bats.add(new int[] { 795, 326 });
				bats.add(new int[]  { 513, 219 });
				
				vLoot.add(new AmmoLoot(this,player,1, 520, 650 - 23));

				vPlatforms.add(new int[] { 200, 650, 120, 10, 1, 1 });
				vMonsters.add( new WalkingMonster(this,"Zombie", 200, 650 - 60,200,200 + 120, 1, 0) );
				
				vPlatforms.add(new int[] { 200, 450, 120, 10, 1, 1 });
				vPlatforms.add(new int[] { 500, 650, 120, 10, 1, 1 });
				vPlatforms.add(new int[] { 500, 450, 120, 10, 1, 1 });
				vMonsters.add( new WalkingMonster(this,"Zombie",620, 450 - 60,500,500 + 120, 1, 0) );
				
				vMonsters.add( new WalkingMonster(this,"Slug",200, 768 - 8 - 60 ,0,834, 1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",300, 768 - 8 - 60 ,0,834, -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Slug",400, 768 - 8 - 60 ,0,834, -1, 0) );
				
				vMonsters.add( new WalkingMonster(this,"Zombie",600, 768 - 8 - 60,0,834, 1, 0) );
				break;
			case 11:
				
				vMonsters.add( new FlyingMonster(this,"Eagle",  1024,400 ,0, 1024 , 1, 100) );
				vMonsters.add( new FlyingMonster(this,"Eagle",  0,300 ,0, 1024 , -1, 300) );
				
				vMonsters.add( new WalkingMonster(this,"Tree", 500, 768 - 8 - 98 ,0,1024 , 1, 0) );
				vMonsters.add( new WalkingMonster(this,"Plant", 650, 768 - 8 - 98 ,0,1024 , 1, 0) );

				vLoot.add(new WeaponLoot(this,player,4,500, 739));
				
				vMonsters.add( new WalkingMonster(this,"Tree", 800, 768 - 8 - 98 ,0,1024 , -1, 0) );
				break;
			case 12:
				vPlatforms.add(new int[] { 220, 483, 585 - 220, 8, 1, 2 });
				vPlatforms.add(new int[] { 572, 624, 772 - 572, 7, 1, 2 });

				vLoot.add(new AmmoLoot(this,player,1, 572 + 30, 624 - 23));
				
				vMonsters.add( new WalkingMonster(this,"Tree",  220, 483 - 98 ,220, 585 , 1, 0) );
				
				vMonsters.add( new WalkingMonster(this,"Tree",   572, 624 - 98 , 572,772, 1, 0) );
				
				vMonsters.add( new WalkingMonster(this,"Plant", 1024, 768 - 8 - 98 ,0, 1024 , 1, 300) );
				vMonsters.add( new WalkingMonster(this,"Plant",  650, 768 - 8 - 98 ,0, 1024 , 1, 0) );
				vMonsters.add( new WalkingMonster(this,"Plant", 0, 768 - 8 - 98 ,0, 1024 , 1, 500) );

				vLoot.add(new AmmoLoot(this,player,1, 210, 768 - 10 - 23));

				vMonsters.add( new FlyingMonster(this,"Eagle",  1024, 100 ,0, 1024 , 1, 100) );
				break;
			case 13:
				vPlatforms.add(new int[] {0, 666, 62, 5, 1, 5});
				vPlatforms.add(new int[] { 965, 666, 200, 5, 2, 5 });
				vPlatforms.add(new int[] { 150, 630, 100, 15, 1, 3 });
				vPlatforms.add(new int[] { 350, 630, 100, 15, 1, 3 });
				vPlatforms.add(new int[]  { 550, 630, 100, 15, 1, 3 });

				vLoot.add(new AmmoLoot(this,player,1, 560, 630 - 23));

				vPlatforms.add(new int[] { 750, 630, 100, 15, 1, 3 });
				
				for(int i=0; i<8;i++) {
					vMonsters.add( new FlyingMonster(this,"Eagle",  1024, 100 ,0, 1024 , 1, 100+150*i) );
				}

				imgTIBU = new ImageIcon(getClass().getResource(miscImgFolder+"tibu2.png"));
				break;
			case 14:
				vLoot.add(new AmmoLoot(this,player, 1,310, 768 - 10 - 33));

				cstage = false;
				break;
			case 15:
				vMonsters.add( new FlyingMonster(this,"Ship",  1024, 200 ,0, 1024 , -1, 10) );
				vMonsters.add( new FlyingMonster(this,"Ship",  1024, 300 ,0, 1024 , -1, 200) );

				vLoot.add(new WeaponLoot(this,player,5,500, 739));
				vLoot.add(new ShieldLoot(this,player,2, 929, 768 - 8 - 31 ));

				vMonsters.add( new WalkingMonster(this,"Alien", 1024, 768 - 8 - 98 ,0, 1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Alien",  800, 768 - 8 - 98 ,0, 1024 , -1, 0) );
				vMonsters.add( new WalkingMonster(this,"Alien", 300, 768 - 8 - 98 ,0, 1024 , -1, 0) );
				
				break;
			case 16:
				vLoot.add(new AmmoLoot(this,player, 1,310, 768 - 10 - 33));
				vLoot.add(new AmmoLoot(this,player, 1,410, 768 - 10 - 33));
				vLoot.add(new HPLoot(this,player, 1,810, 768 - 10 - 33));
				vMonsters.add( new FlyingMonster(this,"Ship", 100, 100 ,0, 1024 , -1, 0) );
				vMonsters.add( new FlyingMonster(this,"Ship", 500, 100 ,0, 1024 , -1, 0) );
				vMonsters.add( new FlyingMonster(this,"Ship", 800, 100 ,0, 1024 , -1, 0) );

				vMonsters.add( new WalkingMonster(this,"Alien", 1024, 768 - 8 - 98 ,0, 1024 , -1, 300) );
				vMonsters.add( new WalkingMonster(this,"Alien", 1024, 768 - 8 - 98 ,0, 1024 , -1, 500) );
				vMonsters.add( new WalkingMonster(this,"Alien", 1024, 768 - 8 - 98 ,0, 1024 , -1, 700) );
				break;
			case 17:
				vMonsters.add( new FlyingMonster(this,"Ship", 100, 300 ,0, 1024 , -1, 0) );
				
				vMonsters.add(new SpawningMonster(this,"Hu",1024/2,768-8-250,0,1024,-1,0));
				
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
		long elapsed = ((new Date()).getTime() - startTime)/1000;
		
		gop.receiveScore(player.score,elapsed);
		
		gop.setSize(omniFrame.getSize());
		
		
		omniLayout.show(omniPanel,"GAMEOVER");
		gop.requestFocus();
	}

	public void generateDrip(int xxx) {
		int[] g = { xxx, 326, (int) stageTime };
		drips.add(g);
	}

	public boolean isStageClear() {
		if (vMonsters.size()==0) {
			return true;
		} else {
			return false;
		}
	}


	private Color colorChoice(int color) {
		if (color == 1) {
			return new Color(139, 124, 99);
		} else if (color == 2) {
			return new Color(83, 60, 44);
		} else if (color == 3) {
			return new Color(146, 150, 154);
		} else if (color == 5) {
			return new Color(86, 137, 64);
		}else {
			return new Color(0, 0, 0);
		}
	}
	
	public boolean checkGameRun() {
		return gameRun;
	}
	
	private void drawBats(Graphics g) {
		if (bats.size() > 0) {
			for (int batIndex = 0; batIndex < bats.size(); batIndex++) {
				int[] bat = (int[]) bats.elementAt(batIndex);
				g.drawImage(imgmur.getImage(),bat[0], bat[1], null);
			}
		}
	}
	
	private void drawPlatforms(Graphics2D g2) {
		if (vPlatforms.size() > 0) {
			for (int b = 0; b < vPlatforms.size(); b++) {
				int[] plataforma = (int[]) vPlatforms.elementAt(b);
				g2.setPaint(colorChoice(plataforma[5]));
				Rectangle2D plat = new Rectangle2D.Double(plataforma[0], plataforma[1], plataforma[2], plataforma[3]);
				g2.fill(plat);
				g2.draw(plat);
			}
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
	
	private void drawRiverCreatures(Graphics2D g2) {
		if (stage == 2) {
			g2.drawImage(imgTIBU.getImage(), player.x - 13, (int) (768 - 52 - 5 * Math.sin(stageTime * 2 * Math.PI/1000)), null);
		} else if (stage == 13) {
			int xt = 0;
			if (player.x < 70) {
				xt = 70;
			} else if (player.x + 85 > 970) {
				xt = 970 - 85;
			} else {
				xt = player.x;
			}
			g2.drawImage(imgTIBU.getImage(), xt - 13, (int) (768 - 45 - 3 * Math.sin(stageTime * 2 * Math.PI/1000)), null);
		}
	}
	
	private void drawDrips(Graphics2D g2) {
		if (drips.size() > 0) {
			for (int b = 0; b < drips.size(); b++) {
				int[] drip = (int[]) drips.elementAt(b);
				int xp = drip[0];
				int yp = drip[1];
				int t0 = drip[2];
				g2.setPaint(Color.blue);
				yp += 0.5 * 0.3 * Math.pow((stageTime-t0)/100, 2);
				if (yp >= 768 - 9) {
					drips.remove(b);
					Ellipse2D el = new Ellipse2D.Double(xp, 768 - 9, 6, 6);
					g2.fill(el);
					g2.draw(el);
					playSound(clipDrip);
					el = new Ellipse2D.Double(xp - 10 + 3, 768 - 11 - 10 + 3,20, 20);
					g2.draw(el);
					el = new Ellipse2D.Double(xp - 15 + 3, 768 - 11 - 15 + 3,30, 30);
					g2.draw(el);
				} else {
					drip[1] = (int) yp;
					drips.remove(b);
					drips.insertElementAt(drip, b);
					Ellipse2D el = new Ellipse2D.Double(Math.pow(xp, 1), yp, 6, 6);
					g2.fill(el);
					g2.draw(el);
				}
			}
		}
	}
	
	private void drawMiscGraphics(Graphics2D g2) {
		if (stage == 9) {
			dripCounter += 1;
			if (dripCounter > 150) {
				generateDrip(869);
				dripCounter = 0;
			}
		}else if (stage == 10) {
			dripCounter += 1;
			if (dripCounter > 150 * random) {
				generateDrip(869);
				random = Math.random();
				dripCounter = 0;
			} else if ((dripCounter > 50 * random)
					&& (dripCounter < 50 * random + 2)) {
				generateDrip(166);
			} else if ((dripCounter > 60 * random)
					&& (dripCounter < 60 * random + 2)) {
				generateDrip(444);
			}
		}
		
		if (stage == 7) {
			ImageIcon imgtunel = new ImageIcon(getClass().getResource(miscImgFolder+"tunel.png"));
			g2.drawImage(imgtunel.getImage(), 1024 - 29, 768 - 204, null);
		}
		if (stage == 11) {
			ImageIcon imgar = new ImageIcon(getClass().getResource(miscImgFolder+"A1.png"));
			g2.drawImage(imgar.getImage(), 100, 768 - 6 - 377, null);
			imgar = new ImageIcon(getClass().getResource(miscImgFolder+"A2.png"));
			g2.drawImage(imgar.getImage(), 600, 768 - 6 - 286, null);
		}
		if (stage == 14) {
			if (yn == 768 - 50 - 384) {
				g2.drawImage(imgMoonInvader.getImage(), (int) xn, (int) yn, null);
			} else {
				if (MIanimationCounter <= 10) {
					g2.drawImage(imgMoonInvader2.getImage(), (int) xn, (int) yn, null);
					MIanimationCounter += 1;
				} else if ((MIanimationCounter > 10) && (MIanimationCounter <= 20)) {
					g2.drawImage(imgMoonInvader3.getImage(), (int) xn, (int) yn, null);
					MIanimationCounter += 1;
				} else {
					MIanimationCounter = 1;
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		chooseStage();
		
		Graphics2D g2 = (Graphics2D) g;
		
		Insets decoration = omniFrame.getInsets();
		vScale = (float) (omniFrame.getHeight()- decoration.top - decoration.bottom)/768;
		hScale = (float) (omniFrame.getWidth()- decoration.left - decoration.right)/1024;
		g2.scale(hScale, vScale);
		
		// stageTime += 1;
		stageTime = (int) (System.currentTimeMillis() - stageStartTime);
		
		// Background image
		g.drawImage(imgBackground, 0, 0, null);
		
		// Next stage sign
		if ((isStageClear()) && (stage != 14) && (stage != 17)) {
			g.drawImage(imgNextStage.getImage(), 1024 - 200, 200, null);
		}else if ((isStageClear())&&(stage==17)){
			g.drawImage(imgHallOfFame.getImage(), 1024 - 180, 768 -330, null);
		}
		
		// Sleeping bats
		drawBats(g);

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
				cortey = 720;
			}
			
			// Platform collision
			int platformIndex = 0;
			while ((!eliminated) && (platformIndex < vPlatforms.size())) {
				int[] plataforma = vPlatforms.elementAt(platformIndex);
				if (bullet.hitsPlatform((int[]) plataforma)) {
					// Record impact point
					int yp = plataforma[1];
					cortex = (bullet.y - yp) / (Math.tan(bullet.tita)) + bullet.x;
					cortey = yp;
					// Eliminate bullet
					eliminated = true;
				}
				platformIndex += 1;
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
			if (player.dir == 1) {
				posx = player.x;
				posy = player.y;
			} else {
				posx = player.x - 102;
				posy = player.y;
			}
			rotation.translate(posx, posy + 20);
			// Light
			if ((stage == 8) || (stage == 9) || (stage == 10)) {
				if (player.dir == 1) {
					g2.drawImage(imgLI.getImage(), rotation, null);
				} else {
					g2.drawImage(imgLI2.getImage(), rotation, null);
				}
			}
			g2.drawImage(player.getWeaponImage().getImage(), rotation, null);
		}

		// River creatures
		drawRiverCreatures(g2);

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
		
		// Drips
		drawDrips(g2);

		// Misc. graphics
		drawMiscGraphics(g2);
		
		
	}

}