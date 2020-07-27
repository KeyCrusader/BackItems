package mod.keycrusader.backitems.common.capability;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryQuiver extends ItemStackHandler {
    private final ItemStack stack;

    public InventoryQuiver(ItemStack stack) {
        super(5);
        this.stack = stack;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof ArrowItem;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        stack.getOrCreateTag().putBoolean("sync", true);
    }
}
