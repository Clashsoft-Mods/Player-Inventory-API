package com.pocteam.playerinventoryapi.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.pocteam.playerinventoryapi.inventory.ContainerCreativeList;
import com.pocteam.playerinventoryapi.inventory.ContainerCustomInventoryCreative;
import com.pocteam.playerinventoryapi.inventory.ContainerCustomInventorySurvival;
import com.pocteam.playerinventoryapi.inventory.IButtonHandler;
import com.pocteam.playerinventoryapi.lib.GuiHelper.GuiPos;
import com.pocteam.playerinventoryapi.lib.GuiHelper.GuiSize;

public class GuiCustomInventorySurvival extends InventoryEffectRenderer
{
    /**
     * x size of the inventory window in pixels. Defined as float, passed as int
     */
    private float xSize_lo;

    /**
     * y size of the inventory window in pixels. Defined as float, passed as int.
     */
    private float ySize_lo;
    
    private final EntityPlayer player;
    
    private static GuiSize windowSize = new GuiSize(176, 166);
    private static GuiPos playerDisplayPos = new GuiPos(25, 7);
    private static GuiPos craftingArrowPos = new GuiPos(125, 37);
    private static float craftingArrowRotation = 0F;
    private final GuiPos[] slotPos;
    
    private static Map<GuiButton, IButtonHandler> buttons = new HashMap<GuiButton, IButtonHandler>();

    public GuiCustomInventorySurvival(EntityPlayer par1EntityPlayer, ContainerCustomInventorySurvival par2ContainerCustomInventorySurvival)
    {
        super(par1EntityPlayer.inventoryContainer);
        this.allowUserInput = true;
        player = par1EntityPlayer;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
        
        List<Slot> slots = par2ContainerCustomInventorySurvival.inventorySlots;
        slotPos = new GuiPos[slots.size()];
        for (int i = 0; i < slots.size(); i++)
        {
        	if (slots.get(i) != null)
        		slotPos[i] = new GuiPos(slots.get(i).xDisplayPosition, slots.get(i).yDisplayPosition);
        }
    }
    
    public static void setWindowSize(int width, int height) { windowSize = new GuiSize(width, height); }
    public static void setPlayerDisplayPos(int x, int y) { playerDisplayPos = new GuiPos(x, y); }
    public static void setCraftArrowPos(int x, int y) { craftingArrowPos = new GuiPos(x, y); }
    public static void setCraftArrowRot(float r) { craftingArrowRotation = r; }
    public static void addButton(IButtonHandler handler, GuiButton button) { buttons.put(button, handler); }

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
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 86, 16, 4210752);
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
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(GuiCustomInventoryCreative.custominventory);
        int k = (this.width - 176 + (!this.mc.thePlayer.getActivePotionEffects().isEmpty() ? 120 : 0)) / 2;
		int l = (this.height - 166) / 2;
		int m = (this.width - windowSize.getWidth() + (!this.mc.thePlayer.getActivePotionEffects().isEmpty() ? 120 : 0)) / 2;
		this.drawBackgroundFrame(m, (this.height - windowSize.getHeight()) / 2, windowSize.getWidth(), windowSize.getHeight());
        GL11.glTranslatef(k, l, 0);
        this.drawCraftArrow(craftingArrowPos.getX(), craftingArrowPos.getY(), craftingArrowRotation);
        this.drawPlayerBackground(playerDisplayPos.getX(), playerDisplayPos.getY());
        for (GuiPos pos : slotPos)
        {
        	if (pos != null)
        		this.drawSlot(pos.getX(), pos.getY());
        }
        GL11.glTranslatef(-k, -l, 0);
        
        drawPlayerOnGui(this.mc, k + this.playerDisplayPos.getX() + 26, l + this.playerDisplayPos.getY() + 65, 30, k + this.playerDisplayPos.getX() + 26 - this.xSize_lo, l + this.playerDisplayPos.getY() + 65 - 50 - this.ySize_lo);
    }
    
    public void drawBackgroundFrame(int posX, int posY, int sizeX, int sizeY)
    {
    	this.mc.func_110434_K().func_110577_a(GuiCustomInventoryCreative.custominventory);
    	
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
    	this.mc.func_110434_K().func_110577_a(GuiCustomInventoryCreative.custominventory);
    	this.drawTexturedModalRect(posX, posY, 0, 19, 54, 72);
    }
    
    public void drawCraftArrow(int posX, int posY, float rotation)
    {
    	GL11.glRotatef(craftingArrowRotation, 0, 0, 0);
    	this.drawTexturedModalRect(posX, posY, 34, 0, 16, 13);
    	GL11.glRotatef(-craftingArrowRotation, 0, 0, 0);
    }
    
    public void drawSlot(int posX, int posY)
    {
    	this.mc.func_110434_K().func_110577_a(GuiCustomInventoryCreative.custominventory);
    	this.drawTexturedModalRect(posX - 1, posY - 1, 16, 0, 18, 18);
    }

    public static void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef((-par3), par3, par3);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = par0Minecraft.thePlayer.renderYawOffset;
        float f3 = par0Minecraft.thePlayer.rotationYaw;
        float f4 = par0Minecraft.thePlayer.rotationPitch;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan(par5 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        par0Minecraft.thePlayer.renderYawOffset = (float)Math.atan(par4 / 40.0F) * 20.0F;
        par0Minecraft.thePlayer.rotationYaw = (float)Math.atan(par4 / 40.0F) * 40.0F;
        par0Minecraft.thePlayer.rotationPitch = -((float)Math.atan(par5 / 40.0F)) * 20.0F;
        par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
        GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        par0Minecraft.thePlayer.renderYawOffset = f2;
        par0Minecraft.thePlayer.rotationYaw = f3;
        par0Minecraft.thePlayer.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
	protected void actionPerformed(GuiButton par1GuiButton)
    {
        this.buttons.get(par1GuiButton).onButtonPressed(par1GuiButton);
    }
}
