package mod.keycrusader.backitems.common.util;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.network.client.CSelectArrowIndexPacket;
import mod.keycrusader.backitems.common.network.client.COpenGui;
import mod.keycrusader.backitems.common.network.server.SPlayerInfoPacket;
import mod.keycrusader.backitems.common.network.server.SUsingQuiverPacket;
import mod.keycrusader.backitems.common.network.client.CUsingParachutePacket;
import mod.keycrusader.backitems.common.network.server.SUsingParachutePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(BackItems.MODID, "network"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(),
                COpenGui.class,
                COpenGui::toBytes,
                COpenGui::new,
                COpenGui::handle);
        INSTANCE.registerMessage(nextID(),
                CSelectArrowIndexPacket.class,
                CSelectArrowIndexPacket::toBytes,
                CSelectArrowIndexPacket::new,
                CSelectArrowIndexPacket::handle);
        INSTANCE.registerMessage(nextID(),
                CUsingParachutePacket.class,
                CUsingParachutePacket::toBytes,
                CUsingParachutePacket::new,
                CUsingParachutePacket::handle);

        INSTANCE.registerMessage(nextID(),
                SPlayerInfoPacket.class,
                SPlayerInfoPacket::toBytes,
                SPlayerInfoPacket::new,
                SPlayerInfoPacket::handle);
        INSTANCE.registerMessage(nextID(),
                SUsingQuiverPacket.class,
                SUsingQuiverPacket::toBytes,
                SUsingQuiverPacket::new,
                SUsingQuiverPacket::handle);
        INSTANCE.registerMessage(nextID(),
                SUsingParachutePacket.class,
                SUsingParachutePacket::toBytes,
                SUsingParachutePacket::new,
                SUsingParachutePacket::handle);
    }
}
