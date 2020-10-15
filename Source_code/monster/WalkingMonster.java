package monster;
import panels.GamePanel;

public class WalkingMonster extends Monster {

	public WalkingMonster(GamePanel gp,String code, int xm, int ym, int xi, int xf, int dir, int tDelay) {
		super(gp, code, xm, ym, xi, xf, dir, tDelay);
		
		switch(code) {
		case "Tree":
			super.health = 60;
			break;
		case "Plant":
			super.health = 30;
			break;
		case "Worm":
			super.health = 20;
			break;
		case "Skelleton":
			super.health = 30;
			break;
		case "Zombie":
			super.health = 30;
			break;
		case "SUV":
			super.health = 50;
			break;
		case "Alien":
			super.health = 60;
		}
	}
	
	@Override
	public void moveMonster(int x, int y) {
		// Direction
		if (xm <= xi) {
			dir = 1;
		} else if (xm >= xf - w) {
			dir = -1;
		}
		
		// Movement
		xm += dir;
		switch(dir) {
		case 1: imgCode = "R"; break;
		case -1: imgCode = "L"; break;
		}
		
		// Animation
		if (animationTime >= animationFrequency) {
			animationTime = 0;
			if (animation == 1) {
				animation = 2;
			} else {
				animation = 1;
			}
		} else {
			animationTime += 1;
		}
		imgCode = imgCode + Integer.toString(animation);
	}
	
	
	
}
