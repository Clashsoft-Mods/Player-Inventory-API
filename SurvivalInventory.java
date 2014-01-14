package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.api.invobject.InventoryObject;
import clashsoft.playerinventoryapi.client.gui.GuiSurvivalInventory;
import clashsoft.playerinventoryapi.inventory.ContainerSurvivalInventory;

import net.minecraft.client.gui.GuiButton;

/**
 * Survival Inventory editing class. Contains delegate methods for {@link GuiSurvivalInventory} and {@link ContainerSurvivalInventory}
 * 
 * @author Clashsoft
 */
public class SurvivalInventory
{
	/**
	 * Moves the survival inventory crafting grid to the upper right corner to free some space
	 * 
	 * @see SurvivalInventory#setCraftingArrowPosition(int, int)
	 * @see SurvivalInventory#setSlotPosition(int, int, int)
	 */
	public static void compactCraftingGrid()
	{
		setCraftingArrowPosition(142, 45);
		setCraftingArrowRotation(90);
		
		setSlotPosition(0, 143, 62);
		setSlotPosition(1, 134, 8);
		setSlotPosition(2, 134 + 18, 8);
		setSlotPosition(3, 134, 26);
		setSlotPosition(4, 134 + 18, 26);
	}
	
	/**
	 * Adds a slot handler to the survival inventory
	 * 
	 * @see ISlotHandler
	 * @param slothandler
	 *            the slot handler
	 */
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		ContainerSurvivalInventory.addSlotHandler(slothandler);
	}
	
	/**
	 * Sets the position of a slot in the survival inventory
	 * 
	 * @param slotid
	 *            the id of the slot
	 * @param x
	 * @param y
	 */
	public static void setSlotPosition(int slotid, int x, int y)
	{
		ContainerSurvivalInventory.setSlotPos(slotid, x, y);
	}
	
	/**
	 * Sets the player preview display position in the survival inventory
	 * 
	 * @param x
	 * @param y
	 */
	public static void setPlayerDisplayPosition(int x, int y)
	{
		if (Inventory.checkSide())
			GuiSurvivalInventory.setPlayerDisplayPos(x, y);
	}
	
	/**
	 * Sets the position of the survival inventory crafting arrow
	 * 
	 * @param x
	 * @param y
	 */
	public static void setCraftingArrowPosition(int x, int y)
	{
		if (Inventory.checkSide())
			GuiSurvivalInventory.setCraftArrowPos(x, y);
	}
	
	/**
	 * Sets the rotation of the survival inventory crafting arrow
	 * 
	 * @param rotation
	 *            the rotation in degrees
	 */
	public static void setCraftingArrowRotation(float rotation)
	{
		if (Inventory.checkSide())
			GuiSurvivalInventory.setCraftArrowRot(rotation);
	}
	
	/**
	 * Sets the window size of the survival inventory
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public static void setWindowSize(int width, int height)
	{
		if (Inventory.checkSide())
			GuiSurvivalInventory.setWindowSize(width, height);
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
			GuiSurvivalInventory.addButton(handler, button);
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
			GuiSurvivalInventory.addObject(object);
		return object;
	}
}