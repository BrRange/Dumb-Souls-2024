package entities.shots;

import java.awt.image.BufferedImage;

import entities.Entity;
import entities.types.Collider.ColliderCircle;
import entities.types.Vector;
import graphics.Shader;
import main.Game;

public class Shot extends Entity{
	
	protected Vector dir = new Vector(0, 0);
	
	public Shot(int x, int y, int w, int h, double dx, double dy, double ang, double spd, double dmg, int life, BufferedImage sprt) {
		super(x, y, w, h, sprt);
		dir.set(dx, dy);
		speed = spd;
		damage = dmg;
		maxLife = this.life = life;
		sprite = Shader.rotate(sprite, ang);
		hitbox = new ColliderCircle(pos, w / 2);
	}

	public void die(Entity target) {
		if(Game.shots.contains(this)) Game.shots.remove(this); else Game.eShots.remove(this);
	}

	public void tick() {
		pos.move(dir.x * speed, dir.y * speed);
		life--;
		if (life == 0) die(null);
	}
}
