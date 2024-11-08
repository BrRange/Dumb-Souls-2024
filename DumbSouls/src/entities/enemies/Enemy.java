package entities.enemies;

import java.awt.image.BufferedImage;
import entities.*;
import entities.shots.*;
import entities.weapons.*;
import main.*;
import graphics.Shader;

public class Enemy extends Entity{
	
	protected BufferedImage[] animation;
	public static BufferedImage baseSprite = Game.sheet.getSprite(0, 80, 16, 16);
	public int maxLife, expValue, soulValue;
	public double speed, maxSpeed, frost, life;
	protected boolean spawning, specialRare;
	protected int timeSpawn, contTS, specialMult = 1;
	protected int hue = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		isSpecial();
	}

	void isSpecial(){
		if (Game.rand.nextInt(256) == 0)
			this.specialRare = true;
	}
	
	protected void getAnimation(int x, int y, int width, int height, int frames) {
		animation = new BufferedImage[frames];
		
		for(int i = 0; i < animation.length; i++ ) {
			animation[i] = Game.sheet.getSprite(x , y, width, height);
			animation[i] = Shader.reColor(animation[i], hue);
			x += width;
		}
	}

	protected void frostEffect(double thaw) {
		speed = maxSpeed / (1 + frost);
		frost = frost < 0.01 ? 0 : frost * thaw;
	}
	
	protected void shotDamage() {
		for (int i = 0;  i < Game.shots.size(); i++) {
			Shot sh = Game.shots.get(i);
			if (isColiding(sh)) {
				this.life -= sh.damage;
				receiveKnockback(Game.player);
				sh.die();
				if (Game.player.playerWeapon instanceof Ice_Weapon) {
					Ice_Weapon.IceEffect(this, sh);
				}
			}
		}
	}

	protected void movement() {
		double deltaX = Game.player.getX() - x;
		double deltaY = Game.player.getY() - y;
		double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		this.x += deltaX * this.speed / magnitude;
		this.y += deltaY * this.speed / magnitude;
	}

	protected void reverseMovement() {
		double deltaX = Game.player.getX() - x;
		double deltaY = Game.player.getY() - y;
		double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		this.x += deltaX * -speed / magnitude;
		this.y += deltaY * -speed / magnitude;
	}
	
	protected void objectiveMovement(int xObjct, int yObjct) {
		double deltaX = xObjct - x;
		double deltaY = yObjct - y;
		double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		this.x += deltaX * this.speed / magnitude;
		this.y += deltaY * this.speed / magnitude;
	}
	
	int[] redoMask = {};
	protected void spawnAnimation(int frames) {
		if (contTS == 0) {
			redoMask = this.getMask();
			Game.enemies.add(new Enemy_Animation(this.getX() - this.width/2, this.getY() - this.height, this.width*2, this.height*2, null, timeSpawn, frames, 3, 112, 144, 32, 32, "frames_1", null, specialRare));
		}
		this.setMask(0, 0, 0, 0);
		this.contTS++;
		if (this.contTS == this.timeSpawn) {
			this.setMask(redoMask);
			spawning = false;
		}
	}
}
