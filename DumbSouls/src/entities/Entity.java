 package entities;

import java.awt.image.BufferedImage;
import java.util.Comparator;

import main.Game;

import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Entity {
	
	public double x;
	public double y;
	public double life;
	public double maxLife;
	public double damage;
	public int width;
	public int height;
	protected int mx, my, mw, mh;
	protected Rectangle mask;
	public double push;
	public int depth;
	
	public BufferedImage sprite;
	
	public static BufferedImage player_sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		player_sprite = Game.sheet.getSprite(0, 16, 16, 16);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public static double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
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
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	public int getY() {
		return (int)this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public double getAngle(double destY, double startY, double destX, double startX){
		return Math.atan2(destY - startY,destX - startX); 
	}
	
	public void tick() {
		
	}
	
	public void render() {
		Game.gameGraphics.drawImage(sprite, this.getX(), this.getY(), null);
	}

	protected int[] getMask(){
		int[] temp = {this.mx, this.my, this.mw, this.mh};
		return temp;
	}
	
	protected void setMask(int mx, int my, int mw, int mh) {
		this.mx = mx;
		this.my = my;
		this.mw = mw;
		this.mh = mh;
	}

	protected void setMask(int[] mask) {
		this.mx = mask[0];
		this.my = mask[1];
		this.mw = mask[2];
		this.mh = mask[3];
	}
	
	public static boolean isColiding(Entity e1, Entity e2) {
		e1.mask = new Rectangle(e1.getX() + e1.mx, e1.getY() + e1.my, e1.mw, e1.mh);
		e2.mask = new Rectangle(e2.getX() + e2.mx, e2.getY() + e2.my, e2.mw, e2.mh);
		return e1.mask.intersects(e2.mask);
	}
	
	public static boolean lineCollision(Line2D line, Entity ent) {
		ent.mask = new Rectangle(ent.getX() + ent.mx, ent.getY() + ent.my, ent.mw, ent.mh);
		return line.intersects(ent.mask);
	}

	public void receiveKnockback(Entity source){
		double angle = getAngle(
			getY() + getHeight() / 2, source.getY() + source.getHeight() / 2,
			getX() + getWidth() / 2, source.getX() + source.getWidth() / 2
		);
		x += Math.cos(angle) * source.push;
		y += Math.sin(angle) * source.push;
	}
	
	public void receiveDamage(Entity source){
		this.life -= source.damage;
	}
}
