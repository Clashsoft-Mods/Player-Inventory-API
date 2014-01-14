package clashsoft.playerinventoryapi.inventory;

import clashsoft.playerinventoryapi.client.gui.GuiCreativeInventory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

@SideOnly(Side.CLIENT)
public class SlotCreative extends Slot
{
	private final Slot					theSlot;
	
	final GuiCreativeInventory	theCreativeInventory;
	
	public SlotCreative(GuiCreativeInventory creativeInventory, Slot slot, int slotIndex)
	{
		super(slot.inventory, slotIndex, slot.xDisplayPosition, slot.yDisplayPosition);
		this.theCreativeInventory = creativeInventory;
		this.theSlot = slot;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		this.theSlot.onPickupFromSlot(player, stack);
	}
	
	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return this.theSlot.isItemValid(stack);
	}
	
	/**
	 * Helper fnct to get the stack in the slot.
	 */
	@Override
	public ItemStack getStack()
	{
		return this.theSlot.getStack();
	}
	
	/**
	 * Returns if this slot contains a stack.
	 */
	@Override
	public boolean getHasStack()
	{
		return this.theSlot.getHasStack();
	}
	
	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack(ItemStack stack)
	{
		this.theSlot.putStack(stack);
	}
	
	/**
	 * Called when the stack in a Slot changes
	 */
	@Override
	public void onSlotChanged()
	{
		this.theSlot.onSlotChanged();
	}
	
	/**
	 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit()
	{
		return this.theSlot.getSlotStackLimit();
	}
	
	/**
	 * Returns the icon index on items.png that is used as background image of the slot.
	 */
	@Override
	public Icon getBackgroundIconIndex()
	{
		return this.theSlot.getBackgroundIconIndex();
	}
	
	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(int amount)
	{
		return this.theSlot.decrStackSize(amount);
	}
	
	/**
	 * returns true if this slot is in slotID of inventory
	 */
	@Override
	public boolean isSlotInInventory(IInventory inventory, int slotID)
	{
		return this.theSlot.isSlotInInventory(inventory, slotID);
	}
	
	public Slot getSlot()
	{
		return this.theSlot;
	}
}
