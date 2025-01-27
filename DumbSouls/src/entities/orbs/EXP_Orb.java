package entities.orbs;

import entities.Entity;
import entities.enemies.Enemy;
import main.Game;
import world.World;

public class EXP_Orb extends Enemy{
	public EXP_Orb(int x, int y, int exp, int hue) {
		super(x, y, 16, 16, Game.sheet.getSprite(0, 144, 16, 16));
		this.hue = hue;
		getAnimation(0, 144, 16, 16, 3);
		expValue = World.bossTime ? 0 : exp;
		speed = 0.8;
		setMask(0, 0, 16, 16);
		depth = 1;
		maxFrames = 6;
	}
	
    public void tick() {
		if (isColiding(Game.player) || expValue == 0){
            Game.entities.remove(this);
			Game.player.exp += expValue;
        }
		animate();
		if (Entity.calculateDistance(centerX(), centerY(), Game.player.centerX(), Game.player.centerY()) < 128)
			movement();
	}
}