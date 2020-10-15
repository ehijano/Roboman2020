package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.Loot;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage0 extends Stage{
	private Image imgBackground;

	public Stage0(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(0)+"p.png")).getImage();
		
		
		Platform plat = new Platform( 700, 650, 120, 15, false, colorBrown );
		vPlatform.add(plat);

		vMonsters.add( new WalkingMonster(gpInstance,"Slug", 700, plat.yp, 700, 820 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug", 810, 768 - floorh, 1, 1024, -1, 0) );
		
		
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
