public class ShootingMonster extends Monster {
	private boolean shooting=false;
	public ShootingMonster(GamePanel gp,String code, int xm, int ym, int xi, int xf, int dir, int tDelay) {
		super(gp,code, xm, ym, xi, xf, dir, tDelay);
		
		super.health = 20;
		
		shootFrequency = 10;

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
		if (isInRange(x,y)) {
			xm -= dir; // If in range, do not move.
			shooting = true;
			if (x + 30 < xm) {
				imgCode = "S";
				animation = 1;
				shootingDir = -1;
			} else {
				imgCode = "S";
				animation = 2;
				shootingDir = 1;
			}
			
		}else {
			shooting = false;
		}
		imgCode = imgCode + Integer.toString(animation);
	}
	
	@Override
	public void shoot() {
		if ( (shootTime > shootFrequency) && (shooting) ){
			shootTime = 0;
			//gpInstance.playSound(mClipShoot.get(code));
			gpInstance.playNewSound(audioFolder+code+"S.au");
			gpInstance.monsterShoot(xm + w/2 ,  ym + h/2 , 20 , 0.0 , shootingDir, 10 , 2 );
		} else {
			shootTime += 1;
		}
	}
	
	@Override
	public double shootingDirection() {
		return (double) shootingDir;
	}
	
	
	private boolean isInRange(int x, int y) {
		if (((xm > x + 20) && (xm - x - 20 > 270))
				|| ((xm + w < x) && (x - xm - w > 270))
				|| (y + 50 < ym) || (y > ym + h)) {
			return false;
		}else {
			return true;
		}
	}
}
