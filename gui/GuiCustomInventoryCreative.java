package com.pocteam.playerinventoryapi.gui;

import java.util.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.pocteam.playerinventoryapi.inventory.*;
import com.pocteam.playerinventoryapi.lib.GuiHelper.GuiPos;
import com.pocteam.playerinventoryapi.lib.GuiHelper.GuiSize;

public class GuiCustomInventoryCreative extends InventoryEffectRenderer
{
	public static ResourceLocation custominventory = new ResourceLocation("gui/custominventory.png");

	private static InventoryBasic inventory = new InventoryBasic("tmp", true, 45);

	/** Currently selected creative inventory tab index. */
	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();

	/** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
	private float currentScroll = 0.0F;

	/** True if the scrollbar is being dragged */
	private boolean isScrolling = false;

	/**
	 * True if the left mouse button was held down last time drawScreen was called.
	 */
	private boolean wasClicking;
	private GuiTextField searchField;

	/**
	 * Used to back up the ContainerCustomInventoryCreative's inventory slots before filling it with the player's inventory slots for
	 * the inventory tab.
	 */
	private List backupContainerSlots;
	private Slot binSlot = null;
	private boolean field_74234_w = false;
	private CreativeCrafting field_82324_x;
	private static int tabPage = 0;
	private int maxPages = 0;

	private int xSize_lo;
	private int ySize_lo;

	private final EntityPlayer player;

	private final ContainerCustomInventoryCreative creativecontainer;

	//PLAYER INVENTORY API
	private static GuiSize windowSize = new GuiSize(195, 136);
	private static GuiPos playerDisplayPos = new GuiPos(8, 5);
	private final GuiPos[] slotPos;

	private ResourceLocation field_110424_t = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private static GuiPos binSlotPos = new GuiPos(173, 112);

	private static Map<GuiButton, IButtonHandler> buttons = new HashMap<GuiButton, IButtonHandler>();

	public GuiCustomInventoryCreative(EntityPlayer par1EntityPlayer, ContainerCreativeList par2ContainerCreativeList, ContainerCustomInventoryCreative par3ContainerCustomInventoryCreative)
	{
		super(par2ContainerCreativeList);
		par1EntityPlayer.openContainer = this.inventorySlots;
		this.creativecontainer = par3ContainerCustomInventoryCreative;
		this.allowUserInput = true;
		this.player = par1EntityPlayer;
		par1EntityPlayer.addStat(AchievementList.openInventory, 1);
		this.ySize = 136;
		this.xSize = 195;

		List<Slot> slots = par3ContainerCustomInventoryCreative.inventorySlots;
		slotPos = new GuiPos[slots.size()];
		for (int i = 0; i < slots.size(); i++)
		{
			if (slots.get(i) != null)
				slotPos[i] = new GuiPos(slots.get(i).xDisplayPosition, slots.get(i).yDisplayPosition);
		}
	}

	public static void resetGui()
	{
		windowSize = new GuiSize(195, 136);
		playerDisplayPos = new GuiPos(28, 5);
		binSlotPos = new GuiPos(173, 112);
	}

	//Only width set is possible in creative inv
	public static void setWindowWidth(int width)
	{
		windowSize.setWidth(width);
	}

	public static void setPlayerDisplayPos(int x, int y)
	{
		playerDisplayPos = new GuiPos(x, y);
	}

	public static void setBinSlotPos(int x, int y)
	{
		binSlotPos = new GuiPos(x, y);
	}

	public static void addButton(IButtonHandler handler, GuiButton button)
	{
		buttons.put(button, handler);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		if (!this.mc.playerController.isInCreativeMode())
		{
			this.mc.displayGuiScreen(new GuiCustomInventorySurvival(player, new ContainerCustomInventorySurvival(player.inventory, false, player)));
		}
	}

