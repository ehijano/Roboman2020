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

public class Stage12 extends Stage{
	private Image imgBackground;

	public Stage12(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(12)+"p.png")).getImage();
		
		vPlatform.add(new Platform( 220, 483, 585 - 220,8, false, colorWood ));
		vPlatform.add(new Platform( 572, 624, 772 - 572,7, false, colorWood ));

		vLoot.add(new AmmoLoot(gpInstance,player,1, 572 + 30, 624 - 23));
		
		vMonsters.add( new WalkingMonster(gpInstance,"Tree",  220, 483  ,220, 585 , 1, 0) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Tree",   572, 624  , 572,772, 1, 0) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Plant", 1024, 768 - floorh ,0, 1024 , 1, 300) );
		vMonsters.add( new WalkingMonster(gpInstance,"Plant",  650, 768 - floorh ,0, 1024 , 1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Plant", 0, 768 - floorh ,0, 1024 , 1, 500) );

		vLoot.add(new AmmoLoot(gpInstance,player,1, 210, 768 - 10 - 23));

		vMonsters.add( new FlyingMonster(gpInstance,"Eagle",  1024, 160 ,0, 1024 , 1, 100) );
		
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