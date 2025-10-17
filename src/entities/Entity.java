 package entities;

import java.awt.image.BufferedImage;
import java.util.Comparator;

import entities.types.Collider;
import entities.types.Vector;
import graphics.Shader;
import graphics.Spritesheet;
import main.Game;
import world.Camera;

public abstract class Entity {
	public Vector pos = new Vector(0, 0);
	public Collider hitbox;
	public double life, maxLife, damage, push, speed, maxSpeed, slowness, weight = 1.f;
	public int width, height, depth, damagedFrames, maxDamagedFrames = 15, damagedHue, state = 0, hue = 0, index, maxIndex = 3;
	public boolean damaged;
	public BufferedImage[] animation;
	
	public BufferedImage sprite;
	
	public Entity(int x, int y, int w, int h, BufferedImage sprt) {
		pos.set(x - w / 2, y - h / 2);
		width = w;
		height = h;
		sprite = sprt;
	}

	public Entity(int x, int y, int w, int h) {
		pos.set(x - w / 2, y - h / 2);
		width = w;
		height = h;
	}

	protected void getAnimation(int x, int y, int width, int height, int frames, Spritesheet sheet) {
		animation = new BufferedImage[frames];

		for (int i = 0; i < animation.length; i++) {
			animation[i] = Shader.reColor(sheet.getSprite(x, y, width, height), hue);
			x += width;
		}
	}

	protected void getAnimation(int x, int y, int width, int height, int frames, Spritesheet sheet, int states) {
		animation = new BufferedImage[frames * states];

	for(int i = y; i < states; i++)
		for (int j = x; j < frames; j++)
			animation[j + i * frames] = Shader.reColor(sheet.getSprite(width * j, height * i, width, height), hue);
	}

	
	public static double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.hypot(x1 - x2, y1 - y2);
	}
	
	public int outOfBounds(){
		return (pos.x > 1264 - width ? 1 : 0) + (pos.y > 1264 - height ? 2 : 0) - (pos.x < 16 ? 1 : 0) - (pos.y < 16 ? 2 : 0);
	}

	public void clampBounds(int sign){
		switch(sign){
		case -1:
			pos.x = 16; break;
		case -2:
			pos.y = 16; break;
		case -3:
			pos.x = pos.y = 16; break;
		case 1:
			pos.x = 1264 - width; break;
		case 2:
			pos.y = 1264 - height; break;
		case 3:
			pos.x = 1264 - width;
			pos.y = 1264 - height; break;
		}
	}

	public static Comparator<Entity> entityDepth = new Comparator<Entity>() {
		public int compare(Entity n0, Entity n1) {
			if (n1.depth < n0.depth)
				return +1;
			if (n1.depth > n0.depth)
				return -1;
			return 0;
		}
	};
	
	public int centerX(){
		return pos.getX() + width / 2;
	}
	public int centerY(){
		return pos.getY() + height / 2;
	}

	public abstract void tick();
	
	public void render() {
		if(offScreen()) return;
		Game.gameGraphics.drawImage(sprite, pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
	}

	private boolean outOfPerimeter(Entity other){
		return Math.abs(centerX() - other.centerX()) + Math.abs(centerY() - other.centerY()) > Math.max(width, other.width) + Math.max(height, other.height);
	}
	
	protected boolean offScreen(){
		return (pos.getX() + width < Camera.getX() && pos.getY() + height < Camera.getY()) || (pos.getX() > Camera.getX() + Game.width && pos.getY() > Camera.getY() + Game.height);
	}

	public boolean isColiding(Entity other) {
		if(outOfPerimeter(other)) return false;
		return hitbox.collide(other.hitbox);
	}

	public void applySlowness(double slow){
		slowness = Math.max(slowness, slow);
	}

	public void receiveKnockback(Entity source, double amount){
		if(weight <= 0) return;
		Vector delta = new Vector(centerX() - source.centerX(), centerY() - source.centerY()).normalize();
		amount /= weight;
		pos.move(delta.x * amount, delta.y * amount);
	}

	protected void damagedAnimation() {
		if (damaged) {
			damagedFrames++;
			if(damagedFrames % 5 == 0) {
				damagedHue = (damagedHue == 0x444444) ? 0x400000 : 0x444444;
			}
			if(damagedFrames == maxDamagedFrames) {
				damaged = false;
				damagedFrames = 0;
			}
		}
	}
}
