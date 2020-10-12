

public class FlyingMonster extends Monster {

	public FlyingMonster(GamePanel gp,String code, int xm, int ym, int xi, int xf, int dir, int tDelay) {
		super(gp,code, xm, ym, xi, xf, dir, tDelay);
		
		
		switch(code) {
		case "Eagle":
			super.health = 40;
			break;
		case "Ship":
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
		ym = (int) (ym0 + 20 * Math.sin(xm * Math.PI * 8 / 1024));
		
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
	
	@Override
	public void shoot() {
		if ( (shootTime > shootFrequency) ){
			shootTime = 0;
			gpInstance.playNewSound(audioFolder+code+"S.au");
			gpInstance.monsterShoot(xm + w/2 ,  ym + h/2 , 5 , -1.571*dir , dir , 20 , 3 );
		} else {
			shootTime += 1;
		}
	}
}
