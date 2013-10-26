package com.chaosdev.playerinventoryapi;

import com.chaosdev.playerinventoryapi.common.CommonProxy;
import com.chaosdev.playerinventoryapi.handlers.PIAPIEventHandler;
import com.chaosdev.playerinventoryapi.handlers.PIAPIPacketHandler;
import com.chaosdev.playerinventoryapi.lib.ExtendedInventory;
import com.chaosdev.playerinventoryapi.lib.PIAPIConstants;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraftforge.common.MinecraftForge;

import static com.chaosdev.playerinventoryapi.lib.PIAPIConstants.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION)
@NetworkMod(channels = { CHANNEL_NAME, ExtendedInventory.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PIAPIPacketHandler.class)
public class PlayerInventoryAPI
{
	@Instance(PIAPIConstants.MOD_ID)
	public static PlayerInventoryAPI	instance;
	
	@SidedProxy(clientSide = CLIENT_PROXY_LOCATION, serverSide = COMMON_PROXY_LOCATION)
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
