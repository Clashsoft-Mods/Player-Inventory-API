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
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
		return item != null && item.isValidArmor(par1ItemStack, armorType, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBackgroundIconIndex()
	{
		return backgroundIcon;
	}
}
