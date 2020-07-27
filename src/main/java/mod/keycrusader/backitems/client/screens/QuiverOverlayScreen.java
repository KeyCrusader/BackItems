package mod.keycrusader.backitems.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.containers.QuiverContainer;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class QuiverOverlayScreen extends AbstractGui {
    private final ResourceLocation GUI = new ResourceLocation("textures/gui/widgets.png");
    private final Minecraft mc = Minecraft.getInstance();

    public void render() {
        Entity player = this.getRenderViewPlayer();
        if (player != null) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();

            TextureManager textureManager = mc.getTextureManager();
            int scaledWidth = mc.getMainWindow().getScaledWidth() / 2;

            textureManager.bindTexture(GUI);

            // Multiple texture draws to use vanilla textures
            this.blit(scaledWidth - 51, 0, 0, 0, 101, 22);
            this.blit(scaledWidth + 50, 0, 0, 0, 1, 22);
            this.blit(scaledWidth - 52 + (player.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null).getArrowSelectedIndex() * 20), 1, 0, 24, 24, 22);

            RenderSystem.enableRescaleNormal();
            RenderHelper.enableStandardItemLighting();
            RenderSystem.defaultBlendFunc();

            ItemStack backItem = Helpers.getBackItem((LivingEntity) player);
            IItemHandler backItemInventory = Helpers.getBackInventory(backItem);
            for (int slot = 0; slot < backItemInventory.getSlots(); slot++) {
                ItemStack item = backItemInventory.getStackInSlot(slot);
                if (!item.isEmpty()) {
                    mc.getItemRenderer().renderItemAndEffectIntoGUI(item, scaledWidth - 48 + (slot * 20), 3);
                    mc.getItemRenderer().renderItemOverlays(mc.getFontResourceManager().getFontRenderer(Minecraft.DEFAULT_FONT_RENDERER_NAME), item, scaledWidth - 48 + (slot * 20), 3);
                }
            }

            RenderHelper.disableStandardItemLighting();
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
        }

    }

    private PlayerEntity getRenderViewPlayer() {
        return !(this.mc.getRenderViewEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.mc.getRenderViewEntity();
    }
}
