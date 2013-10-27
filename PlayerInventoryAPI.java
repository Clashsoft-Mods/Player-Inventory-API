package com.chaosdev.playerinventoryapi;

import clashsoft.clashsoftapi.util.CSUpdate;

import com.chaosdev.playerinventoryapi.common.CommonProxy;
import com.chaosdev.playerinventoryapi.handlers.PIAPIEventHandler;
import com.chaosdev.playerinventoryapi.handlers.PIAPIPacketHandler;
import com.chaosdev.playerinventoryapi.lib.ExtendedInventory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "PlayerInventoryAPI", name = "Player Inventory API", version = PlayerInventoryAPI.VERSION)
@NetworkMod(channels = { "PIAPI", ExtendedInventory.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PIAPIPacketHandler.class)
public class PlayerInventoryAPI
{
	public static final int REVISION = 0;
	public static final String VERSION = CSUpdate.CURRENT_VERSION + "-" + REVISION;
	
	@Instance("PlayerInventoryAPI")
	public static PlayerInventoryAPI	instance;
	
	@SidedProxy(clientSide = "com.chaosdev.playerinventoryapi.client.ClientProxy", serverSide = "com.chaosdev.playerinventoryapi.common.CommonProxy")
	public static CommonProxy			proxy;
	
	public static PIAPIPacketHandler	packetHandler;
	
	public static boolean				enableCustomSurvivalInventory	= true;
	public static boolean				enableCustomCreativeInventory	= true;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry nr = NetworkRegistry.instance();
		nr.registerGuiHandler(instance, proxy);
		packetHandler = new PIAPIPacketHandler();
		packetHandler.registerChannels();
		
		proxy.registerTickHandler();
		MinecraftForge.EVENT_BUS.register(new PIAPIEventHandler());
	}
}
