import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Platform {
	public int xp,yp,wp,hp;
	public Color color;
	public boolean playerStanding = false;
	public Platform(int xp,int yp,int wp,int hp, boolean ps,Color color) {
		this.xp = xp;
		this.yp = yp;
		this.wp = wp;
		this.hp = hp;
		this.color = color;
		this.playerStanding = ps;
	}
	
	public void draw(Graphics2D g2) {
		g2.setPaint(color);
		Rectangle2D plat = new Rectangle2D.Double(xp, yp, wp, hp);
		g2.fill(plat);
		g2.draw(plat);
	}
	
	public boolean belowPlayer(int x, int y) {
		if ((x + 30 > xp -1) && (x < xp + wp +1) && (y + 50 > yp)) {
			return true;
		}else {
			return false;
		}
	}
	
	public double playerYDistance(int yfoot) {
		return yfoot - yp;
	}
	
	public void setStanding(boolean info) {
		playerStanding = info;
	}
	

}

/*
vPlatforms2 = gpInstance.getPlatforms();
for (int b = 0; b < vPlatforms2.size(); b++) {
	int[] plat = (int[]) vPlatforms2.elementAt(b);
	int xp = plat[0];
	int yp = plat[1];
	int standingOn = plat[4];
	if ((x + 30 > xp -1) && (x < xp + plat[2]+1)) {
		if( (speedV < 0) && (y + 50 > yp -2* Math.abs(speedV) ) && (y + 50 < yp +2* Math.abs(speedV) )) {
			// Landed on platform
			standingOn = 1;
			jumpTime = System.currentTimeMillis();
			speedV = 0;
			jumping = false;
			falling = false;
			y = yp - 50 + 2;
			y0 = yp - 50 + 2;
		}
	} else if (plat[4] == 1) {
		// Fall from platform
		jumpTime = System.currentTimeMillis();
		y0 = y;
		standingOn = 2;
		jumping = false;
		falling = true;
	}
	plat[4] = standingOn;
	vPlatforms2.remove(b);
	vPlatforms2.insertElementAt(plat, b);
}
*/