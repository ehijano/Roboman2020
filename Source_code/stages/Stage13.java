package stages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.AmmoLoot;
import loot.Loot;
import monster.FlyingMonster;
import monster.Monster;
import panels.GamePanel;
import player.Player;

public class Stage13 extends Stage{
	private Image imgBackground;
	private ImageIcon imgShark;

	public Stage13(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(13)+"p.png")).getImage();
		imgShark = new ImageIcon(getClass().getResource(miscFolder+"tibu2.png"));
		
		vPlatform.add(new Platform(0, 660, 100, 15, true, Color.gray ));
		vPlatform.add(new Platform(965, 666, 200, 5, false, Color.gray ));
		vPlatform.add(new Platform(150, 630, 100, 15, false, Color.gray ));
		vPlatform.add(new Platform(350, 630, 100, 15, false, Color.gray ));
		vPlatform.add(new Platform(550, 630, 100, 15, false, Color.gray ));

		vLoot.add(new AmmoLoot(gpInstance,player,1, 560, 630 - 23));

		vPlatform.add(new Platform(750, 630, 100, 15, false, Color.gray ));
		
		for(int i=0; i<8;i++) {
			vMonsters.add( new FlyingMonster(gpInstance,"Eagle",  1024, 170 ,0, 1024 , 1, 100+150*i) );
		}
		
		player.setLocation(0,650 - 50);
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
		int xt = 0;
		if (player.x < 70) {
			xt = 70;
		} else if (player.x + 85 > 970) {
			xt = 970 - 85;
		} else {
			xt = player.x;
		}
		g2.drawImage(imgShark.getImage(), xt - 13, (int) (768 - 45 - 3 * Math.sin(stageTime * 2 * Math.PI/1000)), null);
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