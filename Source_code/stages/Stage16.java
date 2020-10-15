package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import javax.swing.ImageIcon;
import environment.Platform;
import loot.AmmoLoot;
import loot.HPLoot;
import loot.Loot;
import monster.FlyingMonster;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage16 extends Stage{
	private Image imgBackground;

	public Stage16(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(16)+"p.png")).getImage();
		
		vLoot.add(new AmmoLoot(gpInstance,player, 1,310, 768 - 10 - 33));
		vLoot.add(new AmmoLoot(gpInstance,player, 1,410, 768 - 10 - 33));
		vLoot.add(new HPLoot(gpInstance,player, 1,810, 768 - 10 - 33));
		vMonsters.add( new FlyingMonster(gpInstance,"Ship", 100, 200 ,0, 1024 , -1, 0) );
		vMonsters.add( new FlyingMonster(gpInstance,"Ship", 500, 200 ,0, 1024 , -1, 0) );
		vMonsters.add( new FlyingMonster(gpInstance,"Ship", 800, 200 ,0, 1024 , -1, 0) );

		vMonsters.add( new WalkingMonster(gpInstance,"Alien", 1024,768 - floorh,0, 1024 , -1, 300) );
		vMonsters.add( new WalkingMonster(gpInstance,"Alien", 1024, 768 - floorh ,0, 1024 , -1, 500) );
		vMonsters.add( new WalkingMonster(gpInstance,"Alien", 1024, 768 - floorh ,0, 1024 , -1, 700) );
		
		player.setLocation(10,713);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(imgBackground, 0, 0, null);
		if (isStageClear()) {
			g2.drawImage(imgNextStage.getImage(), 1024 - 200, 200, null);
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
	
	
}