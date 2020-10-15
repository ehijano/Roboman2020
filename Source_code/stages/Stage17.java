package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.Loot;
import monster.FlyingMonster;
import monster.Monster;
import monster.SpawningMonster;
import panels.GamePanel;
import player.Player;

public class Stage17 extends Stage{
	private Image imgBackground;
	private ImageIcon imgHallOfFame;

	public Stage17(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgHallOfFame = new ImageIcon(getClass().getResource(miscFolder+"hof.png"));
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(17)+"p.png")).getImage();
		
		vMonsters.add( new FlyingMonster(gpInstance,"Ship", 100, 350 ,0, 1024 , -1, 0) );
		
		vMonsters.add(new SpawningMonster(gpInstance,"Hu",1024/2,768 - floorh,0,1024,-1,0));
		
		player.setLocation(10,713);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(imgBackground, 0, 0, null);
		if (isStageClear()) {
			g2.drawImage(imgHallOfFame.getImage(), 1024 - 180, 768 -330, null);
		}
	}
	
	@Override
	public double gravity() {
		return 0.4;
	}
	
	@Override
	public double drag() {
		return 0.1 * 0.4 * Math.pow(0.7, -1);
	}
	
	@Override
	public void enforceBounds() {
		if ((player.x > 1024 - 30) && (isStageClear())) {// Stage ends
			gpInstance.finalScreen();
			gpInstance.setVisible(false);
			gpInstance.setGameRun(false);
		}else {// Otherwise respect screen bounds
			player.fixOutOfBounds();
		}
	}
	
	
}