package mod.keycrusader.backitems.common.items;

import mod.keycrusader.backitems.common.capability.CurioParachute;
import mod.keycrusader.backitems.common.util.Helpers;
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
            private final ICurio curio = new CurioParachute(stack);

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