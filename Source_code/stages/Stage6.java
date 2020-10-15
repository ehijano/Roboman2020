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
import loot.ShieldLoot;
import monster.Monster;
import monster.ShootingMonster;
import panels.GamePanel;
import player.Player;

public class Stage6 extends Stage{
	private Image imgBackground;

	public Stage6(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(6)+"p.png")).getImage();
		
		vLoot.add(new HPLoot(gpInstance,player, 1,100, 768 - 10 - 33));

		vLoot.add(new ShieldLoot(gpInstance,player,1,150, 729 ));

		vPlatform.add(new Platform( 158, 679, 236, 15, false, Color.gray ));
		vPlatform.add(new Platform( 434, 573, 128, 15, false, Color.gray ));
		vPlatform.add(new Platform( 603, 335, 61, 15, false, Color.gray ));

		vLoot.add(new AmmoLoot(gpInstance,player,1, 614, 335 - 23));

		vPlatform.add(new Platform( 692, 335, 61, 15, false, Color.gray ));

		vLoot.add(new AmmoLoot(gpInstance,player,1, 704, 335 - 23));

		vMonsters.add( new ShootingMonster(gpInstance,"Soldier",158, 679 ,158,158 + 236, 1, 0) );
		vMonsters.add( new ShootingMonster(gpInstance,"Soldier",434, 573 , 434,434 + 128, 1, 0) );
		
		for( int i=0; i<4;i++) {
			vMonsters.add( new ShootingMonster(gpInstance,"Soldier",1024,768 - floorh ,0, 1024 , -1, 410+40*i) );
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