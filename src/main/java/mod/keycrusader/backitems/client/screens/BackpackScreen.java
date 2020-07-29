package mod.keycrusader.backitems.client.screens;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.containers.BackpackContainer;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosAPI;

@OnlyIn(Dist.CLIENT)
public class BackpackScreen extends ContainerScreen<BackpackContainer> {
    private final ResourceLocation GUI = new ResourceLocation(BackItems.MODID, "textures/gui/container/backpack.png");
    private int rowCount;

    public BackpackScreen(BackpackContainer screenContainer, PlayerInventory inventoryPlayer, ITextComponent titleIn) {
        super(screenContainer, inventoryPlayer, titleIn);

        this.rowCount = EnchantmentHelper.getEnchantmentLevel(RegistryHandler.STORAGE_ENCHANTMENT.get(), Helpers.getBackItem(inventoryPlayer.player))+1;
        this.xSize = 176;
        this.ySize = 114+(this.rowCount*18);
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
        this.blit(xStartGui, yStartGui, 0, 0, this.xSize, 17+(18*this.rowCount));
        this.blit(xStartGui, yStartGui+17+(18*this.rowCount), 0, 125, this.xSize, 97);
    }
}
