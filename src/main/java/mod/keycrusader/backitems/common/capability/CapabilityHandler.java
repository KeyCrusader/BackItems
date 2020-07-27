package mod.keycrusader.backitems.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityHandler {
    // The instance used for Player Status
    @CapabilityInject(IPlayerStatus.class)
    public static final Capability<IPlayerStatus> PLAYER_STATUS_CAPABILITY = null;

    public static void register() {
        // Register the Player Status capability
        CapabilityManager.INSTANCE.register(IPlayerStatus.class, new Capability.IStorage<IPlayerStatus>() {

            @Nullable
            @Override
            public INBT writeNBT(Capability<IPlayerStatus> capability, IPlayerStatus instance, Direction side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IPlayerStatus> capability, IPlayerStatus instance, Direction side, INBT nbt) {
                instance.deserializeNBT((CompoundNBT) nbt);

            }
        }, () -> new PlayerStatusHandler());

    }

}
