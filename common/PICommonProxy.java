package clashsoft.playerinventoryapi.common;

import clashsoft.playerinventoryapi.client.gui.GuiCreativeInventory;
import clashsoft.playerinventoryapi.client.gui.GuiSurvivalInventory;
import clashsoft.playerinventoryapi.handlers.PITickHandler;
import clashsoft.playerinventoryapi.inventory.ContainerCreativeList;
import clashsoft.playerinventoryapi.inventory.ContainerCreativeInventory;
import clashsoft.playerinventoryapi.inventory.ContainerSurvivalInventory;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;

public class PICommonProxy implements IGuiHandler
{
	public static int	CUSTOM_INVENTORY_SURVIVAL_ID	= 30;
	public static int	CUSTOM_INVENTORY_CREATIVE_ID	= 31;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
		{
			ContainerCreativeInventory container = new ContainerCreativeInventory(player.inventory, false, player);
			this.replacePlayerInventoryContainer(player, container);
			return container;
		}
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
		{
			ContainerSurvivalInventory container = new ContainerSurvivalInventory(player.inventory, false, player);
			this.replacePlayerInventoryContainer(player, container);
			return container;
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == CUSTOM_INVENTORY_CREATIVE_ID)
		{
			ContainerCreativeInventory container = new ContainerCreativeInventory(player.inventory, true, player);
			this.replacePlayerInventoryContainer(player, container);
			return new GuiCreativeInventory(player, new ContainerCreativeList(player), container);
			
		}
		else if (ID == CUSTOM_INVENTORY_SURVIVAL_ID)
		{
			ContainerSurvivalInventory container = new ContainerSurvivalInventory(player.inventory, true, player);
			this.replacePlayerInventoryContainer(player, container);
			return new GuiSurvivalInventory(player, container);
		}
		return null;
	}
	
	public void replacePlayerInventoryContainer(EntityPlayer player)
	{
		Container container = player.capabilities.isCreativeMode ? new ContainerCreativeInventory(player.inventory, !player.worldObj.isRemote, player) : new ContainerSurvivalInventory(player.inventory, !player.worldObj.isRemote, player);
		this.replacePlayerInventoryContainer(player, container);
	}
	
	public void replacePlayerInventoryContainer(EntityPlayer player, Container container)
	{
		player.inventoryContainer = player.openContainer = container;
	}
	
	public void registerTickHandler()
	{
		TickRegistry.registerTickHandler(new PITickHandler(), Side.SERVER);
	}
	
	public void onGUIOpened(GuiOpenEvent event)
	{
	}
}
