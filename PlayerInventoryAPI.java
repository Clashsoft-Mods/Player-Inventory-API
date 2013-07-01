package com.pocteam.playerinventoryapi;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;

import com.pocteam.playerinventoryapi.gui.GuiCustomInventoryCreative;
import com.pocteam.playerinventoryapi.gui.GuiCustomInventorySurvival;
import com.pocteam.playerinventoryapi.inventory.*;
import com.pocteam.playerinventoryapi.lib.ExtendedInventory;
import com.pocteam.playerinventoryapi.lib.PIAPIEventHandler;
import com.pocteam.playerinventoryapi.lib.PIAPITickHandler;
import com.pocteam.playerinventoryapi.lib.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
@NetworkMod(channels = { Reference.CHANNEL_NAME }, serverSideRequired = false, clientSideRequired = true)
public class PlayerInventoryAPI
{	
	@Instance(Reference.MOD_ID)
	public static PlayerInventoryAPI instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_LOCATION, serverSide = Reference.COMMON_PROXY_LOCATION)
	public static CommonProxy proxy = new CommonProxy();
	
	public static boolean ENABLE_CUSTOM_CREATIVE_INVENTORY = true;
	public static boolean ENABLE_CUSTOM_SURVIVAL_INVENTORY = true;
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		TickRegistry.registerTickHandler(new PIAPITickHandler(), Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(new PIAPIEventHandler());
		
		SURVIVAL_INVENTORY.addSlotHandler(new TestSlotHandler());
		CREATIVE_INVENTORY.addSlotHandler(new TestSlotHandler());
	}
	
	public static class TestSlotHandler implements ISlotHandler
	{
		@Override
		public List<Slot> addSlots(EntityPlayer player)
		{
			List<Slot> slots = new LinkedList<Slot>();
			slots.add(new SlotExtendedInventory(37, 27 + 18, 24, player));
			slots.add(new SlotExtendedInventory(38, 27 + 36, 24, player));
			return slots;
		}
		
	}
	
	public static class CREATIVE_INVENTORY
	{
		public static void addSlotHandler(ISlotHandler slothandler)
		{
			ContainerCustomInventoryCreative.addSlotHandler(slothandler);
		}
		
		public static void setSlotPosition(int slotid, int x, int y)
		{
			ContainerCustomInventoryCreative.setSlotPos(slotid, x, y);
		}
		
		public static void setBinSlotPosition(int x, int y)
		{
			GuiCustomInventoryCreative.setBinSlotPos(x, y);
		}
		
		public static void setPlayerDisplayPosition(int x, int y)
		{
			GuiCustomInventoryCreative.setPlayerDisplayPos(x, y);
		}
		
		public static void setWindowSize(int width)
		{
			GuiCustomInventoryCreative.setWindowWidth(width);
		}
		
		public static void addButton(IButtonHandler handler, GuiButton button)
		{
			GuiCustomInventoryCreative.addButton(handler, button);
		}
	}
	
	public static class SURVIVAL_INVENTORY
	{
		public static void addSlotHandler(ISlotHandler slothandler)
		{
			ContainerCustomInventorySurvival.addSlotHandler(slothandler);
		}
		
		public static void setSlotPosition(int slotid, int x, int y)
		{
			ContainerCustomInventorySurvival.setSlotPos(slotid, x, y);
		}
		
		public static void setPlayerDisplayPosition(int x, int y)
		{
			GuiCustomInventorySurvival.setPlayerDisplayPos(x, y);
		}
		
		public static void setCraftingArrowPositionAndRotation(int x, int y, float r)
		{
			GuiCustomInventorySurvival.setCraftArrowPos(x, y);
			GuiCustomInventorySurvival.setCraftArrowRot(r);
		}
		
		public static void setWindowSize(int width, int height)
		{
			GuiCustomInventorySurvival.setWindowSize(width, height);
		}
		
		public static void addButton(IButtonHandler handler, GuiButton button)
		{
			GuiCustomInventorySurvival.addButton(handler, button);
		}
	}
}
