package clashsoft.playerinventoryapi.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import clashsoft.playerinventoryapi.api.IButtonHandler;
import clashsoft.playerinventoryapi.api.inventorycomponents.InventoryObject;
import clashsoft.playerinventoryapi.inventory.ContainerCreativeList;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import clashsoft.playerinventoryapi.inventory.ContainerCustomInventorySurvival;
import clashsoft.playerinventoryapi.lib.GuiHelper.GuiPos;
import clashsoft.playerinventoryapi.lib.GuiHelper.GuiSize;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StatCollector;

public class GuiCustomInventorySurvival extends InventoryEffectRenderer
{
	/**
	 * x size of the inventory window in pixels. Defined as float, passed as int
	 */
	private float									xSize_lo;
	
	/**
	 * y size of the inventory window in pixels. Defined as float, passed as
	 * int.
	 */
	private float									ySize_lo;
	
	private final EntityPlayer						player;
	
	private static GuiSize							windowSize				= new GuiSize(176, 166);
	private static GuiPos							playerDisplayPos		= new GuiPos(25, 7);
	private static GuiPos							craftingArrowPos		= new GuiPos(125, 37);
	private static float							craftingArrowRotation	= 0F;
	private static boolean							drawCraftingLabel		= true;
	
	private final GuiPos[]							slotPositions;
	
	private static Map<GuiButton, IButtonHandler>	buttons					= new HashMap<GuiButton, IButtonHandler>();
	
	private static List<InventoryObject>			objects					= new ArrayList<InventoryObject>();
	
	public GuiCustomInventorySurvival(EntityPlayer par1EntityPlayer, ContainerCustomInventorySurvival par2ContainerCustomInventorySurvival)
	{
		super(par2ContainerCustomInventorySurvival);
		
		this.allowUserInput = true;
		player = par1EntityPlayer;
		par1EntityPlayer.addStat(AchievementList.openInventory, 1);
		
		this.slotPositions = ContainerCustomInventorySurvival.slotPositions;
	}
	
	public static void setWindowSize(int width, int height)
	{
		windowSize = new GuiSize(width, height);
	}
	
	public static void setPlayerDisplayPos(int x, int y)
	{
		playerDisplayPos = new GuiPos(x, y);
	}
	
	public static void setCraftArrowPos(int x, int y)
	{
		craftingArrowPos = new GuiPos(x, y);
	}
	
