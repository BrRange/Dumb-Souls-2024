package world;

import main.Game;

public class Camera {
	private static int x = World.WIDTH / 2, y = World.HEIGHT / 2;
	
	public static void Clamp(int targetX, int targetY) { 
		if (targetX < 0) {
			targetX = 0;
		} 
		else if (targetX > World.WIDTH * 16 - Game.width) {
			targetX = World.WIDTH * 16 - Game.width;
		}
		if (targetY < 0) {
			targetY = 0;
		} 
		else if (targetY > World.HEIGHT * 16 - Game.height) {
			targetX = World.HEIGHT * 16 - Game.height;
		}
		final int deltaX = targetX - x, deltaY = targetY - y;
		double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		if(magnitude == 0.0) magnitude = 1.0;

		x += deltaX / magnitude * Math.ceil(magnitude / 10);
		y += deltaY / magnitude * Math.ceil(magnitude / 10);
	}

	public static int getX(){
		return x;
	}

	public static int getY(){
		return y;
	}
}