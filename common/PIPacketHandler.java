package clashsoft.playerinventoryapi.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import clashsoft.playerinventoryapi.lib.ExtendedInventory;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PIPacketHandler implements IPacketHandler
{
	public void registerChannels()
	{
		NetworkRegistry.instance().registerChannel(this, ExtendedInventory.CHANNEL);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (ExtendedInventory.CHANNEL.equals(packet.channel))
			ExtendedInventory.setByPacket((EntityPlayer) player, packet);
		else if ("PIAPI".equals(packet.channel))
		{
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			try
			{
				int slotID = dis.readInt();
				int var1 = dis.readInt();
				int var2 = dis.readInt();
				
				EntityPlayer entityplayer = (EntityPlayer) player;
				entityplayer.inventoryContainer.slotClick(slotID, var1, var2, entityplayer);
			}
			catch (Exception ex)
			{
				
			}
		}
	}
}
