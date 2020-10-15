package stages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.util.Vector;

import javax.swing.ImageIcon;

import environment.Platform;
import loot.AmmoLoot;
import loot.Loot;
import monster.Monster;
import monster.WalkingMonster;
import panels.GamePanel;
import player.Player;

public class Stage10 extends Stage{
	private Image imgBackground;
	private ImageIcon imgmur;
	public Vector<int[]> bats;
	private Vector<int[]> drips;
	private int dripCounter = 0;
	private double random = Math.random();

	public Stage10(GamePanel gpInstance, Vector<Platform> vPlatform, Vector<Monster> vMonsters, Vector<Loot> vLoot, Player player) {
		super(gpInstance, vPlatform, vMonsters, vLoot, player);
		
		imgBackground = new ImageIcon(getClass().getResource(stagesFolder+Integer.toString(10)+"p.png")).getImage();
		imgmur = new ImageIcon(getClass().getResource(miscFolder+"mur.png"));
		
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie", 500, 768 -floorh,0,834, 1, 0) );
		
		drips = new Vector<int[]>();
		bats = new Vector<int[]>();
		bats.add(new int[] { 295, 326 });
		bats.add(new int[] { 795, 326 });
		bats.add(new int[]  { 513, 219 });
		
		vLoot.add(new AmmoLoot(gpInstance,player,1, 520, 650 - 23));

		vPlatform.add(new Platform( 200, 650, 120, 10, false, Color.gray ));
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie", 200, 650,200,200 + 120, 1, 0) );
		
		vPlatform.add(new Platform( 200, 450, 120, 10, false, Color.gray ));
		vPlatform.add(new Platform( 500, 650, 120, 10, false, Color.gray ));
		vPlatform.add(new Platform( 500, 450, 120, 10, false, Color.gray ));
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie",600, 450 ,500,500 + 120, 1, 0) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",200, 768 - floorh,0,834, 1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",300,768 - floorh ,0,834, -1, 0) );
		vMonsters.add( new WalkingMonster(gpInstance,"Slug",400, 768 - floorh ,0,834, -1, 0) );
		
		vMonsters.add( new WalkingMonster(gpInstance,"Zombie",600, 768 - floorh,0,834, 1, 0) );
		
		player.setLocation(10,713);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(imgBackground, 0, 0, null);
		if (isStageClear()) {
			g2.drawImage(imgNextStage.getImage(), 1024 - 200, 200, null);
		}
	}
	
	private void generateDrip(int xxx) {
		int[] g = { xxx, 326, (int) stageTime };
		drips.add(g);
	}
	
	@Override
	public void drawExtras(Graphics2D g2) {
		// Bats
		for (int batIndex = 0; batIndex < bats.size(); batIndex++) {
			int[] bat = (int[]) bats.elementAt(batIndex);
			g2.drawImage(imgmur.getImage(),bat[0], bat[1], null);
		}
		
		// Drips
		// generate
		dripCounter += 1;
		if (dripCounter > 150 * random) {
			generateDrip(869);
			random = Math.random();
			dripCounter = 0;
		} else if ((dripCounter > 50 * random)
				&& (dripCounter < 50 * random + 2)) {
			generateDrip(166);
		} else if ((dripCounter > 60 * random)
				&& (dripCounter < 60 * random + 2)) {
			generateDrip(444);
		}
		//draw
		if (drips.size() > 0) {
			for (int b = 0; b < drips.size(); b++) {
				int[] drip = (int[]) drips.elementAt(b);
				int xp = drip[0];
				int yp = drip[1];
				int t0 = drip[2];
				g2.setPaint(Color.blue);
				yp += 0.5 * 0.3 * Math.pow((stageTime-t0)/100, 2);
				if (yp >= 768 - 9) {
					drips.remove(b);
					Ellipse2D el = new Ellipse2D.Double(xp, 768 - 9, 6, 6);
					g2.fill(el);
					g2.draw(el);
					gpInstance.playNewSound(GamePanel.audioFolder+"goti.au");
					el = new Ellipse2D.Double(xp - 10 + 3, 768 - 11 - 10 + 3,20, 20);
					g2.draw(el);
					el = new Ellipse2D.Double(xp - 15 + 3, 768 - 11 - 15 + 3,30, 30);
					g2.draw(el);
				} else {
					drip[1] = (int) yp;
					drips.remove(b);
					drips.insertElementAt(drip, b);
					Ellipse2D el = new Ellipse2D.Double(Math.pow(xp, 1), yp, 6, 6);
					g2.fill(el);
					g2.draw(el);
				}
			}
		}
	}
	
	@Override
	public Vector<int[]> getBats() {
		return bats;
	}
	
}