package mod.keycrusader.backitems.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SashimonoModel<T extends LivingEntity> extends BipedModel<T> {
    public ModelRenderer sashimonoSlate;

    public SashimonoModel() {
        super(0.0F);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.sashimonoSlate = new ModelRenderer(this, 0, 0);
        this.sashimonoSlate.addBox(0.0F, -53.75F, -0.5F, 20, 40, 1, 0.0F);
        this.sashimonoSlate.rotateAngleY = degToRad(-90.0F);
        this.sashimonoSlate.rotateAngleX = degToRad(180.0F);
        this.sashimonoSlate.rotateAngleZ = degToRad(-22.5F);
        this.sashimonoSlate.setRotationPoint(0.0F, -11.25F, 10.0F);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int light, int overlay, float red, float green, float blue, float alpha) {
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        this.sashimonoSlate.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
        matrixStack.scale(2.5F, 2.5F, 2.5F);
    }

    protected float degToRad(float degrees)
    {
        return degrees * (float)Math.PI / 180;
    }
}

