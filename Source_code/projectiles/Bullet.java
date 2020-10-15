package projectiles;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

import environment.Platform;
import monster.Monster;
import panels.GamePanel;

public class Bullet {
	
	protected GamePanel gpInstance;
	protected int speed;
	public int damage;
	protected int code;
	public boolean enemy;
	public double x;
	public double y;
	protected double w;
	protected double h;
	protected double dir;
	public double tita;
	protected String audioFolder="sounds/bullet/";
	protected ImageIcon imgBOMB, imgbala1;
	
	public static Clip clipBat, clipHit, clipAuch, clipBoom;
	
	public Bullet(GamePanel gp,double x,double y, int speed, double tita, double dir, int damage, boolean enemy, int code ) {
		gpInstance = gp;
		this.damage = damage;
		this.speed = speed;
		this.code = code;
		this.enemy = enemy;
		this.dir = dir;
		this.tita = tita;
		this.x = x;
		this.y = y;
		
		if (code==4) {
			imgbala1 = new ImageIcon(getClass().getResource("img/bala1.png"));
		} else if (code==3) {
			imgBOMB = new ImageIcon(getClass().getResource("img/Minivida.png"));
		}
		
		if (clipHit==null) {
			try {
				clipBat = gpInstance.loadSound(audioFolder+"mur.au");
				clipHit = gpInstance.loadSound(audioFolder+"rebound.au");
				clipAuch = gpInstance.loadSound(audioFolder+"auch.au");
				//clipBoom = gpInstance.loadSound(audioFolder+"boom.au");
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean isOutOfBounds() {
		if ((x + w > 1024) || (x < 0) || (y > 760) || (y < 0)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public boolean hitsPlayer(int xP,int yP) {
		 boolean condition = ( (((x >= xP) && (x <= xP + 30) && (y >= yP) && (y <= yP + 50))|| ((x + 10 >= xP) && (x + 10 <= xP + 30) && (y >= yP) && (y <= yP + 50)))   );
		 if (condition) {
			 gpInstance.playSound(clipAuch);
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public boolean hitsPlatform(int[] plataforma) {
		int xp = plataforma[0];
		int yp = plataforma[1];
		int ancho = plataforma[2];
		int alto = plataforma[3];
		
		double Xd = x + speed * Math.cos(tita) * dir;
		double Yd = y - speed * Math.sin(tita) * dir;
		
		double cortex = (y - yp) / (Math.tan(tita)) + x;
		double cortey = yp;
		boolean condicion1 = false, condicion2 = false, condicion3 = false;
		if ((cortex >= xp) && (cortex <= xp + ancho)) {
			condicion1 = true;
		}
		if ((cortey > yp - 3) && (cortey < yp + alto + 3)) {
			condicion2 = true;
		}
		if (Math.sqrt(Math.pow((cortex - x), 2)+ Math.pow((cortey - y), 2)) < Math.sqrt(Math.pow((Xd - x), 2)+ Math.pow((Yd - y), 2))) {
			condicion3 = true;
		}
		
		if ((condicion1) && (condicion2) && (condicion3)) {
			if (code!= 3) {gpInstance.playSound(clipHit);}
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hitsPlat(Platform plat) {
		double x2 = x + speed * Math.cos(tita) * dir;
		double y2 = y - speed * Math.sin(tita) * dir;
		
		double cutx = (y - plat.yp) / (Math.tan(tita)) + x;
		double cuty = plat.yp;
		
		boolean c1 = false, c2 = false, c3 = false;
		
		if ((cutx >= plat.xp) && (cutx <= plat.xp + plat.wp)) {
			c1 = true;
		}
		if ((cuty > plat.yp - 3) && (cuty < plat.yp + plat.hp + 3)) {
			c2 = true;
		}
		if (Math.sqrt(Math.pow((cutx - x), 2)+ Math.pow((cuty - y), 2)) < Math.sqrt(Math.pow((x2 - x), 2)+ Math.pow((y2 - y), 2))) {
			c3 = true;
		}
		
		if ((c1) && (c2) && (c3)) {
			if (code!= 3) {gpInstance.playNewSound(audioFolder+"rebound.au");}
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean hitsMonster(Monster monster) {
		return monster.isShot(x,y);
	}
	
	public boolean hitsBat(int[] bat) {
		int xm = bat[0];
		int ym = bat[1];
		if (((x >= xm) && (x <= xm + 48) && (y >= ym) && (y <= ym + 40))
				|| ((x + 10 >= xm) && (x + 10 <= xm + 48)
						&& (y >= ym) && (y <= ym + 40))) {
			gpInstance.playSound(clipBat);
			return true;
		}else {
			return false;
		}
	}
	
	public void propagate() {
		// Update bullet
		x +=  speed * Math.cos(tita) * dir;
		y -=  speed * Math.sin(tita) * dir;
	}
	
	public void drawBullet(Graphics2D g) {
		Graphics2D gInstance = g;
		Rectangle2D rec = new Rectangle2D.Double(x, y, 20, 4);
		switch(code) {
		case 0:
			gInstance.setPaint(Color.red);
			Ellipse2D el = new Ellipse2D.Double(x, y, 5, 5);
			gInstance.draw(el);
			gInstance.fill(el);
			break;
		case 1:
			gInstance.setPaint(new Color(136, 16, 249));
			rec = new Rectangle2D.Double(x, y, 20, 4);
			AffineTransform rotation = AffineTransform.getRotateInstance(-tita, x + 10 * Math.cos(tita) * dir, y - 2* Math.sin(tita) * dir);
			GeneralPath elt = new GeneralPath(rotation.createTransformedShape(rec));
			gInstance.fill(elt);
			break;
		case 2:
			gInstance.setPaint(Color.red);
			rec = new Rectangle2D.Double(x + 20, y, 10 , 3);
			gInstance.draw(rec);
			gInstance.setPaint(Color.orange);
			gInstance.fill(rec);
			break;
		case 3:
			gInstance.drawImage(imgBOMB.getImage(), (int) x, (int) y + 5, null);
			break;
		case 4:
			gInstance.drawImage(imgbala1.getImage(), (int) x, (int) y, null);
		}
	}
	
	public void drawExplosion(Graphics2D g,double cortex, double cortey) {
		Graphics2D gInstance = g;
		Ellipse2D el1 = new Ellipse2D.Double(cortex, cortey,10, 10);
		Ellipse2D el2 = new Ellipse2D.Double(cortex, cortey,10, 10);
		switch(code) {
		case 0:
			gInstance.setPaint(Color.orange);
			el1 = new Ellipse2D.Double(cortex, cortey,10, 10);
			gInstance.draw(el1);
			gInstance.setPaint(Color.red);
			el2 = new Ellipse2D.Double(cortex, cortey, 5,5);
			gInstance.draw(el2);
			break;
		case 1:
			gInstance.setPaint(Color.pink);
			el1 = new Ellipse2D.Double(cortex-15, cortey-15,30, 30);
			gInstance.draw(el1);
			gInstance.fill(el1);
			gInstance.setPaint(Color.magenta);
			el2 = new Ellipse2D.Double(cortex-5, cortey-5, 10,10);
			gInstance.draw(el2);
			gInstance.fill(el2);
			break;
		case 2:
			gInstance.setPaint(Color.orange);
			el1 = new Ellipse2D.Double(cortex, cortey,10, 10);
			gInstance.draw(el1);
			gInstance.setPaint(Color.red);
			el2 = new Ellipse2D.Double(cortex, cortey, 5,5);
			gInstance.draw(el2);
			break;
		case 3:
			gInstance.setPaint(Color.orange);
			el1 = new Ellipse2D.Double(x - 24 + 10, y - 24 + 10, 48, 48);
			gInstance.draw(el1);
			gInstance.fill(el1);
			gInstance.setPaint(Color.red);
			el2 = new Ellipse2D.Double(x - 14 + 10, y - 14 + 10, 28, 28);
			gInstance.draw(el2);
			gInstance.fill(el2);
			gpInstance.playNewSound(audioFolder+"boom.au");
			break;
		case 4:
			
			break;
		}
	}
	
}
