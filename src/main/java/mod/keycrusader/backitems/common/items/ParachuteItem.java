package mod.keycrusader.backitems.common.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ParachuteItem extends Item implements IDyeableArmorItem, IDyeableItem {
    public ParachuteItem() {
        super(new Properties()
                .group(ItemGroup.TRANSPORTATION)
                .maxStackSize(1)
                .rarity(Rarity.COMMON)
        );
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            private final ICurio curio = new ICurio() {
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
                    return !Helpers.isUsingParachute(livingEntity);
                }

                @Override
                public boolean hasRender(String identifier, LivingEntity livingEntity) {
                    return true;
                }

                @Override
                public void render(String identifier, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                    matrixStack.push();

                    RenderHandler.alignToBack(matrixStack, livingEntity);
                    ItemStack stack = Helpers.getItem(livingEntity, identifier);
                    int colour = getColor(stack);

                    if (Helpers.isUsingParachute(livingEntity)) {
                        RenderHandler.renderColouredModel(ModelHandler.CHUTE.getModel(1).get(0), matrixStack, renderTypeBuffer, 16777215, light);
                        RenderHandler.renderColouredModel(ModelHandler.CHUTE.getModel(1).get(1), matrixStack, renderTypeBuffer, colour, light);
                    } else {
                        RenderHandler.renderColouredModel(ModelHandler.PARACHUTE.getModel(), matrixStack, renderTypeBuffer, colour, light);
                    }

                    matrixStack.pop();
                }
            };

            private final LazyOptional<ICurio> curioCapability = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
                if (capability == CuriosCapability.ITEM)
                    return (LazyOptional<T>) curioCapability;
                else
                    return LazyOptional.empty();
            }
        };
    }
}