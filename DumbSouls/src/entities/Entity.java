 package entities;

import java.awt.image.BufferedImage;
import java.util.Comparator;

import main.Game;
import world.Camera;

import java.awt.Rectangle;
import java.awt.geom.Line2D;

public abstract class Entity {
	
	public class Vector{
		public double x, y;
	
		public Vector(double setx, double sety){
			x = setx;
			y = sety;
		}

		public static double getMagnitude(double dx, double dy){
			final double mag = Math.hypot(dx, dy);
			return mag == 0 ? 1 : mag;
		}

		public Vector normal(){
			final double mag = getMagnitude(x, y);
			return new Vector(x / mag, y / mag);
		}

		public Vector normalize(){
			final double mag = getMagnitude(x, y);
			x /= mag;
			y /= mag;
			return this;
		}

		public int getX(){
			return (int) x;
		}

		public int getY(){
			return (int) y;
		}

		public void set(double setx, double sety){
			x = setx;
			y = sety;
		}

		public void move(double dx, double dy){
			x += dx;
			y += dy;
		}
	}

	public Vector pos = new Vector(0, 0);
	public double life, maxLife, damage, push, speed, maxSpeed, slowness, weight = 1.f;
	public int width, height, depth, damagedFrames, maxDamagedFrames = 15, damagedHue;
	public boolean damaged;
	protected Rectangle mask = new Rectangle();
	
	public BufferedImage sprite;
	
	public Entity(int x, int y, int w, int h, BufferedImage sprt) {
		pos.set(x - w / 2, y - h / 2);
		width = w;
		height = h;
		sprite = sprt;
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
		Game.gameGraphics.drawImage(sprite, pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), null);
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
		Rectangle sourceHitBox = new Rectangle(pos.getX() + mask.x, pos.getY() + mask.y, mask.width, mask.height);
		return sourceHitBox.intersects(new Rectangle(other.pos.getX() + other.mask.x, other.pos.getY() + other.mask.y, other.mask.width, other.mask.height));
	}
	
	public static boolean lineCollision(Line2D line, Entity ent) {
		ent.mask = new Rectangle(ent.pos.getX() + ent.mask.x, ent.pos.getY() + ent.mask.y, ent.mask.width, ent.mask.height);
		return line.intersects(ent.mask);
	}

	public void applySlowness(double slow){
		slowness = Math.max(slowness, slow);
	}

	public void receiveKnockback(Entity source, double amount){
		Vector delta = new Vector(centerX() - source.centerX(), centerY() - source.centerY()).normalize();
		amount /= weight;
		pos.move(delta.x * amount, delta.y * amount);
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
