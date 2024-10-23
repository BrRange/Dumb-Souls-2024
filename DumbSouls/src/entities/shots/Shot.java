package entities.shots;

import java.awt.image.BufferedImage;

import graphics.Shader;

import entities.*;
import entities.weapons.*;
import main.Game;
import world.Camera;

public class Shot extends Entity{
	
	protected double dirx, diry;
	protected double speed;
	protected int life;
	protected int curlife = 0;
	protected BufferedImage sprite;
	
	public Shot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy, double ang, int dmg, double speed, int life) {
		super(x, y, width, height, sprite);
		this.dirx = dx;
		this.diry = dy;
		this.damage = dmg;
		this.speed = speed;
		this.mw = width;
		this.mh = height;
		this.sprite = Shader.rotate(sprite, ang);
		this.life = life;
		this.push = Game.player.push;
		this.setMask(4, 5, width, height);
	}
	
	public void render() {
		Game.gameGraphics.drawImage(sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
	
	public void die() {
		Game.shots.remove(this);
		if (Game.player.playerWeapon instanceof Poison_Weapon ) {
			Poison_Weapon.poisonEffect(this);
		}
		else if (Game.player.playerWeapon instanceof Mana_Weapon) {
			Mana_Weapon.manaEffect(this);
		}
	}
	
	public void tick() {
		x += dirx * speed;
		y += diry * speed;
		curlife++;
		if (curlife == life) {
			this.die();
		}
	}
}
