package com.chaosdev.playerinventoryapi.common;

import com.chaosdev.playerinventoryapi.client.gui.GuiCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.client.gui.GuiCustomInventorySurvival;
import com.chaosdev.playerinventoryapi.handlers.PIAPITickHandler;
import com.chaosdev.playerinventoryapi.inventory.ContainerCreativeList;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventorySurvival;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler
{
	public static int	CUSTOM_INVENTORY_SURVIVAL_ID	= 30;
	public static int	CUSTOM_INVENTORY_CREATIVE_ID	= 31;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
		{
			ContainerCustomInventoryCreative container = new ContainerCustomInventoryCreative(player.inventory, false, player);
			player.inventoryContainer = container;
			return container;
		}
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
		{
			ContainerCustomInventorySurvival container = new ContainerCustomInventorySurvival(player.inventory, false, player);
			player.inventoryContainer = container;
			return container;
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
		{
			ContainerCustomInventoryCreative container = new ContainerCustomInventoryCreative(player.inventory, true, player);
			player.inventoryContainer = container;
			return new GuiCustomInventoryCreative(player, new ContainerCreativeList(player), container);
		}
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
		{
			ContainerCustomInventorySurvival container = new ContainerCustomInventorySurvival(player.inventory, true, player);
			player.inventoryContainer = container;
			return new GuiCustomInventorySurvival(player, container);
		}
		return null;
	}
	
	public void registerTickHandler()
	{
		TickRegistry.registerTickHandler(new PIAPITickHandler(), Side.SERVER);
	}
}
