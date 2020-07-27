package mod.keycrusader.backitems.common.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.capability.CurioBackpack;
import mod.keycrusader.backitems.common.capability.InventoryBackpack;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.*;
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
import java.util.Map;

public class BackpackItem extends Item implements IDyeableArmorItem, IDyeableItem {
    public BackpackItem() {
        super(new Item.Properties()
                .group(ItemGroup.INVENTORY)
                .maxStackSize(1)
                .rarity(Rarity.COMMON)
        );

        addPropertyOverride(new ResourceLocation("has_items"), (stack, world, entity) -> Helpers.doesInventoryHaveItems(stack) ? 1 : 0);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilitySerializable<INBT>() {
            private final ICurio curio = new CurioBackpack(stack);
            private final IItemHandler inventory = new InventoryBackpack();

            private final LazyOptional<ICurio> curioCapability = LazyOptional.of(() -> curio);
            private final LazyOptional<IItemHandler> inventoryCapability = LazyOptional.of(() -> inventory);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
                if (capability == CuriosCapability.ITEM)
                    return (LazyOptional<T>) curioCapability;
                else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    return (LazyOptional<T>) inventoryCapability;
                else
                    return LazyOptional.empty();
            }


            @Override
            public INBT serializeNBT() {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.inventory, null);
            }

            @Override
            public void deserializeNBT(INBT nbt) {
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inventory, null, nbt);
            }
        };
    }

    @Override
    public boolean shouldSyncTag() {
        return true;
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        IItemHandler handlerBackpack = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        INBT inventoryTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerBackpack, null);

        CompoundNBT itemTag = super.getShareTag(stack);
        if (itemTag == null) {
            itemTag = new CompoundNBT();
        }

        itemTag.put("backpackInventory", inventoryTag);

        return itemTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT tag) {
        if (tag.contains("backpackInventory")) {
            INBT inventoryTag = tag.get("backpackInventory");

            if (inventoryTag != null) {
                IItemHandler handlerBackpack = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerBackpack, null, inventoryTag);
            }

            tag.remove("backpackInventory");
        }

        super.readShareTag(stack, tag);
    }
}
