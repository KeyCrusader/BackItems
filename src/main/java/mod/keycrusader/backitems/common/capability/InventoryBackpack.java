package mod.keycrusader.backitems.common.capability;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.items.QuiverItem;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryBackpack extends ItemStackHandler {

    public InventoryBackpack() {
        super(36);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return !(stack.getItem() instanceof QuiverItem || stack.getItem() instanceof BackpackItem || Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock);
    }
}
