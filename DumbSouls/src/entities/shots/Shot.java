package entities.shots;

import java.awt.image.BufferedImage;

import entities.Entity;
import graphics.Shader;
import main.Game;

public class Shot extends Entity{
	
	protected double dirx, diry;
	
	public Shot(int x, int y, int w, int h, double dx, double dy, double ang, double spd, double dmg, int life, BufferedImage sprt) {
		super(x, y, w, h, sprt);
		dirx = dx;
		diry = dy;
		speed = spd;
		damage = dmg;
		maxLife = this.life = life;
		sprite = Shader.rotate(sprite, ang);
		push = Game.player.push;
		setMask(0, 0, w, h);
	}

	public void die(Entity target) {
		if(Game.shots.contains(this)) Game.shots.remove(this); else Game.eShots.remove(this);
	}
	
	protected void movement(){
		x += dirx * speed;
		y += diry * speed;
	}

	public void tick() {
		movement();
		life--;
		if (life == 0) die(null);
	}
}
