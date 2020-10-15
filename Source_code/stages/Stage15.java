package stages;

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
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage15 extends Stage{
	private Image imgBackground;

	public Stage15(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(15)+"p.png")).getImage();
		
		vMonsters.add( new FlyingMonster(gpInstance,"Ship",  1024, 260 ,0, 1024 , -1, 10) );
		vMonsters.add( new FlyingMonster(gpInstance,"Ship",  1024, 360 ,0, 1024 , -1, 200) );

		vLoot.add(new WeaponLoot(gpInstance,player,5,500, 739));
		vLoot.add(new ShieldLoot(gpInstance,player,2, 929, 768 - 8 - 31 ));

		vMonsters.add( new WalkingMonster(gpInstance,"Alien", 1024, 768 - floorh ,0, 1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Alien",  800, 768 - floorh ,0, 1024 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Alien", 300, 768 - floorh ,0, 1024 , -1, 0) );
		
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