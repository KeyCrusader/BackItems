package mod.keycrusader.backitems.common.capability;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;

public class PlayerStatusHandler implements IPlayerStatus{
    private boolean usingParachute = false;
    private int arrowSelectedIndex = 0;
    private EquipmentSlotType arrowFromQuiver = null;

    @Override
    public boolean isUsingParachute() {
        return this.usingParachute;
    }

    @Override
    public int getArrowSelectedIndex() {
        return this.arrowSelectedIndex;
    }

    @Override
    public EquipmentSlotType isArrowFromQuiver() {
        return this.arrowFromQuiver;
    }

    @Override
    public void setUsingParachute(boolean using) {
        this.usingParachute = using;
    }

    @Override
    public void setArrowSelectedIndex(int index) {
        this.arrowSelectedIndex = index;
    }

    @Override
    public void setArrowFromQuiver(EquipmentSlotType fromQuiver) {
        this.arrowFromQuiver = fromQuiver;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("Parachute", this.usingParachute);
        nbt.putInt("ArrowIndex", this.arrowSelectedIndex);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setUsingParachute(nbt.getBoolean("Parachute"));
        setArrowSelectedIndex(nbt.getInt("ArrowIndex"));
    }
}

