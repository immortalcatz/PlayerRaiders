package com.gendeathrow.pmobs.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.client.data.KillCounter;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.typesafe.config.ConfigSyntax;

public class ConfigHandler 
{

	public static File configDir = new File("config/raiders");
	
	public static Configuration config;
	
	private static String mobs = "Mobs";
	private static String mobai = "MobsAI";
	private static String classes = "raidersClasses";
	private static String ESMHooks = "ESMControl";
	private static ConfigCategory remove = new ConfigCategory("remove");
	public static void load()
	{
		RaidersCore.logger.log(Level.INFO, "Loading Configs...");  
		
		PMSettings.noSpawnDays = config.get(config.CATEGORY_GENERAL, "No Spawn Days", new int[0], "Certain days that raiders dont spawn").getIntList();
		
		PMSettings.whitelists = config.getStringList("WhiteLists", config.CATEGORY_GENERAL, new String[0], "Meant for Twitch/Other sub whitelist. # One whitelist link per line. Example: http://whitelist.twitchapps.com/list.php?id=12345 [default: ]");

		
		PMSettings.raidersSpawnWeight = config.getInt("SpawnWeight", mobs, 10, 1, 1000, "Weight of Raiders spawning");

		PMSettings.raidersMaxGroupSpawn = config.getInt("MaxSpawnGroup", mobs, 1, 1, 10, "Max Spawn group size");

		PMSettings.setEquitmentDefault = config.getFloat("Set Equipment Defult Difficulty", mobs, .025f, .01f, 1f, "Sets Raiders default chance to wear equipment based on Easy and Medium difficulty");
		
		PMSettings.setEquptmentHard = config.getFloat("Set Equipment Hard Difficulty", mobs, .1f, .01f, 1f, "Sets Raiders chance to wear equipment based on Hard difficulty");
		
		//PMSettings.sprintersOnlyNight = config.getBoolean("Special Mobs Spawn at Night Only", mobs, false, "Hard/Fast/Special Mobs will only Spawn at night time only. (Except on hard days!)");
		
		PMSettings.removeVanillaSpawners = config.getBoolean("Remove Vanilla Mob Spawner", mobs, false, "Remove Vanilla Mob spawners from Dungeon Hooks");
			
		PMSettings.raidersSpawnerWeight = config.getInt("Mob Spawner Weight", mobs, 200, 1, 1000, "Changes dungeon spawner weight for raiders. Example is zombies are 200, where skeletons are 100.");
		
		PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents All Mobs from Spawning during first day above Y lvl 50");
		
		PMSettings.factionsEnabled = config.getBoolean("Enable Factions", mobs, true, "If true Some Raiders can belong to a friendly/hostile faction. If false they are all hostile");
		
		PMSettings.daySpeedRestiction = config.getFloat("DayTime Speed Modifier", mobs, 0.4f, -1f, 2f, "Mulitply raiders speed during Daytime. 1 is normal speed.");
		PMSettings.anyLightLvlSpawning = config.getBoolean("Spawn no light levels", mobs, true, "Raiders will spawn reguardless of light levels. Basically Daytime Spawning.");
		
		// Mob AI stuff
		PMSettings.leapAttackAI = config.getBoolean("Leap Attack", mobai, true, "Gives some Raiders the abilit to leap attack, small chance increases with each raid difficulty");
		PMSettings.veryHostile = config.getBoolean("Hostile to All", mobai, true, "Raiders will attack any Hostile mob that moves. Raiders will always attack passive mobs regaurdless of settings.");
		
		PMSettings.spawnNether = config.getBoolean("Enable Nether", mobs, true, "Spawn in Nether");
		PMSettings.spawnEnd = config.getBoolean("Enable The End", mobs, false, "Spawn in The End");
		
		// Raiders Classes		
		config.addCustomCategoryComment(classes, "Edit Class Specific options. Adding Drops Example: 'ModID:Item:Meta:Qty:Chance(0.01 - 1)'");
			PMSettings.bruteClass = config.getBoolean("Brute Class Enabled", classes, true, "Adds Brute to the mix. They are slower, bigger, extra health, and hit alot harder");
			PMSettings.bruteWeight = config.get(classes, "Brute Weight", 7).getInt(7);
			PMSettings.bruteDrops =  ItemDrop.getArrayItemDrops(config.get(classes, "Brute Drops", new String[0]).getStringList());
			PMSettings.bruteStartDiff = config.get(classes, "Brute Start Difficulty", 0).getInt(0);
		// ModName:ItemID:meta:qty:chance

			PMSettings.tweakersClass = config.getBoolean("Tweakers Class Enabled", classes, true, "Adds Tweakers to the mix. They are a lot faster,and have lower health");
			PMSettings.tweakerWeight = config.get(classes,"Tweakers Weight", 5).getInt(5);
			PMSettings.tweakerOnlyNight = config.get(classes, "Tweakers Only at Night", false).getBoolean();
			PMSettings.tweakerDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Tweakers Drops", new String[0]).getStringList());
			PMSettings.tweakerStartDiff = config.get(classes, "Tweakers Start Difficulty", 0).getInt(0);
		
		
			PMSettings.pyroClass = config.getBoolean("Pyromaniac Class Enabled", classes, true, "Adds Pyromaniacs to the mix, they will seek out blocks to catch fire. small chance increases with each raid difficulty");
			PMSettings.pyroWeight = config.get(classes, "Pyromaniac Weight", 4).getInt(4);
			PMSettings.pyroDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Pyromaniac Drops", new String[0]).getStringList());
			PMSettings.pyroStartDiff = config.get(classes, "Pyromaniac Start Difficulty", 0).getInt(0);
		
			PMSettings.noneWeight = config.get(classes, "No Class Weight", 80).getInt(80);
			PMSettings.noneDrops = ItemDrop.getArrayItemDrops(config.get(classes, "No Class Drops", new String[0], "No Class is a normal raider").getStringList());

			String[] screamerDrops = new String[3];
			screamerDrops[0] = Items.EXPERIENCE_BOTTLE.getRegistryName().toString() +":0:3:1";
			screamerDrops[1] = Items.GOLDEN_APPLE.getRegistryName().toString() +":0:1:1";
			screamerDrops[2] = Items.DRAGON_BREATH.getRegistryName().toString() +":0:1:0.01";

			PMSettings.screamerClass = config.getBoolean("Screamer Class Enabled", classes, true, "Adds Screamer to the mix, A type of witch class");
			PMSettings.screamerWeight = config.get(classes, "Screamer Weight", 2).getInt(2);
			PMSettings.screamerDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Screamer Drops", screamerDrops).getStringList());
			PMSettings.screamerStartDiff = config.get(classes, "Screamer Start Difficulty", 0).getInt(0);
			PMSettings.screamerFogOn = config.get(classes, "Screamer Fog", true).getBoolean(true);
		
			PMSettings.rangerClass = config.getBoolean("Ranger Class Enabled", classes, true, "Adds Ranger to the mix, These guys are Bow wielding maniacs.");
			PMSettings.rangerWeight = config.get(classes, "Ranger Weight", 10).getInt(10);
			PMSettings.rangerDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Ranger Drops", new String[0]).getStringList());
			PMSettings.rangerStartDiff = config.get(classes, "Ranger Start Difficulty", 1).getInt(1);
			
		// Progression Difficulty stats
		String progCat = "Progessive Difficulty";
		config.addCustomCategoryComment(progCat, "You can set what the raiders get on each Difficulty Increase");
		
		PMSettings.HealthIncrease = config.getInt("Health Increase", progCat, 2, 10, 100, "Each point = Half a Heart.");
		PMSettings.HealthMaxOut = config.getInt("Health Max Outs", progCat, -1, -1, 100, "Sets what Raid Difficulty the health will max out at(Does not mean max health). -1 Means there is no max Difficulty.");

		if(config.hasKey(mobs, "Mob Difficulty Progression"))
		{
			config.moveProperty(mobs, "Mob Difficulty Progression", progCat);
			config.renameProperty(progCat, "Mob Difficulty Progression", "Raid Day Difficulty Progression");
		}
		  
		PMSettings.dayDifficultyProgression = config.getInt("Raid Day Difficulty Progression", progCat, 5, 1, 100, "This is the Raid difficulty. Each set amount of days the mobs get harder(Raid Difficulty increases +1). Each x amount of days harder mobs have more of a chance to spawn its cumulative");
		
		String esmaddon = "esm_addon";
 		//ESM stuff
		PMSettings.esmDiggingPercentage = config.getFloat("Digging AI Percentage", esmaddon, .3f, 0, 1, "Sets percentage of Raiders that will gain the Digging AI(RNG Gods make the true decisions)");
		PMSettings.esmDemolitionRaidDiff = config.getInt("Demolition AI Raid Difficulty", esmaddon, 1, 0, Integer.MAX_VALUE, "TNT mobs wont start till x Raid Difficulty based of x amount of dayd set in 'Mob Difficulty Progression' ");
		PMSettings.esmDemoPercentage = config.getFloat("Demolition AI FineTuning %", esmaddon, 1, 0, 1, "This is meant to make fine tuning to esm percentage. Math->(esmmod% * raidermod% = new%) Example:(1%(.01) * 50%(.5) = 0.5%(.005))");
		PMSettings.esmDiggingTools = config.getInt("Digging Tools Chance", esmaddon, 20, 0, 100, "Sets how many diggers get digging tools?");
		PMSettings.esmDiamondDiggingTools = config.getInt("Digging Tools Diamond Chance", esmaddon, 5, 0, 100, "Sets how many diggers get Diamond digging tools?");
		PMSettings.esmDiamondToolsRaidDiff = config.getInt("Digging Tools Diamond on Raid Difficulty", esmaddon, 2, 0, Integer.MAX_VALUE, "Sets on what Raid Difficulty you see diamond Tools");
		PMSettings.esmDigginRaidDiff = config.getInt("Digging AI Raid Difficulty", esmaddon, 0, 0, Integer.MAX_VALUE, "Digging mobs wont start till x Raid Difficulty based of x amount of dayd set in 'Mob Difficulty Progression' ");
		
		config.removeCategory(remove);
		
		if(config.hasChanged())
			config.save();
		

	}
	
