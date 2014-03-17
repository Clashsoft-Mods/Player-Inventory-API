package clashsoft.playerinventoryapi.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.playerinventoryapi.lib.ExtendedInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class EIPacket extends CSPacket
{
	public int			slot;
	public ItemStack	stack;
	
	public EIPacket()
	{
	}
	
	public EIPacket(ExtendedInventory ei, int slot)
	{
		this.slot = slot;
		this.stack = ei.getStackInSlot(slot);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.slot);
		buf.writeItemStackToBuffer(this.stack);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.slot = buf.readInt();
		this.stack = buf.readItemStackFromBuffer();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
		this.handleServer(player);
	}
	
	@Override
	public void handleServer(EntityPlayer player)
	{
		ExtendedInventory ei = ExtendedInventory.get(player);
		ei.itemStacks[this.slot] = this.stack;
	}
}
