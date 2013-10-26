package com.chaosdev.playerinventoryapi.handlers;

import clashsoft.clashsoftapi.util.CSUpdate;

import com.chaosdev.playerinventoryapi.lib.ExtendedInventory;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PIAPIEventHandler
{	
	@ForgeSubscribe
	public void entityConstructing(EntityConstructing event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && event.entity instanceof EntityPlayer)
		{
			ExtendedInventory props = new ExtendedInventory((EntityPlayer) event.entity);
			ExtendedInventory.setExtendedInventory((EntityPlayer) event.entity, props);
		}
	}
	
	@ForgeSubscribe
	public void entityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			
			CSUpdate.doClashsoftUpdateCheck(player, "Player Inventory API", "piapi", "");
			
			if (!event.world.isRemote)
			{
				ExtendedInventory ei = ExtendedInventory.getExtendedInventory(player);
				ei.sync(player);
			}
		}
	}
}
