package clashsoft.playerinventoryapi.client.gui;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.invobject.InventoryObject;
import clashsoft.playerinventoryapi.inventory.*;
import clashsoft.playerinventoryapi.lib.GuiHelper.GuiPos;
import clashsoft.playerinventoryapi.lib.GuiHelper.GuiSize;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
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
import net.minecraft.util.ResourceLocation;

public class GuiCustomInventoryCreative extends InventoryEffectRenderer
{
	public static ResourceLocation					custominventory		= new ResourceLocation("gui/custominventory.png");
	
	private static InventoryBasic					inventory			= new InventoryBasic("tmp", true, 128);
	
	/** Currently selected creative inventory tab index. */
	private static int								selectedTabIndex	= CreativeTabs.tabBlock.getTabIndex();
	
	/** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
	private float									currentScroll		= 0.0F;
	
	/** True if the scrollbar is being dragged */
	private boolean									isScrolling			= false;
	
	/**
	 * True if the left mouse button was held down last time drawScreen was
	 * called.
	 */
	private boolean									wasClicking;
	private GuiTextField							searchField;
	
	/**
	 * Used to back up the ContainerCustomInventoryCreative's inventory slots
	 * before filling it with the player's inventory slots for the inventory
	 * tab.
	 */
	private List									backupContainerSlots;
	private Slot									binSlot				= null;
	private boolean									mouseClicked		= false;
	private CreativeCrafting						creativeCrafting;
	private static int								tabPage				= 0;
	private int										maxPages			= 0;
	
	private int										mouseX;
	private int										mouseY;
	
	private final EntityPlayer						player;
	
	private final ContainerCustomInventoryCreative	inventoryCreativeContainer;
	
	// PLAYER INVENTORY API
	private static GuiSize							windowSize			= new GuiSize(195, 136);
	private static GuiPos							playerDisplayPos	= new GuiPos(8, 5);
	private final GuiPos[]							slotPos;
	
	private ResourceLocation						tabsResource		= new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private static GuiPos							binSlotPos			= new GuiPos(173, 112);
	
	private static Map<GuiButton, IButtonHandler>	buttons				= new HashMap<GuiButton, IButtonHandler>();
	
	private static List<InventoryObject>			objects				= new ArrayList<InventoryObject>();
	
	public GuiCustomInventoryCreative(EntityPlayer player, ContainerCreativeList creativelist, ContainerCustomInventoryCreative inventoryCreativeContainer)
	{
		super(creativelist);
		
		this.inventoryCreativeContainer = inventoryCreativeContainer;
		
		this.allowUserInput = true;
		this.player = player;
		this.player.addStat(AchievementList.openInventory, 1);
		
		this.ySize = 136;
		this.xSize = 195;
		
		this.slotPos = ContainerCustomInventoryCreative.slotPositions;
	}
	
	public static void resetGui()
	{
		windowSize = new GuiSize(195, 136);
		playerDisplayPos = new GuiPos(28, 5);
		binSlotPos = new GuiPos(173, 112);
	}
	
	// Only width set is possible in creative inv
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
	
