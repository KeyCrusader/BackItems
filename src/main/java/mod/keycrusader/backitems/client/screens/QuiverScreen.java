package mod.keycrusader.backitems.client.screens;

import mod.keycrusader.backitems.common.containers.QuiverContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuiverScreen extends ContainerScreen<QuiverContainer> {
    private final ResourceLocation GUI = new ResourceLocation("textures/gui/container/hopper.png");

    public QuiverScreen(QuiverContainer screenContainer, PlayerInventory inventoryPlayer, ITextComponent titleIn) {
        super(screenContainer, inventoryPlayer, titleIn);

        this.xSize = 176;
        this.ySize = 133;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.textureManager.bindTexture(GUI);
        int xStartGui = (this.width - this.xSize) / 2; // this.width is width of screen - this.xSize is width of current GUI
        int yStartGui = (this.height - this.ySize) / 2; // this.height is height of screen - this.ySize is height of current GUI
        this.blit(xStartGui, yStartGui, 0, 0, this.xSize, this.ySize);
    }
}
