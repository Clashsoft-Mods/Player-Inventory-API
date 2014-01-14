package clashsoft.playerinventoryapi.inventory;

import java.util.ArrayList;
import java.util.List;

import clashsoft.cslib.reflect.CSReflection;
import clashsoft.playerinventoryapi.api.ICustomPlayerContainer;
import clashsoft.playerinventoryapi.api.ISlotHandler;
import clashsoft.playerinventoryapi.lib.GuiHelper.GuiPos;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerCreativeInventory extends Container implements ICustomPlayerContainer
{
	public InventoryCrafting			craftMatrix		= new InventoryCrafting(this, 2, 2);
	public IInventory					craftResult		= new InventoryCraftResult();
	
	public boolean						isLocalWorld	= false;
	public final EntityPlayer			thePlayer;
	
	public static GuiPos[]				slotPositions	= getDefaultSlotPositions();
	public static List<ISlotHandler>	slotHandlers	= new ArrayList<ISlotHandler>();
	
	public ContainerCreativeInventory(InventoryPlayer inventory, boolean localWorld, EntityPlayer player)
	{
		this.isLocalWorld = localWorld;
		this.thePlayer = player;
		
		this.inventorySlots = new ArrayList()
		{
			private static final long	serialVersionUID	= 5436247638996771146L;
			
			@Override
			public int size()
			{
				if (ContainerCreativeInventory.this.isLocalWorld)
				{
					String clazz = CSReflection.getCallerClassName();
					if (clazz.equals(getMinecraftClassName()))
						return 45;
				}
				return super.size();
			}
		};
		
		List<Slot> slots = this.createSlots();
		int defaultSlots = slots.size();
		
		for (ISlotHandler handler : slotHandlers)
			handler.addSlots(slots, this.thePlayer, true);
		
		for (int i = 0; i < slots.size(); i++)
		{
			Slot slot = slots.get(i);
			this.addSlotToContainer(slot);
			
			if (i >= defaultSlots)
				slotPositions[slot.slotNumber] = new GuiPos(slot.xDisplayPosition, slot.yDisplayPosition);
		}
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	protected static String getMinecraftClassName()
	{
		try
		{
			return Minecraft.class.getName();
		}
		catch (NoClassDefFoundError error)
		{
			return "";
		}
	}
	
	@Override
	public List<Slot> createSlots()
	{
		List<Slot> slots = new ArrayList<Slot>(slotPositions.length);
		GuiPos[] pos = slotPositions;
		slots.add(new SlotCrafting(this.thePlayer, this.craftMatrix, this.craftResult, 0, pos[0].getX(), pos[0].getY()));
		
		int i;
		int j;
		
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 2; ++j)
			{
				int index = j + i * 2;
				slots.add(new Slot(this.craftMatrix, index, pos[1 + index].getX(), pos[1 + index].getY()));
			}
		}
		
		for (i = 0; i < 4; ++i)
		{
			slots.add(new SlotCustomArmor(this.thePlayer, this.thePlayer.inventory, this.thePlayer.inventory.getSizeInventory() - 1 - i, pos[8 - i].getX(), pos[8 - i].getY(), i));
		}
		
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				int index = j + (i + 1) * 9;
				slots.add(new Slot(this.thePlayer.inventory, index, pos[index].getX(), pos[index].getY()));
			}
		}
		
		for (i = 0; i < 9; ++i)
		{
			int index = i + 36;
			slots.add(new Slot(this.thePlayer.inventory, i, pos[index].getX(), pos[index].getY()));
		}
		
		return slots;
	}
	
	public static GuiPos[] getDefaultSlotPositions()
	{
		GuiPos[] pos = new GuiPos[128];
		int i;
		int j;
		
		// 44 = Crafting output
		pos[0] = new GuiPos(-2000, -2000);
		
		// 1 - 4 = Crafting
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 2; ++j)
			{
				pos[1 + j + i * 2] = new GuiPos(-2000, -2000);
			}
		}
		
		// 5 - 8 = Armor
		for (i = 0; i < 4; ++i)
		{
			pos[8 - i] = new GuiPos(45 + i * 18, 6);
		}
		
		// 9 - 35 = Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				pos[j + (i + 1) * 9] = new GuiPos(9 + j * 18, 54 + i * 18);
			}
		}
		
		// 36 - 44 = Hotbar
		for (i = 0; i < 9; ++i)
		{
			pos[36 + i] = new GuiPos(9 + i * 18, 112);
		}
		return pos;
	}
	
	public static void setSlotPos(int slotID, int x, int y)
	{
		if (slotID < slotPositions.length)
			slotPositions[slotID] = new GuiPos(x, y);
		else
			throw new IllegalArgumentException("Tried to set the slot position of a slot that does not exist - Add that slot first.");
	}
	
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		slotHandlers.add(slothandler);
	}
	
	public static void resetSlots()
	{
		slotPositions = getDefaultSlotPositions();
	}
	
	@Override
	public EntityPlayer getPlayer()
	{
		return this.thePlayer;
	}
	
	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
	}
	
	/**
	 * Callback for when the crafting gui is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		
		for (int i = 0; i < 4; ++i)
		{
			ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
			
			if (itemstack != null)
			{
				player.dropPlayerItem(itemstack);
			}
		}
		this.craftResult.setInventorySlotContents(0, (ItemStack) null);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotID);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int armorSlotID = -1;
			
			if (itemstack.getItem() instanceof ItemArmor)
			{
				ItemArmor itemArmor = (ItemArmor) itemstack.getItem();
				int armorType = itemArmor.armorType;
				
				if (armorType < 4)
				{
					Slot armorSlot = (Slot) this.inventorySlots.get(5 + armorType);
					if (!armorSlot.getHasStack())
						armorSlotID = 5 + armorType;
				}
				else
				{
					for (int i = 45; i < this.inventorySlots.size(); i++)
					{
						Slot armorSlot = (Slot) this.inventorySlots.get(i);
						
						if (!armorSlot.getHasStack() && armorSlot.isItemValid(itemstack1))
						{
							armorSlotID = i;
							break;
						}
					}
				}
			}
			
			if (slotID == 0) // Crafting output
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (slotID >= 1 && slotID < 5) // Crafting grid
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, false))
				{
					return null;
				}
			}
			else if (slotID >= 5 && slotID < 9) // Armor slots
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, false))
				{
					return null;
				}
			}
			else if (slotID >= 45)
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, false))
				{
					return null;
				}
			}
			else if (itemstack.getItem() instanceof ItemArmor && armorSlotID != -1) // Armor items
			{
				if (!this.mergeItemStack(itemstack1, armorSlotID, armorSlotID + 1, false))
				{
					return null;
				}
			}
			else if (slotID >= 9 && slotID < 36) // Normal inventory
			{
				if (!this.mergeItemStack(itemstack1, 36, 45, false))
				{
					return null;
				}
			}
			else if (slotID >= 36 && slotID < 45) // Hotbar
			{
				if (!this.mergeItemStack(itemstack1, 9, 36, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 9, 45, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}
	
	@Override
	public boolean func_94530_a(ItemStack stack, Slot slot)
	{
		return slot.inventory != this.craftResult && super.func_94530_a(stack, slot);
	}
}