	public static void addObject(InventoryObject object)
	{
		objects.add(object);
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
	protected void handleMouseClick(Slot slot, int slotID, int var1, int var2)
	{
		this.mouseClicked = true;
		boolean flag = var2 == 1;
		var2 = slotID == -999 && var2 == 0 ? 4 : var2;
		ItemStack itemstack;
		InventoryPlayer inventoryplayer;
		
		if (slot == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && var2 != 5)
		{
			inventoryplayer = this.player.inventory;
			
			if (inventoryplayer.getItemStack() != null)
			{
				if (var1 == 0)
				{
					this.mc.thePlayer.dropPlayerItem(inventoryplayer.getItemStack());
					this.mc.playerController.func_78752_a(inventoryplayer.getItemStack());
					inventoryplayer.setItemStack((ItemStack) null);
				}
				
				if (var1 == 1)
				{
					itemstack = inventoryplayer.getItemStack().splitStack(1);
					this.mc.thePlayer.dropPlayerItem(itemstack);
					this.mc.playerController.func_78752_a(itemstack);
					
					if (inventoryplayer.getItemStack().stackSize == 0)
					{
						inventoryplayer.setItemStack((ItemStack) null);
					}
				}
			}
		}
		else
		{
			int l;
			
			if (slot == this.binSlot && flag)
			{
				for (l = 0; l < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++l)
				{
					this.mc.playerController.sendSlotPacket((ItemStack) null, l);
				}
			}
			else
			{
				ItemStack itemstack1;
				
				if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
				{
					if (slot == this.binSlot)
					{
						this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
					}
					else if (var2 == 4 && slot != null && slot.getHasStack())
					{
						itemstack1 = slot.decrStackSize(var1 == 0 ? 1 : slot.getStack().getMaxStackSize());
						this.mc.thePlayer.dropPlayerItem(itemstack1);
						this.mc.playerController.func_78752_a(itemstack1);
					}
					else if (var2 == 4 && this.mc.thePlayer.inventory.getItemStack() != null)
					{
						this.mc.thePlayer.dropPlayerItem(this.mc.thePlayer.inventory.getItemStack());
						this.mc.playerController.func_78752_a(this.mc.thePlayer.inventory.getItemStack());
						this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
					}
					else
					{
						Container container = this.mc.thePlayer.inventoryContainer;
						container.slotClick(slot == null ? slotID : ((SlotCustomCreativeInventory) slot).getSlot().slotNumber, var1, var2, this.mc.thePlayer);
						container.detectAndSendChanges();
					}
				}
				else if (var2 != 5 && slot.inventory == inventory)
				{
					inventoryplayer = this.mc.thePlayer.inventory;
					itemstack = inventoryplayer.getItemStack();
					ItemStack itemstack2 = slot.getStack();
					ItemStack itemstack3;
					
					if (var2 == 2)
					{
						if (itemstack2 != null && var1 >= 0 && var1 < 9)
						{
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							this.mc.thePlayer.inventory.setInventorySlotContents(var1, itemstack3);
							this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
						}
						
						return;
					}
					
					if (var2 == 3)
					{
						if (inventoryplayer.getItemStack() == null && slot.getHasStack())
						{
							itemstack3 = slot.getStack().copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							inventoryplayer.setItemStack(itemstack3);
						}
						
						return;
					}
					
					if (var2 == 4)
					{
						if (itemstack2 != null)
						{
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = var1 == 0 ? 1 : itemstack3.getMaxStackSize();
							this.mc.thePlayer.dropPlayerItem(itemstack3);
							this.mc.playerController.func_78752_a(itemstack3);
						}
						
						return;
					}
					
					if (itemstack != null && itemstack2 != null && itemstack.isItemEqual(itemstack2) && ItemStack.areItemStackTagsEqual(itemstack, itemstack2))
					{
						if (var1 == 0)
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
							inventoryplayer.setItemStack((ItemStack) null);
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
						inventoryplayer.setItemStack((ItemStack) null);
					}
				}
				else
				{
					this.inventorySlots.slotClick(slot == null ? slotID : slot.slotNumber, var1, var2, this.mc.thePlayer);
					
					if (Container.func_94532_c(var1) == 2)
					{
						for (l = 0; l < 9; ++l)
						{
							this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + l).getStack(), 36 + l);
						}
					}
					else if (slot != null)
					{
						itemstack1 = this.inventorySlots.getSlot(slot.slotNumber).getStack();
						this.mc.playerController.sendSlotPacket(itemstack1, slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
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
			this.creativeCrafting = new CreativeCrafting(this.mc);
			this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.creativeCrafting);
			int tabCount = CreativeTabs.creativeTabArray.length;
			if (tabCount > 12)
			{
				buttonList.add(new GuiButton(101, guiLeft, guiTop - 50, 20, 20, "<"));
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
	 * Called when the screen is unloaded. Used to disable keyboard repeat
	 * events
	 */
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
		{
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.creativeCrafting);
		}
		
		Keyboard.enableRepeatEvents(false);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char c, int key)
	{
		if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
		{
			if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
			{
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			}
			else
			{
				super.keyTyped(c, key);
			}
		}
		else
		{
			if (this.mouseClicked)
			{
				this.mouseClicked = false;
				this.searchField.setText("");
			}
			
			if (!this.checkHotbarKeys(key))
			{
				if (this.searchField.textboxKeyTyped(c, key))
				{
					this.updateCreativeSearch();
				}
				else
				{
					super.keyTyped(c, key);
				}
			}
		}
	}
	
	private void updateCreativeSearch()
	{
		ContainerCreativeList creativeList = (ContainerCreativeList) this.inventorySlots;
		creativeList.itemList.clear();
		Item[] itemList = Item.itemsList;
		int i = itemList.length;
		int j;
		
		for (j = 0; j < i; ++j)
		{
			Item item = itemList[j];
			
			if (item != null && item.getCreativeTab() != null)
			{
				item.getSubItems(item.itemID, (CreativeTabs) null, creativeList.itemList);
			}
		}
		
		Enchantment[] enchantmentList = Enchantment.enchantmentsList;
		i = enchantmentList.length;
		
		for (j = 0; j < i; ++j)
		{
			Enchantment enchantment = enchantmentList[j];
			
			if (enchantment != null && enchantment.type != null)
			{
				Item.enchantedBook.func_92113_a(enchantment, creativeList.itemList);
			}
		}
		
		Iterator iterator = creativeList.itemList.iterator();
		String s = this.searchField.getText().toLowerCase();
		
		while (iterator.hasNext())
		{
			ItemStack itemstack = (ItemStack) iterator.next();
			boolean flag = false;
			Iterator iterator1 = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();
			
			while (true)
			{
				if (iterator1.hasNext())
				{
					String s1 = (String) iterator1.next();
					
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
		creativeList.scrollTo(0.0F);
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		
		if (creativetabs != null && creativetabs.drawInForegroundOfTab())
		{
			this.fontRenderer.drawString(I18n.getString(creativetabs.getTranslatedTabLabel()), 8, 6, 4210752);
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		if (button == 0)
		{
			int l = x - this.guiLeft;
			int i1 = y - this.guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;
			
			for (int k1 = 0; k1 < j1; ++k1)
			{
				CreativeTabs creativetabs = acreativetabs[k1];
				
				if (this.isMouseHoveringTab(creativetabs, l, i1))
				{
					return;
				}
			}
		}
		
		super.mouseClicked(x, y, button);
	}
	
	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@Override
	protected void mouseMovedOrUp(int x, int y, int button)
	{
		if (button == 0)
		{
			int l = x - this.guiLeft;
			int i1 = y - this.guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;
			
			for (int k1 = 0; k1 < j1; ++k1)
			{
				CreativeTabs creativetabs = acreativetabs[k1];
				
				if (creativetabs != null && isMouseHoveringTab(creativetabs, l, i1))
				{
					this.setCurrentCreativeTab(creativetabs);
					return;
				}
			}
		}
		
		super.mouseMovedOrUp(x, y, button);
	}
	
	/**
	 * returns (if you are not on the inventoryTab) and (the flag isn't set)
	 * and( you have more than 1 page of items)
	 */
	private boolean needsScrollBars()
	{
		if (CreativeTabs.creativeTabArray[selectedTabIndex] == null)
			return false;
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreativeList) this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}
	
	private void setCurrentCreativeTab(CreativeTabs creativeTab)
	{
		if (creativeTab == null)
			return;
		
		int i = selectedTabIndex;
		selectedTabIndex = creativeTab.getTabIndex();
		ContainerCreativeList creativeList = (ContainerCreativeList) this.inventorySlots;
		this.field_94077_p.clear();
		creativeList.itemList.clear();
		creativeTab.displayAllReleventItems(creativeList.itemList);
		
		if (creativeTab == CreativeTabs.tabInventory)
		{
			Container container = this.inventoryCreativeContainer;
			
			if (this.backupContainerSlots == null)
			{
				this.backupContainerSlots = inventorySlots.inventorySlots;
			}
			
			this.inventorySlots.inventorySlots = new ArrayList();
			
			for (int j = 0; j < container.inventorySlots.size(); ++j)
			{
				if (container.inventorySlots.get(j) != null)
				{
					SlotCustomCreativeInventory slotcreativeinventory = new SlotCustomCreativeInventory(this, (Slot) container.inventorySlots.get(j), j);
					inventorySlots.inventorySlots.add(slotcreativeinventory);
				}
			}
			
			this.binSlot = new Slot(inventory, 0, GuiCustomInventoryCreative.binSlotPos.getX(), GuiCustomInventoryCreative.binSlotPos.getY());
			inventorySlots.inventorySlots.add(this.binSlot);
		}
		else if (i == CreativeTabs.tabInventory.getTabIndex())
		{
			creativeList.inventorySlots = this.backupContainerSlots;
			this.backupContainerSlots = null;
		}
		
		if (this.searchField != null)
		{
			if (creativeTab == CreativeTabs.tabAllSearch)
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
			int j = ((ContainerCreativeList) this.inventorySlots).itemList.size() / 9 - 5 + 1;
			
			if (i > 0)
			{
				i = 1;
			}
			
			if (i < 0)
			{
				i = -1;
			}
			
			this.currentScroll = (float) (this.currentScroll - (double) i / (double) j);
			
			if (this.currentScroll < 0.0F)
			{
				this.currentScroll = 0.0F;
			}
			
			if (this.currentScroll > 1.0F)
			{
				this.currentScroll = 1.0F;
			}
			
			((ContainerCreativeList) this.inventorySlots).scrollTo(this.currentScroll);
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTickTime)
	{
		boolean flag = Mouse.isButtonDown(0);
		int k = this.guiLeft;
		int l = this.guiTop;
		int i1 = k + 175;
		int j1 = l + 18;
		int k1 = i1 + 14;
		int l1 = j1 + 112;
		
		if (!this.wasClicking && flag && mouseX >= i1 && mouseY >= j1 && mouseX < k1 && mouseY < l1)
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
			this.currentScroll = (mouseY - j1 - 7.5F) / (l1 - j1 - 15.0F);
			
			if (this.currentScroll < 0.0F)
			{
				this.currentScroll = 0.0F;
			}
			
			if (this.currentScroll > 1.0F)
			{
				this.currentScroll = 1.0F;
			}
			
			((ContainerCreativeList) this.inventorySlots).scrollTo(this.currentScroll);
		}
		
		if (maxPages != 0)
		{
			GL11.glColor3f(1F, 1F, 1F);
			String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
			int width = fontRenderer.getStringWidth(page);
			GL11.glDisable(GL11.GL_LIGHTING);
			this.zLevel = 300.0F;
			itemRenderer.zLevel = 300.0F;
			fontRenderer.drawString(page, guiLeft + (xSize / 2) - (width / 2), guiTop - 44, -1);
			this.zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}
		
		super.drawScreen(mouseX, mouseY, partialTickTime);
		CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
		int start = tabPage * 10;
		int i2 = Math.min(acreativetabs.length, ((tabPage + 1) * 10) + 2);
		if (tabPage != 0)
			start += 2;
		boolean rendered = false;
		
		for (int j2 = start; j2 < i2; ++j2)
		{
			CreativeTabs creativetabs = acreativetabs[j2];
			
			if (creativetabs != null && this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY))
			{
				rendered = true;
				break;
			}
		}
		
		if (!rendered && !renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, mouseX, mouseY))
		{
			renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, mouseX, mouseY);
		}
		
		if (this.binSlot != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.binSlot.xDisplayPosition, this.binSlot.yDisplayPosition, 16, 16, mouseX, mouseY))
		{
			this.drawCreativeTabHoveringText(I18n.getString("inventory.binSlot"), mouseX, mouseY);
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	public void drawInventoryObjects()
	{
		for (InventoryObject object : objects)
		{
			if (object != null)
				object.render(this.width, this.height);
		}
	}
	
	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY)
	{
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
		int k = acreativetabs.length;
		int l;
		
		int start = tabPage * 10;
		k = Math.min(acreativetabs.length, ((tabPage + 1) * 10 + 2));
		if (tabPage != 0)
			start += 2;
		
		for (l = start; l < k; ++l)
		{
			CreativeTabs creativetabs1 = acreativetabs[l];
			this.mc.renderEngine.bindTexture(tabsResource);
			
			if (creativetabs1 != null && creativetabs1.getTabIndex() != selectedTabIndex)
			{
				this.renderCreativeTab(creativetabs1);
			}
		}
		
		if (tabPage != 0)
		{
			if (creativetabs != CreativeTabs.tabAllSearch)
			{
				this.mc.renderEngine.bindTexture(tabsResource);
				renderCreativeTab(CreativeTabs.tabAllSearch);
			}
			if (creativetabs != CreativeTabs.tabInventory)
			{
				this.mc.renderEngine.bindTexture(tabsResource);
				renderCreativeTab(CreativeTabs.tabInventory);
			}
		}
		
		if (creativetabs == CreativeTabs.tabInventory)
		{
			this.renderInventoryTab();
		}
		else
		{
			this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
			this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		}
		this.searchField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i1 = this.guiLeft + 175;
		k = this.guiTop + 18;
		l = k + 112;
		this.mc.renderEngine.bindTexture(tabsResource);
		
		if (creativetabs != null && creativetabs.shouldHidePlayerInventory())
		{
			this.drawTexturedModalRect(i1, k + (int) ((l - k - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}
		
		if (creativetabs == null || creativetabs.getTabPage() != tabPage)
		{
			if (creativetabs != CreativeTabs.tabAllSearch && creativetabs != CreativeTabs.tabInventory)
			{
				return;
			}
		}
		
		this.renderCreativeTab(creativetabs);
		
		this.drawInventoryObjects();
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
		GuiCustomInventorySurvival.drawPlayerOnGui(this.mc, var1 + GuiCustomInventoryCreative.playerDisplayPos.getX() + 16, var2 + GuiCustomInventoryCreative.playerDisplayPos.getY() + 41, 20, var1 + GuiCustomInventoryCreative.playerDisplayPos.getX() + 16 - this.mouseX, var2 + GuiCustomInventoryCreative.playerDisplayPos.getY() + 10 - this.mouseY);
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	public void drawBackgroundFrame(int posX, int posY, int sizeX, int sizeY)
	{
		this.mc.renderEngine.bindTexture(custominventory);
		
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
		
		// Edges
		this.drawTexturedModalRect(posX, posY, 0, 0, 8, 8);
		this.drawTexturedModalRect(posX + sizeX - 8, posY, 8, 0, 8, 8);
		this.drawTexturedModalRect(posX, posY + sizeY - 8, 0, 8, 8, 8);
		this.drawTexturedModalRect(posX + sizeX - 8, posY + sizeY - 8, 8, 8, 8, 8);
	}
	
	public void drawPlayerBackground(int posX, int posY)
	{
		this.mc.renderEngine.bindTexture(custominventory);
		this.drawTexturedModalRect(posX, posY, 54, 18, 34, 45);
	}
	
	public void drawSlot(int posX, int posY, boolean isBinSlot)
	{
		this.mc.renderEngine.bindTexture(custominventory);
		if (!isBinSlot)
			this.drawTexturedModalRect(posX - 1, posY - 1, 16, 0, 18, 18);
		else
			this.drawTexturedModalRect(posX - 1, posY - 1, 54, 0, 18, 18);
	}
	
	protected boolean isMouseHoveringTab(CreativeTabs tab, int mouseX, int mouseY)
	{
		if (tab.getTabPage() != tabPage)
		{
			if (tab != CreativeTabs.tabAllSearch && tab != CreativeTabs.tabInventory)
			{
				return false;
			}
		}
		
		int k = tab.getTabColumn();
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
		
		if (tab.isTabInFirstRow())
		{
			i1 = b0 - 32;
		}
		else
		{
			i1 = b0 + this.ySize;
		}
		
		return mouseX >= l && mouseX <= l + 28 && mouseY >= i1 && mouseY <= i1 + 32;
	}
	
	/**
	 * Renders the creative inventory hovering text if mouse is over it. Returns
	 * true if did render or false otherwise. Params: current creative tab to be
	 * checked, current mouse x position, current mouse y position.
	 */
	protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int x, int y)
	{
		int k = tab.getTabColumn();
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
		
		if (tab.isTabInFirstRow())
		{
			i1 = b0 - 32;
		}
		else
		{
			i1 = b0 + this.ySize;
		}
		
		if (this.isPointInRegion(l + 3, i1 + 3, 23, 27, x, y))
		{
			this.drawCreativeTabHoveringText(I18n.getString(tab.getTranslatedTabLabel()), x, y);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void drawItemStackTooltip(ItemStack stack, int x, int y)
	{
		if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
		{
			List list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
			CreativeTabs creativetabs = stack.getItem().getCreativeTab();
			
			if (creativetabs == null && stack.itemID == Item.enchantedBook.itemID)
			{
				Map map = EnchantmentHelper.getEnchantments(stack);
				
				if (map.size() == 1)
				{
					Enchantment enchantment = Enchantment.enchantmentsList[((Integer) map.keySet().iterator().next()).intValue()];
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
				list.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.getString(creativetabs.getTranslatedTabLabel()));
			}
			
			for (int i1 = 0; i1 < list.size(); ++i1)
			{
				if (i1 == 0)
				{
					list.set(i1, "\u00a7" + Integer.toHexString(stack.getRarity().rarityColor) + (String) list.get(i1));
				}
				else
				{
					list.set(i1, EnumChatFormatting.GRAY + (String) list.get(i1));
				}
			}
			
			this.func_102021_a(list, x, y);
		}
		else
		{
			super.drawItemStackTooltip(stack, x, y);
		}
	}
	
	/**
	 * Renders passed creative inventory tab into the screen.
	 */
	protected void renderCreativeTab(CreativeTabs tab)
	{
		boolean flag = tab.getTabIndex() == selectedTabIndex;
		boolean flag1 = tab.isTabInFirstRow();
		int i = tab.getTabColumn();
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
		GL11.glColor3f(1F, 1F, 1F); // Forge: Reset color in case Items change
									// it.
		this.drawTexturedModalRect(l, i1, j, k, 28, b0);
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		l += 6;
		i1 += 8 + (flag1 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack itemstack = tab.getIconItemStack();
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
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 101)
		{
			tabPage = Math.max(tabPage - 1, 0);
		}
		else if (button.id == 102)
		{
			tabPage = Math.min(tabPage + 1, maxPages);
		}
		
		if (GuiCustomInventoryCreative.buttons.get(button) != null)
			GuiCustomInventoryCreative.buttons.get(button).onButtonPressed(button);
	}
	
	public int getSelectedTabIndex()
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
