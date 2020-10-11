import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SpawningMonster extends Monster{
	
	private int hSpawn, wSpawn;
	private int slowCounter = 0;
	
	public SpawningMonster(GamePanel gp,String code, int xm, int ym, int xi, int xf, int dir, int tDelay) {
		super(gp,code, xm, ym, xi, xf, dir, tDelay);
		
		super.health = 1000;
		super.shootFrequency = 600;
		
		Monster auxMonster = new WalkingMonster(gpInstance,"Alien",0,0,0,0,1,0);
		hSpawn = auxMonster.h;
		wSpawn = auxMonster.w;
		
		try {
			clipShoot = gpInstance.loadSound(audioFolder+code+"S.au");
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void moveMonster(int x, int y) {
		// Direction
		if ((xm <= xi) || (x > xm + w/2)) {
			dir = 1;
		} else if ((xm >= xf - w) || (x < xm + w/2)) {
			dir = -1;
		}
		
		// Movement
		if (slowCounter > 1) {
			slowCounter = 0;
			xm += dir;
		}else {slowCounter +=1;}
		
		switch(dir) {
		case 1: imgCode = "R"; break;
		case -1: imgCode = "L"; break;
		}
		
		// Animation
		if (animationTime >= animationFrequency) {
			animationTime = 0;
			if (animation == 1) {
				animation = 2;
			} else {
				animation = 1;
			}
		} else {
			animationTime += 1;
		}
		
		imgCode = imgCode + Integer.toString(animation);
	}
	
	@Override
	public void shoot() {
		if ( (shootTime > shootFrequency) ){
			shootTime = 0;
			gpInstance.playSound(clipShoot);
			Monster newMonster = new WalkingMonster(gpInstance,"Alien",xm +(dir+1)*(w-wSpawn)/2 ,ym+h-hSpawn,0,1024,dir,0);
			gpInstance.spawnMonster(newMonster);
		} else {
			shootTime += 1;
		}
	}
	
	@Override
	public void drawExtras(Graphics2D g2) {
		Rectangle2D crec = new Rectangle2D.Double(1024/2 - 250 , 200, health/2 , 15);
		Rectangle2D crec2 = new Rectangle2D.Double(1024/2 - 250, 200, 500, 15);
		g2.setPaint(Color.LIGHT_GRAY);
		g2.fill(crec2);
		g2.setPaint(Color.red);
		g2.fill(crec);
		g2.setPaint(Color.black);
		g2.draw(crec2);
		g2.setPaint(Color.orange);
		g2.setFont(new Font("Arial", Font.BOLD, 20));
		g2.drawString("Boss HP", 1024/2 - 20 , 190 );
	}
	

}
