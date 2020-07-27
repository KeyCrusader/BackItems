package mod.keycrusader.backitems.common.enchantments;

import mod.keycrusader.backitems.common.items.BackpackItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class StorageEnchantment extends Enchantment {
    public StorageEnchantment() {
        super(Rarity.RARE, EnchantmentType.WEARABLE, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 16 + (enchantmentLevel - 1) * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof BackpackItem;
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
