package clashsoft.playerinventoryapi.client;

import clashsoft.playerinventoryapi.common.PICommonProxy;
import clashsoft.playerinventoryapi.handlers.PIAPITickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class PIClientProxy extends PICommonProxy
{
	public void registerTickHandler()
	{
		TickRegistry.registerTickHandler(new PIAPITickHandler(), Side.CLIENT);
	}
}
