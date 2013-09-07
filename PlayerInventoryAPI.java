package com.chaosdev.playerinventoryapi;

import com.chaosdev.playerinventoryapi.gui.GuiCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.gui.GuiCustomInventorySurvival;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import com.chaosdev.playerinventoryapi.inventory.ContainerCustomInventorySurvival;
import com.chaosdev.playerinventoryapi.inventory.IButtonHandler;
import com.chaosdev.playerinventoryapi.inventory.ISlotHandler;
import com.chaosdev.playerinventoryapi.lib.PIAPIEventHandler;
import com.chaosdev.playerinventoryapi.lib.Reference;
import com.chaosdev.playerinventoryapi.lib.objects.InventoryObject;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
@NetworkMod(channels = { Reference.CHANNEL_NAME }, serverSideRequired = false, clientSideRequired = true)
public class PlayerInventoryAPI
{
	@Instance(Reference.MOD_ID)
	public static PlayerInventoryAPI	instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_LOCATION, serverSide = Reference.COMMON_PROXY_LOCATION)
	public static CommonProxy			proxy								= new CommonProxy();
	
	public static boolean				ENABLE_CUSTOM_CREATIVE_INVENTORY	= true;
	public static boolean				ENABLE_CUSTOM_SURVIVAL_INVENTORY	= true;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		proxy.registerTickHandler();
		MinecraftForge.EVENT_BUS.register(new PIAPIEventHandler());
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
		
		public static InventoryObject addObject(InventoryObject object)
		{
			GuiCustomInventoryCreative.addObject(object);
			return object;
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
		
		public static void setCraftingArrowPosition(int x, int y)
		{
			GuiCustomInventorySurvival.setCraftArrowPos(x, y);
		}
		
		public static void setCraftingArrowRotation(float rotation)
		{
			GuiCustomInventorySurvival.setCraftArrowRot(rotation);
		}
		
		public static void setWindowSize(int width, int height)
		{
			GuiCustomInventorySurvival.setWindowSize(width, height);
		}
		
		public static void addButton(IButtonHandler handler, GuiButton button)
		{
			GuiCustomInventorySurvival.addButton(handler, button);
		}
		
		public static InventoryObject addObject(InventoryObject object)
		{
			GuiCustomInventorySurvival.addObject(object);
			return object;
		}
	}
}
