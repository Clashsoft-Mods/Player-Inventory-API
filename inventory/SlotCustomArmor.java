package clashsoft.playerinventoryapi.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class SlotCustomArmor extends Slot
{
	public int			armorType;
	public EntityPlayer	player;
	
	public Icon			backgroundIcon;
	
	public SlotCustomArmor(EntityPlayer player, IInventory inventory, int slotIndex, int x, int y, int armorType)
	{
		this(player, inventory, slotIndex, x, y, armorType, ItemArmor.func_94602_b(armorType));
	}
	
	public SlotCustomArmor(EntityPlayer player, IInventory inventory, int slotIndex, int x, int y, int armorType, Icon backgroundIcon)
	{
		super(inventory, slotIndex, x, y);
		this.armorType = armorType;
		this.backgroundIcon = backgroundIcon;
	}
	
	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
		return item != null && item.isValidArmor(par1ItemStack, armorType, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns the icon index on items.png that is used as background image of the slot.
	 */
	public Icon getBackgroundIconIndex()
	{
		return backgroundIcon;
	}
}
