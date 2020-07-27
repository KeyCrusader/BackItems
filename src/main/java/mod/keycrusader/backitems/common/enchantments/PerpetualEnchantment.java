package mod.keycrusader.backitems.common.enchantments;

import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.items.ParachuteItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class PerpetualEnchantment extends Enchantment {
    public PerpetualEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEARABLE, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ParachuteItem;
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
