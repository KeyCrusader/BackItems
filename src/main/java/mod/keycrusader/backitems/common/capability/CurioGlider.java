package mod.keycrusader.backitems.common.capability;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.items.GliderItem;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.caelus.api.CaelusAPI;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurio;

import java.util.UUID;

public class CurioGlider implements ICurio {

    public static final AttributeModifier ELYTRA_CURIO_MODIFIER = new AttributeModifier(
            UUID.fromString("c754faef-9926-4a77-abbe-e34ef0d735aa"), "Elytra curio modifier", 1.0D,
            AttributeModifier.Operation.ADDITION);

    private ItemStack stack;

    public CurioGlider(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void onCurioTick(String identifier, int index, LivingEntity entityLivingBase) {
        if (entityLivingBase.world.isRemote) {
            return;
        }

        if (entityLivingBase.isElytraFlying() || GliderItem.isUsable(stack)) {
            Integer ticksFlying = ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, entityLivingBase, "field_184629_bo");

            if (ticksFlying != null && (ticksFlying + 1) % 20 == 0) {
                if (GliderItem.isUsable(stack)) {
                    GliderItem.setUsable(stack, false);
                }
            }
        }
        else {
            setElytraFlight(entityLivingBase, false);
        }
    }

    private void setElytraFlight(LivingEntity entityLivingBase, boolean canFly) {
        IAttributeInstance attributeInstance = entityLivingBase.getAttribute(CaelusAPI.ELYTRA_FLIGHT);
        boolean hasModifier = attributeInstance.hasModifier(ELYTRA_CURIO_MODIFIER);

        if (canFly && !hasModifier && GliderItem.isUsable(stack)) {
            attributeInstance.applyModifier(ELYTRA_CURIO_MODIFIER);
        }
        else if (!canFly && hasModifier) {
            entityLivingBase.getAttribute(CaelusAPI.ELYTRA_FLIGHT).removeModifier(ELYTRA_CURIO_MODIFIER);
        }
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity entityLivingBase) {
        return !(entityLivingBase.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ElytraItem)
                && !CuriosAPI.getCurioEquipped(Items.ELYTRA, entityLivingBase).isPresent()
                && !CuriosAPI.getCurioEquipped(RegistryHandler.GLIDER.get(), entityLivingBase).isPresent();
    }

    @Override
    public void onEquipped(String identifier, LivingEntity entityLivingBase) {
        setElytraFlight(entityLivingBase, true);
    }

    @Override
    public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
        setElytraFlight(entityLivingBase, false);
    }

    @Override
    public void playEquipSound(LivingEntity entityLivingBase) {
        entityLivingBase.world
                .playSound(null, entityLivingBase.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA,
                        SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public boolean canRightClickEquip() {
        return true;
    }

    @Override
    public boolean hasRender(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Override
    public void render(String identifier, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.push();
        int colour = ((IDyeableArmorItem) this.stack.getItem()).getColor(this.stack);
        RenderHandler.alignToBack(matrixStack, livingEntity);
        if (livingEntity.isElytraFlying()) {
            matrixStack.translate(-0.5F, -0.875F, -0.3125F);

            RenderHandler.renderColouredModel(ModelHandler.GLIDER_WINGS.getModel(), matrixStack, renderTypeBuffer, colour, light);
        }
        else {
            RenderHandler.renderColouredModel(ModelHandler.GLIDER.getModel(), matrixStack, renderTypeBuffer, colour, light);
        }
        matrixStack.pop();
    }
}