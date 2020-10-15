package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.AmmoLoot;
import loot.HPLoot;
import loot.Loot;
import loot.WeaponLoot;
import monster.ChasingMonster;
import monster.Monster;
import panels.GamePanel;
import player.Player;

public class Stage4 extends Stage{
	private Image imgBackground;

	public Stage4(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(4)+"p.png")).getImage();
		
		vLoot.add(new AmmoLoot(gpInstance,player,1, 210, 768 - 10 - 23));

		vLoot.add(new WeaponLoot(gpInstance,player,2,840, 739));
		
		vLoot.add(new HPLoot(gpInstance,player, 1,710, 768 - 10 - 33));

		vMonsters.add( new ChasingMonster(gpInstance,"SK", 700 - 100, 768 - floorh ,  0, 1024 , -1, 0) );
		
		player.setLocation(10,713);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(imgBackground, 0, 0, null);
		if (isStageClear()) {
			g2.drawImage(imgNextStage.getImage(), 1024 - 200, 200, null);
		}
	}
	
	
}