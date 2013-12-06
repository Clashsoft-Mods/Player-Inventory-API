package clashsoft.playerinventoryapi;

import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.playerinventoryapi.common.PICommonProxy;
import clashsoft.playerinventoryapi.common.PIPacketHandler;
import clashsoft.playerinventoryapi.handlers.PIEventHandler;
import clashsoft.playerinventoryapi.lib.ExtendedInventory;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "PlayerInventoryAPI", name = "Player Inventory API", version = PlayerInventoryAPI.VERSION)
@NetworkMod(channels = { "PIAPI", ExtendedInventory.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PIPacketHandler.class)
public class PlayerInventoryAPI
{
	public static final int				REVISION						= 0;
	public static final String			VERSION							= CSUpdate.CURRENT_VERSION + "-" + REVISION;
	
	@Instance("PlayerInventoryAPI")
	public static PlayerInventoryAPI	instance;
	
	@SidedProxy(clientSide = "clashsoft.playerinventoryapi.client.PIClientProxy", serverSide = "clashsoft.playerinventoryapi.common.PICommonProxy")
	public static PICommonProxy		proxy;
	
	public static PIPacketHandler	packetHandler;
	
	public static boolean				enableCustomSurvivalInventory	= true;
	public static boolean				enableCustomCreativeInventory	= true;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry nr = NetworkRegistry.instance();
		nr.registerGuiHandler(instance, proxy);
		packetHandler = new PIPacketHandler();
		packetHandler.registerChannels();
		
		proxy.registerTickHandler();
		MinecraftForge.EVENT_BUS.register(new PIEventHandler());
	}
}
