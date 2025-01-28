 package entities;

import java.awt.image.BufferedImage;
import java.util.Comparator;

import main.Game;
import world.Camera;

import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Entity {
	
	public double x, y, life, maxLife, damage, push, speed, maxSpeed, slowness, weight = 1.f;
	public int width, height, depth, damagedFrames, maxDamagedFrames = 15, damagedHue;
	public boolean damaged;
	protected Rectangle mask = new Rectangle();
	
	public BufferedImage sprite;
	
	public Entity(int x, int y, int w, int h, BufferedImage sprt) {
		this.x = x - w / 2;
		this.y = y - h / 2;
		width = w;
		height = h;
		sprite = sprt;
	}
	
	public static double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.hypot(x1 - x2, y1 - y2);
	}
	
	public int outOfBounds(){
		return (x > 1264 - width ? 1 : 0) + (y > 1264 - height ? 2 : 0) - (x < 16 ? 1 : 0) - (y < 16 ? 2 : 0);
	}

	public void clampBounds(int sign){
		switch(sign){
		case -1:
			x = 16; break;
		case -2:
			y = 16; break;
		case -3:
			x = y = 16; break;
		case 1:
			x = 1264 - width; break;
		case 2:
			y = 1264 - height; break;
		case 3:
			x = 1264 - width;
			y = 1264 - height; break;
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
	
	public void setX(int newX) {
		x = newX;
	}
	public void setY(int newY) {
		y = newY;
	}
	
	public int getX() {
		return (int)x;
	}
	public int getY() {
		return (int)y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int centerX(){
		return (int)x + width / 2;
	}
	public int centerY(){
		return (int)y + height / 2;
	}

	public void tick() {
		
	}
	
	public void render() {
		Game.gameGraphics.drawImage(sprite, getX() - Camera.getX(), getY() - Camera.getY(), null);
	}

	protected Rectangle getMask(){
		return mask;
	}
	
	protected void setMask(int mx, int my, int mw, int mh) {
		mask.x = mx;
		mask.y = my;
		mask.width = mw;
		mask.height = mh;
	}

	protected void setMask(Rectangle mask) {
		this.mask = mask;
	}
	
	public boolean isColiding(Entity other) {
		Rectangle sourceHitBox = new Rectangle(getX() + mask.x, getY() + mask.y, mask.width, mask.height);
		return sourceHitBox.intersects(new Rectangle(other.getX() + other.mask.x, other.getY() + other.mask.y, other.mask.width, other.mask.height));
	}
	
	public static boolean lineCollision(Line2D line, Entity ent) {
		ent.mask = new Rectangle(ent.getX() + ent.mask.x, ent.getY() + ent.mask.y, ent.mask.width, ent.mask.height);
		return line.intersects(ent.mask);
	}
	
	public static double getMagnitude(double dx, double dy){
		double mag = Math.hypot(dx, dy);
		return mag == 0 ? 1 : mag;
	}

	public void applySlowness(double slow){
		slowness = Math.max(slowness, slow);
	}

	public void receiveKnockback(Entity source){
		double deltaX = centerX() - source.centerX();
		double deltaY = centerY() - source.centerY();
		double mag = getMagnitude(deltaX, deltaY) * weight;
		x += deltaX / mag * source.push;
		y += deltaY / mag * source.push;
	}

	public void receiveKnockback(Entity source, double amount){
		double deltaX = centerX() - source.centerX();
		double deltaY = centerY() - source.centerY();
		double mag = getMagnitude(deltaX, deltaY) * weight;
		x += deltaX / mag * amount;
		y += deltaY / mag * amount;
	}
	
	public void receiveDamage(Entity source){
		life -= source.damage;
		damaged = true;
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
