package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.api.inventorycomponents.InventoryObject;
import clashsoft.playerinventoryapi.client.gui.GuiCustomInventorySurvival;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventorySurvival;

import net.minecraft.client.gui.GuiButton;

public class SurvivalInventory
{
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
	
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		ContainerCustomInventorySurvival.addSlotHandler(slothandler);
	}
	
	public static void setSlotPosition(int slotid, int x, int y)
	{
		ContainerCustomInventorySurvival.setSlotPos(slotid, x, y);
	}
	
	public static void setPlayerDisplayPosition(int x, int y)
	{
		GuiCustomInventorySurvival.setPlayerDisplayPos(x, y);
	}
	
	public static void setCraftingArrowPosition(int x, int y)
	{
		GuiCustomInventorySurvival.setCraftArrowPos(x, y);
	}
	
	public static void setCraftingArrowRotation(float rotation)
	{
		GuiCustomInventorySurvival.setCraftArrowRot(rotation);
	}
	
	public static void setWindowSize(int width, int height)
	{
		GuiCustomInventorySurvival.setWindowSize(width, height);
	}
	
	public static void addButton(IButtonHandler handler, GuiButton button)
	{
		GuiCustomInventorySurvival.addButton(handler, button);
	}
	
	public static InventoryObject addObject(InventoryObject object)
	{
		GuiCustomInventorySurvival.addObject(object);
		return object;
	}
}