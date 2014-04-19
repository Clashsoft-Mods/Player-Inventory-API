package clashsoft.playerinventoryapi;

import clashsoft.cslib.minecraft.ClashsoftMod;
import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.cslib.minecraft.util.CSConfig;
import clashsoft.playerinventoryapi.api.IInventoryHandler;
import clashsoft.playerinventoryapi.common.PIEventHandler;
import clashsoft.playerinventoryapi.common.PIProxy;
import clashsoft.playerinventoryapi.inventory.ContainerInventory;
import clashsoft.playerinventoryapi.network.PINetHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PlayerInventoryAPI.MODID, name = PlayerInventoryAPI.NAME, version = PlayerInventoryAPI.VERSION)
public class PlayerInventoryAPI extends ClashsoftMod
{
	public static final String			MODID					= "piapi";
	public static final String			NAME					= "Player Inventory API";
	public static final String			ACRONYM					= "piapi";
	public static final String			VERSION					= CSUpdate.CURRENT_VERSION + "-1.0.0-dev";
	
	@Instance(MODID)
	public static PlayerInventoryAPI	instance;
	
	@SidedProxy(clientSide = "clashsoft.playerinventoryapi.client.PIClientProxy", serverSide = "clashsoft.playerinventoryapi.common.PIProxy")
	public static PIProxy				proxy;
	
	public static boolean				customSurvivalInventory	= true;
	public static boolean				customCreativeInventory	= true;
	
	public static boolean				itemTooltip				= false;
	public static boolean				buffTooltip				= true;
	public static boolean				playerTooltip			= true;
	
	public PlayerInventoryAPI()
	{
		super(proxy, MODID, NAME, ACRONYM, VERSION);
		this.hasConfig = true;
		this.eventHandler = new PIEventHandler();
		this.netHandlerClass = PINetHandler.class;
		this.url = "https://github.com/Clashsoft/Player-Inventory-API/wiki/";
	}
	
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
	
	@Override
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}
	
	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
	
	@Override
	public void readConfig()
	{
		itemTooltip = CSConfig.getBool("tooltip", "Item Tooltip", itemTooltip);
		buffTooltip = CSConfig.getBool("tooltip", "Buff Tooltip", buffTooltip);
		playerTooltip = CSConfig.getBool("tooltip", "Player Tooltip", playerTooltip);
	}
	
	/**
	 * Registers a new {@link IInventoryHandler}.
	 * 
	 * @param handler
	 *            the inventory handler
	 */
	public static void addInventoryHandler(IInventoryHandler handler)
	{
		ContainerInventory.addInventoryHandler(handler);
	}
}
