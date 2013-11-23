package clashsoft.playerinventoryapi.api.invobject;

import net.minecraft.client.Minecraft;

public class Label extends RotatableObject
{
	public String text;
	public int x;
	public int y;
	public int color;
	
	public Label(String s, int x, int y)
	{
		this(s, x, y, 4210752);
	}
	
	public Label(String s, int x, int y, int color)
	{
		this.text = s;
		this.x = x;
		this.y = y;
		this.color = color;
	}

	@Override
	public void renderRotated(int width, int height)
	{
		Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color);
	}

	@Override
	public int getX()
	{
		return this.x;
	}

	@Override
	public int getY()
	{
		return this.y;
	}

	@Override
	public int getWidth()
	{
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
	}

	@Override
	public int getHeigth()
	{
		return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
	}
}
