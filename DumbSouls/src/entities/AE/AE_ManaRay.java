package entities.AE;

import entities.Entity;
import entities.enemies.Enemy;
import java.awt.Color;
import main.Game;
import world.Camera;

public class AE_ManaRay extends AE_Attack_Entity {
	
	private static Color manaColor = new Color(47, 141, 224, 150);
	private Enemy closest;
	
	public AE_ManaRay(int x, int y, int time, int dmg) {
		super(x, y, 16, 16, null, time);
		damage = dmg;
		depth = 2;
	}
	
	private void findClosest() {
		closest = null;
		double distE, distS = 144;
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			distE = Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), ene.centerX(), ene.centerY());
			if(closest != null) distS = Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), closest.centerX(), closest.centerY());
			if(distE < distS && distE < 144) {
				closest = ene;
			}
		}
	}
	
	private void Collision() {
		findClosest();
		if (tickTimer % 10 == 0 && closest != null) {
			closest.life -= damage;
			closest.damaged = true;
		}
	}
	
	public void tick() {
		tickTimer++;
		Collision();
		pos.set(Game.player.centerX(), Game.player.centerY());
		
		if (tickTimer == life) {
			die();
		}
		if(closest == null) return; 
		width = (int)Entity.calculateDistance(Game.player.centerX(), Game.player.centerY(), closest.centerX(), closest.centerY());
	}
	
	public void render() {
		if(closest == null) return;
		Game.gameGraphics.setColor(manaColor);
		Game.gameGraphics.drawLine(Game.player.centerX() - Camera.getX(), Game.player.centerY() - Camera.getY(), closest.centerX() - Camera.getX(), closest.centerY()- Camera.getY());
	}
}
