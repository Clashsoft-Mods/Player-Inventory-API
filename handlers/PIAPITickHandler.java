package com.chaosdev.playerinventoryapi.handlers;

import java.util.EnumSet;

import com.chaosdev.playerinventoryapi.PlayerInventoryAPI;
import com.chaosdev.playerinventoryapi.common.CommonProxy;
import com.chaosdev.playerinventoryapi.lib.ExtendedInventory;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class PIAPITickHandler implements ITickHandler
{
	public static KeyBinding getInventoryKeyBinding()
	{
		return Minecraft.getMinecraft().gameSettings.keyBindInventory;
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT, TickType.PLAYER);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.CLIENT))
			updateInventory();
		if (type.contains(TickType.PLAYER))
		{
			EntityPlayer player = (EntityPlayer) tickData[0];
			ExtendedInventory.getExtendedInventory(player).onUpdate();
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.CLIENT))
			updateInventory();
	}
	
	public void updateInventory()
	{	
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory && PlayerInventoryAPI.enableCustomSurvivalInventory)
			player.openGui(PlayerInventoryAPI.instance, CommonProxy.CUSTOM_INVENTORY_SURVIVAL_ID, player.worldObj, 0, 0, 0);
		else if (Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative && PlayerInventoryAPI.enableCustomCreativeInventory)
			player.openGui(PlayerInventoryAPI.instance, CommonProxy.CUSTOM_INVENTORY_CREATIVE_ID, player.worldObj, 0, 0, 0);
	}
	
	@Override
	public String getLabel()
	{
		return "Player Inventory API";
	}
}
