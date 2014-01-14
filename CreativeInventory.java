package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.api.invobject.InventoryObject;
import clashsoft.playerinventoryapi.client.gui.GuiCreativeInventory;
import clashsoft.playerinventoryapi.inventory.ContainerCreativeInventory;

import net.minecraft.client.gui.GuiButton;

/**
 * Creative Inventory editing class. Contains delegate methods for {@link GuiCreativeInventory} and {@link ContainerCreativeInventory}
 * 
 * @author Clashsoft
 */
public class CreativeInventory extends Inventory
{
	/**
	 * Adds a slot handler to the creative inventory
	 * 
	 * @see ISlotHandler
	 * @param slothandler
	 *            the slot handler
	 */
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		ContainerCreativeInventory.addSlotHandler(slothandler);
	}
	
	/**
	 * Sets the position of a slot in the creative inventory
	 * 
	 * @param slotid
	 *            the id of the slot
	 * @param x
	 * @param y
	 */
	public static void setSlotPosition(int slotid, int x, int y)
	{
		ContainerCreativeInventory.setSlotPos(slotid, x, y);
	}
	
	/**
	 * Sets the position of the bin/delete items slot in the creative inventory
	 * 
	 * @param x
	 * @param y
	 */
	public static void setBinSlotPosition(int x, int y)
	{
		if (Inventory.checkSide())
			GuiCreativeInventory.setBinSlotPos(x, y);
	}
	
	/**
	 * Sets the player preview display position in the creative inventory
	 * 
	 * @param x
	 * @param y
	 */
	public static void setPlayerDisplayPosition(int x, int y)
	{
		if (Inventory.checkSide())
			GuiCreativeInventory.setPlayerDisplayPos(x, y);
	}
	
	/**
	 * Sets the window width of the creative inventory
	 * 
	 * @param width
	 *            the width
	 */
	public static void setWindowSize(int width)
	{
		if (Inventory.checkSide())
			GuiCreativeInventory.setWindowWidth(width);
	}
	
	/**
	 * Adds a button to the creative inventory
	 * 
	 * @see IButtonHandler
	 * @param handler
	 *            the button click handler
	 * @param button
	 *            the button
	 */
	public static void addButton(IButtonHandler handler, GuiButton button)
	{
		if (Inventory.checkSide())
			GuiCreativeInventory.addButton(handler, button);
	}
	
	/**
	 * Adds an inventory object to the creative inventory
	 * 
	 * @see InventoryObject
	 * @param object
	 *            the inventory object
	 * @return
	 */
	public static InventoryObject addObject(InventoryObject object)
	{
		if (Inventory.checkSide())
			GuiCreativeInventory.addObject(object);
		return object;
	}
}