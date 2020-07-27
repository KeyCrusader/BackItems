package mod.keycrusader.backitems.client.events;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.client.util.InitHandler;
import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.items.QuiverItem;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BackItems.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        // Register ContainerType Screens
        // ScreenManager.registerFactory is not safe to call during parallel mod loading so we queue it to run later
        DeferredWorkQueue.runLater(() -> {
            InitHandler.initScreens();
            InitHandler.initItemColours();
            InitHandler.initKeybindings();
        });
    }


    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        InitHandler.initModels();
    }
}
