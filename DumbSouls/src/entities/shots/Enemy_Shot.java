package entities.shots;

import java.awt.image.BufferedImage;
import graphics.Shader;
import main.Game;
import world.Camera;

public class Enemy_Shot extends Shot{

	private String typeMovement;

	public Enemy_Shot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy, int dmg, int speed, int life, String type) {
		super(x, y, width, height, sprite, dx, dy, 0, dmg, speed, life);
		this.dirx = dx;
		this.diry = dy;
		this.damage = dmg;
		this.speed = speed;
		this.mw = width;
		this.mh = height;
		this.sprite = sprite;
		this.life = life;
		this.typeMovement = type;
	}
	
	public void render() {
		Game.gameGraphics.drawImage(this.sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
	
	private void movement() {
		switch (typeMovement) {
			case "straight":
				x += this.dirx * this.speed;
				y += this.diry * this.speed;
			break;
			case "focused":
				int xP = Game.player.getX();
				int yP = Game.player.getY();
				double angle = Math.atan2(yP - this.y, xP - this.x);

				this.x += Math.cos(angle) * this.speed;
				this.y += Math.sin(angle) * this.speed;
				
				this.sprite = Shader.rotate(sprite, angle);
			break;
		}
	}
	
	public void tick() {
		movement();
		curlife++;
		if (this.curlife == life) {
			Game.eShots.remove(this);
		}
	}
}
