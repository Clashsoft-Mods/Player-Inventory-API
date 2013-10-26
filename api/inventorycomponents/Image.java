package com.chaosdev.playerinventoryapi.api.inventorycomponents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class Image extends RotatableObject
{
	public ResourceLocation image;
	public int x, y, u, v, width, heigth;
	
	public Image(ResourceLocation resource, int x, int y, int width, int heigth)
	{
		this(resource, x, y, 0, 0, width, heigth);
	}
	
	public Image(ResourceLocation resource, int x, int y, int u, int v, int width, int heigth)
	{
		this.image = resource;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
		this.width = width;
		this.heigth = heigth;
	}

	@Override
	public void renderRotated(int width, int height)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(image);
		new GuiScreen().drawTexturedModalRect(0, 0, u, v, this.width, this.width);
	}

	@Override
	public int getX()
	{
		return x;
	}

	@Override
	public int getY()
	{
		return y;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeigth()
	{
		return heigth;
	}	
}
