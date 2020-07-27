package mod.keycrusader.backitems.common.network.server;

import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SUsingQuiverPacket {
    private int entityID;
    private EquipmentSlotType hand;

    public SUsingQuiverPacket(PacketBuffer buf) {
        this.entityID = buf.readInt();
        this.hand = null;
        if (buf.readBoolean()) {
            this.hand = buf.readEnumValue(EquipmentSlotType.class);
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityID);
        if (this.hand != null) {
            buf.writeBoolean(true);
            buf.writeEnumValue(this.hand);
        }
        else {
            buf.writeBoolean(false);
        }
    }

    public SUsingQuiverPacket(LivingEntity entity, EquipmentSlotType hand) {
        this.entityID = entity.getEntityId();
        this.hand = hand;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Helpers.setArrowFromQuiver((LivingEntity) Minecraft.getInstance().world.getEntityByID(this.entityID), hand);
        });
        ctx.get().setPacketHandled(true);
    }
}