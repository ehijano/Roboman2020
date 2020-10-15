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

public class Stage2 extends Stage{
	private Image imgBackground;
	private ImageIcon imgShark;

	public Stage2(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(2)+"p.png")).getImage();
		imgShark = new ImageIcon(getClass().getResource(miscFolder+"tibu.png"));
		
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",300, 640  , 300, 300 + 120 , -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",900 + 120, 670  , 900, 900 + 120, -1, 0) );

		vMonsters.add( new FlyingMonster(gpInstance,"Bee",1024, 250,  0, 1024 , -1, 700) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bee", 1, 250,  0, 1024 , 1, 0) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bee", 1, 250,  0, 1024 , -1, 200) );
		vMonsters.add( new FlyingMonster(gpInstance,"Bee", 1, 450,  0, 1024 , 1, 700) );

		vPlatform.add(new Platform( 0, 700, 120, 15, false, colorBrown ));
		vPlatform.add(new Platform( 200, 670, 50, 15, false, colorBrown ));
		vPlatform.add(new Platform( 300, 640, 120, 15, false, colorBrown ));
		vPlatform.add(new Platform( 500, 600, 120, 15, false, colorBrown ));
		vPlatform.add(new Platform( 650, 640, 180, 15, false, colorBrown ));
		vPlatform.add(new Platform( 900, 670, 122, 15, false, colorBrown ));

		vLoot.add(new AmmoLoot(gpInstance,player,1,210, 670 - 23));
		
		player.setLocation(10,700-50);
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
		g2.drawImage(imgShark.getImage(), player.x - 13, (int) (768 - 52 - 5 * Math.sin(stageTime * 2 * Math.PI/1000)), null);
	}
	
	@Override
	public void enforceBounds() {
		if ((player.x > 1024 - 30) && (isStageClear())) {// Stage ends
			endStage();
		}else if(player.y>713) {
			player.heal(-player.health);
		}else {// Otherwise respect screen bounds
			player.fixOutOfBounds();
		}
	}

	
}
