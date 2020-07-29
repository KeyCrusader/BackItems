package mod.keycrusader.backitems.common.capability;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;


public class CurioBackpack implements ICurio {
    private final ItemStack stack;

    public CurioBackpack(ItemStack stack) {
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
    public boolean hasRender(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Override
    public void render(String identifier, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.push();

        RenderHandler.alignToBack(matrixStack, livingEntity);
        int colour = ((IDyeableArmorItem) this.stack.getItem()).getColor(this.stack);

        int level = EnchantmentHelper.getEnchantmentLevel(RegistryHandler.STORAGE_ENCHANTMENT.get(), this.stack);

        for (IBakedModel model : ModelHandler.BACKPACK.getModel(level)) {
            RenderHandler.renderColouredModel(model, matrixStack, renderTypeBuffer, colour, light);
        }
        matrixStack.pop();
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity) {
        return !Helpers.doesInventoryHaveItems(this.stack);
    }
}
