package stages;

import java.awt.Color;
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
import monster.ShootingMonster;
import monster.SpamMonster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage5 extends Stage{
	private Image imgBackground;

	public Stage5(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(5)+"p.png")).getImage();
		
		vLoot.add(new AmmoLoot(gpInstance,player,1, 220, 600 - 23));

		vLoot.add(new HPLoot(gpInstance,player, 1,470, 400 - 33));

		vPlatform.add(new Platform( 200, 600, 120, 15, false, Color.gray ));
		vMonsters.add( new ShootingMonster(gpInstance,"Soldier", 200, 600 ,  200,200 + 120 , 1, 0) );

		vPlatform.add(new Platform( 450, 400, 100, 15, false, Color.gray ));
		vMonsters.add( new ShootingMonster(gpInstance,"Soldier", 450, 400 , 450,450 + 100 , 1, 0) );

		vPlatform.add(new Platform( 600, 600, 120, 15, false, Color.gray ));
		vMonsters.add( new ShootingMonster(gpInstance,"Soldier",600 + 100, 600, 600, 600 + 100, -1, 0) );

		vMonsters.add( new ShootingMonster(gpInstance,"Soldier",1023, 768 - floorh, 0, 1024, -1, 600) );
		vMonsters.add( new WalkingMonster(gpInstance,"SUV", 1023, 768 - floorh,  0, 1024 , -1, 750) );
		vMonsters.add( new ShootingMonster(gpInstance,"Soldier",1023, 768 - floorh, 0, 1024, -1, 900) );

		vMonsters.add( new FlyingMonster(gpInstance,"Helicopter",1023, 160,  0, 1024 , -1, 30) );
		vMonsters.add( new FlyingMonster(gpInstance,"Helicopter",1023, 260,  0, 1024 , -1, 200) );
		
		vMonsters.add( new SpamMonster(gpInstance,"Tank",1024, 768 - floorh,  0, 1024 , -1, 1100) );
		
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