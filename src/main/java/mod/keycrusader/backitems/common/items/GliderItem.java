package mod.keycrusader.backitems.common.items;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.capability.CurioGlider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GliderItem extends Item implements IDyeableArmorItem, IDyeableItem {
    public GliderItem() {
        super(new Properties()
                .group(ItemGroup.TRANSPORTATION)
                .maxStackSize(1)
                .rarity(Rarity.COMMON)
                .maxDamage(20)
        );
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.LEATHER;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            LazyOptional<ICurio> curio = LazyOptional.of(() -> new CurioGlider(stack));

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curio);
            }
        };
    }
}
