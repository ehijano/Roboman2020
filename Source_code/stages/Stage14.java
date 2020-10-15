package stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.AmmoLoot;
import loot.Loot;
import monster.Monster;
import panels.GamePanel;
import player.Player;

public class Stage14 extends Stage{
	private Image imgBackground;
	private ImageIcon imgMoonInvader,imgMoonInvader2,imgMoonInvader3;
	private boolean playMovie = false;
	private static double xMoonInvader = 760, yMoonInvader = 768 - 50 - 384;
	private static int MIanimationCounter = 0;

	public Stage14(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(14)+"p.png")).getImage();
		imgMoonInvader = new ImageIcon(getClass().getResource(miscFolder+"MoonInvader.png"));
		imgMoonInvader2 = new ImageIcon(getClass().getResource(miscFolder+"MoonInvader2.png"));
		imgMoonInvader3 = new ImageIcon(getClass().getResource(miscFolder+"MoonInvader3.png"));
		
		vLoot.add(new AmmoLoot(gpInstance,player, 1,310, 768 - 10 - 33));
		
		player.setLocation(10,713);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(imgBackground, 0, 0, null);
	}
	
	@Override
	public boolean playingMovie() {
		return playMovie;
	}
	
	@Override
	public void enforceBounds() {
		if (player.x > 729) {// Movie
			playMovie = true;
			player.setVisible(false);
		}else {// Otherwise respect screen bounds
			player.fixOutOfBounds();
		}
	}
	
	@Override
	public void drawExtras(Graphics2D g2) {
		if (playMovie) {
			if (yMoonInvader == 768 - 50 - 384) {
				gpInstance.playNewSound(GamePanel.audioFolder+"despegue.au");
			}
			player.setVisible(false);
			yMoonInvader -= (int) 500/(200.0);
			
			if (yMoonInvader + 384 < 0) {
				playMovie = false;
				player.setVisible(true);
				player.setLocation(100, 713);
				gpInstance.changeStage();
			}
			
			if (MIanimationCounter <= 10) {
				g2.drawImage(imgMoonInvader2.getImage(), (int) xMoonInvader, (int) yMoonInvader, null);
				MIanimationCounter += 1;
			} else if ((MIanimationCounter > 10) && (MIanimationCounter <= 20)) {
				g2.drawImage(imgMoonInvader3.getImage(), (int) xMoonInvader, (int) yMoonInvader, null);
				MIanimationCounter += 1;
			} else {
				MIanimationCounter = 1;
			}
			
		}else if (yMoonInvader<=768 - 50 - 384){
			g2.drawImage(imgMoonInvader.getImage(), (int) xMoonInvader, (int) yMoonInvader, null);
		}
		
		 
			

		
		
		
	}
	
	
}