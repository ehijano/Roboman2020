package player;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

import environment.Platform;
import panels.GamePanel;

public class Player {
	private static int pistolPower = 2;
	public double tita = 0;
	protected double speedV = 0.0; 
	public int currentWeapon = 0;
	protected int currentHelmet = 0;
	public static int dir=1;
	protected int walkingDir = 0;
	protected int animationTime = 10;
	protected int animationCount = 0;
	public int health;
	protected int shield=0;
	protected static int ammo = 100;
	public int score=0;
	public int x = 10;
	public int y= 713;
	protected int y0;
	protected int speedH=2;
	protected int animationNumber = 1;
	protected long jumpTime=0;
	public int maxHealth = 100;
	public boolean visible = true;
	protected boolean jumping = false;
	protected boolean falling = false;
	protected boolean isWalkingRight=false;
	protected boolean isWalkingLeft=false;
	protected boolean clicking = false;
	protected String miscImgFolder="img/misc/";
	protected static String audioFolder="sounds/events/";
	protected String lootImgFolder="img/loot/";
	private ImageIcon imgbalas,imgESC,imgCoin,imgINF;
	private Weapon weapon;
	private Vector<Integer> availableWeapons;
	private GamePanel gpInstance;
	private Clip clipJump;
	private Vector<Weapon> vWeapons;
	private Vector<ImageIcon> vWeaponImages;
	
