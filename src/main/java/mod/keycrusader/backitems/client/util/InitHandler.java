package mod.keycrusader.backitems.client.util;

import mod.keycrusader.backitems.client.screens.BackpackScreen;
import mod.keycrusader.backitems.client.screens.QuiverScreen;
import mod.keycrusader.backitems.common.items.IDyeableItem;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class InitHandler {
    public static void initScreens() {
        ScreenManager.registerFactory(RegistryHandler.BACKPACK_CONTAINER.get(), BackpackScreen::new);
        ScreenManager.registerFactory(RegistryHandler.QUIVER_CONTAINER.get(), QuiverScreen::new);
    }

    public static void initItemColours() {
        for (RegistryObject<Item> item : RegistryHandler.ITEMS.getEntries()) {
            if (item.get() instanceof IDyeableItem) {
                Minecraft.getInstance().getItemColors().register(((IDyeableItem) item.get())::getColor, item.get());
            }
        }
    }

    public static void initKeybindings() {
        ClientRegistry.registerKeyBinding(KeybindHandler.open);
        ClientRegistry.registerKeyBinding(KeybindHandler.action);
    }

    public static void initModels() {
        for (ModelHandler model : ModelHandler.values()) {
            for (ResourceLocation resourceLocation : model.modelLocation) {
                ModelLoader.addSpecialModel(resourceLocation);
            }
        }
    }
}
