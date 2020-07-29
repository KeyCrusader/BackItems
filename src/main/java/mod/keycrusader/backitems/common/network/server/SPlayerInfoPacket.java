package mod.keycrusader.backitems.common.network.server;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPlayerInfoPacket {
    private int entityID, quiver;
    private boolean parachute;

    public SPlayerInfoPacket(PacketBuffer buf) {
        this.entityID = buf.readInt();
        this.quiver = buf.readInt();
        this.parachute = buf.readBoolean();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(this.quiver);
        buf.writeBoolean(this.parachute);
    }

    public SPlayerInfoPacket(LivingEntity entity, int quiver, boolean parachute) {
        BackItems.LOGGER.info("Send player info packet");
        this.entityID = entity.getEntityId();
        this.quiver = quiver;
        this.parachute = parachute;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LivingEntity livingEntity = (LivingEntity) Minecraft.getInstance().world.getEntityByID(this.entityID);
            Helpers.setArrowSelectedIndex(livingEntity, this.quiver);
            Helpers.setUsingParachute(livingEntity, this.parachute);
        });
        ctx.get().setPacketHandled(true);
    }
}