package com.chaosdev.playerinventoryapi.lib;

import clashsoft.clashsoftapi.util.CSUpdate;
import clashsoft.clashsoftapi.util.update.ModUpdate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PIAPIEventHandler
{
	@ForgeSubscribe
	public void entityConstructing(EntityConstructing ec)
	{
		if (ec.entity instanceof EntityPlayer)
		{
			replaceInventory(((EntityPlayer) ec.entity));
		}
		ExtendedInventory.initEntityEI(ec.entity);
	}
	
	private void replaceInventory(EntityPlayer entityPlayer)
	{
		System.out.println("Replacing Player Inventory");
	}
	
	@ForgeSubscribe
	public void playerJoined(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			ModUpdate update = CSUpdate.checkForUpdate("Player Inventory API", "piapi", Reference.VERSION);
			CSUpdate.notifyUpdate((EntityPlayer) event.entity, "Player Inventory API", update);
		}
	}
}
