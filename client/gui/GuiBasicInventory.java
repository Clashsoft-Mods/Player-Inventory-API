package clashsoft.playerinventoryapi.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import clashsoft.cslib.minecraft.client.gui.GuiBuilder;
import clashsoft.cslib.util.CSString;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.StatCollector;

public abstract class GuiBasicInventory extends GuiContainer
{
	protected final GuiBuilder			guiBuilder;
	
	private boolean						wasEmpty	= false;
	private Collection<PotionEffect>	effects;
	
	public GuiBasicInventory(Container container)
	{
		super(container);
		this.guiBuilder = new GuiBuilder(this);
	}
	
	public void drawBackgroundFrame(int posX, int posY, int width, int height)
	{
		this.guiBuilder.drawFrame(posX, posY, width, height);
	}
	
	public void drawPlayerBackground(int posX, int posY)
	{
		this.guiBuilder.drawPlayerBackgroundL(posX, posY);
	}
	
	public void drawSlot(int posX, int posY)
	{
		this.guiBuilder.drawSlot(posX, posY);
	}
	
	public void drawPlayerHoveringText(EntityPlayer player, int x, int y, FontRenderer font)
	{
		List<String> list = new ArrayList();
		Team team = player.getTeam();
		
		if (team != null)
		{
			list.add(team.formatString(player.getDisplayName()));
			list.add(team.getRegisteredName());
		}
		else
		{
			list.add(player.getDisplayName());
		}
		
		this.drawHoveringText(list, x - this.guiLeft, y - this.guiTop, font);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		this.effects = this.mc.thePlayer.getActivePotionEffects();
		boolean isEmpty = this.effects.isEmpty();
		if (isEmpty)
		{
			if (!this.wasEmpty)
			{
				this.initGui();
				return;
			}
		}
		else
		{
			if (this.wasEmpty)
			{
				this.initGui();
				return;
			}
		}
		this.wasEmpty = isEmpty;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		int i = this.guiLeft - 124;
		int j = this.guiTop;
		
		Collection effects = this.effects;
		
		if (!effects.isEmpty())
		{
			GL11.glDisable(2896);
			
			int l = 33;
			if (effects.size() > 5)
			{
				l = 132 / (effects.size() - 1);
			}
			
			for (Object o : effects)
			{
				PotionEffect effect = (PotionEffect) o;
				Potion localPotion = Potion.potionTypes[effect.getPotionID()];
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.getTextureManager().bindTexture(field_147001_a);
				
				drawTexturedModalRect(i, j, 0, 166, 140, 32);
				
				if (localPotion.hasStatusIcon())
				{
					int i1 = localPotion.getStatusIconIndex();
					drawTexturedModalRect(i + 6, j + 7, (i1 % 8) * 18, 198 + (i1 / 8) * 18, 18, 18);
				}
				
				String text = StatCollector.translateToLocal(localPotion.getName());
				if (effect.getAmplifier() > 0)
				{
					text += " " + CSString.convertToRoman(effect.getAmplifier() + 1);
				}
				
				this.fontRendererObj.drawStringWithShadow(text, i + 10 + 18, j + 6, 16777215);
				
				String str2 = Potion.getDurationString(effect);
				this.fontRendererObj.drawStringWithShadow(str2, i + 10 + 18, j + 6 + 10, 8355711);
				
				j += l;
			}
		}
	}
}
