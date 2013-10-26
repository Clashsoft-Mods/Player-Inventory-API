package com.chaosdev.playerinventoryapi.client;

import com.chaosdev.playerinventoryapi.common.CommonProxy;
import com.chaosdev.playerinventoryapi.handlers.PIAPITickHandler;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	public void registerTickHandler()
	{
		super.registerTickHandler();
		TickRegistry.registerTickHandler(new PIAPITickHandler(), Side.CLIENT);
	}
}