	public static void setCraftArrowRot(float r)
	{
		craftingArrowRotation = r;
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
		if (this.mc.playerController.isInCreativeMode())
		{
			this.mc.displayGuiScreen(new GuiCustomInventoryCreative(this.player, new ContainerCreativeList(player), new ContainerCustomInventoryCreative(player.inventory, false, player)));
		}
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		this.buttonList.clear();
		for (GuiButton button : buttons.keySet())
		{
			this.buttonList.add(button);
		}
		
		if (this.mc.playerController.isInCreativeMode())
		{
			this.mc.displayGuiScreen(new GuiCustomInventoryCreative(this.player, new ContainerCreativeList(player), new ContainerCustomInventoryCreative(player.inventory, false, player)));
		}
		else
		{
			super.initGui();
		}
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		if (drawCraftingLabel)
		{
			int x = this.slotPositions[1].getX();
			int y = this.slotPositions[1].getY();
			this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), x - 1, y - 10, 4210752);
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		this.xSize_lo = par1;
		this.ySize_lo = par2;
	}
	
	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GuiCustomInventoryCreative.custominventory);
		int k = (this.width - 176 + (!this.mc.thePlayer.getActivePotionEffects().isEmpty() ? 120 : 0)) / 2;
		int l = (this.height - 166) / 2;
		int m = (this.width - windowSize.getWidth() + (!this.mc.thePlayer.getActivePotionEffects().isEmpty() ? 120 : 0)) / 2;
		this.drawBackgroundFrame(m, (this.height - windowSize.getHeight()) / 2, windowSize.getWidth(), windowSize.getHeight());
		GL11.glTranslatef(k, l, 0);
		this.drawCraftArrow(craftingArrowPos.getX(), craftingArrowPos.getY(), craftingArrowRotation);
		this.drawPlayerBackground(playerDisplayPos.getX(), playerDisplayPos.getY());
		for (GuiPos pos : slotPositions)
		{
			if (pos != null)
				this.drawSlot(pos.getX(), pos.getY());
		}
		for (InventoryObject object : objects)
		{
			if (object != null)
				object.render(this.width, this.height);
		}
		GL11.glTranslatef(-k, -l, 0);
		
		drawPlayerOnGui(this.mc, k + GuiCustomInventorySurvival.playerDisplayPos.getX() + 26, l + GuiCustomInventorySurvival.playerDisplayPos.getY() + 65, 30, k + GuiCustomInventorySurvival.playerDisplayPos.getX() + 26 - this.xSize_lo, l + GuiCustomInventorySurvival.playerDisplayPos.getY() + 65 - 50 - this.ySize_lo);
	}
	
	public void drawBackgroundFrame(int posX, int posY, int sizeX, int sizeY)
	{
		this.mc.renderEngine.bindTexture(GuiCustomInventoryCreative.custominventory);
		
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
		this.mc.renderEngine.bindTexture(GuiCustomInventoryCreative.custominventory);
		this.drawTexturedModalRect(posX, posY, 0, 19, 54, 72);
	}
	
	public void drawCraftArrow(int posX, int posY, float rotation)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(posX + 8, posY + 7, 0F);
		GL11.glRotatef(craftingArrowRotation, 0, 0, 1);
		GL11.glTranslatef(-8F, -7F, 0F);
		this.drawTexturedModalRect(0, 0, 34, 0, 16, 13);
		GL11.glPopMatrix();
	}
	
	public void drawSlot(int posX, int posY)
	{
		this.mc.renderEngine.bindTexture(GuiCustomInventoryCreative.custominventory);
		this.drawTexturedModalRect(posX - 1, posY - 1, 16, 0, 18, 18);
	}
	
	public static void drawPlayerOnGui(Minecraft mc, int par1, int par2, int par3, float par4, float par5)
	{
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(par1, par2, 50.0F);
		GL11.glScalef(-par3, par3, par3);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = mc.thePlayer.renderYawOffset;
		float f3 = mc.thePlayer.rotationYaw;
		float f4 = mc.thePlayer.rotationPitch;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float) Math.atan(par5 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		
		mc.thePlayer.renderYawOffset = (float) Math.atan(par4 / 40.0F) * 20.0F;
		mc.thePlayer.rotationYaw = (float) Math.atan(par4 / 40.0F) * 40.0F;
		mc.thePlayer.rotationPitch = -((float) Math.atan(par5 / 40.0F)) * 20.0F;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			mc.thePlayer.renderYawOffset += 180;
			mc.thePlayer.rotationYaw += 180;
			mc.thePlayer.rotationPitch = (-mc.thePlayer.rotationPitch);
		}
		
		mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw;
		GL11.glTranslatef(0.0F, mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		mc.thePlayer.renderYawOffset = f2;
		mc.thePlayer.rotationYaw = f3;
		mc.thePlayer.rotationPitch = f4;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
	
	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		GuiCustomInventorySurvival.buttons.get(par1GuiButton).onButtonPressed(par1GuiButton);
	}
	
	@Override
	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
	{
		boolean flag = par4 == 1;
		par4 = par2 == -999 && par4 == 0 ? 4 : par4;
		ItemStack itemstack;
		InventoryPlayer inventoryplayer;
		
		int l;
		ItemStack itemstack1;
		
		if (par4 == 4 && par1Slot != null && par1Slot.getHasStack())
		{
			itemstack1 = par1Slot.decrStackSize(par3 == 0 ? 1 : par1Slot.getStack().getMaxStackSize());
			this.mc.thePlayer.dropPlayerItem(itemstack1);
			this.mc.playerController.func_78752_a(itemstack1);
			this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
		}
		else if (par4 == 4 && this.mc.thePlayer.inventory.getItemStack() != null)
		{
			this.mc.thePlayer.dropPlayerItem(this.mc.thePlayer.inventory.getItemStack());
			this.mc.playerController.func_78752_a(this.mc.thePlayer.inventory.getItemStack());
			this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
			this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
		}
		else
		{
			Container container = this.mc.thePlayer.inventoryContainer;
			container.slotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
			container.detectAndSendChanges();
		}
		
	}
}
