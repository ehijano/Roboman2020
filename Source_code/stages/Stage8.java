package stages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import javax.swing.ImageIcon;
import environment.Platform;
import loot.Loot;
import monster.FlyingMonster;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage8 extends Stage{
	private Image imgBackground;

	public Stage8(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(8)+"p.png")).getImage();
		
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",810, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",200, 768 - floorh, 0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",512, 553 , 512, 626 , 1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",280, 768 - floorh,0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie",612, 768 - floorh,0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie",712, 768 - floorh,0,1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie",912, 768 - floorh,0,1024 , -1, 0) );
		
		vMonsters.add( new FlyingMonster(gpInstance,"Bat",1024, 500, 0, 1024 , -1, 400) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat",1024, 450, 0, 1024 , -1, 100) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat", 1024, 350, 0, 1024 , -1, 400) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bat",1024, 300, 0, 1024 , -1, 100) );

		vPlatform.add(new Platform( 512, 553, 626 - 512, 14, false, Color.gray ));
		
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