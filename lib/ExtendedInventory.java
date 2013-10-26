package com.chaosdev.playerinventoryapi.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

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
	}
	
	public void onUpdate()
	{
		for (ItemStack stack : itemStacks)
		{
			if (stack != null && stack.getItem() != null)
			{
				stack.getItem().onUpdate(stack, entity.worldObj, entity, entity.inventory.currentItem, false);
				stack.getItem().onArmorTickUpdate(entity.worldObj, entity, stack);
			}
		}
	}
	
	public static ExtendedInventory getExtendedInventory(EntityPlayer player)
	{
		ExtendedInventory props = getExtendedInventory_(player);
		return props == null ? setExtendedInventory(player, new ExtendedInventory(player)) : props;
	}
	
	protected static ExtendedInventory getExtendedInventory_(EntityPlayer player)
	{
		return (ExtendedInventory) player.getExtendedProperties(IDENTIFIER);
	}
	
	public static ExtendedInventory setByPacket(EntityPlayer player, Packet250CustomPayload packet)
	{
		ExtendedInventory ei = getExtendedInventory(player);
		ei.readFromPacket(packet);
		return ei;
	}
	
	public static ExtendedInventory setExtendedInventory(EntityPlayer player, ExtendedInventory properties)
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
	
	public static void copy(ExtendedInventory source, ExtendedInventory dest)
	{
		dest.itemStacks = source.itemStacks;
		dest.entity = source.entity;
	}
	
	public void sync(EntityPlayer player)
	{
		for (int i = 0; i < itemStacks.length; i++)
			sync(player, i);
	}
	
	public void sync(EntityPlayer player, int slot)
	{
		Packet250CustomPayload packet = createPacket(slot);
		
		if (player instanceof EntityPlayerMP) // Server
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
		else if (player instanceof EntityClientPlayerMP) // Client
			((EntityClientPlayerMP) player).sendQueue.addToSendQueue(packet);
	}
	
	protected Packet250CustomPayload createPacket(int slot)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(128);
		DataOutputStream dos = new DataOutputStream(bos);
		
		try
		{
			dos.writeInt(slot);
			Packet.writeItemStack(getStackInSlot(slot), dos);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(CHANNEL, bos.toByteArray());
		
		return packet;
	}
	
	public ExtendedInventory readFromPacket(Packet250CustomPayload packet)
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
		try
		{
			int slot = dis.readInt();
			itemStacks[slot] = Packet.readItemStack(dis);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return this;
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
					this.setInventorySlotContents(slotID, null);
				
				this.onInventoryChanged();
				return itemstack;
			}
		}
		else
			return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slotID)
	{
		return getStackInSlot(slotID);
	}
	
	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemstack)
	{
		if (itemstack != null && itemstack.stackSize <= 0)
			itemstack = null;
		this.itemStacks[slotID] = itemstack;
		sync(entity, slotID);
	}
	
	@Override
	public String getInvName()
	{
		return IDENTIFIER;
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
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
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return true;
	}
}
