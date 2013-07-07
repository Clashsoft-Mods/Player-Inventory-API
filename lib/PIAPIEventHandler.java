package com.pocteam.playerinventoryapi.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

public class PIAPIEventHandler
{
	@ForgeSubscribe
	public void entityConstructing(EntityConstructing ec)
	{
		ExtendedInventory.initEntityEI(ec.entity);
	}
}
