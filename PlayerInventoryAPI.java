package clashsoft.playerinventoryapi;

import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.playerinventoryapi.common.PICommonProxy;
import clashsoft.playerinventoryapi.common.PIEventHandler;
import clashsoft.playerinventoryapi.network.PINetHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraftforge.common.MinecraftForge;

@Mod(modid = PlayerInventoryAPI.MODID, name = PlayerInventoryAPI.NAME, version = PlayerInventoryAPI.VERSION)
public class PlayerInventoryAPI
{
	public static final String			MODID					= "piapi";
	public static final String			NAME					= "Player Inventory API";
	public static final int				REVISION				= 0;
	public static final String			VERSION					= CSUpdate.CURRENT_VERSION + "-" + REVISION;
	
	@Instance(MODID)
	public static PlayerInventoryAPI	instance;
	
	@SidedProxy(clientSide = "clashsoft.playerinventoryapi.client.PIClientProxy", serverSide = "clashsoft.playerinventoryapi.common.PICommonProxy")
	public static PICommonProxy			proxy;
	
	public static PINetHandler			netHandler;
	
	public static boolean				customSurvivalInventory	= true;
	public static boolean				customCreativeInventory	= true;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		netHandler = new PINetHandler();
		netHandler.init();
		
		proxy.registerTickHandler();
		MinecraftForge.EVENT_BUS.register(new PIEventHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		netHandler.postInit();
	}
}
