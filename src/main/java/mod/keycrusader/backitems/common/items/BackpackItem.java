package mod.keycrusader.backitems.common.items;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.capability.CurioBackpack;
import mod.keycrusader.backitems.common.capability.InventoryBackpack;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
        IItemHandler handlerBackpack = Helpers.getBackInventory(stack);
        INBT inventoryTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerBackpack, null);

        CompoundNBT itemTag = super.getShareTag(stack);

        if (inventoryTag != null) {
            if (itemTag == null) {
                itemTag = new CompoundNBT();
            }

            itemTag.put("backpackInventory", inventoryTag);
        }
        return itemTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT tag) {
        if (tag != null && tag.contains("backpackInventory")) {
                INBT inventoryTag = tag.get("backpackInventory");

                if (inventoryTag != null) {
                    IItemHandler handlerBackpack = Helpers.getBackInventory(stack);
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerBackpack, null, inventoryTag);
                }

                tag.remove("backpackInventory");
            }
        super.readShareTag(stack, tag);
    }
}
