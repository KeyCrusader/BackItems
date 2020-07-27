package mod.keycrusader.backitems.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderHandler {
    public static void renderColouredModel(IBakedModel model, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int colour, int light) {
        IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(Atlases.getCutoutBlockType());
        renderModel(model, ItemStack.EMPTY, colour, light, 0 | 10 << 16, matrixStack, vertexBuilder);
    }

    private static void renderModel(IBakedModel model, ItemStack stack, int colour, int lightTexture, int overlayTexture, MatrixStack matrixStack, IVertexBuilder vertexBuilder)
    {
        Random random = new Random();
        random.setSeed(42L);

        renderQuads(matrixStack, vertexBuilder, model.getQuads(null, null, random, null), stack, colour, lightTexture, overlayTexture);
    }

    private static void renderQuads(MatrixStack matrixStack, IVertexBuilder vertexBuilder, List<BakedQuad> quads, ItemStack stack, int colour, int lightTexture, int overlayTexture)
    {
        boolean useItemColor = !stack.isEmpty() && colour == -1;
        MatrixStack.Entry entry = matrixStack.getLast();
        for(BakedQuad quad : quads)
        {
            int tintColor = 0xFFFFFF;
            if(quad.hasTintIndex())
            {
                if(useItemColor)
                {
                    tintColor = Minecraft.getInstance().getItemColors().getColor(stack, quad.getTintIndex());
                }
                else
                {
                    tintColor = colour;
                }
            }
            float red = (float) (tintColor >> 16 & 255) / 255.0F;
            float green = (float) (tintColor >> 8 & 255) / 255.0F;
            float blue = (float) (tintColor & 255) / 255.0F;
            vertexBuilder.addVertexData(entry, quad, red, green, blue, lightTexture, overlayTexture, false);
        }
    }

    public static void alignToBack(MatrixStack matrixStack, LivingEntity livingEntity) {
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));

        if (livingEntity.isSneaking() && !(((PlayerEntity) livingEntity).abilities.isFlying) && !Helpers.isUsingParachute(livingEntity)) {
            matrixStack.translate(0F, -0.2F, 0F);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-28.647888F));
        }
        // If the entity is wearing chest armour, offset the models by 1.0F (overlap size) * scale
        if (!livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
            matrixStack.translate(0.0F, 0.0F, 0.0625F);
        }
    }
}
