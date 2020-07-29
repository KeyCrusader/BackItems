package mod.keycrusader.backitems.common.network.server;

import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SUsingParachutePacket {
    private boolean value;
    private int entityID;

    public SUsingParachutePacket(PacketBuffer buf) {
        this.value = buf.readBoolean();
        this.entityID = buf.readInt();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(this.value);
        buf.writeInt(this.entityID);
    }

    public SUsingParachutePacket(LivingEntity entity, boolean value) {
        this.value = value;
        this.entityID = entity.getEntityId();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity playerEntity = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (playerEntity == null) throw new IllegalArgumentException("Player not found");
            Helpers.setUsingParachute(playerEntity, this.value);
        });
        ctx.get().setPacketHandled(true);
    }
}
