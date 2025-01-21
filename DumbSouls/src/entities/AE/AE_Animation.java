package entities.AE;

import main.Game;

import world.Camera;

public class AE_Animation extends AE_Attack_Entity {

	private int time;

	public AE_Animation(int x, int y, int width, int height, int time, int xSprite, int ySprite) {
		super(x, y, width, height, null, time);
		getAnimation(xSprite, ySprite, width, height, 1);
		depth = 2;
	}

	public void tick() {
		time++;

		x += Game.rand.nextDouble() - 0.5;
		y -= Game.rand.nextDouble() / 2;

		if (time == life) {
			die();
		}
	}

	public void render() {
		Game.gameGraphics.drawImage(animation[0], getX() - Camera.getX(), getY() - Camera.getY(), getWidth(),
				getHeight(), null);
	}
}
