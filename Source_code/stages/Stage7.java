package stages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import javax.swing.ImageIcon;
import environment.Platform;
import loot.Loot;
import loot.ShieldLoot;
import loot.WeaponLoot;
import monster.FlyingMonster;
import monster.Monster;
import monster.ShootingMonster;
import monster.SpamMonster;
import panels.GamePanel;
import player.Player;

public class Stage7 extends Stage{
	private Image imgBackground;
	private ImageIcon imgtunel;

	public Stage7(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(7)+"p.png")).getImage();
		imgtunel = new ImageIcon(getClass().getResource(GamePanel.miscImgFolder+"tunel.png"));
		vLoot.add(new WeaponLoot(gpInstance,player,3,200, 739));
		
		vPlatform.add(new Platform( 341, 515, 398 - 341, 15, false, Color.gray ));
		vPlatform.add(new Platform( 398, 580, 509 - 398, 15, false, Color.gray ));
		vPlatform.add(new Platform( 509, 289, 613 - 509, 15, false, Color.gray ));
		vPlatform.add(new Platform( 613, 580, 726 - 613, 15, false, Color.gray ));
		vPlatform.add(new Platform( 726, 515, 785 - 726, 15, false, Color.gray ));

		vMonsters.add( new ShootingMonster(gpInstance,"Soldier", 398, 580 ,398, 509, 1, 0) );
		vMonsters.add( new ShootingMonster(gpInstance,"Soldier", 726, 580 , 613,726, -1, 0) );

		vLoot.add(new ShieldLoot(gpInstance,player,2,529, 289 - 31 ));

		vMonsters.add( new FlyingMonster(gpInstance,"Helicopter",1023, 260,  0, 1024 , -1, 200) );
		
		vMonsters.add( new FlyingMonster(gpInstance,"Helicopter",-100, 360,  0, 1024 , 1, 500) );
		
		vMonsters.add( new SpamMonster(gpInstance,"Tank",1024, 768 - floorh,0, 1024, -1, 1100) );
		
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
	public void drawExtras(Graphics2D g2) {
		g2.drawImage(imgtunel.getImage(), 1024 - 29, 768 - 204, null);
	}
	
	
}