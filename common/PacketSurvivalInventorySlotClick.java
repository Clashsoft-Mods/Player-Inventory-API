package clashsoft.playerinventoryapi.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketSurvivalInventorySlotClick extends Packet250CustomPayload
{
	public PacketSurvivalInventorySlotClick(int slotID, int var1, int var2)
	{
		super("PIAPI", getData(slotID, var1, var2));
	}
	
	public static byte[] getData(int slotID, int var1, int var2)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try
		{
			dos.writeInt(slotID);
			dos.writeInt(var1);
			dos.writeInt(var2);
		}
		catch (Exception ex)
		{
		}
		
		return bos.toByteArray();
	}
}
