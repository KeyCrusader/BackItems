package mod.keycrusader.backitems.common.network.client;

import mod.keycrusader.backitems.BackItems;
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

import java.util.function.Supplier;

public class COpenGui {
    public COpenGui(PacketBuffer buf) {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public COpenGui() {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ItemStack stack = Helpers.getBackItem(ctx.get().getSender());

            if (stack.getItem() instanceof BackpackItem) {
                INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new BackpackContainer(w, p, stack), stack.getDisplayName());
                NetworkHooks.openGui((ServerPlayerEntity) ctx.get().getSender(), container, buf -> {
                    buf.writeItemStack(stack);
                });
            }
            else if (stack.getItem() instanceof QuiverItem) {
                INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new QuiverContainer(w, p, stack), stack.getDisplayName());
                NetworkHooks.openGui((ServerPlayerEntity) ctx.get().getSender(), container, buf -> {
                    buf.writeItemStack(stack);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
