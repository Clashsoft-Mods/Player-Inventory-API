package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.api.invobject.InventoryObject;
import clashsoft.playerinventoryapi.client.gui.GuiCustomInventoryCreative;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.gui.GuiButton;

/**
 * Creative Inventory editing class.
 * Contains delegate methods for {@link GuiCustomInventoryCreative} and {@link ContainerCustomInventoryCreative}
 * 
 * @author Clashsoft
 *
 */
public class CreativeInventory
{
	/**
	 * Adds a slot handler to the creative inventory
	 * 
	 * @see ISlotHandler
	 * 
	 * @param slothandler the slot handler
	 */
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		ContainerCustomInventoryCreative.addSlotHandler(slothandler);
	}
	
	/** Sets the position of a slot in the creative inventory
	 * 
	 * @param slotid the id of the slot
	 * @param x
	 * @param y
	 */
	public static void setSlotPosition(int slotid, int x, int y)
	{
		ContainerCustomInventoryCreative.setSlotPos(slotid, x, y);
	}
	
	/**
	 * Sets the position of the bin/delete items slot in the creative inventory
	 * @param x
	 * @param y
	 */
	public static void setBinSlotPosition(int x, int y)
	{
		if (checkSide())
		GuiCustomInventoryCreative.setBinSlotPos(x, y);
	}
	
	/**
	 * Sets the player preview display position in the creative inventory
	 * @param x
	 * @param y
	 */
	public static void setPlayerDisplayPosition(int x, int y)
	{
		if (checkSide())
		GuiCustomInventoryCreative.setPlayerDisplayPos(x, y);
	}
	
	/** Sets the window width of the creative inventory
	 * 
	 * @param width the width
	 */
	public static void setWindowSize(int width)
	{
		if (checkSide())
		GuiCustomInventoryCreative.setWindowWidth(width);
	}
	
	/**
	 * Adds a button to the creative inventory
	 * 
	 * @see IButtonHandler
	 * 
	 * @param handler the button click handler
	 * @param button the button
	 */
	public static void addButton(IButtonHandler handler, GuiButton button)
	{
		if (checkSide())
		GuiCustomInventoryCreative.addButton(handler, button);
	}
	
	/**
	 * Adds an inventory object to the creative inventory
	 * 
	 * @see InventoryObject
	 * 
	 * @param object the inventory object
	 * @return
	 */
	public static InventoryObject addObject(InventoryObject object)
	{
		if (checkSide())
			GuiCustomInventoryCreative.addObject(object);
		return object;
	}
	
	public static boolean checkSide()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
	}
}