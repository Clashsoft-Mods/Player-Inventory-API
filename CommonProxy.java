package com.chaosdev.playerinventoryapi;

import com.chaosdev.playerinventoryapi.gui.GuiCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.gui.GuiCustomInventorySurvival;
import com.chaosdev.playerinventoryapi.inventory.ContainerCreativeList;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventorySurvival;

import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler
{
	public static int CUSTOM_INVENTORY_SURVIVAL_ID = 30;
	public static int CUSTOM_INVENTORY_CREATIVE_ID = 31;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
			return new ContainerCustomInventoryCreative(player.inventory, false, player);
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
			return new ContainerCustomInventorySurvival(player.inventory, false, player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
			return new GuiCustomInventoryCreative(player, new ContainerCreativeList(player), (ContainerCustomInventoryCreative) getServerGuiElement(ID, player, world, x, y, z));
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
			return new GuiCustomInventorySurvival(player, (ContainerCustomInventorySurvival) getServerGuiElement(ID, player, world, x, y, z));
		return null;
	}
	
	public void registerTickHandler()
	{
		
	}
}
