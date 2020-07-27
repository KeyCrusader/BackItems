package mod.keycrusader.backitems.common.network.client;

import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.containers.BackpackContainer;
import mod.keycrusader.backitems.common.containers.QuiverContainer;
import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.items.QuiverItem;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.function.Supplier;

public class CSelectArrowIndexPacket {
    private int index;

    public CSelectArrowIndexPacket(PacketBuffer buf) {
        this.index = buf.readInt();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.index);
    }

    public CSelectArrowIndexPacket(int index) {
        this.index = index;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ItemStack stack = Helpers.getBackItem(ctx.get().getSender());

            if (stack.getItem() instanceof BackpackItem) {
            }
            else if (stack.getItem() instanceof QuiverItem) {
                ctx.get().getSender().getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null).setArrowSelectedIndex(this.index);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
