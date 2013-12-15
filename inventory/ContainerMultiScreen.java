package clashsoft.playerinventoryapi.inventory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMultiScreen extends Container
{	
	public List<Container> subContainers = new ArrayList();
	
	public ContainerMultiScreen()
	{
		super();
	}
	
	public void addSubContainer(Container sub)
	{
		this.subContainers.add(sub);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return false;
	}
	
	@Override
	public List getInventory()
	{
		List arraylist = super.getInventory();

        for (Container sub : this.subContainers)
        {
        	arraylist.addAll(sub.getInventory());
        }
        
        return arraylist;
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		for (Container sub : this.subContainers)
		{
			sub.addCraftingToCrafters(icrafting);
		}
	}
	
	@Override
	public void removeCraftingFromCrafters(ICrafting icrafting)
	{
		super.removeCraftingFromCrafters(icrafting);
		for (Container sub : this.subContainers)
		{
			sub.removeCraftingFromCrafters(icrafting);
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (Container sub : this.subContainers)
		{
			sub.detectAndSendChanges();
		}
	}
	
	@Override
	public boolean enchantItem(EntityPlayer player, int level)
	{
		for (Container sub : this.subContainers)
		{
			sub.enchantItem(player, level);
		}
		return super.enchantItem(player, level);
	}
	
	@Override
	public Slot getSlot(int slotID)
	{
		return this.subContainers.get(containerIndex(slotID)).getSlot(slotIndex(slotID));
	}
	
	public int containerIndex(int slotID)
	{
		return slotID & 15;
	}
	
	public int slotIndex(int slotID)
	{
		return slotID >> 4;
	}
	
	public ItemStack slotClick(int slotID, int var1, int var2, EntityPlayer player)
    {
		int slotIndex = slotIndex(slotID);
		int containerIndex = containerIndex(slotID);
        return this.subContainers.get(containerIndex).slotClick(slotIndex, var1, var2, player);
    }
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		for (Container sub : this.subContainers)
		{
			sub.onContainerClosed(player);
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		super.onCraftMatrixChanged(inventory);
		for (Container sub : subContainers)
		{
			sub.onCraftMatrixChanged(inventory);
		}
	}
	
	@Override
	public void setPlayerIsPresent(EntityPlayer player, boolean isPresent)
	{
		super.setPlayerIsPresent(player, isPresent);
		for (Container sub : subContainers)
		{
			sub.setPlayerIsPresent(player, isPresent);
		}
	}
	
	@Override
	protected boolean mergeItemStack(ItemStack stack, int minSlotID, int maxSlotID, boolean flag)
	{
		int range = maxSlotID - minSlotID;
		int containerIndex = containerIndex(minSlotID);
		int slotIndex = slotIndex(minSlotID);
		Container container = this.subContainers.get(containerIndex);
		
		Method method = Container.class.getDeclaredMethods()[21]; // Should be mergeItemStack(ItemStack, int, int, boolean)
		
		try
		{
			return (boolean) method.invoke(container, stack, slotIndex, slotIndex + range, flag);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
