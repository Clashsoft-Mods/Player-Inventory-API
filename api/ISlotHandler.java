package clashsoft.playerinventoryapi.api;

import net.minecraft.entity.player.EntityPlayer;

public interface ISlotHandler
{
	public void addSlots(ISlotList container, EntityPlayer player, boolean creative);
}
