package mod.keycrusader.backitems.common.enchantments;

import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.items.QuiverItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class QuickdrawEnchantment extends Enchantment {
    public QuickdrawEnchantment() {
        super(Rarity.RARE, EnchantmentType.WEARABLE, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 10 + 5 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof QuiverItem;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
}
