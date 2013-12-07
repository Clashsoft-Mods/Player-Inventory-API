package clashsoft.playerinventoryapi.handlers;

import java.util.EnumSet;

import clashsoft.playerinventoryapi.PlayerInventoryAPI;
import clashsoft.playerinventoryapi.common.PICommonProxy;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;

public class PIClientTickHandler implements ITickHandler
{
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.CLIENT))
			this.updateInventory();
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.CLIENT))
			this.updateInventory();
	}
	
	public void updateInventory()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory && PlayerInventoryAPI.enableCustomSurvivalInventory)
			player.openGui(PlayerInventoryAPI.instance, PICommonProxy.CUSTOM_INVENTORY_SURVIVAL_ID, player.worldObj, 0, 0, 0);
		else if (Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative && PlayerInventoryAPI.enableCustomCreativeInventory)
			player.openGui(PlayerInventoryAPI.instance, PICommonProxy.CUSTOM_INVENTORY_CREATIVE_ID, player.worldObj, 0, 0, 0);
	}
	
	@Override
	public String getLabel()
	{
		return "PIAPI Client";
	}
}
