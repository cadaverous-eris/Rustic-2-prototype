package rustic.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import rustic.Rustic;
import rustic.container.ContainerItemStorage;
import rustic.tileentity.TileItemStorage;

public class GuiItemStorage extends GuiContainer {

	private static final ResourceLocation background = new ResourceLocation(Rustic.MODID, "textures/gui/container_flexible.png");
	
	private TileItemStorage te;
	private InventoryPlayer playerInv;
	
	public GuiItemStorage(ContainerItemStorage container, InventoryPlayer playerInv) {
		super(container);
		
		this.playerInv = playerInv;
		this.te = container.getTile();
		this.xSize = 176;
		this.ySize = 112 + (te.getRows() * 18);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		
		int teRows = te.getRows();
		int yOffset = 0;
		
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 16);
		yOffset += 16;
		drawTexturedModalRect(guiLeft, guiTop + yOffset, 0, yOffset, xSize, teRows * 18);
		yOffset += teRows * 18;
		drawTexturedModalRect(guiLeft, guiTop + yOffset, 0, 160, xSize, 96);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(te.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 3, 4210752);
    }

}
