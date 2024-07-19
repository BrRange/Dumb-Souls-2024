package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;

import entities.Entity;
import entities.Player;
import entities.enemies.*;
import entities.shots.*;
import graphics.Spritesheet;
import graphics.UI;
import sounds.SoundPlayer;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;
	private static Thread thread;
	private static boolean isRuning = false;
	
	public static HashSet<Integer> keyController = new HashSet<Integer>(); 
	public static HashSet<Integer> clickController = new HashSet<Integer>(); 
	public static JFrame frame;
	public static final int width = 320;
	public static final int height = 160;
	public static final int scale = 3;
	public static int mx, my, amountTicks;
	
	private static BufferedImage image;
	public static Spritesheet sheet;
	public static enum gameState{
		NORMAL(() -> tick(), () -> render()),
		MENULEVEL(() -> Menu_Level.tick(), () -> Menu_Level.render()),
    	MENUINIT(() -> Menu_Init.tick(), () -> Menu_Init.render()),
		MENUPLAYER(() -> Menu_Player.tick(), () -> Menu_Player.render()),
		MENUHELP(() -> Menu_Help.tick(), () -> Menu_Help.render()),
		MENUPAUSE(() -> Menu_Pause.tick(), () -> {render(); Menu_Pause.render();}),
		MENURUNES(() -> Menu_Runes.tick(), () -> Menu_Runes.render());

		private Runnable stateTick, stateRender;
		gameState(Runnable stateTick, Runnable stateRender){
			this.stateTick = stateTick;
			this.stateRender = stateRender;
		}
	} public static gameState gameStateHandler;

	public static UI ui;
	public static World world;
	public static Player player;
	public static Random rand;
	public static Menu_Level levelUpMenu;
	public static Menu_Player playerMenu;
	public static Menu_Init startMenu;
	public static Menu_Help helpMenu;
	public static Menu_Pause pauseMenu;
	public static Menu_Runes runesMenu;
	private static SoundPlayer soundtrack;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shot> shots;
	public static List<Enemy_Shot> eShots;
	public static Graphics gameGraphics;

	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(width * scale, height * scale));
		initFrame();
		entities = new ArrayList<Entity>();
		shots = new ArrayList<Shot>();
		enemies = new ArrayList<Enemy>();
		eShots = new ArrayList<Enemy_Shot>();
		rand = new Random();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		gameGraphics = image.getGraphics();
		sheet = new Spritesheet("spritesheet.png");
		player = new Player(0, 0, 16, 16, sheet.getSprite(0, 16, 16, 16));
		entities.add(player);
		world = new World("map00.png");
		ui = new UI();
		soundtrack = new SoundPlayer("sndTrack.wav");
		startMenu = new Menu_Init();
		playerMenu = new Menu_Player();
		pauseMenu = new Menu_Pause();
		levelUpMenu = new Menu_Level(3);
		helpMenu = new Menu_Help();
		runesMenu = new Menu_Runes();
		gameStateHandler = gameState.MENUINIT;
		Save_Game.loadSave();
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
		isRuning = true;
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
		soundtrack.LoopSound();
	}
	public void end() {
		isRuning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void initFrame() {
		frame = new JFrame("Dumb Souls");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.createBufferStrategy(3);
	}
	
	private static void spawnEnemies() {
		if (enemies.size() == 0) {
			enemies.add(new Barrier_Enemy(60, 60, 48, 32, null));
			if (World.wave % 10 != 0) {
				world.raiseMaxEnemies();
				World.wave++;
				for (int c = 0; c <= World.maxEnemies; c++) {
					world.spawnEnemy();
				}
			}
			else {
				world.spawnBoss();
				World.wave++;
			}
		}
	}

	private void baseRender(){
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		gameGraphics = Game.image.getGraphics();
		gameStateHandler.stateRender.run();
		gameGraphics.dispose();
		gameGraphics = bs.getDrawGraphics();
		gameGraphics.drawImage(image, 0, 0, width * scale, height * scale, null);
		if(gameStateHandler == gameState.NORMAL) ui.render();
		bs.show();
	}

	private static void tick() {
		player.moveX = player.moveY = 0;
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		for(int i = 0; i < shots.size(); i++) {
			shots.get(i).tick();
		}
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick();
		}
		for(int i = 0; i < eShots.size(); i++) {
			eShots.get(i).tick();
		}
		spawnEnemies();
	}

	private static void render() {
		gameGraphics.setColor(new Color(0, 0, 0));
		gameGraphics.fillRect(0, 0, width, height);
		world.render();
		Collections.sort(entities, Entity.entityDepth);
		Collections.sort(enemies, Entity.entityDepth);
		for(Entity e : entities) {
			e.render();
		}
		for(Enemy e : enemies) {
			e.render();
		}
		for(Shot e : shots) {
			e.render();
		}
		for(Enemy_Shot e : eShots) {
			e.render();
		}
	}
	
	public void run() {
		requestFocus();
		long lastTime = System.currentTimeMillis();
		int amountTicks = 60;
		float framesSec = 1f / amountTicks;
		double delta;
		
		while(isRuning) {
			long now = System.currentTimeMillis();
			delta = (now - lastTime) / 1000.0;
			if(delta >= framesSec) {
				gameStateHandler.stateTick.run();
				lastTime = now;
			}
			baseRender();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyController.add(e.getKeyCode());

		if (keyController.contains(KeyEvent.VK_ESCAPE)) {
			switch(gameStateHandler) {
				case NORMAL:
					gameStateHandler = gameState.MENUPAUSE;
					break;
				case MENUPAUSE:
					player.stopMoving();
					gameStateHandler = gameState.NORMAL;
					break;
				default: break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyController.remove(e.getKeyCode());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		clickController.add(e.getButton());
		player.playerWeapon.mx = e.getX() / scale;
		player.playerWeapon.my = e.getY() / scale;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clickController.remove(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		clickController.clear();
		keyController.clear();
		if(gameStateHandler == gameState.NORMAL) gameStateHandler = gameState.MENUPAUSE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
}