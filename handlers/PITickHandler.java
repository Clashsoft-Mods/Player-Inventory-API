package clashsoft.playerinventoryapi.handlers;

import java.util.EnumSet;

import clashsoft.playerinventoryapi.lib.ExtendedInventory;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.entity.player.EntityPlayer;

public class PITickHandler implements ITickHandler
{
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.PLAYER))
		{
			EntityPlayer player = (EntityPlayer) tickData[0];
			ExtendedInventory.get(player).onUpdate();
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
	}
	
	@Override
	public String getLabel()
	{
		return "PIAPI Player";
	}
}
