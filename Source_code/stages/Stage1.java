package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.Loot;
import loot.MoneyLoot;
import loot.ShieldLoot;
import loot.WeaponLoot;
import monster.FlyingMonster;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage1 extends Stage{
	private Image imgBackground;

	public Stage1(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(1)+"p.png")).getImage();
		
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",700, 650, 700, 820 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug", 810, 768 - 8,  0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug", 280, 768 - 8 ,  0,1024 , -1, 0) );
		
		vMonsters.add( new FlyingMonster(gpInstance,"Bee",1, 200,  0, 1024 , -1, 400) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bee",1024, 200,  0, 1024 , -1, 400) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bee", 300, 200,  0, 1024 , 1, 0) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bee",1, 500,  0, 1024 , -1, 700) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Worm",1023,768 - floorh,  0, 1024  , -1, 600) );
		vMonsters.add( new WalkingMonster(gpInstance,"Worm",1023,768 - floorh ,  0, 1024  , -1, 400) );
		vMonsters.add( new WalkingMonster(gpInstance,"Worm",1023,768 - floorh ,  0, 1024  , -1, 100) );
		vMonsters.add( new WalkingMonster(gpInstance,"Worm",1023,768 - floorh,  0, 1024  , -1, 0) );

		
		vLoot.add(new WeaponLoot(gpInstance,player,1,300,739));

		vLoot.add(new ShieldLoot(gpInstance,player,1,100, 729 ));

		vPlatform.add(new Platform( 700, 650, 120, 15, false, colorBrown ));
		vPlatform.add(new Platform( 570, 550, 120, 15, false, colorBrown ));
		vPlatform.add(new Platform( 370, 350, 120, 15, false, colorBrown ));
		
		for(int i=0; i<3; i++) {
			vLoot.add(new MoneyLoot(gpInstance,player, 1, 600+40*i, 739 ));
		}
		
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
