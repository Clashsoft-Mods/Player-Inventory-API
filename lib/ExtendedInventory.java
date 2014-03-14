package clashsoft.playerinventoryapi.lib;

import clashsoft.playerinventoryapi.PlayerInventoryAPI;
import clashsoft.playerinventoryapi.network.EIFullPacket;
import clashsoft.playerinventoryapi.network.EIPacket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

/**
 * Extended Inventory class. Stores extra slot data of a custom player inventory
 * 
 * @author Clashsoft
 */
public class ExtendedInventory implements IExtendedEntityProperties, IInventory
{
	public static final String	IDENTIFIER	= "ExtendedInventory";
	public static final String	CHANNEL		= "ExtInvChannel";
	
	public EntityPlayer			entity;
	
	public ItemStack[]			itemStacks	= new ItemStack[128];
	
	public ExtendedInventory(EntityPlayer entity)
	{
		this.entity = entity;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();
		for (int slot = 0; slot < this.itemStacks.length; slot++)
		{
			ItemStack is = this.itemStacks[slot];
			
			NBTTagCompound nbt = new NBTTagCompound();
			if (is != null)
			{
				is.writeToNBT(nbt);
			}
			nbt.setInteger("Slot", slot);
			list.appendTag(nbt);
		}
		compound.setTag("ItemStacks", list);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		if (compound.hasKey("ItemStacks"))
		{
			NBTTagList list = compound.getTagList("ItemStacks", Constants.NBT.TAG_COMPOUND);
			if (list.tagCount() > 0)
			{
				for (int i = 0; i < list.tagCount(); i++)
				{
					NBTTagCompound nbt = list.getCompoundTagAt(i);
					ItemStack is = ItemStack.loadItemStackFromNBT(nbt);
					int slot = nbt.getInteger("Slot");
					this.itemStacks[slot] = is;
				}
			}
		}
	}
	
	@Override
	public void init(Entity entity, World world)
	{
	}
	
	/**
	 * Called each tick to update items
	 */
	public void onUpdate()
	{
		for (ItemStack stack : this.itemStacks)
		{
			if (stack != null && stack.getItem() != null)
			{
				stack.getItem().onUpdate(stack, this.entity.worldObj, this.entity, this.entity.inventory.currentItem, false);
				stack.getItem().onArmorTick(this.entity.worldObj, this.entity, stack);
			}
		}
	}
	
	/**
	 * Returns the Extended Inventory for a player. Use this when constructing slots or when reading
	 * slot data.
	 * <p>
	 * If the player does not have a registered Extended Inventory, one will be created.
	 * 
	 * @param player
	 * @return the extended inventory
	 */
	public static ExtendedInventory get(EntityPlayer player)
	{
		ExtendedInventory props = getUnsafe(player);
		return props == null ? set(player, new ExtendedInventory(player)) : props;
	}
	
	/**
	 * Directly gets the Extended Inventory from get player extended inventory map.
	 * <p>
	 * May be null because it needs to be created and applied at least once. Always use
	 * {@link ExtendedInventory#get(EntityPlayer)}
	 * 
	 * @param player
	 * @return the extended inventory
	 */
	protected static ExtendedInventory getUnsafe(EntityPlayer player)
	{
		return (ExtendedInventory) player.getExtendedProperties(IDENTIFIER);
	}
	
	/**
	 * Sets the Extended Inventory for a player.
	 * 
	 * @param player
	 * @param properties
	 * @return
	 */
	public static ExtendedInventory set(EntityPlayer player, ExtendedInventory properties)
	{
		ExtendedInventory props = (ExtendedInventory) player.getExtendedProperties(IDENTIFIER);
		if (props == null)
		{
			props = new ExtendedInventory(player);
			player.registerExtendedProperties(IDENTIFIER, props);
		}
		else
			copy(properties, props);
		return props;
	}
	
	/**
	 * Copies {@code source} data to {@code dest}.
	 * 
	 * @param source
	 * @param dest
	 */
	public static void copy(ExtendedInventory source, ExtendedInventory dest)
	{
		dest.itemStacks = source.itemStacks;
		dest.entity = source.entity;
	}
	
	/**
	 * Syncs all slots with the player
	 * 
	 * @param player
	 */
	public void sync(EntityPlayer player)
	{
		PlayerInventoryAPI.netHandler.send(new EIFullPacket(this));
	}
	
	/**
	 * Syncs slot # {@code slot} with the player
	 * 
	 * @param player
	 * @param slot
	 */
	public void sync(EntityPlayer player, int slot)
	{
		PlayerInventoryAPI.netHandler.send(new EIPacket(this, slot));
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.itemStacks[slot];
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.itemStacks.length;
	}
	
	@Override
	public ItemStack decrStackSize(int slotID, int amount)
	{
		if (this.itemStacks[slotID] != null)
		{
			ItemStack itemstack;
			
			if (this.itemStacks[slotID].stackSize <= amount)
			{
				itemstack = this.itemStacks[slotID];
				this.setInventorySlotContents(slotID, null);
				return itemstack;
			}
			else
			{
				itemstack = this.itemStacks[slotID].splitStack(amount);
				
				if (this.itemStacks[slotID].stackSize == 0)
				{
					this.setInventorySlotContents(slotID, null);
				}
				
				this.markDirty();
				return itemstack;
			}
		}
		else
			return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slotID)
	{
		return this.getStackInSlot(slotID);
	}
	
	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemstack)
	{
		if (itemstack != null && itemstack.stackSize <= 0)
			itemstack = null;
		this.itemStacks[slotID] = itemstack;
		this.sync(this.entity, slotID);
	}
	
	@Override
	public String getInventoryName()
	{
		return IDENTIFIER;
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public void markDirty()
	{
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public void openInventory()
	{
	}
	
	@Override
	public void closeInventory()
	{
	}
}
