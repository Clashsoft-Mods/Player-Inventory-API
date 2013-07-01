package com.pocteam.playerinventoryapi.inventory;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public interface ISlotHandler
{
	public List<Slot> addSlots(EntityPlayer player);
}
