package clashsoft.playerinventoryapi.handlers;

import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.playerinventoryapi.PlayerInventoryAPI;
import clashsoft.playerinventoryapi.lib.ExtendedInventory;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PIEventHandler
{
	@ForgeSubscribe
	public void entityConstructing(EntityConstructing event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && event.entity instanceof EntityPlayer)
		{
			ExtendedInventory props = new ExtendedInventory((EntityPlayer) event.entity);
			ExtendedInventory.set((EntityPlayer) event.entity, props);
		}
	}
	
	@ForgeSubscribe
	public void entityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			
			CSUpdate.doClashsoftUpdateCheck(player, "Player Inventory API", "piapi", "");
			
			PlayerInventoryAPI.proxy.replacePlayerInventoryContainer(player);
			
			if (!event.world.isRemote)
			{
				ExtendedInventory ei = ExtendedInventory.get(player);
				ei.sync(player);
			}
		}
	}
}
