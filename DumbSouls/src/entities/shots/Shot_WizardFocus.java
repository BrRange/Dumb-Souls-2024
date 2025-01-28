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
        double deltaX = 1000 * dirx + Game.player.centerX() - centerX();
        double deltaY = 1000 * diry + Game.player.centerY() - centerY();
        double mag = Math.hypot(deltaX, deltaY);
        if(mag == 0) mag = 1;
        dirx = deltaX / mag;
        diry = deltaY / mag;
    }

	public void tick() {
        narrowAngle();
		movement();
		life--;
		if (life == 0) die(null);
	}
}