package loot;

import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

import panels.GamePanel;
import player.Player;

public class AmmoLoot extends Loot{

	private String imageFolder="img/loot/",soundFolder="sounds/loot/";
	private int value=0,w,h;
	private ImageIcon lootIcon;
	private static Clip clipPickUp;
	
	public AmmoLoot(GamePanel gp,Player player, int code, int x, int y) {
		super(gp,player, code, x, y);
		value = 100*code;
		
		lootIcon = new ImageIcon(getClass().getResource(imageFolder+"Ammo"+Integer.toString(code)+".png"));
		w = lootIcon.getIconWidth();
		h = lootIcon.getIconHeight();
		
		if (clipPickUp==null) {
			try {
				clipPickUp = gpInstance.loadSound(soundFolder+"Ammo"+Integer.toString(code)+".au");
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
		

	}
	
	@Override
	public int h() {
		return h;
	}
	@Override
	public int w() {
		return w;
	}
	
	@Override
	public void pickUp() {
		player.ammo(-value);
		gpInstance.playSound(clipPickUp);
	}
	
	@Override
	public ImageIcon lootImage() {
		return lootIcon;
	}
	

}
