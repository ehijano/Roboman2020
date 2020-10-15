package loot;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

import panels.GamePanel;
import player.Player;

public class ShieldLoot extends Loot{
	
	private String soundFolder="sounds/loot/";
	private int w,h;
	private ImageIcon lootIcon;
	static Clip clipPickUp;
	static boolean loadedSound = false;

	public ShieldLoot(GamePanel gp,Player player, int code, int x, int y) {
		super(gp,player, code, x, y);


		lootIcon = new ImageIcon(getClass().getResource(imageFolder+"Shield"+Integer.toString(code)+".png"));
		w = lootIcon.getIconWidth();
		h = lootIcon.getIconHeight();
		
		if(clipPickUp==null) {
			try {
				clipPickUp = gpInstance.loadSound(soundFolder+"Shield"+Integer.toString(code)+".au");
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
		player.setHelmet(code);
		gpInstance.playSound(clipPickUp);
	}
	
	@Override
	public ImageIcon lootImage() {
		return lootIcon;
	}

}
