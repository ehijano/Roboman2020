package monster;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

import panels.GamePanel;

public class Monster {
	public int health = 10;
	protected int h;
	protected int w;
	protected int xm0;
	protected int ym0;
	public boolean alive=true;
	public int xm;
	public int ym;
	protected int xi;
	protected int xf;
	protected int dir;
	protected int shootingDir;
	protected int animationFrequency = 10;
	protected int animationTime=0;
	protected int animation=1;
	public int tDelay;
	protected int shootFrequency=150;
	protected int shootTime=0;
	protected String imgCode="L1", code, audioFolder = "sounds/monsters/", imageFolder = "img/";
	protected boolean shooting = false;
	protected GamePanel gpInstance;
	public double xA,yA,xB,yB,intersection;

	public Monster(GamePanel gp,String code, int xm, int ym, int xi, int xf, int dir, int tDelay) {
		// Game Panel Instance
		gpInstance = gp;
		
		// Range
		this.xi = xi;
		this.xf = xf;
		// Direction
		this.dir = dir;
		this.tDelay = 10 * tDelay;
		
		// Kind of monster
		this.code = code;
		
		// Load images
		ImageIcon L1 = new ImageIcon(getClass().getResource(imageFolder+code+"L1"+".png"));
		
		w = L1.getIconWidth();
		h = L1.getIconHeight();
		
		// Original position
		xm0 = xm;
		ym0 = ym-h;
		// Position
		this.xm = xm;
		this.ym = ym-h;
	}
	
	public boolean touchesPlayer(int x,int y) {
		xA = Math.max(xm,x); 
		yA = Math.max(ym,y); 
		
		xB = Math.min(xm+w,x+30); 
		yB = Math.min(ym+h,y+50);
		
		intersection = Math.max(0.0, xB-xA)*Math.max(0.0 ,yB-yA);
		
		if (intersection>0) {
			return true;
		} else {
			return false;
		}
	}
	

	public void moveMonster(int x, int y) {

	}
	
	public void shoot() {

	}
	
	public void drawExtras(Graphics2D g2) {
		
	}
	
	public void setHP(int hpr) {
		health -= hpr;
		if (health<0) {
			health = 0;
		}else {
			gpInstance.playNewSound(audioFolder+code+"H.au");
		}
	}
	
	public double shootingDirection() {
		return dir;
	}
	
	public void die() {
		alive = false;
		gpInstance.playNewSound(audioFolder+code+"D.au");
		gpInstance.dropCurrency((int) xm , (int) (ym + h/2) , 1 );
	}
	
	public ImageIcon monsterImage() {
		return new ImageIcon(getClass().getResource(imageFolder+code+imgCode+".png"));
	}

	public boolean isShot(double X, double Y) {
		if (((X >= xm) && (X <= xm + w) && (Y >= ym) && (Y <= ym + h))
				|| ((X + 10 >= xm) && (X + 10 <= xm + w)
						&& (Y >= ym) && (Y <= ym + h))) {
			return true;
		}else {
			return false;
		}
	}

}
