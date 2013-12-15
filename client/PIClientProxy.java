package clashsoft.playerinventoryapi.client;

import clashsoft.playerinventoryapi.client.gui.GuiMultiScreen;
import clashsoft.playerinventoryapi.common.PICommonProxy;
import clashsoft.playerinventoryapi.handlers.PIClientTickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiOpenEvent;

public class PIClientProxy extends PICommonProxy
{
	public static boolean ENABLE_MULTI_GUI = false;
	
	public GuiMultiScreen	theMultiGUI;
	
	@Override
	public void registerTickHandler()
	{
		super.registerTickHandler();
		TickRegistry.registerTickHandler(new PIClientTickHandler(), Side.CLIENT);
	}
	
	@Override
	public void onGUIOpened(GuiOpenEvent event)
	{
		if (ENABLE_MULTI_GUI && event.gui instanceof GuiContainer)
		{
			if (theMultiGUI == null)
			{
				theMultiGUI = new GuiMultiScreen();
				theMultiGUI.addGUI(event.gui);
				event.gui = theMultiGUI;
			}
			else
			{
				theMultiGUI.addGUI(event.gui);
				event.setCanceled(true);
				
				Minecraft.getMinecraft().currentScreen = theMultiGUI;
				Minecraft.getMinecraft().inGameHasFocus = false;
			}
		}
	}
}
