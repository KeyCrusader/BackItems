package mod.keycrusader.backitems.common.items;

import mod.keycrusader.backitems.common.capability.CurioQuiver;
import mod.keycrusader.backitems.common.capability.InventoryQuiver;
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

public class QuiverItem extends Item implements IDyeableArmorItem, IDyeableItem {
    public QuiverItem() {
        super(new Properties()
                .group(ItemGroup.INVENTORY)
                .maxStackSize(1)
                .rarity(Rarity.COMMON)
        );

        addPropertyOverride(new ResourceLocation("has_items"), (stack, world, entity) -> (Helpers.doesInventoryHaveItems(stack)) ? 1 : 0);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilitySerializable<INBT>() {
            private final ICurio curio = new CurioQuiver(stack);
            private final IItemHandler inventory = new InventoryQuiver();

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

    public int checkArrowCount(ItemStack stack) {
        IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);

        int arrowCount = 0;
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                arrowCount++;
            }
        }
        return arrowCount;
    }

    @Override
    public boolean shouldSyncTag() {
        return true;
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        IItemHandler handlerQuiver = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        INBT inventoryTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerQuiver, null);

        CompoundNBT itemTag = super.getShareTag(stack);
        if (itemTag == null) {
            itemTag = new CompoundNBT();
        }

        itemTag.put("quiverInventory", inventoryTag);

        return itemTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT tag) {
        if (tag.contains("quiverInventory")) {
            INBT inventoryTag = tag.get("quiverInventory");

            if (inventoryTag != null) {
                IItemHandler handlerQuiver = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerQuiver, null, inventoryTag);
            }

            tag.remove("quiverInventory");
        }

        super.readShareTag(stack, tag);
    }
}
