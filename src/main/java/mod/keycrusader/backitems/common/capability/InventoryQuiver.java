package mod.keycrusader.backitems.common.capability;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryQuiver extends ItemStackHandler {
    public InventoryQuiver() {
        super(5);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof ArrowItem;
    }
}
