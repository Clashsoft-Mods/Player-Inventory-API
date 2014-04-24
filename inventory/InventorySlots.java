package clashsoft.playerinventoryapi.inventory;

import java.util.Arrays;

import clashsoft.cslib.math.Point2i;

public class InventorySlots
{
	public static Point2i[]	survivalSlots			= new Point2i[64];
	public static Point2i[]	creativeSlots			= new Point2i[64];
	
	public static int		CRAFTING_OUTPUT			= 0;
	public static int		CRAFTING_TOP_LEFT		= 1;
	public static int		CRAFTING_TOP_RIGHT		= 2;
	public static int		CRAFTING_BOTTOM_LEFT	= 3;
	public static int		CRAFTING_BOTTOM_RIGHT	= 4;
	
	public static int		HELMET					= 5;
	public static int		CHESTPLATE				= 6;
	public static int		LEGGINGS				= 7;
	public static int		BOOTS					= 8;
	
	static
	{
		reset();
	}
	
	private InventorySlots()
	{
	}
	
	protected static void reset()
	{
		getSurvivalSlots(survivalSlots);
		getCreativeSlots(creativeSlots);
	}
	
	protected static void getSurvivalSlots(Point2i[] pos)
	{
		int i;
		int j;
		
		// 0 = Crafting output
		pos[CRAFTING_OUTPUT] = new Point2i(144, 36);
		
		pos[CRAFTING_TOP_LEFT] = new Point2i(88, 26);
		pos[CRAFTING_TOP_RIGHT] = new Point2i(106, 26);
		pos[CRAFTING_BOTTOM_LEFT] = new Point2i(88, 44);
		pos[CRAFTING_BOTTOM_RIGHT] = new Point2i(106, 44);
		
		pos[HELMET] = new Point2i(8, 8);
		pos[CHESTPLATE] = new Point2i(8, 26);
		pos[LEGGINGS] = new Point2i(8, 44);
		pos[BOOTS] = new Point2i(8, 62);
		
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
	}
	
	protected static void getCreativeSlots(Point2i[] pos)
	{
		int i;
		int j;
		
		pos[CRAFTING_OUTPUT] = new Point2i(-2000, -2000);
		pos[CRAFTING_TOP_LEFT] = new Point2i(-2000, -2000);
		pos[CRAFTING_TOP_RIGHT] = new Point2i(-2000, -2000);
		pos[CRAFTING_BOTTOM_LEFT] = new Point2i(-2000, -2000);
		pos[CRAFTING_BOTTOM_RIGHT] = new Point2i(-2000, -2000);
		
		pos[HELMET] = new Point2i(45, 6);
		pos[CHESTPLATE] = new Point2i(63, 6);
		pos[LEGGINGS] = new Point2i(81, 6);
		pos[BOOTS] = new Point2i(99, 6);
		
		// 9 - 35 = Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				pos[9 + j + i * 9] = new Point2i(9 + j * 18, 54 + i * 18);
			}
		}
		
		// 36 - 44 = Hotbar
		for (i = 0; i < 9; ++i)
		{
			pos[36 + i] = new Point2i(9 + i * 18, 112);
		}
	}
	
	public static void setSurvivalSlot(int slotID, int x, int y)
	{
		Point2i[] array = InventorySlots.survivalSlots;
		if (slotID >= array.length)
		{
			array = Arrays.copyOf(array, slotID + 1);
			InventorySlots.survivalSlots = array;
		}
		
		if (array[slotID] == null)
		{
			array[slotID] = new Point2i(x, y);
		}
		else
		{
			array[slotID].setX(x).setY(y);
		}
	}
	
	public static void setCreativeSlot(int slotID, int x, int y)
	{
		Point2i[] array = InventorySlots.creativeSlots;
		if (slotID >= array.length)
		{
			array = Arrays.copyOf(array, slotID + 1);
			InventorySlots.creativeSlots = array;
		}
		
		if (array[slotID] == null)
		{
			array[slotID] = new Point2i(x, y);
		}
		else
		{
			array[slotID].setX(x).setY(y);
		}
	}
}
