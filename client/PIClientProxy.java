package clashsoft.playerinventoryapi.client;

import clashsoft.playerinventoryapi.common.PICommonProxy;
import clashsoft.playerinventoryapi.handlers.PIClientTickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class PIClientProxy extends PICommonProxy
{
	@Override
	public void registerTickHandler()
	{
		super.registerTickHandler();
		TickRegistry.registerTickHandler(new PIClientTickHandler(), Side.CLIENT);
	}
}
