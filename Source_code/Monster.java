import java.awt.Graphics2D;
import javax.swing.ImageIcon;

public class Monster {
	protected int health = 10, h, w,xm0,ym0;
	//protected Clip clipDeath, clipHurt, clipShoot;
	//protected static Map<String, Clip> mClipDeath, mClipHurt,mClipShoot;
	protected boolean alive=true;
	protected int xm,ym,xi,xf,dir,shootingDir, animationFrequency = 10,animationTime=0,animation=1,tDelay,shootFrequency=150,shootTime=0;
	protected String imgCode="L1", code, audioFolder = "sounds/monsters/", imageFolder = "img/monsters/";
	protected boolean shooting = false;
	protected GamePanel gpInstance;

	public Monster(GamePanel gp,String code, int xm, int ym, int xi, int xf, int dir, int tDelay) {
		// Game Panel Instance
		gpInstance = gp;
		// Original position
		xm0 = xm;
		ym0 = ym;
		// Position
		this.xm = xm;
		this.ym = ym;
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
	}
	
	public boolean touchesPlayer(int x,int y) {
		boolean corner1 = false, corner2 = false, corner3 = false, corner4 = false;
		if ((x > xm) && (x < xm + w) && (y < ym + h) && (y > ym)) {
			corner1 = true;
		} else if ((x + 30 > xm) && (x + 30 < xm + w) && (y < ym + h)&& (y > ym)) {
			corner2 = true;
		} else if ((x + 30 > xm) && (x + 30 < xm + w)&& (y + 50 < ym + h) && (y + 50 > ym)) {
			corner3 = true;
		} else if ((x > xm) && (x < xm + w) && (y + 50 < ym + h)&& (y + 50 > ym)) {
			corner4 = true;
		}
		if ((xm > x) && (xm < x + 30) && (ym < y + 50) && (ym > y)) {
			corner1 = true;
		} else if ((xm + w > x) && (xm + w < x + 30) && (ym < y + 50)&& (ym > y)) {
			corner2 = true;
		} else if ((xm + w > x) && (xm + w < x + 30)&& (ym + h < y + 50) && (ym + h > y)) {
			corner3 = true;
		} else if ((xm > x) && (xm < x + 30) && (ym + h < y + 50) && (ym + h > y)) {
			corner4 = true;
		}
		return ((corner1) || (corner2) || (corner3) || (corner4));
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