	public static void preInit()
	{
		File file = new File(configDir, "settings.cfg");
		
		config = new Configuration(file);
		
		config.load();
		
		
		PMSettings.renderNameTags = config.getBoolean("Render Name Tags", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders Name tags about thier heads.");
		
		PMSettings.renderOverlays = config.getBoolean("Render Skin Overlays", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders skins overlay features.");
	}
	
	
	public static void PostLoad()
	{
		RaiderManager.readRaiderFile();
		
		EquipmentManager.readEquipmentFile();
		
		// Load Kill Counter
		
		KillCounter.initilize();
	}
	
	public static class ItemDrop
	{
		String itemID;
		int metaID;
		double chance;
		int qty;
		
		public ItemDrop(String itemID, int metaID, double chance, int qty)
		{
			this.itemID = itemID;
			this.metaID = metaID;
			this.chance = chance;
			this.qty = qty;
		}
		
		public static ArrayList<ItemDrop> getArrayItemDrops(String[] itemList)
		{
			ArrayList<ItemDrop> drops = new ArrayList<ItemDrop>();
			
			for(String item : itemList)
			{
				try
				{
					drops.add(getItemDrop(item));
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
			return drops;
		}
		
		public Item getItem()
		{
			return Item.getByNameOrId(this.itemID);
		}
		
		public int getMeta()
		{
			return this.metaID;
		}
		
		public static ItemDrop getItemDrop(String data) throws NumberFormatException
		{
			String[] split = data.split(":");

			String itemID = split[0]+":"+split[1];
			int metaID = Integer.parseInt(split[2]);
			double chance= Double.parseDouble(split[4]);
			int qty = Integer.parseInt(split[3]);
			
			
			return new ItemDrop(itemID, metaID,chance,qty);
		}
		
		public boolean shouldDrop(Random rand)
		{
			return rand.nextDouble() <= this.chance;
		}
		
		public ItemStack getStack(Random rand)
		{
			return new ItemStack(Item.getByNameOrId(this.itemID), this.qty > 1 ? rand.nextInt(this.qty-1)+1 : this.qty, this.metaID);
		}
	}
	
	
}