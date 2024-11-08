package entities.enemies;

import java.awt.image.BufferedImage;

import entities.orbs.EXP_Orb;
import world.Camera;
import world.World;
import main.*;

public class Debri_Enemy extends Enemy{
    private int index, maxIndex = 3, frames, maxFrames = 6, timer = 0;
	
	public Debri_Enemy(int x, int y, int width, int height, BufferedImage sprite, int expValue, boolean specialRare) {
		super(x, y, width, height, sprite);
		this.specialRare = specialRare;
		if (specialRare){
			specialMult = 3;
			hue = 0xFFFFFF;
		}
		this.getAnimation(48, 144, 16, 16, 3);
        this.expValue = expValue;
        this.maxLife = 8 * specialMult + (int)(0.08 * World.wave);
        this.life = this.maxLife;
		this.expValue = expValue;
        this.maxSpeed = 0.2 + (specialMult - 1)/3;
        this.speed = this.maxSpeed;
        this.frost = 0;
		this.setMask(1, 1, 14, 14);
	}

    private void animate() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
	}

    private void attack() {
		Game.player.life -= 6 * specialMult + 0.06 * World.wave;
		timer = 0;
	}

    public void die(){
        Game.enemies.remove(this);
        Game.entities.add(new EXP_Orb(this.getX(), this.getY(), 16, 16, Enemy.baseSprite, this.expValue, this.hue));
    }

    public void tick() {
		animate();

        if (calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 48){
            this.maxSpeed = 1.5;
        }

        if (!isColiding(Game.player)) {
			this.movement();
		}

		else {
			if (timer % 15 == 0) {
				attack();
				timer = 0;
			}
			timer += 1;
		}
        this.frostEffect(0.95);

        this.shotDamage();
		if (life <= 0) {
			die();
		}
	}

    public void render() {
		Game.gameGraphics.drawImage(this.animation[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}