	public Player(GamePanel gp) {
		gpInstance = gp;
		health = maxHealth;
		// Weapon
		weapon = new Pistol(this,gpInstance,0);
		availableWeapons = new Vector<Integer>();
		availableWeapons.add(0);
		
		ammo = 100;
		score = 0;
		
		 // HUD Images
		imgINF = new ImageIcon(getClass().getResource(miscImgFolder+"inf.png"));
		imgbalas = new ImageIcon(getClass().getResource(miscImgFolder+"balas.png"));
		imgESC = new ImageIcon(getClass().getResource(miscImgFolder+"shield.png"));
		imgCoin = new ImageIcon(getClass().getResource(miscImgFolder+"Money1.png"));
		
		// Weapon Images
		vWeaponImages = new Vector<ImageIcon>();
		for(int i=0; i<6; i++) {
			vWeaponImages.add(new ImageIcon(getClass().getResource(lootImgFolder+"Weapon"+Integer.toString(i)+".png")));
		}
		
		if(clipJump==null){
			try {
				clipJump = gpInstance.loadSound(audioFolder+"drip.au");
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
		
		vWeapons = new Vector<Weapon>();
		
		vWeapons.add(new Pistol(this,gp,0));
		vWeapons.add(new Cannon(this,gp,1));
		vWeapons.add(new AK47(this,gp,2));
		vWeapons.add(new ShotGun(this,gp,3));
		vWeapons.add(new MiniGun(this,gp,4));
		vWeapons.add(new RayGun(this,gp,5));

	}
	
	public void noJump() {
		jumping=false;
		falling=false;
	}
	
	public void setLocation(int newX,int newY) {
		x= newX;
		y= newY;
	}
	
	public void interactPlatforms(Vector<Platform> vP) {
		for (int b = 0; b < vP.size(); b++) {
			Platform plat = vP.elementAt(b);
			if ((speedV < 0) && (plat.belowPlayer(x,y)) && ( plat.playerYDistance(y + 50) < 2*Math.abs(speedV) )) {
				plat.setStanding(true);
				jumpTime = System.currentTimeMillis();
				speedV = 0;
				jumping = false;
				falling = false;
				y = plat.yp - 50 + 2;
				y0 = plat.yp - 50 + 2;
			} else if (jumping) {
				plat.playerStanding = false;
			}
			if ((plat.playerStanding) && (!plat.belowPlayer(x,y))) {
				plat.setStanding(false);
				jumpTime = System.currentTimeMillis();
				y0 = y;
				jumping = false;
				falling = true;
			}
		}
	}
	
	public void update() {
		long currentTime = System.currentTimeMillis();
		
		if (weapon.automatic()) {
			weapon.automaticShot(clicking);
		}
		
		// Movement 
		if (((isWalkingRight)||(isWalkingLeft)) && (!jumping) && (!falling)) {
			speedV = 0;
			x += walkingDir*speedH;
			animateWalking();
			
		} else if (jumping) {// Normal jump
			double gravity = gpInstance.getGravity();
			double drag = gpInstance.getDrag();
			long t = (currentTime - jumpTime)/10;
			
			y = (int) (y0 - (20 * t - 0.5 * gravity * t * t));
			x = (int) (x + t * walkingDir * drag);
			speedV = 20 - 2 * 0.5 * gravity * t;
			
		} else if (falling) {
			long t = (currentTime - jumpTime)/10;
			y = (int) (y0 - (-0.5 * 0.7 * t * t));
			x = (int) (x + t * walkingDir * 0.1);
			speedV = -2 * 0.5 * 0.7 * t;
		}
	}
	
	public void fixOutOfBounds() {
		if (x < 0) {
			x = 0;
		} else if ((x > 1024 - 30)) {
			x = 1024 - 30 - 1;
		}
		
		if (y > 713) {
			y0 = 713;
			y = 713;
			jumping = false;
			falling = false;
		}
		
	}
	
	public void setState(boolean iR, boolean iL, int d,int wd, boolean c) {
		isWalkingRight = iR;
		isWalkingLeft = iL;
		dir = d;
		walkingDir = wd;
		clicking = c;
	}
	
	public ImageIcon getBodyImage() {
		if ((jumping)||(falling)) {
			return new ImageIcon(getClass().getResource("img/movement/"+Integer.toString(dir+1)+"persjump.png"));
		}else {
			return new ImageIcon(getClass().getResource("img/movement/"+Integer.toString(dir+1)+"pers"+Integer.toString(animationNumber)+".png"));
		}
	}
	
	public ImageIcon getHelmetImage() {
		return new ImageIcon(getClass().getResource("img/helmets/"+ Integer.toString(currentHelmet) +"helmet"+Integer.toString(dir+1)+".png"));
	}
	
	public ImageIcon getWeaponImage() {
		return weapon.getImage();
	}
	
	public Vector<Integer> getAvailableWeapons() {
		return availableWeapons;
	}
	
	public void jump() {
		if((!falling)&&(!jumping)) {
			jumping= true;
			jumpTime = System.currentTimeMillis();
			y0 = y;

			gpInstance.playNewSound(audioFolder+"drip.au");
		}
	}
	
	public void pickWeapon(int code) {
		availableWeapons.add(code);
		changeWeapon(code);
	}
	
	private void animateWalking() {
		if (animationCount > animationTime) {
			animationCount = 0;
			if (animationNumber == 1) {
				animationNumber = 2;
			} else {
				animationNumber = 1;
			}
		} else {
			animationCount += 1;
		}
	}
	
	public void changeWeapon(int code) {
		currentWeapon = code;
		weapon = vWeapons.elementAt(code);
	}
	
	public void ammo(int am) {
		ammo -= am;
		if (ammo<0) {ammo=0;}
	}
	
	public void heal(int hp) {
		health += hp;
	}
	
	public void money(int money) {
		score+= money;
	}
	
	public void setHelmet(int codeHelmet) {
		shield = codeHelmet*100;
		currentHelmet = codeHelmet;
	}
	
	public void changeAngle(double angle) {
		tita=angle;
	}
	public void reduceHP(int hpr) {
		if (shield == 0) {
			health -= hpr;
		} else if (shield < hpr) {
			health -= (hpr - shield);
			shield = 0;
		} else {
			shield -= hpr;
		}
		if (shield<=0) {
			setHelmet(0);
		}
		if (health < 0) {
			health = 0;
		}
	}
	
	public void setVisible(boolean vis) {
		visible = vis;
	}
	

	public void drawHUD(Graphics2D g2, int x0, int y0) {
		// Background
		Rectangle2D raux = new Rectangle2D.Double(x0, y0, 360, 100);
		g2.setPaint(Color.white);
		g2.fill(raux);
		
		// Available Weapons
		int currentPosition = availableWeapons.indexOf(currentWeapon);
		

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.5));
		if (currentPosition>0) {
			g2.drawImage(vWeaponImages.elementAt(availableWeapons.elementAt(currentPosition-1)).getImage(),x0+ 245, y0+10, null);
		}
		if (currentPosition<availableWeapons.size()-1) {
			g2.drawImage(vWeaponImages.elementAt(availableWeapons.elementAt(currentPosition+1)).getImage(),x0+ 245, y0+70, null);
		}
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 1.0));
		g2.drawImage(vWeaponImages.elementAt(currentWeapon).getImage(),x0+ 245, y0+40, null);
		g2.setPaint(Color.black);
		g2.drawString("Damage:", x0+310 ,y0+ 40);
		g2.setPaint(Color.red);
		g2.drawString(Integer.toString(weapon.power()), x0+310 ,y0+ 60);
		
		weapon.drawExtras(g2,x0,y0);
		
		// h.p.
		Rectangle2D healthrec = new Rectangle2D.Double(x0+23, y0+20, health * 2, 15);
		Rectangle2D healthrec2 = new Rectangle2D.Double(x0+23, y0+20, 100 * 2, 15);
		g2.setPaint(Color.LIGHT_GRAY);
		g2.fill(healthrec2);
		g2.setPaint(Color.red);
		g2.fill(healthrec);
		g2.setPaint(Color.black);
		g2.draw(healthrec2);

		// ammo
		g2.drawImage(imgbalas.getImage(), x0+20,y0+ 41, null);
		if (currentWeapon>0) {
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setPaint(Color.black);
			g2.drawString(Integer.toString(ammo), x0+ 45, y0+60);
		}else {
			g2.drawImage(imgINF.getImage(),x0+ 45, y0+45, null);
		}

		// money
		g2.drawImage(imgCoin.getImage(),x0+ 90, y0+41, null);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.setPaint(Color.black);
		g2.drawString(Integer.toString(score), x0+115, y0+60);

		// shield
		g2.drawImage(imgESC.getImage(), x0+170, y0+41, null);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.setPaint(Color.black);
		g2.drawString(Integer.toString(shield), x0+195,y0+ 60);
	}

	
	public void shootWeapon() {
		if (!weapon.automatic()) {
			if ((ammo>0)||(weapon.code==0)) {
				weapon.shoot();
			}else {
				weapon.empty();
			}
		}
	}
	
	private static class Weapon{
		
		protected int code;
		Player player;
		GamePanel gpInstance;
		protected long cdTimer;
		
		
		public Weapon(Player player,GamePanel gp, int code) {
			gpInstance = gp;
			this.player = player;
			this.code = code;
			
			cdTimer = System.currentTimeMillis()-cdThreshold(); //Starts able to shoot
		}
		
		public long cdThreshold() {
			return 100;
		}
		
		public void shoot() {
			long currentTime =  System.currentTimeMillis();
			if (currentTime-cdTimer >= cdThreshold()) {
				cdTimer = System.currentTimeMillis();
				gpInstance.addBullet(player.x , player.y, dir, player.tita, 0, power());
				gpInstance.playNewSound(audioFolder+Integer.toString(code)+"shot1.au");
				player.ammo(1);
			}
			//gpInstance.playNewSound(audioFolder+Integer.toString(code)+"shot1.au");
			//player.ammo(1);
			//gpInstance.addBullet(player.x , player.y, dir, player.tita, 0, power());
		}
		
		public int power() {
			return 1;
		}
		
		public void empty() {
			gpInstance.playNewSound(audioFolder+"click2.au");
		}
		
		
		private ImageIcon getImage() {
			return new ImageIcon(getClass().getResource("img/weapons/"+ Integer.toString(code) +"gun"+Integer.toString(dir+1)+ ".png"));
		}
		
		public boolean automatic() {
			if (code==4) {return true;}else {return false;}
		}
		
		public void automaticShot(boolean clicking) {

		}
		
		public void drawExtras(Graphics2D g2,int x0,int y0) {
			
		}
	}
	
	private static class Pistol extends Weapon{
		private long cdTimer,cdThreshold;
		//private static Clip clipShot;
		public Pistol(Player player, GamePanel gp,int code) {
			super(player,gp,code);
			
			cdThreshold = 500;
			cdTimer = System.currentTimeMillis()-cdThreshold; //Starts able to shoot
		}
		
		@Override
		public void shoot() {
			long currentTime =  System.currentTimeMillis();
			if (currentTime-cdTimer >= cdThreshold) {
				cdTimer = System.currentTimeMillis();
				gpInstance.addBullet(player.x , player.y, dir, player.tita, 0, power());
				gpInstance.playNewSound(audioFolder+Integer.toString(code)+"shot1.au");
			}
		}
		
		@Override
		public int power() {
			return pistolPower;
		}
	}
	
	private class Cannon extends Weapon{
		//private Clip clipShot;
		public Cannon(Player player, GamePanel gp,int code) {
			super(player,gp,code);
		}
		
		
		@Override
		public int power() {
			return 2;
		}
	}
	
	private static class AK47 extends Weapon{
		//private static Clip clipShot;
		public AK47(Player player, GamePanel gp,int code) {
			super(player,gp,code);
		}

		
		@Override
		public long cdThreshold() {
			return 50;
		}
		
		@Override
		public int power() {
			return 3;
		}
	}
	
	private static class ShotGun extends Weapon{
		private long cdTimer, cdThreshold;
		//private static Clip clipShot;
		public ShotGun(Player player, GamePanel gp,int code) {
			super(player,gp,code);
			
			cdThreshold = 1000;
			cdTimer = System.currentTimeMillis()-cdThreshold; //Starts able to shoot
		}
		
		@Override
		public void shoot() {
			long currentTime =  System.currentTimeMillis();
			if ((currentTime-cdTimer >= cdThreshold)) {
				cdTimer = System.currentTimeMillis();

				gpInstance.playNewSound(audioFolder+Integer.toString(code)+"shot1.au");
				
				gpInstance.addBullet(player.x, player.y, dir, player.tita+0.157,0, power());
				gpInstance.addBullet(player.x, player.y, dir, player.tita, 0, power());
				gpInstance.addBullet(player.x, player.y, dir, player.tita-0.157,0, power());
				player.ammo(1);
			}

		}
		
		@Override
		public int power() {
			return 5;
		}
	}
	
	private static class MiniGun extends Weapon{
		private double heat=0.0,overheat=0.0, maxHeat=35;
		private long delayTimer=500,wheelTimer=0;
		private boolean shooting=false;
		private static Clip clipPS, clipOH;
		public MiniGun(Player player, GamePanel gp,int code) {
			super(player,gp,code);
			
			if(clipPS==null){
				try {
					//clipShot = gpInstance.loadSound(audioFolder+Integer.toString(code)+"shot1.au");
					clipPS = gpInstance.loadSound(audioFolder+"p2.au");
					clipOH = gpInstance.loadSound(audioFolder+"pss.au");
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		@Override
		public int power() {
			return 3;
		}
		
		@Override
		public long cdThreshold() {
			return 10;
		}
		
		@Override
		public void automaticShot(boolean clicking) {
			if ((!clicking) && (heat < maxHeat)) { // Cool-down
				delayTimer = 500;
				wheelTimer = 0;
				heat -= 0.2;
				overheat -= 0.2;
				if (heat < 0) {
					heat = 0;
					overheat = 0;
				}
			}
			if (clicking) {
				wheelTimer += 1;
			}
			if (heat < maxHeat) {
				if ( (ammo > 0)&& (wheelTimer > 3) && (clicking)) {
					if (delayTimer == 500) {
						delayTimer = 0;
						playPreShoot();
					} else if (delayTimer > 501) {
						delayTimer = 0;
					}
					if (delayTimer > 40) {
						wheelTimer = 0;
						heat += 1;
						overheat = heat;
						shooting = true;
						super.shoot();
						gpInstance.playNewSound(audioFolder+Integer.toString(code)+"shot1.au");
						player.ammo(1);
					} else {
						delayTimer += 1;
					}
				}
			} else {
				if (shooting) {
					playOverheat();
				}
				shooting = false;
				delayTimer = 500;
				overheat -= 0.1;
				if (overheat <= 0) {
					heat = 0;
					overheat = 0;
				} 
			}
		}
		private void playOverheat( ) {
			clipOH.setMicrosecondPosition(0);
			clipOH.start();
		}
		
		private void playPreShoot( ) {
			clipPS.setMicrosecondPosition(0);
			clipPS.start();
		}
		
		@Override
		public void drawExtras(Graphics2D g2,int x0,int y0) {
			Rectangle2D crec = new Rectangle2D.Double(x0+63, y0+75, heat * 2, 15);
			Rectangle2D crec2 = new Rectangle2D.Double(x0+63, y0+75, maxHeat * 2, 15);
			g2.setPaint(Color.LIGHT_GRAY);
			g2.fill(crec2);
			g2.setPaint(Color.red);
			g2.fill(crec);
			g2.setPaint(Color.black);
			g2.draw(crec2);
			g2.setPaint(Color.black);
			g2.setFont(new Font("Arial", Font.BOLD, 15));
			g2.drawString("Heat: ", x0+23, y0+75 + 15);
			
		}
	}
	
	
	private static class RayGun extends Weapon{
		private long cdTimer,cdThreshold;
		//private static Clip clipShot;
		public RayGun(Player player, GamePanel gp,int code) {
			super(player,gp,code);
			
			cdThreshold = 300;
			cdTimer = System.currentTimeMillis()-cdThreshold; //Starts able to shoot
		}
		
		@Override
		public long cdThreshold() {
			return 300;
		}
		
		@Override
		public void shoot() {
			long currentTime =  System.currentTimeMillis();
			if (currentTime-cdTimer >= cdThreshold) {
				cdTimer = System.currentTimeMillis();
				gpInstance.playNewSound(audioFolder+Integer.toString(code)+"shot1.au");
				player.ammo(1);
				gpInstance.addBullet(player.x , player.y, dir, player.tita, 1, power());
			}
		}
		
		@Override
		public int power() {
			return 10;
		}
	}

}
