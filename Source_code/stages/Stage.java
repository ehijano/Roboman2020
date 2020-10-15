package stages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.Loot;
import monster.Monster;
import panels.GamePanel;
import player.Player;

public class Stage {
	public long stageTime, stageStartTime;
	protected Vector<Platform> vPlatform;
	protected Vector<Monster> vMonsters;
	protected Vector<Loot> vLoot;
	protected static Color colorBrown, colorWood;
	GamePanel gpInstance;
	protected static String stagesFolder="img/stages/",miscFolder="img/misc/";
	protected Player player;
	protected static ImageIcon imgNextStage;
	protected static int floorh = 8;
	
	public Stage(GamePanel gpInstance, Vector<Platform> vPlatform,Vector<Monster> vMonsters,Vector<Loot> vLoot, Player player) {
		this.gpInstance = gpInstance;
		this.vPlatform = vPlatform;
		this.vMonsters = vMonsters;
		this.vLoot = vLoot;
		this.player = player;
		
		stageStartTime = System.currentTimeMillis();
		
		colorBrown = new Color(139, 124, 99);
		colorWood = new Color(83, 60, 44);
		
		imgNextStage = new ImageIcon(getClass().getResource(miscFolder+"flecha.png"));
	}
	
	public void update() {
		stageTime = (int) (System.currentTimeMillis() - stageStartTime);
	}
	
	public void draw(Graphics2D g2) {
		
	}
	
	public void drawExtras(Graphics2D g2) {
		
	}
	
	public void enforceBounds(Player player) {
		
	}
	
	public double gravity() {
		return 0.7;
	}
	
	public double drag() {
		return 0.1;
	}
	
	public boolean isStageClear() {
		if (vMonsters.size()==0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean playingMovie() {
		return false;
	}
	
	public void endStage() {
		player.setLocation(10,713);
		player.noJump();
		gpInstance.changeStage();
	}
	
	public void enforceBounds() {
		if ((player.x > 1024 - 30) && (isStageClear())) {// Stage ends
			endStage();
		}else {// Otherwise respect screen bounds
			player.fixOutOfBounds();
		}
	}
	
	public Vector<int[]> getBats() {
		return new Vector<int[]>();
	}

}
