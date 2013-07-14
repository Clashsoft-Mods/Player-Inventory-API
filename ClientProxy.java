package com.chaosdev.playerinventoryapi;

import com.chaosdev.playerinventoryapi.lib.PIAPITickHandler;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


public class ClientProxy extends CommonProxy
{
	public void registerTickHandler()
	{
		TickRegistry.registerTickHandler(new PIAPITickHandler(), Side.CLIENT);
	}
}
