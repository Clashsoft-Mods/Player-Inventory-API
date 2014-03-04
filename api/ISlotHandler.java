package clashsoft.playerinventoryapi.api;

import clashsoft.playerinventoryapi.inventory.ContainerInventory;

import net.minecraft.entity.player.EntityPlayer;

public interface ISlotHandler
{
	public void addSlots(ContainerInventory container, EntityPlayer player, boolean creative);
}
