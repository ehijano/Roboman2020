package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import javax.swing.ImageIcon;
import environment.Platform;
import loot.AmmoLoot;
import loot.Loot;
import monster.FlyingMonster;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage3 extends Stage{
	private Image imgBackground;

	public Stage3(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(3)+"p.png")).getImage();
		
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton",713, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton",413, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton",1013, 768 -floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 513, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 913, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 953, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 933, 768 -floorh, 0,1024 , -1, 0) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 1024, 768 - floorh, 0,1024 , -1, 600) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 1024, 768 -floorh, 0,1024 , -1, 620) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 1024, 768 - floorh, 0,1024 , -1, 640) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 1024, 768 - floorh, 0,1024 , -1, 660) );
		vMonsters.add( new WalkingMonster(gpInstance,"Skelleton", 1024, 768 -floorh, 0,1024 , -1, 680) );
		
		vMonsters.add( new FlyingMonster(gpInstance,"Bat",1024, 550,  0, 1024 , -1, 700) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat",1024, 550,  0, 1024 , -1, 800) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat", 1024, 450,  0, 1024 , -1, 900) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat", 1024, 400,  0, 1024 , -1, 950) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat", 1024, 570,  0, 1024 , -1, 400) );

		
		vLoot.add(new AmmoLoot(gpInstance,player,1, 210, 768 - 10 - 23));
		vLoot.add(new AmmoLoot(gpInstance,player,1, 810, 768 - 10 - 23));
		
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