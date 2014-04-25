package clashsoft.playerinventoryapi;

import clashsoft.cslib.minecraft.ClashsoftMod;
import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.cslib.minecraft.util.CSConfig;
import clashsoft.playerinventoryapi.api.IInventoryHandler;
import clashsoft.playerinventoryapi.api.invobject.IInventoryObject;
import clashsoft.playerinventoryapi.client.gui.GuiCreativeInventory;
import clashsoft.playerinventoryapi.client.gui.GuiSurvivalInventory;
import clashsoft.playerinventoryapi.common.PIEventHandler;
import clashsoft.playerinventoryapi.common.PIProxy;
import clashsoft.playerinventoryapi.inventory.ContainerInventory;
import clashsoft.playerinventoryapi.inventory.InventorySlots;
import clashsoft.playerinventoryapi.network.PINetHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.client.gui.GuiButton;

@Mod(modid = PlayerInventoryAPI.MODID, name = PlayerInventoryAPI.NAME, version = PlayerInventoryAPI.VERSION)
public class PlayerInventoryAPI extends ClashsoftMod
{
	public static final String			MODID					= "piapi";
	public static final String			NAME					= "Player Inventory API";
	public static final String			ACRONYM					= "piapi";
	public static final String			VERSION					= CSUpdate.CURRENT_VERSION + "-1.0.0-alpha";
	
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
	
	// ---------- API ---------- \\
	
	/**
	 * Sets the position of a slot in the both the survival and the creative
	 * player inventory.
	 * 
	 * @see PlayerInventoryAPI#setSurvivalSlot(int, int, int)
	 * @see PlayerInventoryAPI#setCreativeSlot(int, int, int)
	 * @param slotID
	 *            the ID of the slot
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setSlot(int slotID, int x, int y)
	{
		InventorySlots.setSurvivalSlot(slotID, x, y);
		InventorySlots.setCreativeSlot(slotID, x, y);
	}
	
	/**
	 * Sets the position of a slot in the creative player inventory.
	 * 
	 * @param slotID
	 *            the ID of the slot
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setCreativeSlot(int slotID, int x, int y)
	{
		InventorySlots.setCreativeSlot(slotID, x, y);
	}
	
	/**
	 * Sets the position of a slot in the survival player inventory.
	 * 
	 * @param slotID
	 *            the ID of the slot
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setSurvivalSlot(int slotID, int x, int y)
	{
		InventorySlots.setSurvivalSlot(slotID, x, y);
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
	
	/**
	 * Sets the position of the player display in the survival inventory.
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setSurvivalPlayer(int x, int y)
	{
		GuiSurvivalInventory.playerDisplayX = x;
		GuiSurvivalInventory.playerDisplayY = y;
	}
	
	/**
	 * Sets the position of the player display in the survival inventory.
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setCreativePlayer(int x, int y)
	{
		GuiCreativeInventory.playerDisplayX = x;
		GuiCreativeInventory.playerDisplayY = y;
	}
	
	/**
	 * Sets the position of the bin slot in the creative inventory.
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setBinSlot(int x, int y)
	{
		GuiCreativeInventory.binSlotX = x;
		GuiCreativeInventory.binSlotY = y;
	}
	
	/**
	 * Sets the position and rotation of the crafting arrow in the survival
	 * inventory.
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param rotation
	 *            the ratation
	 */
	public static void setCraftArrow(int x, int y, float rotation)
	{
		GuiSurvivalInventory.craftArrowX = x;
		GuiSurvivalInventory.craftArrowY = y;
		GuiSurvivalInventory.craftArrowRot = rotation;
	}
	
	/**
	 * Sets the position of the crafting label in the survival inventory.
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public static void setCraftLabel(int x, int y)
	{
		GuiSurvivalInventory.craftLabelX = x;
		GuiSurvivalInventory.craftLabelY = y;
	}
	
	/**
	 * Sets the size of the survival inventory window.
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public static void setSurvivalWindowSize(int width, int height)
	{
		GuiSurvivalInventory.windowWidth = width;
		GuiSurvivalInventory.windowHeight = height;
	}
	
	/**
	 * Sets the size of the creative inventory window.
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public static void setCreativeWindowSize(int width, int height)
	{
		GuiCreativeInventory.windowWidth = width;
		GuiCreativeInventory.windowHeight = height;
	}
	
	/**
	 * Adds a button to both the survival and the creative inventory.
	 * 
	 * @param handler
	 *            the button handler
	 * @param button
	 *            the button
	 */
	public static void addButton(IInventoryHandler handler, GuiButton button)
	{
		GuiSurvivalInventory.addButton(handler, button);
		GuiCreativeInventory.addButton(handler, button);
	}
	
	/**
	 * Adds a button to the survival inventory.
	 * 
	 * @param handler
	 *            the button handler
	 * @param button
	 *            the button
	 */
	public static void addSurvivalButton(IInventoryHandler handler, GuiButton button)
	{
		GuiSurvivalInventory.addButton(handler, button);
	}
	
	/**
	 * Adds a button to the creative inventory.
	 * 
	 * @param handler
	 *            the button handler
	 * @param button
	 *            the button
	 */
	public static void addCreativeButton(IInventoryHandler handler, GuiButton button)
	{
		GuiCreativeInventory.addButton(handler, button);
	}
	
	/**
	 * Adds an {@link IInventoryObject} to both the survival and the creative
	 * inventory.
	 * 
	 * @param object
	 *            the object
	 */
	public static void addObject(IInventoryObject object)
	{
		GuiSurvivalInventory.addObject(object);
		GuiCreativeInventory.addObject(object);
	}
	
	/**
	 * Adds an {@link IInventoryObject} to the survival inventory.
	 * 
	 * @param object
	 *            the object
	 */
	public static void addSurvivalObject(IInventoryObject object)
	{
		GuiSurvivalInventory.addObject(object);
	}
	
	/**
	 * Adds an {@link IInventoryObject} to the creative inventory.
	 * 
	 * @param object
	 *            the object
	 */
	public static void addCreativeObject(IInventoryObject object)
	{
		GuiCreativeInventory.addObject(object);
	}
}
