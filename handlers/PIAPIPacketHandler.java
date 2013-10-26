package com.chaosdev.playerinventoryapi.handlers;

import com.chaosdev.playerinventoryapi.lib.ExtendedInventory;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PIAPIPacketHandler implements IPacketHandler
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
	}
}
