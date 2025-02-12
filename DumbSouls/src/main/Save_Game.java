package main;

import entities.Player;
import entities.runes.*;
import entities.weapons.Weapon_Fire;
import entities.weapons.Weapon_Fisical;
import entities.weapons.Weapon_Ice;
import entities.weapons.Weapon_Wind;
import entities.weapons.Weapon_Poison;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Save_Game{

	private static int bitIntWriter(boolean value, int bitIndex) {
		return value ? bitIndex : 0;
	}
	
	public static void save() throws Exception{
		int weaponSum = bitIntWriter(Weapon_Fire.unlocked, 1);
		weaponSum += bitIntWriter(Weapon_Wind.unlocked, 2);
		weaponSum += bitIntWriter(Weapon_Ice.unlocked, 4);
		weaponSum += bitIntWriter(Weapon_Fisical.unlocked, 8);
		weaponSum += bitIntWriter(Weapon_Poison.unlocked, 16);
		int runeSum = 0;
		for(int i = 0; i < Rune.runesInGame; i++)
			runeSum += bitIntWriter(Player.runesInventory.get(i).collected, 1 << i);
		try (final PrintWriter writer = new PrintWriter("SaveDS.save")) {
			writer.write((char)(Player.souls & -16777216));
			writer.write((char)(Player.souls & 16711680));
			writer.write((char)(Player.souls & 65280));
			writer.write((char)(Player.souls & 255));
			writer.write((char)weaponSum);
			writer.write((char)runeSum);
			writer.write((char)runeSum);
			writer.close();
		} catch(Exception exc){
			createSaveFile();
		}
	}

	private static boolean intBitReader(int value, int bitIndex){
		return (value & bitIndex) != 0;
	}
	
	public static void loadSave(){
		try {
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream("SaveDS.save"));
			
			int souls[] = new int[4], weapons[] = new int[1], runes[] = new int[1];
			for(int i = 0; i < souls.length; i++){
				souls[i] = reader.read();
			}
			weapons[0] = reader.read();
			runes[0] = reader.read();
			runes[0] = reader.read();
			reader.close();
			Player.souls = souls[0] << 24 | souls[1] << 16 | souls[2] << 8 | souls[3];
			Weapon_Fire.unlocked = intBitReader(weapons[0], 1);
			Weapon_Wind.unlocked = intBitReader(weapons[0], 2);
			Weapon_Ice.unlocked = intBitReader(weapons[0], 4);
			Weapon_Fisical.unlocked = intBitReader(weapons[0], 8);
			Weapon_Poison.unlocked = intBitReader(weapons[0], 16);
			System.out.println(weapons[0]);
			if(Player.runesInventory == null) InventoryMaker();
			for(int i = 0; i < Rune.runesInGame; i++){
				Player.runesInventory.get(i).collected = intBitReader(runes[0], 1 << i);
			}
		}
		catch (Exception exc) {
			createSaveFile();
		}
	}
	
	private static void InventoryMaker() {
		Player.runesInventory = new ArrayList<Rune>(Rune.runesInGame);
		Player.runesInventory.add(new Life_Rune());
		Player.runesInventory.add(new Mana_Rune());
		Player.runesInventory.add(new Speed_Rune());
		Player.runesInventory.add(new MultiAttack_Rune());
		Player.runesInventory.add(new EXP_Rune());
		Player.runesEquipped = new ArrayList<Rune>(Rune.runesInGame);
	}

	private static void createSaveFile(){
		try{
			new File("SaveDS.save").createNewFile();
			PrintWriter pWriter = new PrintWriter("SaveDS.save");
			pWriter.print("\0\0\0\0\0\0");
			pWriter.close();
			if(Player.runesInventory == null) InventoryMaker();
		} catch(Exception err){}
	}
}