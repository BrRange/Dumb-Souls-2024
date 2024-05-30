package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import entities.Player;
import entities.weapons.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import entities.runes.*;

public class Save_Game {
	
	private static int weaponSum;

	private static int bitIntWriter(boolean value, int bitIndex) {
		return value ? 0 : 1 << bitIndex;
	}
	
	public static void save() throws IOException{
		weaponSum = bitIntWriter(Fire_Weapon.block, 0);
		weaponSum += bitIntWriter(Wind_Weapon.block, 1);
		weaponSum += bitIntWriter(Ice_Weapon.block, 2);
		weaponSum += bitIntWriter(Fisical_Weapon.block, 3);
		weaponSum += bitIntWriter(Poison_Weapon.block, 4);
		
		final Path path = Paths.get("SaveDS.txt");
		
		 try (final BufferedWriter writer = Files.newBufferedWriter(path,StandardCharsets.UTF_8, StandardOpenOption.CREATE);) {
			 writer.write("" + Player.souls);
			 writer.newLine();
			 writer.write("" + weaponSum);
			 writer.newLine();
			 for (int i = 0; i < Player.runesInventory.size(); i++) {
				 writer.write(Player.runesInventory.get(i).name + ";");
			 }
		     writer.flush();
		     writer.close();
		 }
	}

	private static boolean intBitReader(int value, int bitIndex){
		return (value & bitIndex) == 0;
	}
	
	public static void loadSave() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("SaveDS.txt"));
			
			Player.souls = Integer.parseInt(reader.readLine());
			weaponSum = Integer.parseInt(reader.readLine());
			String[] runes = reader.readLine().split(";");
			
			reader.close();
			
			Fire_Weapon.block = intBitReader(weaponSum, 1);
			Wind_Weapon.block = intBitReader(weaponSum, 2);
			Ice_Weapon.block = intBitReader(weaponSum, 4);
			Fisical_Weapon.block = intBitReader(weaponSum, 8);
			Poison_Weapon.block = intBitReader(weaponSum, 16);
			if (runes.length > 0) {
				for (int i = 0; i < runes.length; i++) {
					Player.runesInventory.add(InventoryMaker(runes[i]));
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			try{
			PrintWriter writer = new PrintWriter("SaveDS.txt", "UTF-8");
			writer.println("0");
			writer.println("0");
			writer.println(";");
			writer.close();
			loadSave();
			} catch (IOException ee){
				ee.printStackTrace();
			}
		}
	}
	
	private static Rune InventoryMaker(String name) {
		switch(name) {
		case "Rune of Life":
			return new Life_Rune();
		case "Rune of Mana":
			return new Mana_Rune();
		case "Rune of Speed":
			return new Speed_Rune();
		case "Double Attack Rune":
			return new MultiAttack_Rune();
		case "Rune of Expirience":
			return new EXP_Rune();
		default:
			return null;
		}
	}
}
