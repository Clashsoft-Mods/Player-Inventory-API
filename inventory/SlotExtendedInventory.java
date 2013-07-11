package com.chaosdev.playerinventoryapi.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.chaosdev.playerinventoryapi.lib.ExtendedInventory;

public class SlotExtendedInventory extends Slot
{
	private EntityPlayer thePlayer;
	private final ExtendedInventory extendedInventory;
	
	public SlotExtendedInventory(int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
	{
		super(ExtendedInventory.getEntityEI(par5EntityPlayer), par2, par3, par4);
		this.thePlayer = par5EntityPlayer;
		extendedInventory = ExtendedInventory.getEntityEI(par5EntityPlayer);
	}
	
	public void setPlayer(EntityPlayer player) { this.thePlayer = player; }
	
	/**
     * Helper fnct to get the stack in the slot.
     */
    @Override
	public ItemStack getStack()
    {
        return extendedInventory.itemStacks[this.getSlotIndex()];
    }

    /**
     * Returns if this slot contains a stack.
     */
    @Override
	public boolean getHasStack()
    {
        return this.getStack() != null;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    @Override
	public void putStack(ItemStack par1ItemStack)
    {
        super.putStack(par1ItemStack);
    }

    /**
     * Called when the stack in a Slot changes
     */
    @Override
	public void onSlotChanged()
    {
        super.onSlotChanged();
    }
}
