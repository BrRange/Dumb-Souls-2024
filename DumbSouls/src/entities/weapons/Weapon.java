package entities.weapons;

import java.awt.image.BufferedImage;
import main.Game;
import world.Camera;

public abstract class Weapon {
	
	public BufferedImage imgDash, imgPowerMove, imgSpecialMove;
	protected BufferedImage[] animation;
	protected int frames, maxFrames = 10, index, maxIndex = 3;
	protected int dashDuration, dashTick = 0, shotDamage;
	protected double shotSpeed;
	public int attackTimer;
	public String[] listNames;
	public boolean useDash, usePowerMove, useSpecialMove, availableDash, availablePowerMove, availableSpecialMove;
	
	protected void getAnimation(int x, int y, int width, int height, int frames) {
		animation = new BufferedImage[frames];
		
		for(int i = 0; i < animation.length; i++ ) {
			animation[i] = Game.sheet.getSprite(x , y, width, height);
			x += 16;
		}
	}
	
	protected void setAttackTimer(int frames){
		attackTimer = frames;
	}

	public abstract void checkOptions(String opt);
	
	public String checkOptionName(int opt) {
		switch(opt){
			case 0:
				return listNames[0];
			case 1:
				return listNames[1];
			case 2:
				return listNames[2];
			case 3:
				return listNames[3];
			case 4:
				return listNames[4];
			case 5:
				return listNames[5];
			case 6:
				return listNames[6];
			case 7:
				return listNames[7];
			case 8:
				return listNames[8];
			default:
				return "Blank";
		}
	}
	
	public void tick() {
		frames ++;
		if (frames == maxFrames) {
			index++;
			frames = 0;
			if (index == maxIndex) {
				index = 0;
			}
		}
	}
	
	public void render() {
		Game.gameGraphics.drawImage(animation[index], (Game.player.pos.getX() - Camera.getX()) - 12, (Game.player.pos.getY() - Camera.getY()) - 8, null);
	}
	
	protected static double getMagnitude(double dx, double dy){
		double mag = Math.hypot(dx, dy);
		return mag == 0 ? 1 : mag;
	}

	public abstract void Attack();
	
	public abstract void Dash();
	
	public abstract void powerMove();

	public abstract void specialMove();
}
