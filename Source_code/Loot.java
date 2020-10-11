import javax.swing.ImageIcon;

public class Loot {
	
	protected Player player;
	protected int code,x,y, y0;
	protected long spawnTime;
	protected GamePanel gpInstance;
	
	public Loot(GamePanel gp,Player player, int code, int x, int y) {
		gpInstance = gp;
		this.player = player;
		this.code = code;
		this.x = x;
		this.y = y - 20;
		y0 = y - 20;
		spawnTime = System.currentTimeMillis();
		
	}
	
	public boolean touchesPlayer(int xP, int yP) {
		boolean corner1 = false, corner2 = false, corner3 = false, corner4 = false;
		if ((xP > x) && (xP < x + w()) && (yP < y + h()) && (yP > y)) {
			corner1 = true;
		} else if ((xP + 30 > x) && (xP + 30 < x + w()) && (yP < y + h())&& (yP > y)) {
			corner2 = true;
		} else if ((xP + 30 > x) && (xP + 30 < x + w())&& (yP + 50 < y + h()) && (yP + 50 > y)) {
			corner3 = true;
		} else if ((xP > x) && (xP < x + w()) && (yP + 50 < y + h())&& (yP + 50 > y)) {
			corner4 = true;
		}
		if ((x > xP) && (x < xP + 30) && (y < yP + 50) && (y > yP)) {
			corner1 = true;
		} else if ((x + w() > xP) && (x + w() < xP + 30) && (y < yP + 50)&& (y > yP)) {
			corner2 = true;
		} else if ((x + w() > xP) && (x + w() < xP + 30)&& (y + h() < yP + 50) && (y + h() > yP)) {
			corner3 = true;
		} else if ((x > xP) && (x < xP + 30) && (y + h() < yP + 50) && (y + h() > yP)) {
			corner4 = true;
		}
		return ((corner1) || (corner2) || (corner3) || (corner4));
	}
	
	public int h() {
		return -1;//Never used
	}
	public int w() {
		return -1;//Never used
	}
	
	public void pickUp() {
		
	}
	public void update() {
		long currentTime = System.currentTimeMillis();
		y = (int) (y0 + 15 * Math.sin(10*(currentTime-spawnTime)/(1000*2*Math.PI)));
	}
	
	public ImageIcon lootImage() {
		return null;
		// return new ImageIcon(getClass().getResource(imageFolder+code+".png"));
	}

}
