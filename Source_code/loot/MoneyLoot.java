package loot;
import javax.swing.ImageIcon;

import panels.GamePanel;
import player.Player;

public class MoneyLoot extends Loot {
	
	private String imageFolder="img/loot/",soundFolder="sounds/loot/";
	private int value=0,w,h;
	private ImageIcon lootIcon;
	//static Clip clipPickUp;
	
	public MoneyLoot(GamePanel gp,Player player, int code, int x, int y) {
		super(gp,player, code,x,y);
		if (code == 1) {
			value = 10;
		}else if(code == 2) {
			value = 20;
		}
		lootIcon = new ImageIcon(getClass().getResource(imageFolder+"Money"+Integer.toString(code)+".png"));
		w = lootIcon.getIconWidth();
		h = lootIcon.getIconHeight();
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
		player.money(value);
		gpInstance.playNewSound(soundFolder+"Money"+Integer.toString(code)+".au");
	}
	
	@Override
	public ImageIcon lootImage() {
		return lootIcon;
	}
	

}
