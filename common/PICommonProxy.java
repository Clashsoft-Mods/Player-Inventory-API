package clashsoft.playerinventoryapi.common;

import clashsoft.playerinventoryapi.inventory.ContainerInventory;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PICommonProxy implements IGuiHandler
{
	public static int	GUI_SURVIVAL_ID	= 0;
	public static int	GUI_CREATIVE_ID	= 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == GUI_SURVIVAL_ID || ID == GUI_CREATIVE_ID)
		{
			return this.replaceInventory(player);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
	
	public ContainerInventory replaceInventory(EntityPlayer player)
	{
		if (player.inventoryContainer instanceof ContainerInventory)
		{
			return (ContainerInventory) player.inventoryContainer;
		}
		
		ContainerInventory container = new ContainerInventory(player.inventory, player);
		player.inventoryContainer = player.openContainer = container;
		return container;
	}
	
	public void registerTickHandler()
	{
	}
	
	public boolean isClient()
	{
		return false;
	}
}
