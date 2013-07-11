package com.chaosdev.playerinventoryapi.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedInventory implements IExtendedEntityProperties, IInventory
{
	public Entity entity;
	
	public ItemStack[] itemStacks = new ItemStack[128];
	public static Map<Integer, String> entityKey = new HashMap<Integer, String>();
	
	public ExtendedInventory(Entity entity)
	{
		this.entity = entity;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();
		for (int slot = 0; slot < itemStacks.length; slot++)
		{
			ItemStack is = itemStacks[slot];
			
			NBTTagCompound nbt = new NBTTagCompound();
			if (is != null)
				is.writeToNBT(nbt);
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
			NBTTagList list = compound.getTagList("ItemStacks");
			if (list.tagCount() > 0)
			{
				for (int i = 0; i < list.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound) list.tagAt(i);
					ItemStack is = ItemStack.loadItemStackFromNBT(nbt);
					int slot = nbt.getInteger("Slot");
					itemStacks[slot] = is;
				}
			}
		}
	}

	@Override
	public void init(Entity entity, World world)
	{
		this.entity = entity;
	}
	
	public static ExtendedInventory initEntityEI(Entity entity)
	{
		ExtendedInventory m = (ExtendedInventory) entity.getExtendedProperties("ExtendedInventory");
		if (m != null)
			return m;
		else
		{
			setEntityEI(entity, new ExtendedInventory(entity));
			return new ExtendedInventory(entity);
		}
	}

	public static void setEntityEI(Entity entity, ExtendedInventory ei)
	{
		entityKey.put(entity.entityId, entity.registerExtendedProperties("ExtendedInventory", ei));
	}
	
	public static ExtendedInventory getEntityEI(Entity entity)
	{
		ExtendedInventory m = (ExtendedInventory) entity.getExtendedProperties(entityKey.get(entity.entityId));
		if (m == null)
			m = (ExtendedInventory) entity.getExtendedProperties("ExtendedInventory");
		return m == null ? initEntityEI(entity) : m;
	}
	
	public void setStackInSlot(int slotid, ItemStack itemStack)
	{
		itemStacks[slotid] = itemStack;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return itemStacks[slot];
	}

	@Override
	public int getSizeInventory()
	{
		return itemStacks.length;
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		itemStacks[i].stackSize -= j;
		return itemStacks[i];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return getStackInSlot(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		setStackInSlot(i, itemstack);
	}

	@Override
	public String getInvName()
	{
		return "";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
		
	}
	
	public void onPlayerInventoryChanged(EntityPlayer player)
	{
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
}
