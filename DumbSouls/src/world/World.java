package world;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.enemies.*;
import entities.orbs.Rune_Orb;
import main.Game;

public class World {
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static int maxEnemies = 10, wave = 0;
	public static String bossName;
	public static boolean bossTime;

	private static enum waveBuckets {
		WAVE5(
				new Bucket(Rune_Orb.class, 1)),
		WAVE10(
				new Bucket(Enemy_Stain.class, 20),
				new Bucket(Enemy_Eye.class, 14),
				new Bucket(Enemy_Mouth.class, 10),
				new Bucket(Enemy_Mortar.class, 2)),
		WAVE17(
				new Bucket(Enemy_Stain.class, 20),
				new Bucket(Enemy_Eye.class, 14),
				new Bucket(Enemy_Mouth.class, 10),
				new Bucket(Enemy_Trapper.class, 5),
				new Bucket(Enemy_Barrier.class, 3),
				new Bucket(Enemy_Mortar.class, 2)),
		WAVEPLUS(
				new Bucket(Enemy_Stain.class, 20),
				new Bucket(Enemy_Eye.class, 14),
				new Bucket(Enemy_Mouth.class, 10),
				new Bucket(Enemy_Trapper.class, 5),
				new Bucket(Enemy_Barrier.class, 3),
				new Bucket(Enemy_Mortar.class, 2),
				new Bucket(Enemy_Wizard.class, 1));

		private final Bucket[] buckets;

		waveBuckets(Bucket... bcks) {
			buckets = bcks;
		}

		public Bucket[] getVar() {
			return buckets;
		}
	}

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(new FileInputStream(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			tiles = new Tile[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {

					int current = pixels[xx + (yy * WIDTH)];

					tiles[xx + (yy * WIDTH)] = new Floor_Tile(xx * 16, yy * 16,
							Tile.floor_sprite[Game.rand.nextInt(Tile.floor_sprite.length)]);

					switch (current) {
						case 0xFFFFFFFF:
							tiles[xx + (yy * WIDTH)] = new Wall_Tile(xx * 16, yy * 16, Tile.wall_sprite);
							break;
						case 0xFF0000FF:
							Game.player.pos.set(xx * 16, yy * 16);
							Camera.x = xx * 16;
							Camera.y = yy * 16;
							Game.mx = Game.width / 2;
							Game.my = Game.height / 2;
							break;
					}
				}
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	public void raiseMaxEnemies() {
		maxEnemies += 2;
	}

	public void spawnBoss() {
		while (true) {
			int pe = Game.rand.nextInt(3);
			int ex = Game.rand.nextInt(WIDTH - 2);
			int ey = Game.rand.nextInt(HEIGHT - 2);
			if (tiles[ex + (ey * WIDTH)] instanceof Floor_Tile) {
				switch (pe) {
					case 0:
						Game.enemies.add(new Boss_Sucubus(ex * 16, ey * 16));
						bossName = "Sucubus";
						break;
					case 1:
						Game.enemies.add(new Boss_Duality(ex * 16, ey * 16));
						bossName = "Duality";
						break;
					case 2:
						Game.enemies.add(new Boss_Hive(ex * 16, ey * 16));
						bossName = "Hive";
						break;
				}
				bossTime = true;
				break;
			}
		}
	}

	private static class Bucket {
		Class<? extends Enemy> eClass;
		int poolWeight, poolMin, poolMax;

		Bucket(Class<? extends Enemy> ene, int pw) {
			eClass = ene;
			poolWeight = pw;
		}
	};

	private static Enemy poolEnemy(int x, int y, Bucket... buckets) {
		int total = 0;
		for (Bucket b : buckets) {
			b.poolMin = total;
			total += b.poolWeight;
			b.poolMax = total - 1;
		}
		total = Game.rand.nextInt(total);
		for (Bucket b : buckets) {
			if (b.poolMin <= total && b.poolMax >= total) {
				try {
					return b.eClass.getConstructor(int.class, int.class).newInstance(x, y);
				} catch (Exception e) {
					return new Enemy_Stain(x, y);
				}
			}
		}
		return new Enemy_Stain(x, y);
	}

	public void spawnEnemy() {
		while (true) {
			int ex = Game.rand.nextInt(WIDTH);
			int ey = Game.rand.nextInt(HEIGHT);
			if (tiles[ex + (ey * WIDTH)] instanceof Floor_Tile) {
				if (wave <= 5) {
					Game.enemies.add(poolEnemy(ex * 16, ey * 16, waveBuckets.WAVE5.getVar()));
				} else if (wave <= 10) {
					Game.enemies.add(poolEnemy(ex * 16, ey * 16, waveBuckets.WAVE10.getVar()));
				} else if (wave <= 17) {
					Game.enemies.add(poolEnemy(ex * 16, ey * 16, waveBuckets.WAVE17.getVar()));
				} else {
					Game.enemies.add(poolEnemy(ex * 16, ey * 16, waveBuckets.WAVEPLUS.getVar()));
				}
				break;
			}
		}

	}

	public void render() {
		int xstart = Camera.getX() >> 4;
		int ystart = Camera.getY() >> 4;

		int xend = xstart + (Game.width >> 4);
		int yend = ystart + (Game.height >> 4);

		for (int xx = xstart; xx <= xend; xx++) {
			for (int yy = ystart; yy <= yend; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render();
			}
		}
	}
}
