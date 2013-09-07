package com.chaosdev.playerinventoryapi.lib;

import java.util.EnumSet;

import com.chaosdev.playerinventoryapi.CommonProxy;
import com.chaosdev.playerinventoryapi.PlayerInventoryAPI;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventorySurvival;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class PIAPITickHandler implements ITickHandler
{
	public PIAPITickHandler()
	{
		
	}

	public static KeyBinding getInventoryKeyBinding()
	{
		KeyBinding kb = Minecraft.getMinecraft().gameSettings.keyBindInventory;
		return kb;
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.CLIENT))
			updateInventory();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if (type.contains(TickType.CLIENT))
			updateInventory();
		if (type.contains(TickType.PLAYER))
		{
			replacePlayerInventory((EntityPlayer) tickData[0]);
		}
	}
	
	public void replacePlayerInventory(EntityPlayer ep)
	{
		
	}
	
	public void updateInventory()
	{
		ContainerCustomInventoryCreative.resetSlots();
		ContainerCustomInventorySurvival.resetSlots();
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory && PlayerInventoryAPI.ENABLE_CUSTOM_SURVIVAL_INVENTORY)
		{
			player.closeScreen();
			player.openContainer = null;
			player.inventoryContainer = null;
			player.openGui(PlayerInventoryAPI.instance, CommonProxy.CUSTOM_INVENTORY_SURVIVAL_ID, player.worldObj, 0, 0, 0);
		}
		else if (Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative && PlayerInventoryAPI.ENABLE_CUSTOM_CREATIVE_INVENTORY)
		{
			player.closeScreen();
			player.openContainer = null;
			player.inventoryContainer = null;
			player.openGui(PlayerInventoryAPI.instance, CommonProxy.CUSTOM_INVENTORY_CREATIVE_ID, player.worldObj, 0, 0, 0);
		}
	}

	@Override
	public String getLabel()
	{
		return "Player Inventory API";
	}
}
