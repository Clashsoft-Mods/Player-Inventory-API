package clashsoft.playerinventoryapi.client.gui;

import java.util.ArrayList;
import java.util.List;

import clashsoft.playerinventoryapi.inventory.ContainerMultiScreen;
import clashsoft.playerinventoryapi.lib.GuiHelper.GuiPos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiMultiScreen extends GuiContainer
{
	public List<GuiScreen> subGUIs = new ArrayList();
	public List<GuiPos> subGUIPositions = new ArrayList();
	
	public ContainerMultiScreen multiContainer;
	
	public GuiMultiScreen()
	{
		super(new ContainerMultiScreen());
		this.multiContainer = (ContainerMultiScreen) this.inventorySlots;
	}
	
	public void addGUI(GuiScreen sub)
	{
		this.subGUIs.add(sub);
		this.subGUIPositions.add(new GuiPos(0, 0));
		
		if (sub instanceof GuiContainer)
		{
			this.multiContainer.addSubContainer(((GuiContainer)sub).inventorySlots);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTickTime)
	{
		for (int i = 0; i < subGUIs.size(); i++)
		{
			GuiScreen gui = this.subGUIs.get(i);
			GuiPos pos = this.subGUIPositions.get(i);
			
			gui.drawScreen(mouseX - pos.getX(), mouseY - pos.getY(), partialTickTime);
		}
		
		super.drawScreen(mouseX, mouseY, partialTickTime);
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height)
	{
		super.setWorldAndResolution(mc, width, height);
		for (GuiScreen sub : this.subGUIs)
		{
			sub.setWorldAndResolution(mc, width, height);
		}
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		for (GuiScreen sub : this.subGUIs)
		{
			sub.onGuiClosed();
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int i, int j)
	{
	}
}
