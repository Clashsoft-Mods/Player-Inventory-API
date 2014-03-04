package clashsoft.playerinventoryapi.inventory;

import clashsoft.cslib.math.Point2i;

public class InventorySlots
{
	public static Point2i[]	survivalSlots	= getSurvivalSlots();
	public static Point2i[]	creativeSlots	= getCreativeSlots();
	
	private InventorySlots()
	{
	}
	
	public static Point2i[] getSurvivalSlots()
	{
		Point2i[] pos = new Point2i[128];
		int i;
		int j;
		
		// 0 = Crafting output
		pos[0] = new Point2i(144, 36);
		
		// 1 - 4 = Crafting
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 2; ++j)
			{
				pos[1 + j + i * 2] = new Point2i(88 + j * 18, 26 + i * 18);
			}
		}
		
		// 5 - 8 = Armor
		for (i = 0; i < 4; ++i)
		{
			pos[8 - i] = new Point2i(8, 26 + i * 18 - 18);
		}
		
		// 9 - 35 = Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				pos[9 + j + i * 9] = new Point2i(8 + j * 18, 84 + i * 18);
			}
		}
		
		// 36 - 44 = Hotbar
		for (i = 0; i < 9; ++i)
		{
			pos[36 + i] = new Point2i(8 + i * 18, 142);
		}
		return pos;
	}
	
	public static Point2i[] getCreativeSlots()
	{
		Point2i[] pos = new Point2i[128];
		int i;
		int j;
		
		// 44 = Crafting output
		pos[0] = new Point2i(-2000, -2000);
		
		// 1 - 4 = Crafting
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 2; ++j)
			{
				pos[1 + j + i * 2] = new Point2i(-2000, -2000);
			}
		}
		
		// 5 - 8 = Armor
		for (i = 0; i < 4; ++i)
		{
			pos[8 - i] = new Point2i(45 + i * 18, 6);
		}
		
		// 9 - 35 = Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				pos[j + (i + 1) * 9] = new Point2i(9 + j * 18, 54 + i * 18);
			}
		}
		
		// 36 - 44 = Hotbar
		for (i = 0; i < 9; ++i)
		{
			pos[36 + i] = new Point2i(9 + i * 18, 112);
		}
		return pos;
	}
	
	public static void setSlot(int slotID, int x, int y)
	{
		setCreativeSlot(slotID, x, y);
		setSurvivalSlot(slotID, x, y);
	}
	
	public static void setCreativeSlot(int slotID, int x, int y)
	{
		if (creativeSlots[slotID] == null)
		{
			creativeSlots[slotID] = new Point2i(x, y);
		}
		// else if (creativeSlots[slotID].isPoint(x, y))
		// {
		// creativeSlots[slotID].setX(x).setY(y);
		// }
	}
	
	public static void setSurvivalSlot(int slotID, int x, int y)
	{
		if (survivalSlots[slotID] == null)
		{
			survivalSlots[slotID] = new Point2i(x, y);
		}
		// else if (survivalSlots[slotID].isPoint(x, y))
		// {
		// survivalSlots[slotID].setX(x).setY(y);
		// }
	}
}
