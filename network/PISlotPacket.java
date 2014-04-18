package clashsoft.playerinventoryapi.network;

import clashsoft.cslib.minecraft.network.CSPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class PISlotPacket extends CSPacket
{
	public int	i;
	public int	j;
	public int	k;
	
	public PISlotPacket()
	{
	}
	
	public PISlotPacket(int i, int j, int k)
	{
		this.i = i;
		this.j = j;
		this.k = k;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.i);
		buf.writeInt(this.j);
		buf.writeInt(this.k);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.i = buf.readInt();
		this.j = buf.readInt();
		this.k = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
		player.inventoryContainer.slotClick(this.i, this.j, this.k, player);
	}
}
