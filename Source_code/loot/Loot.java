package loot;
import javax.swing.ImageIcon;
import panels.GamePanel;
import player.Player;

public class Loot {
	
	protected Player player;
	protected int code;
	public int x;
	public int y;
	protected int y0;
	protected long spawnTime;
	protected GamePanel gpInstance;
	public double xA,yA,xB,yB,intersection;
	
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
		xA = Math.max(x,xP); 
		yA = Math.max(y,yP); 
		
		xB = Math.min(x+w(),xP+30); 
		yB = Math.min(y+h(),yP+50);
		
		intersection = Math.max(0.0, xB-xA)*Math.max(0.0 ,yB-yA);
		
		if (intersection>0) {
			return true;
		} else {
			return false;
		}
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
