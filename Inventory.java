package clashsoft.playerinventoryapi;

import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.inventory.ContainerInventory;

public final class Inventory
{
	public static void addSlotHandler(ISlotHandler handler)
	{
		ContainerInventory.addSlotHandler(handler);
	}
}