	@Override
	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
	{
		this.field_74234_w = true;
		boolean flag = par4 == 1;
		par4 = par2 == -999 && par4 == 0 ? 4 : par4;
		ItemStack itemstack;
		InventoryPlayer inventoryplayer;

		if (par1Slot == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && par4 != 5)
		{
			inventoryplayer = this.player.inventory;

			if (inventoryplayer.getItemStack() != null)
			{
				if (par3 == 0)
				{
					this.mc.thePlayer.dropPlayerItem(inventoryplayer.getItemStack());
					this.mc.playerController.func_78752_a(inventoryplayer.getItemStack());
					inventoryplayer.setItemStack((ItemStack)null);
				}

				if (par3 == 1)
				{
					itemstack = inventoryplayer.getItemStack().splitStack(1);
					this.mc.thePlayer.dropPlayerItem(itemstack);
					this.mc.playerController.func_78752_a(itemstack);

					if (inventoryplayer.getItemStack().stackSize == 0)
					{
						inventoryplayer.setItemStack((ItemStack)null);
					}
				}
			}
		}
		else
		{
			int l;

			if (par1Slot == this.binSlot && flag)
			{
				for (l = 0; l < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++l)
				{
					this.mc.playerController.sendSlotPacket((ItemStack)null, l);
				}
			}
			else
			{
				ItemStack itemstack1;

				if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
				{
					if (par1Slot == this.binSlot)
					{
						this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
					}
					else if (par4 == 4 && par1Slot != null && par1Slot.getHasStack())
					{
						itemstack1 = par1Slot.decrStackSize(par3 == 0 ? 1 : par1Slot.getStack().getMaxStackSize());
						this.mc.thePlayer.dropPlayerItem(itemstack1);
						this.mc.playerController.func_78752_a(itemstack1);
					}
					else if (par4 == 4 && this.mc.thePlayer.inventory.getItemStack() != null)
					{
						this.mc.thePlayer.dropPlayerItem(this.mc.thePlayer.inventory.getItemStack());
						this.mc.playerController.func_78752_a(this.mc.thePlayer.inventory.getItemStack());
						this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
					}
					else
					{
						this.mc.thePlayer.inventoryContainer.slotClick(par1Slot == null ? par2 : SlotCustomCreativeInventory.func_75240_a((SlotCustomCreativeInventory)par1Slot).slotNumber, par3, par4, this.mc.thePlayer);
						this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
					}
				}
				else if (par4 != 5 && par1Slot.inventory == inventory)
				{
					inventoryplayer = this.mc.thePlayer.inventory;
					itemstack = inventoryplayer.getItemStack();
					ItemStack itemstack2 = par1Slot.getStack();
					ItemStack itemstack3;

					if (par4 == 2)
					{
						if (itemstack2 != null && par3 >= 0 && par3 < 9)
						{
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							this.mc.thePlayer.inventory.setInventorySlotContents(par3, itemstack3);
							this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
						}

						return;
					}

					if (par4 == 3)
					{
						if (inventoryplayer.getItemStack() == null && par1Slot.getHasStack())
						{
							itemstack3 = par1Slot.getStack().copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							inventoryplayer.setItemStack(itemstack3);
						}

						return;
					}

					if (par4 == 4)
					{
						if (itemstack2 != null)
						{
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = par3 == 0 ? 1 : itemstack3.getMaxStackSize();
							this.mc.thePlayer.dropPlayerItem(itemstack3);
							this.mc.playerController.func_78752_a(itemstack3);
						}

						return;
					}

					if (itemstack != null && itemstack2 != null && itemstack.isItemEqual(itemstack2) && ItemStack.areItemStackTagsEqual(itemstack, itemstack2)) //Forge: Bugfix, Compare NBT data, allow for deletion of enchanted books, MC-12770
					{
						if (par3 == 0)
						{
							if (flag)
							{
								itemstack.stackSize = itemstack.getMaxStackSize();
							}
							else if (itemstack.stackSize < itemstack.getMaxStackSize())
							{
								++itemstack.stackSize;
							}
						}
						else if (itemstack.stackSize <= 1)
						{
							inventoryplayer.setItemStack((ItemStack)null);
						}
						else
						{
							--itemstack.stackSize;
						}
					}
					else if (itemstack2 != null && itemstack == null)
					{
						inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
						itemstack = inventoryplayer.getItemStack();

						if (flag)
						{
							itemstack.stackSize = itemstack.getMaxStackSize();
						}
					}
					else
					{
						inventoryplayer.setItemStack((ItemStack)null);
					}
				}
				else
				{
					this.inventorySlots.slotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.mc.thePlayer);

					if (Container.func_94532_c(par3) == 2)
					{
						for (l = 0; l < 9; ++l)
						{
							this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + l).getStack(), 36 + l);
						}
					}
					else if (par1Slot != null)
					{
						itemstack1 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
						this.mc.playerController.sendSlotPacket(itemstack1, par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
					}
				}
			}
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		if (this.mc.playerController.isInCreativeMode())
		{	
			super.initGui();
			this.buttonList.clear();

			for (GuiButton button : buttons.keySet())
			{
				this.buttonList.add(button);
			}

			Keyboard.enableRepeatEvents(true);
			this.searchField = new GuiTextField(this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
			this.searchField.setMaxStringLength(15);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField.setVisible(false);
			this.searchField.setTextColor(16777215);
			int i = selectedTabIndex;
			selectedTabIndex = -1;
			this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
			this.field_82324_x = new CreativeCrafting(this.mc);
			this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.field_82324_x);
			int tabCount = CreativeTabs.creativeTabArray.length;
			if (tabCount > 12)
			{
				buttonList.add(new GuiButton(101, guiLeft,              guiTop - 50, 20, 20, "<"));
				buttonList.add(new GuiButton(102, guiLeft + xSize - 20, guiTop - 50, 20, 20, ">"));
				maxPages = ((tabCount - 12) / 10) + 1;
			}
		}
		else
		{
			this.mc.displayGuiScreen(new GuiCustomInventorySurvival(this.mc.thePlayer, new ContainerCustomInventorySurvival(mc.thePlayer.inventory, false, mc.thePlayer)));
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
		{
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_82324_x);
		}

		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
		{
			if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
			{
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			}
			else
			{
				super.keyTyped(par1, par2);
			}
		}
		else
		{
			if (this.field_74234_w)
			{
				this.field_74234_w = false;
				this.searchField.setText("");
			}

			if (!this.checkHotbarKeys(par2))
			{
				if (this.searchField.textboxKeyTyped(par1, par2))
				{
					this.updateCreativeSearch();
				}
				else
				{
					super.keyTyped(par1, par2);
				}
			}
		}
	}

	private void updateCreativeSearch()
	{
		ContainerCreativeList crativeList = (ContainerCreativeList)this.inventorySlots;
		crativeList.itemList.clear();
		Item[] aitem = Item.itemsList;
		int i = aitem.length;
		int j;

		for (j = 0; j < i; ++j)
		{
			Item item = aitem[j];

			if (item != null && item.getCreativeTab() != null)
			{
				item.getSubItems(item.itemID, (CreativeTabs)null, crativeList.itemList);
			}
		}

		Enchantment[] aenchantment = Enchantment.enchantmentsList;
		i = aenchantment.length;

		for (j = 0; j < i; ++j)
		{
			Enchantment enchantment = aenchantment[j];

			if (enchantment != null && enchantment.type != null)
			{
				Item.enchantedBook.func_92113_a(enchantment, crativeList.itemList);
			}
		}

		Iterator iterator = crativeList.itemList.iterator();
		String s = this.searchField.getText().toLowerCase();

		while (iterator.hasNext())
		{
			ItemStack itemstack = (ItemStack)iterator.next();
			boolean flag = false;
			Iterator iterator1 = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

			while (true)
			{
				if (iterator1.hasNext())
				{
					String s1 = (String)iterator1.next();

					if (!s1.toLowerCase().contains(s))
					{
						continue;
					}

					flag = true;
				}

				if (!flag)
				{
					iterator.remove();
				}

				break;
			}
		}

		this.currentScroll = 0.0F;
		crativeList.scrollTo(0.0F);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];

        if (creativetabs != null && creativetabs.drawInForegroundOfTab())
        {
            this.fontRenderer.drawString(I18n.func_135053_a(creativetabs.getTranslatedTabLabel()), 8, 6, 4210752);
        }
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		if (par3 == 0)
		{
			int l = par1 - this.guiLeft;
			int i1 = par2 - this.guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;

			for (int k1 = 0; k1 < j1; ++k1)
			{
				CreativeTabs creativetabs = acreativetabs[k1];

				if (this.func_74232_a(creativetabs, l, i1))
				{
					return;
				}
			}
		}

		super.mouseClicked(par1, par2, par3);
	}

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
	{
		if (par3 == 0)
		{
			int l = par1 - this.guiLeft;
			int i1 = par2 - this.guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;

			for (int k1 = 0; k1 < j1; ++k1)
			{
				CreativeTabs creativetabs = acreativetabs[k1];

				if (creativetabs != null && func_74232_a(creativetabs, l, i1))
				{
					this.setCurrentCreativeTab(creativetabs);
					return;
				}
			}
		}

		super.mouseMovedOrUp(par1, par2, par3);
	}

	/**
	 * returns (if you are not on the inventoryTab) and (the flag isn't set) and( you have more than 1 page of items)
	 */
	private boolean needsScrollBars()
	{
		if (CreativeTabs.creativeTabArray[selectedTabIndex] == null) return false;
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreativeList)this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}

	private void setCurrentCreativeTab(CreativeTabs par1CreativeTabs)
	{
		if (par1CreativeTabs == null)
		{
			return;
		}

		int i = selectedTabIndex;
		selectedTabIndex = par1CreativeTabs.getTabIndex();
		ContainerCreativeList creativeList = (ContainerCreativeList)this.inventorySlots;
		this.field_94077_p.clear();
		ContainerCreativeList.itemList.clear();
		par1CreativeTabs.displayAllReleventItems(ContainerCreativeList.itemList);

		if (par1CreativeTabs == CreativeTabs.tabInventory)
		{
			Container container = this.creativecontainer;

			if (this.backupContainerSlots == null)
			{
				this.backupContainerSlots = inventorySlots.inventorySlots;
			}

			this.inventorySlots.inventorySlots = new ArrayList();

			for (int j = 0; j < container.inventorySlots.size(); ++j)
			{
				if (container.inventorySlots.get(j) != null)
				{
					SlotCustomCreativeInventory slotcreativeinventory = new SlotCustomCreativeInventory(this, (Slot)container.inventorySlots.get(j), j);
					if (slotPos[j] != null)
						slotcreativeinventory.xDisplayPosition = slotPos[j].getX();
					if (slotPos[j] != null)
						slotcreativeinventory.yDisplayPosition = slotPos[j].getY();
					inventorySlots.inventorySlots.add(slotcreativeinventory);
				}
			}

			this.binSlot = new Slot(inventory, 0, this.binSlotPos.getX(), this.binSlotPos.getY());
			inventorySlots.inventorySlots.add(this.binSlot);
		}
		else if (i == CreativeTabs.tabInventory.getTabIndex())
		{
			creativeList.inventorySlots = this.backupContainerSlots;
			this.backupContainerSlots = null;
		}

		if (this.searchField != null)
		{
			if (par1CreativeTabs == CreativeTabs.tabAllSearch)
			{
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.updateCreativeSearch();
			}
			else
			{
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}

		this.currentScroll = 0.0F;
		creativeList.scrollTo(0.0F);
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0 && this.needsScrollBars())
		{
			int j = ((ContainerCreativeList)this.inventorySlots).itemList.size() / 9 - 5 + 1;

			if (i > 0)
			{
				i = 1;
			}

			if (i < 0)
			{
				i = -1;
			}

			this.currentScroll = (float)(this.currentScroll - (double)i / (double)j);

			if (this.currentScroll < 0.0F)
			{
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F)
			{
				this.currentScroll = 1.0F;
			}

			((ContainerCreativeList)this.inventorySlots).scrollTo(this.currentScroll);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		boolean flag = Mouse.isButtonDown(0);
        int k = this.guiLeft;
        int l = this.guiTop;
        int i1 = k + 175;
        int j1 = l + 18;
        int k1 = i1 + 14;
        int l1 = j1 + 112;

        if (!this.wasClicking && flag && par1 >= i1 && par2 >= j1 && par1 < k1 && par2 < l1)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag)
        {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling)
        {
            this.currentScroll = ((float)(par2 - j1) - 7.5F) / ((float)(l1 - j1) - 15.0F);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }

            ((ContainerCreativeList)this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(par1, par2, par3);
        CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
        int start = tabPage * 10;
        int i2 = Math.min(acreativetabs.length, ((tabPage + 1) * 10) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (int j2 = start; j2 < i2; ++j2)
        {
            CreativeTabs creativetabs = acreativetabs[j2];

            if (creativetabs != null && this.renderCreativeInventoryHoveringText(creativetabs, par1, par2))
            {
                rendered = true;
                break;
            }
        }

        if (!rendered && !renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, par1, par2))
        {
            renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, par1, par2);
        }

        if (this.binSlot != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.binSlot.xDisplayPosition, this.binSlot.yDisplayPosition, 16, 16, par1, par2))
        {
            this.drawCreativeTabHoveringText(I18n.func_135053_a("inventory.binSlot"), par1, par2);
        }

        if (maxPages != 0)
        {
            String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
            int width = fontRenderer.getStringWidth(page);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            fontRenderer.drawString(page, guiLeft + (xSize / 2) - (width / 2), guiTop - 44, -1);
            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.xSize_lo = par2;
		this.ySize_lo = par3;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
        int k = acreativetabs.length;
        int l;

        int start = tabPage * 10;
        k = Math.min(acreativetabs.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        for (l = start; l < k; ++l)
        {
            CreativeTabs creativetabs1 = acreativetabs[l];
            this.mc.func_110434_K().func_110577_a(field_110424_t);

            if (creativetabs1 != null && creativetabs1.getTabIndex() != selectedTabIndex)
            {
                this.renderCreativeTab(creativetabs1);
            }
        }

        if (tabPage != 0)
        {
            if (creativetabs != CreativeTabs.tabAllSearch)
            {
                this.mc.func_110434_K().func_110577_a(field_110424_t);
                renderCreativeTab(CreativeTabs.tabAllSearch);
            }
            if (creativetabs != CreativeTabs.tabInventory)
            {
                this.mc.func_110434_K().func_110577_a(field_110424_t);
                renderCreativeTab(CreativeTabs.tabInventory);
            }
        }

        if (creativetabs == CreativeTabs.tabInventory)
        {
        	this.renderInventoryTab();
        }
        else
        {
        	this.mc.func_110434_K().func_110577_a(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        	this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        }
        this.searchField.drawTextBox();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i1 = this.guiLeft + 175;
        k = this.guiTop + 18;
        l = k + 112;
        this.mc.func_110434_K().func_110577_a(field_110424_t);

        if (creativetabs.shouldHidePlayerInventory())
        {
            this.drawTexturedModalRect(i1, k + (int)((float)(l - k - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if (creativetabs == null || creativetabs.getTabPage() != tabPage)
        {
            if (creativetabs != CreativeTabs.tabAllSearch && creativetabs != CreativeTabs.tabInventory)
            {
                return;
            }
        }

        this.renderCreativeTab(creativetabs);
	}

	private void renderInventoryTab()
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		int var1 = (this.width - 195 + (!this.mc.thePlayer.getActivePotionEffects().isEmpty() ? 120 : 0)) / 2;
		int var2 = (this.height - 136) / 2;
		int var3 = (this.width - windowSize.getWidth() + (!this.mc.thePlayer.getActivePotionEffects().isEmpty() ? 120 : 0)) / 2;
		this.drawBackgroundFrame(var3, (this.height - windowSize.getHeight()) / 2, windowSize.getWidth(), windowSize.getHeight());
		GL11.glTranslatef(var1, var2, 0);
		this.drawPlayerBackground(playerDisplayPos.getX(), playerDisplayPos.getY());
		for (GuiPos pos : slotPos)
		{
			if (pos != null)
				this.drawSlot(pos.getX(), pos.getY(), false);
		}
		this.drawSlot(binSlotPos.getX(), binSlotPos.getY(), true);
		GL11.glTranslatef(-var1, -var2, 0);
		GuiCustomInventorySurvival.drawPlayerOnGui(this.mc, var1 + this.playerDisplayPos.getX() + 16, var2 + this.playerDisplayPos.getY() + 41, 20, var1 + this.playerDisplayPos.getX() + 16 - this.xSize_lo, var2 + this.playerDisplayPos.getY() + 10 - this.ySize_lo);
		RenderHelper.enableGUIStandardItemLighting();
	}

	public void drawBackgroundFrame(int posX, int posY, int sizeX, int sizeY)
	{
		this.mc.func_110434_K().func_110577_a(custominventory);

		for (int i = 0; i < sizeX - 16; i += 8)
		{
			for (int j = 0; j < sizeY - 16; j += 8)
			{
				this.drawTexturedModalRect(posX + i + 8, posY + j + 8, 4, 4, 8, 8);
			}
		}

		for (int i = 0; i < sizeX - 16; i += 8)
		{
			this.drawTexturedModalRect(posX + 8 + i, posY, 4, 0, 8, 8);
			this.drawTexturedModalRect(posX + 8 + i, posY + sizeY - 8, 4, 8, 8, 8);
		}

		for (int i = 0; i < sizeY - 16; i += 8)
		{
			this.drawTexturedModalRect(posX, posY + 8 + i, 0, 4, 8, 8);
			this.drawTexturedModalRect(posX + sizeX - 8, posY + 8 + i, 8, 4, 8, 8);
		}

		//Edges
		this.drawTexturedModalRect(posX, posY, 0, 0, 8, 8);
		this.drawTexturedModalRect(posX + sizeX - 8, posY, 8, 0, 8, 8);
		this.drawTexturedModalRect(posX, posY + sizeY - 8, 0, 8, 8, 8);
		this.drawTexturedModalRect(posX + sizeX - 8, posY + sizeY - 8, 8, 8, 8, 8);
	}

	public void drawPlayerBackground(int posX, int posY)
	{
		this.mc.func_110434_K().func_110577_a(custominventory);
		this.drawTexturedModalRect(posX, posY, 54, 18, 34, 45);
	}

	public void drawSlot(int posX, int posY, boolean isBinSlot)
	{
		this.mc.func_110434_K().func_110577_a(custominventory);
		if (!isBinSlot)
			this.drawTexturedModalRect(posX - 1, posY - 1, 16, 0, 18, 18);
		else
			this.drawTexturedModalRect(posX - 1, posY - 1, 54, 0, 18, 18);
	}

	protected boolean func_74232_a(CreativeTabs par1CreativeTabs, int par2, int par3)
	{
		if (par1CreativeTabs.getTabPage() != tabPage)
		{
			if (par1CreativeTabs != CreativeTabs.tabAllSearch &&
					par1CreativeTabs != CreativeTabs.tabInventory)
			{
				return false;
			}
		}

		int k = par1CreativeTabs.getTabColumn();
		int l = 28 * k;
		byte b0 = 0;

		if (k == 5)
		{
			l = this.xSize - 28 + 2;
		}
		else if (k > 0)
		{
			l += k;
		}

		int i1;

		if (par1CreativeTabs.isTabInFirstRow())
		{
			i1 = b0 - 32;
		}
		else
		{
			i1 = b0 + this.ySize;
		}

		return par2 >= l && par2 <= l + 28 && par3 >= i1 && par3 <= i1 + 32;
	}

	/**
	 * Renders the creative inventory hovering text if mouse is over it. Returns true if did render or false otherwise.
	 * Params: current creative tab to be checked, current mouse x position, current mouse y position.
	 */
	protected boolean renderCreativeInventoryHoveringText(CreativeTabs par1CreativeTabs, int par2, int par3)
	{
		int k = par1CreativeTabs.getTabColumn();
        int l = 28 * k;
        byte b0 = 0;

        if (k == 5)
        {
            l = this.xSize - 28 + 2;
        }
        else if (k > 0)
        {
            l += k;
        }

        int i1;

        if (par1CreativeTabs.isTabInFirstRow())
        {
            i1 = b0 - 32;
        }
        else
        {
            i1 = b0 + this.ySize;
        }

        if (this.isPointInRegion(l + 3, i1 + 3, 23, 27, par2, par3))
        {
            this.drawCreativeTabHoveringText(I18n.func_135053_a(par1CreativeTabs.getTranslatedTabLabel()), par2, par3);
            return true;
        }
        else
        {
            return false;
        }
	}
	
	protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3)
    {
        if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
        {
            List list = par1ItemStack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs creativetabs = par1ItemStack.getItem().getCreativeTab();

            if (creativetabs == null && par1ItemStack.itemID == Item.enchantedBook.itemID)
            {
                Map map = EnchantmentHelper.getEnchantments(par1ItemStack);

                if (map.size() == 1)
                {
                    Enchantment enchantment = Enchantment.enchantmentsList[((Integer)map.keySet().iterator().next()).intValue()];
                    CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
                    int k = acreativetabs.length;

                    for (int l = 0; l < k; ++l)
                    {
                        CreativeTabs creativetabs1 = acreativetabs[l];

                        if (creativetabs1.func_111226_a(enchantment.type))
                        {
                            creativetabs = creativetabs1;
                            break;
                        }
                    }
                }
            }

            if (creativetabs != null)
            {
                list.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.func_135053_a(creativetabs.getTranslatedTabLabel()));
            }

            for (int i1 = 0; i1 < list.size(); ++i1)
            {
                if (i1 == 0)
                {
                    list.set(i1, "\u00a7" + Integer.toHexString(par1ItemStack.getRarity().rarityColor) + (String)list.get(i1));
                }
                else
                {
                    list.set(i1, EnumChatFormatting.GRAY + (String)list.get(i1));
                }
            }

            this.func_102021_a(list, par2, par3);
        }
        else
        {
            super.drawItemStackTooltip(par1ItemStack, par2, par3);
        }
    }

	/**
	 * Renders passed creative inventory tab into the screen.
	 */
	protected void renderCreativeTab(CreativeTabs par1CreativeTabs)
	{
		boolean flag = par1CreativeTabs.getTabIndex() == selectedTabIndex;
		boolean flag1 = par1CreativeTabs.isTabInFirstRow();
		int i = par1CreativeTabs.getTabColumn();
		int j = i * 28;
		int k = 0;
		int l = this.guiLeft + 28 * i;
		int i1 = this.guiTop;
		byte b0 = 32;

		if (flag)
		{
			k += 32;
		}

		if (i == 5)
		{
			l = this.guiLeft + this.xSize - 28;
		}
		else if (i > 0)
		{
			l += i;
		}

		if (flag1)
		{
			i1 -= 28;
		}
		else
		{
			k += 64;
			i1 += this.ySize - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
		this.drawTexturedModalRect(l, i1, j, k, 28, b0);
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		l += 6;
		i1 += 8 + (flag1 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack itemstack = par1CreativeTabs.getIconItemStack();
		if (itemstack.getIconIndex() != null)
		{
			itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, l, i1);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, l, i1);
		}
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.id == 101)
		{
			tabPage = Math.max(tabPage - 1, 0);
		}
		else if (par1GuiButton.id == 102)
		{
			tabPage = Math.min(tabPage + 1, maxPages);
		}

		if (this.buttons.get(par1GuiButton) != null)
			this.buttons.get(par1GuiButton).onButtonPressed(par1GuiButton);
	}

	public int func_74230_h()
	{
		return selectedTabIndex;
	}

	/**
	 * Returns the creative inventory
	 */
	public static InventoryBasic getInventory()
	{
		return inventory;
	}
}
