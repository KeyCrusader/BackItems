package mod.keycrusader.backitems.common.capability;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import top.theillusivec4.curios.api.capability.ICurio;

public class CurioParachute implements ICurio {
    private final ItemStack stack;

    public CurioParachute(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean canRightClickEquip() {
        return true;
    }

    @Override
    public void playEquipSound(LivingEntity entityLivingBase) {
        entityLivingBase.world.playSound(null, entityLivingBase.getPosition(),
                SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.NEUTRAL,
                1.0F, 1.0F);
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return true;
        PlayerEntity playerEntity = (PlayerEntity) livingEntity;

        return !Helpers.isUsingParachute(playerEntity);
    }

    @Override
    public void onUnequipped(String identifier, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) livingEntity;

        if (!livingEntity.world.isRemote && Helpers.isUsingParachute(playerEntity)) {
            Helpers.setUsingParachute(playerEntity, false);
        }
    }

    @Override
    public boolean hasRender(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Override
    public void render(String identifier, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) livingEntity;

        matrixStack.push();

        RenderHandler.alignToBack(matrixStack, playerEntity);
        int colour = ((IDyeableArmorItem) this.stack.getItem()).getColor(this.stack);

        if (Helpers.isUsingParachute(playerEntity)) {
            RenderHandler.renderColouredModel(ModelHandler.CHUTE.getModel(1).get(0), matrixStack, renderTypeBuffer, 16777215, light);
            RenderHandler.renderColouredModel(ModelHandler.CHUTE.getModel(1).get(1), matrixStack, renderTypeBuffer, colour, light);
        } else {
            RenderHandler.renderColouredModel(ModelHandler.PARACHUTE.getModel(), matrixStack, renderTypeBuffer, colour, light);
        }

        matrixStack.pop();
    }
}
