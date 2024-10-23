package entities.AE;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import main.Game;
import entities.*;
import entities.enemies.Enemy;
import world.Camera;

public class AE_ManaRay extends Attack_Entity {
	
	private int damage = 0, time = 0;
	private Enemy smallest = Game.enemies.get(0);
	
	public AE_ManaRay(int x, int y, int width, int height, BufferedImage sprite, int time, int damage) {
		super(x, y, width, height, sprite, time);
		this.damage = damage;
		this.depth = 2;
		this.getAnimation(208, 112, 12, 12, 1);
	}
	
	private void findSmallest() {
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy ene = Game.enemies.get(i);
			double distE = Entity.calculateDistance(Game.player.getX(), Game.player.getY(), ene.getX(), ene.getY());
			double distS = Entity.calculateDistance(Game.player.getX(), Game.player.getY(), smallest.getX(), smallest.getY());
			if(distE < distS) {
				smallest = ene;
			}
		}
	}
	
	private void Collision() {
		findSmallest();
		Line2D line = new Line2D.Double(Game.player.getX() + 8, Game.player.getY() + 8, smallest.getX() + smallest.getWidth()/2, smallest.getY() + smallest.getHeight()/2);
		
		if(Entity.lineCollision(line, smallest)) {
			if (time % 10 == 0) {
				smallest.life -= damage;
				if (smallest.life <= 0) {
					smallest = Game.enemies.get(0);
				}
			}
		}
	}
	
	public void tick() {
		time++;
		Collision();
		this.width = (int)Entity.calculateDistance(Game.player.getX() - 8, Game.player.getY() - 8, smallest.getX() - smallest.getWidth()/2, smallest.getY() - smallest.getHeight()/2);
		this.x = Game.player.getX() + Game.player.getWidth()/2;
		this.y = Game.player.getY() + Game.player.getHeight()/2;
		
		if (time == this.timeLife) {
			this.die();
		}
	}
	
	public void render() {
		Game.gameGraphics.setColor(new Color(47, 141, 224, 150));
		Game.gameGraphics.drawLine(Game.player.getX() - Camera.getX() + 8, Game.player.getY() - Camera.getY() + 8, smallest.getX() - Camera.getX() + smallest.getWidth()/2, smallest.getY()- Camera.getY() + smallest.getHeight()/2);
	}
}
