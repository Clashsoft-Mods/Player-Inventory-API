package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.IInventoryHandler;
import clashsoft.playerinventoryapi.inventory.ContainerInventory;

public final class Inventory
{
	public static void addSlotHandler(IInventoryHandler handler)
	{
		ContainerInventory.addSlotHandler(handler);
	}
}
