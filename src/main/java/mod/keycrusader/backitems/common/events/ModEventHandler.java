package mod.keycrusader.backitems.common.events;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.client.util.InitHandler;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.util.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BackItems.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            PacketHandler.registerMessages();
            CapabilityHandler.register();
        });
    }
}
