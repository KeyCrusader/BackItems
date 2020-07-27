package mod.keycrusader.backitems.common.capability;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerStatus extends INBTSerializable<CompoundNBT> {
    boolean isUsingParachute();
    int getArrowSelectedIndex();
    EquipmentSlotType isArrowFromQuiver();
    void setUsingParachute(boolean isUsing);
    void setArrowSelectedIndex(int index);
    void setArrowFromQuiver(EquipmentSlotType fromQuiver);
}

