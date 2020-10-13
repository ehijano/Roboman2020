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
	public double xA,yA,xB,yB,intersection;

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