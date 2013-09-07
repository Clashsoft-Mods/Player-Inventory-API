package com.chaosdev.playerinventoryapi.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

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
}
