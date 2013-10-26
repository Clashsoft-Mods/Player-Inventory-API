package com.chaosdev.playerinventoryapi.inventory;

import java.util.LinkedList;
import java.util.List;

import com.chaosdev.playerinventoryapi.lib.GuiHelper.GuiPos;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerCustomInventorySurvival extends Container implements ICustomPlayerContainer
{
	/** The crafting matrix inventory. */
	public InventoryCrafting			craftMatrix		= new InventoryCrafting(this, 2, 2);
	public IInventory					craftResult		= new InventoryCraftResult();
	
	/** Determines if inventory manipulation should be handled. */
	public boolean						isLocalWorld	= false;
	protected final EntityPlayer		thePlayer;
	
	public static GuiPos[]				slotPos			= getDefaultSlotPositions();
	public static GuiPos[]				slotPos2		= slotPos;
	public static List<ISlotHandler>	slotHandlers	= new LinkedList<ISlotHandler>();
	
	public ContainerCustomInventorySurvival(InventoryPlayer par1InventoryPlayer, boolean par2, EntityPlayer par3EntityPlayer)
	{
		this.isLocalWorld = par2;
		this.thePlayer = par3EntityPlayer;
		par3EntityPlayer.inventoryContainer = this;
		
		slotPos = slotPos2;
		List<Slot> slots = createSlots();
		for (ISlotHandler handler : slotHandlers)
		{
			for (Slot s : handler.addSlots(par3EntityPlayer, false))
			{
				s.slotNumber = slots.size();
				slotPos2[s.slotNumber] = new GuiPos(s.xDisplayPosition, s.yDisplayPosition);
				slots.add(s);
			}
		}
		for (int i = 0; i < slots.size(); i++)
		{
			Slot s = slots.get(i);
			s.xDisplayPosition = slotPos2[i].getX();
			s.yDisplayPosition = slotPos2[i].getY();
			addSlotToContainer(s);
		}
		
		System.out.println(this.inventorySlots.size() + " Slots registered for Survival Inventory.");
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	@Override
	public List<Slot> createSlots()
	{
		List<Slot> slots = new LinkedList<Slot>();
		GuiPos[] pos = slotPos2;
		slots.add(new SlotCrafting(this.thePlayer, this.craftMatrix, this.craftResult, 0, pos[0].getX(), pos[0].getY()));
		
		int i;
		int j;
		
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 2; ++j)
			{
				slots.add(new Slot(this.craftMatrix, j + i * 2, pos[1 + j + i * 2].getX(), pos[1 + j + i * 2].getY()));
			}
		}
		
		for (i = 0; i < 4; ++i)
		{
			slots.add(new SlotCustomArmor(this, this.thePlayer.inventory, this.thePlayer.inventory.getSizeInventory() - 1 - i, pos[8 - i].getX(), pos[8 - i].getY(), i));
		}
		
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				slots.add(new Slot(this.thePlayer.inventory, j + (i + 1) * 9, pos[j + (i + 1) * 9].getX(), pos[j + (i + 1) * 9].getY()));
			}
		}
		
		for (i = 0; i < 9; ++i)
		{
			slots.add(new Slot(this.thePlayer.inventory, i, pos[36 + i].getX(), pos[36 + i].getY()));
		}
		
		return slots;
	}
	
	public static GuiPos[] getDefaultSlotPositions()
	{
		GuiPos[] pos = new GuiPos[128];
		int i;
		int j;
		
		// 0 = Crafting output
		pos[0] = new GuiPos(144, 36);
		
		// 1 - 4 = Crafting
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 2; ++j)
			{
				pos[1 + j + i * 2] = new GuiPos(88 + j * 18, 26 + i * 18);
			}
		}
		
		// 5 - 8 = Armor
		for (i = 0; i < 4; ++i)
		{
			pos[8 - i] = new GuiPos(8, 8 + (4 - i) * 18 - 18);
		}
		
		// 9 - 35 = Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				pos[j + (i + 1) * 9] = new GuiPos(8 + j * 18, 84 + i * 18);
			}
		}
		
		// 36 - 44 = Hotbar
		for (i = 0; i < 9; ++i)
		{
			pos[36 + i] = new GuiPos(8 + i * 18, 142);
		}
		return pos;
	}
	
	public static void resetSlots()
	{
		slotPos = getDefaultSlotPositions();
		slotPos2 = slotPos;
	}
	
	public static void setSlotPos(int slotid, int x, int y)
	{
		if (slotid < slotPos2.length)
		{
			slotPos2[slotid] = new GuiPos(x, y);
		}
		else
			throw new IllegalArgumentException("Tried to set the slot position of a slot that does not exist - Add that slot first.");
	}
	
	public static void addSlotHandler(ISlotHandler slothandler)
	{
		slotHandlers.add(slothandler);
	}
	
	@Override
	public EntityPlayer getPlayer()
	{
		return thePlayer;
	}
	
	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
	}
	
	/**
	 * Callback for when the crafting gui is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		
		for (int i = 0; i < 4; ++i)
		{
			ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
			
			if (itemstack != null)
			{
				par1EntityPlayer.dropPlayerItem(itemstack);
			}
		}
		this.craftResult.setInventorySlotContents(0, (ItemStack) null);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return true;
	}
	
	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (par2 == 0)
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 >= 1 && par2 < 5)
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, false))
				{
					return null;
				}
			}
			else if (par2 >= 5 && par2 < 9)
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, false))
				{
					return null;
				}
			}
			else if (itemstack.getItem() instanceof ItemArmor && !((Slot) this.inventorySlots.get(5 + ((ItemArmor) itemstack.getItem()).armorType)).getHasStack())
			{
				int j = 5 + ((ItemArmor) itemstack.getItem()).armorType;
				
				if (!this.mergeItemStack(itemstack1, j, j + 1, false))
				{
					return null;
				}
			}
			else if (par2 >= 9 && par2 < 36)
			{
				if (!this.mergeItemStack(itemstack1, 36, 45, false))
				{
					return null;
				}
			}
			else if (par2 >= 36 && par2 < 45)
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
			
			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		
		return itemstack;
	}
	
	@Override
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
	{
		return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
	}
}
