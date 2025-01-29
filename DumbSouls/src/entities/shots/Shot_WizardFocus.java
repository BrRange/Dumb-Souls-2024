package entities.shots;

import java.awt.image.BufferedImage;

import main.Game;

public class Shot_WizardFocus extends Shot{
    public Shot_WizardFocus(int x, int y, double dx, double dy, double dmg, BufferedImage sprite){
        super(x, y, 16, 16, dx, dy, 0, 0, dmg, 210, sprite);
    }

    private void narrowAngle(){
        double elapse = maxLife - life;
        speed =  elapse * elapse / 6400;
        dir.set(1000 * dir.x + Game.player.centerX() - centerX(), 1000 * dir.y + Game.player.centerY() - centerY());
        dir.normalize();
    }

	public void tick() {
        narrowAngle();
		pos.move(dir.x * speed, dir.y * speed);
		life--;
		if (life == 0) die(null);
	}
}