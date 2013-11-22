package clashsoft.playerinventoryapi.common;

import java.util.List;

import clashsoft.playerinventoryapi.client.gui.GuiCustomInventoryCreative;
import clashsoft.playerinventoryapi.client.gui.GuiCustomInventorySurvival;
import clashsoft.playerinventoryapi.handlers.PIAPITickHandler;
import clashsoft.playerinventoryapi.inventory.ContainerCreativeList;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventorySurvival;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PICommonProxy implements IGuiHandler
{
	public static int	CUSTOM_INVENTORY_SURVIVAL_ID	= 30;
	public static int	CUSTOM_INVENTORY_CREATIVE_ID	= 31;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
		{
			ContainerCustomInventoryCreative container = new ContainerCustomInventoryCreative(player.inventory, false, player);
			replacePlayerInventoryContainer(player, container);
			return container;
		}
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
		{
			ContainerCustomInventorySurvival container = new ContainerCustomInventorySurvival(player.inventory, false, player);
			replacePlayerInventoryContainer(player, container);
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
			replacePlayerInventoryContainer(player, container);
			return new GuiCustomInventoryCreative(player, new ContainerCreativeList(player), container);
			
		}
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
		{
			ContainerCustomInventorySurvival container = new ContainerCustomInventorySurvival(player.inventory, true, player);
			replacePlayerInventoryContainer(player, container);
			return new GuiCustomInventorySurvival(player, container);
			
		}
		return null;
	}
	
	public void replacePlayerInventoryContainer(EntityPlayer player, Container container)
	{
		List<ItemStack> stacks = container.inventoryItemStacks = player.inventoryContainer.inventoryItemStacks;
		
		// Ensure the stacks size
		while (stacks.size() < container.inventorySlots.size())
			stacks.add(null);
		
		player.inventoryContainer = player.openContainer = container;
	}
	
	public void registerTickHandler()
	{
		TickRegistry.registerTickHandler(new PIAPITickHandler(), Side.SERVER);
	}
}