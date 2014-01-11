package clashsoft.playerinventoryapi.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import clashsoft.cslib.util.CSLog;
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
				stack.getItem().onArmorTickUpdate(this.entity.worldObj, this.entity, stack);
			}
		}
	}
	
	/**
	 * Returns the Extended Inventory for a player. Use this when constructing slots or when reading slot data.
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
	 * May be null because it needs to be created and applied at least once. Always use {@link ExtendedInventory#get(EntityPlayer)}
	 * 
	 * @param player
	 * @return the extended inventory
	 */
	protected static ExtendedInventory getUnsafe(EntityPlayer player)
	{
		return (ExtendedInventory) player.getExtendedProperties(IDENTIFIER);
	}
	
	/**
	 * Sets the Extended Inventory for a player and reads the packet data. Only used by PIAPI Packet Handler.
	 * 
	 * @param player
	 * @param packet
	 * @return
	 */
	public static ExtendedInventory setByPacket(EntityPlayer player, Packet250CustomPayload packet)
	{
		ExtendedInventory ei = get(player);
		ei.readFromPacket(packet);
		return ei;
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
		for (int i = 0; i < this.itemStacks.length; i++)
			this.sync(player, i);
	}
	
	/**
	 * Syncs slot # {@code slot} with the player
	 * 
	 * @param player
	 * @param slot
	 */
	public void sync(EntityPlayer player, int slot)
	{
		Packet250CustomPayload packet = this.createPacket(slot);
		
		if (player instanceof EntityPlayerMP) // Server
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
		else if (player instanceof EntityClientPlayerMP && player.capabilities.isCreativeMode) // Client
			((EntityClientPlayerMP) player).sendQueue.addToSendQueue(packet);
	}
	
	/**
	 * Creates a packet containing data.
	 * 
	 * @param slot
	 * @return the packet
	 */
	protected Packet250CustomPayload createPacket(int slot)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(128);
		DataOutputStream dos = new DataOutputStream(bos);
		
		try
		{
			dos.writeInt(slot);
			Packet.writeItemStack(this.getStackInSlot(slot), dos);
		}
		catch (Exception ex)
		{
			CSLog.error(ex);
			return null;
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(CHANNEL, bos.toByteArray());
		
		return packet;
	}
	
	/**
	 * Copies data from the packet.
	 * 
	 * @param packet
	 *            the data packet
	 * @return this
	 */
	public ExtendedInventory readFromPacket(Packet250CustomPayload packet)
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
		try
		{
			int slot = dis.readInt();
			this.itemStacks[slot] = Packet.readItemStack(dis);
		}
		catch (Exception ex)
		{
			CSLog.error(ex);
		}
		return this;
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
