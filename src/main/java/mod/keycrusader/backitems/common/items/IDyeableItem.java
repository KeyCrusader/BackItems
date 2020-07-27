package mod.keycrusader.backitems.common.items;

import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;

public interface IDyeableItem {
    default int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            return ((IDyeableArmorItem)stack.getItem()).getColor(stack);
        }
        return 0xFFFFFF;
    }
}
