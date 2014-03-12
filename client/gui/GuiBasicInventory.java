package clashsoft.playerinventoryapi.client.gui;

import java.util.ArrayList;
import java.util.List;

import clashsoft.cslib.minecraft.client.gui.GuiBuilder;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.scoreboard.Team;

public abstract class GuiBasicInventory extends InventoryEffectRenderer
{
	protected final GuiBuilder guiBuilder;
	
	public GuiBasicInventory(Container container)
	{
		super(container);
		this.guiBuilder = new GuiBuilder(this);
	}
	
	public void drawBackgroundFrame(int posX, int posY, int width, int height)
	{
		this.guiBuilder.drawFrame(posX, posY, width, height);
	}
	
	public void drawPlayerBackground(int posX, int posY)
	{
		this.guiBuilder.drawPlayerBackgroundL(posX, posY);
	}
	
	public void drawSlot(int posX, int posY)
	{
		this.guiBuilder.drawSlot(posX, posY);
	}
	
	public void drawPlayerHoveringText(EntityPlayer player, int x, int y, FontRenderer font)
	{
		List<String> list = new ArrayList();
		Team team = player.getTeam();
		
		if (team != null)
		{
			list.add(team.formatString(player.getDisplayName()));
			list.add(team.getRegisteredName());
		}
		else
		{
			list.add(player.getDisplayName());
		}
		
		this.drawHoveringText(list, x, y, font);
	}
}
