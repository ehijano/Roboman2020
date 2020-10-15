package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import javax.swing.ImageIcon;
import environment.Platform;
import loot.Loot;
import loot.WeaponLoot;
import monster.FlyingMonster;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage11 extends Stage{
	private Image imgBackground;

	public Stage11(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(11)+"p.png")).getImage();
		
		vMonsters.add( new FlyingMonster(gpInstance,"Eagle",  1024,460 ,0, 1024 , 1, 100) );
		vMonsters.add( new FlyingMonster(gpInstance,"Eagle",  0,360 ,0, 1024 , -1, 300) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Tree", 500, 768 - floorh ,0,1024 , 1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Plant", 650,768 - floorh ,0,1024 , 1, 0) );

		vLoot.add(new WeaponLoot(gpInstance,player,4,500, 739));
		
		vMonsters.add( new WalkingMonster(gpInstance,"Tree", 800, 768 - floorh ,0,1024 , -1, 0) );
		
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
		ImageIcon imgar = new ImageIcon(getClass().getResource(miscFolder+"A1.png"));
		g2.drawImage(imgar.getImage(), 100, 768 - 6 - 377, null);
		imgar = new ImageIcon(getClass().getResource(miscFolder+"A2.png"));
		g2.drawImage(imgar.getImage(), 600, 768 - 6 - 286, null);
	}
	
	
}