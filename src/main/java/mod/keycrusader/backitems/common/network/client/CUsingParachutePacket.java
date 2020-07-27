package mod.keycrusader.backitems.common.network.client;

import mod.keycrusader.backitems.common.items.ParachuteItem;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CUsingParachutePacket {
    private boolean value;

    public CUsingParachutePacket(PacketBuffer buf) {
        this.value = buf.readBoolean();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(this.value);
    }

    public CUsingParachutePacket() {
        this.value = true;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity playerEntity = ctx.get().getSender();
            if (Helpers.getBackItem(playerEntity).getItem() instanceof ParachuteItem) {
                if (Helpers.isUsingParachute(playerEntity)) {
                    Helpers.setUsingParachute(ctx.get().getSender(), false);
                }
                else {
                    if (playerEntity.fallDistance > 3.0F && !(playerEntity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ElytraItem)) {
                        Helpers.setUsingParachute(ctx.get().getSender(), true);
                    }
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
