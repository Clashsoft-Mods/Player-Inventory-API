package clashsoft.playerinventoryapi.lib;

/**
 * Helper class for 2-dimensional sizes.
 * 
 * @author Clashsoft
 */
public class GuiHelper
{
	public static class GuiPos
	{
		private final int	x;
		private final int	y;
		
		public GuiPos(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
		public int getX()
		{
			return this.x;
		}
		
		public int getY()
		{
			return this.y;
		}
		
		@Override
		public String toString()
		{
			return "P(" + this.x + "; " + this.y + ")";
		}
	}
	
	public static class GuiSize
	{
		private int	width;
		private int	height;
		
		public GuiSize(int w, int h)
		{
			this.width = w;
			this.height = h;
		}
		
		public GuiSize setWidth(int width)
		{
			this.width = width;
			return this;
		}
		
		public GuiSize setHeight(int height)
		{
			this.height = height;
			return this;
		}
		
		public int getWidth()
		{
			return this.width;
		}
		
		public int getHeight()
		{
			return this.height;
		}
		
		@Override
		public String toString()
		{
			return "Size(" + this.width + "; " + this.height + ")";
		}
	}
}
