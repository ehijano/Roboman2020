import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class ShieldLoot extends Loot{
	
	private String imageFolder="img/loot/",soundFolder="sounds/loot/";
	private int w,h;
	private ImageIcon lootIcon;
	Clip clipPickUp;

	public ShieldLoot(GamePanel gp,Player player, int code, int x, int y) {
		super(gp,player, code, x, y);


		lootIcon = new ImageIcon(getClass().getResource(imageFolder+"Shield"+Integer.toString(code)+".png"));
		w = lootIcon.getIconWidth();
		h = lootIcon.getIconHeight();
		
		
		try {
			clipPickUp = gpInstance.loadSound(soundFolder+"Shield"+Integer.toString(code)+".au");
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
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
