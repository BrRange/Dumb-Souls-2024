package entities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import graphics.UI;
import entities.shots.Enemy_Shot;
import java.awt.Graphics;
import entities.weapons.*;
import main.*;
import world.*;
import entities.runes.Rune;
import java.util.List;
import java.util.ArrayList;

public class Player extends Entity{
	
	private int tickTimer, attackTimer;
	public boolean moving, attack, levelUp, dash, ablt2, ablt3;
	public short moveX, moveY;
	public int maxLife = 100, exp = 0, maxExp = 100, maxMana = 100;
	public static int souls;
	public int level = 1;
	private int frames, maxFrames = 40;
	public int direct = 2;
	public double maxSpeed = 1.5, speed = maxSpeed, mana = 100, manaRec = 2, life = 100, lifeRec=1.001, moveCos, moveSin;
	public Weapon playerWeapon;
	public static List<Rune> runesInventory;
	public List<Rune> runesEquipped;
	public static int runeLimit = 3;
	
	private BufferedImage[] playerDown;
	private BufferedImage[] playerRight;
	private BufferedImage[] playerLeft;
	private BufferedImage[] playerUp;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		playerDown = new BufferedImage[4];
		playerRight = new BufferedImage[4];
		playerLeft = new BufferedImage[4];
		playerUp = new BufferedImage[4];
		
		runesInventory = new ArrayList<Rune>();
		runesEquipped = new ArrayList<Rune>();
		
		for (int xsp = 0; xsp < 4; xsp++) {
			playerDown[xsp] = Game.sheet.getSprite(xsp * 16, 16, 16, 16);
		}
		for (int xsp = 0; xsp < 4; xsp++) {
			playerRight[xsp] = Game.sheet.getSprite(xsp * 16, 16 * 2, 16, 16);
		}
		for (int xsp = 0; xsp < 4; xsp++) {
			playerLeft[xsp] = Game.sheet.getSprite(xsp * 16, 16 * 3, 16, 16);
		}
		for (int xsp = 0; xsp < 4; xsp++) {
			playerUp[xsp] = Game.sheet.getSprite(xsp * 16, 16 * 4, 16, 16);
		}
		
		setMask(4, 1, 8, 15);
		this.depth = 1;
	}
	
	private void isAttacking() {
		if (attackTimer == playerWeapon.attackTimer) {
			if (attack) {
			attack = false;
			attackTimer = 0;
			playerWeapon.Attack();
			}
		}
		if (attackTimer < playerWeapon.attackTimer){
			attackTimer++;
		}
	}

	public void stopMoving(){
		moveX = moveY = 0;
	}
	
	private void runeTick() {
		if (runesEquipped.size() > 0) {
			for(int i = 0; i < runesEquipped.size(); i++) {
				runesEquipped.get(i).tick();
			}
		}
	}

	
	public static void die() {
		Game.entities.clear();
		Game.shots.clear();
		Game.enemies.clear();
		Game.eShots.clear();
		Game.player = new Player(0, 0, 16, 16, Game.sheet.getSprite(0, 16, 16, 16));
		Game.entities.add(Game.player);
		World.maxEnemies = 5;
		World.wave = 1;
		World.bossName = "";
		World.bossTime = false;
		Game.world = new World("/map00.png");
		Game.ui = new UI();
		Game.startMenu = new Menu_Init();
		Game.playerMenu = new Menu_Player();
		Game.levelUpMenu = new Menu_Level(3);
		Game.gameState = "MENUINIT";
		try {
			Save_Game.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void checkExp() {
		if (exp >= maxExp) {
			levelUp = true;
			level ++;
			exp -= maxExp;
			moveX = moveY = 0;
			maxExp *= 1.2;
			Game.gameState = "LEVELUP";
		}
	}
	
	private void isMoving() {
		frames++;
		if (frames == maxFrames) frames = 0;
	}
	
	private void shotDamage() {
		for (int i = 0;  i < Game.eShots.size(); i++) {
			Enemy_Shot e = Game.eShots.get(i);
			if (isColiding(this, e)) {
				life -= e.damage;
				Game.eShots.remove(e);
			}
		}
	}

	public int getSouls(){
		return souls;
	}

	private boolean TickTimer(int frames) {
		if (this.tickTimer % frames == 0) {
			return true;
		}
		else{
			return false;
		}
	}

	private void refreshTick(){
		this.tickTimer++;
		if (this.tickTimer >= 60)
			this.tickTimer = 0;
	}
	
	public void tick() {
		if (Game.keyController.contains(87) || Game.keyController.contains(38))//W UP
			moveY--;
		if (Game.keyController.contains(83) || Game.keyController.contains(40))//S DOWN
			moveY++;
		if (Game.keyController.contains(68) || Game.keyController.contains(39))//A LEFT
			moveX--;
		if (Game.keyController.contains(65) || Game.keyController.contains(37))//D RIGHT
			moveX++;
		
		moving = moveX != 0 || moveY != 0;
		if (moveX < 0) direct = 0;
		else if (moveX > 0) direct = 1;
		else if (moveY > 0) direct = 2;
		else if (moveY < 0) direct = 3;

		{
			double tempAngle = Math.atan2(moveY, -moveX);
			moveCos = Math.cos(tempAngle);
			moveSin = Math.sin(tempAngle);
		}

		if (Game.keyController.contains(32) || playerWeapon.md1) playerWeapon.Dash();

		if(moving){
			this.x += speed * moveCos;
			this.y += speed * moveSin;
			isMoving();
		}
		
		if (mana < maxMana && TickTimer(20))
			mana = Math.min(maxMana, mana + manaRec);

		if (Game.player.life < Game.player.maxLife && TickTimer(10))
			Game.player.life = Math.min(Game.player.maxLife, Game.player.life * Game.player.lifeRec);

		refreshTick();

		if (life <= 0) {
			//die();
		}
		
		playerWeapon.tick();
		playerWeapon.Effect();
		
		isAttacking();
		checkExp();
		if (Game.keyController.contains(49)) playerWeapon.Ablt2();
		if (Game.keyController.contains(50)) playerWeapon.Ablt3();
		shotDamage();
		runeTick();
		
		if (playerWeapon instanceof Mana_Weapon) {
			Mana_Weapon.graficEffect();
		}

		Camera.x = Camera.Clamp(this.getX() - (Game.width / 2), 0, World.WIDTH * 16 - Game.width);
		Camera.y = Camera.Clamp(this.getY() - (Game.height / 2), 0, World.HEIGHT * 16 - Game.height);

		moveX = moveY = 0;
	}
	
	public void render(Graphics g) {
		switch(direct){
		case 0:
			g.drawImage(playerRight[moving ? frames / 10 : 0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			break;
		case 1:
			g.drawImage(playerLeft[moving ? frames / 10 : 0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			break;
		case 2:
			g.drawImage(playerDown[moving ? frames / 10 : 0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			break;
		case 3:
			g.drawImage(playerUp[moving ? frames / 10 : 0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			break;
		}
		playerWeapon.render(g);
	}
}
