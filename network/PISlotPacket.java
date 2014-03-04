package clashsoft.playerinventoryapi.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import clashsoft.cslib.minecraft.network.CSPacket;

public class PISlotPacket extends CSPacket
{
	public int	slotID;
	public int	var1;
	public int	var2;
	
	public PISlotPacket()
	{
	}
	
	public PISlotPacket(int slotID, int var1, int var2)
	{
		this.slotID = slotID;
		this.var1 = var1;
		this.var2 = var2;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.slotID);
		buf.writeInt(this.var1);
		buf.writeInt(this.var2);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.slotID = buf.readInt();
		this.var1 = buf.readInt();
		this.var2 = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
	}
	
	@Override
	public void handleServer(EntityPlayer player)
	{
		player.inventoryContainer.slotClick(this.slotID, this.var1, this.var2, player);
	}
}
