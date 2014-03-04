package clashsoft.playerinventoryapi.client;

import clashsoft.playerinventoryapi.client.gui.GuiCreativeInventory;
import clashsoft.playerinventoryapi.client.gui.GuiSurvivalInventory;
import clashsoft.playerinventoryapi.common.PICommonProxy;
import clashsoft.playerinventoryapi.inventory.ContainerCreativeList;
import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PIClientProxy extends PICommonProxy
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == GUI_CREATIVE_ID)
		{
			return new GuiCreativeInventory(player, new ContainerCreativeList(player), this.replaceInventory(player));
		}
		else if (ID == GUI_SURVIVAL_ID)
		{
			return new GuiSurvivalInventory(player, this.replaceInventory(player));
		}
		return null;
	}
	
	@Override
	public void registerTickHandler()
	{
		FMLCommonHandler.instance().bus().register(new PIClientEventHandler());
	}
	
	@Override
	public boolean isClient()
	{
		return true;
	}
}
