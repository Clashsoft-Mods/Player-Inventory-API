package clashsoft.playerinventoryapi.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public interface ISlotHandler
{
	public void addSlots(List<Slot> list, EntityPlayer player, boolean creative);
}
