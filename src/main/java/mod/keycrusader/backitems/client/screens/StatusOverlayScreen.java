package mod.keycrusader.backitems.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class StatusOverlayScreen extends AbstractGui {
    private final ResourceLocation STATUS_ICONS = new ResourceLocation(BackItems.MODID, "textures/gui/status_icons.png");
    private final Minecraft mc = Minecraft.getInstance();

    public void render() {
        PlayerEntity player = this.getRenderViewPlayer();
        if (player != null) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();

            TextureManager textureManager = mc.getTextureManager();
            int scaledWidth = mc.getMainWindow().getScaledWidth();
            int scaledHeight = mc.getMainWindow().getScaledHeight();

            textureManager.bindTexture(STATUS_ICONS);
            if (player.isElytraFlying()) {
                this.blit(scaledWidth - 32, (scaledHeight/2) - 16,96, 0, 32, 32);
            }
            else if (Helpers.isUsingParachute(player)) {
                this.blit(scaledWidth - 32, (scaledHeight/2) - 16,32, 0, 32, 32);
            }
            else if (player.fallDistance > 3) {
                this.blit(scaledWidth - 32, (scaledHeight/2) - 16,64, 0, 32, 32);
            }

        }

    }

    private PlayerEntity getRenderViewPlayer() {
        return !(this.mc.getRenderViewEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.mc.getRenderViewEntity();
    }
}
