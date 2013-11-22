package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.api.inventorycomponents.InventoryObject;
import clashsoft.playerinventoryapi.client.gui.GuiCustomInventoryCreative;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventoryCreative;

import net.minecraft.client.gui.GuiButton;

public class CreativeInventory
{
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		ContainerCustomInventoryCreative.addSlotHandler(slothandler);
	}
	
	public static void setSlotPosition(int slotid, int x, int y)
	{
		ContainerCustomInventoryCreative.setSlotPos(slotid, x, y);
	}
	
	public static void setBinSlotPosition(int x, int y)
	{
		GuiCustomInventoryCreative.setBinSlotPos(x, y);
	}
	
	public static void setPlayerDisplayPosition(int x, int y)
	{
		GuiCustomInventoryCreative.setPlayerDisplayPos(x, y);
	}
	
	public static void setWindowSize(int width)
	{
		GuiCustomInventoryCreative.setWindowWidth(width);
	}
	
	public static void addButton(IButtonHandler handler, GuiButton button)
	{
		GuiCustomInventoryCreative.addButton(handler, button);
	}
	
	public static InventoryObject addObject(InventoryObject object)
	{
		GuiCustomInventoryCreative.addObject(object);
		return object;
	}
}