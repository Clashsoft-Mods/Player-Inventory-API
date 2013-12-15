package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.api.invobject.InventoryObject;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.gui.GuiButton;

/**
 * Inventory editing class. Contains delegate methods for {@link InventoryCreative} and {@link InventorySurvival}
 * 
 * @author Clashsoft
 */
public class Inventory
{
	/**
	 * Adds a slot handler to the inventory
	 * 
	 * @see ISlotHandler
	 * @param slothandler
	 *            the slot handler
	 */
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		CreativeInventory.addSlotHandler(slothandler);
		SurvivalInventory.addSlotHandler(slothandler);
	}
	
	/**
	 * Sets the position of a slot in the inventory
	 * 
	 * @param slotid
	 *            the id of the slot
	 * @param x
	 * @param y
	 */
	public static void setSlotPosition(int slotid, int x, int y)
	{
		CreativeInventory.setSlotPosition(slotid, x, y);
		SurvivalInventory.setSlotPosition(slotid, x, y);
	}
	
	/**
	 * Sets the player preview display position in the inventory
	 * 
	 * @param x
	 * @param y
	 */
	public static void setPlayerDisplayPosition(int x, int y)
	{
		CreativeInventory.setPlayerDisplayPosition(x, y);
		SurvivalInventory.setPlayerDisplayPosition(x, y);
	}
	
	/**
	 * Sets the window size of the inventory
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public static void setWindowSize(int width, int height)
	{
		CreativeInventory.setWindowSize(width);
		SurvivalInventory.setWindowSize(width, height);
	}
	
	/**
	 * Adds a button to the inventory
	 * 
	 * @see IButtonHandler
	 * @param handler
	 *            the button click handler
	 * @param button
	 *            the button
	 */
	public static void addButton(IButtonHandler handler, GuiButton button)
	{
		CreativeInventory.addButton(handler, button);
		SurvivalInventory.addButton(handler, button);
	}
	
	/**
	 * Adds an inventory object to the inventory
	 * 
	 * @see InventoryObject
	 * @param object
	 *            the inventory object
	 * @return
	 */
	public static InventoryObject addObject(InventoryObject object)
	{
		CreativeInventory.addObject(object);
		SurvivalInventory.addObject(object);
		return object;
	}
	
	public static boolean checkSide()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
	}
}