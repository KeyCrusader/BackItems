package mod.keycrusader.backitems;
import mod.keycrusader.backitems.client.util.InitHandler;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.items.GliderItem;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.PacketHandler;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.caelus.Caelus;
import top.theillusivec4.caelus.api.CaelusAPI;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

import java.util.function.Function;


@Mod(BackItems.MODID)
public class BackItems {
    public static final String MODID = "backitems";
    public static final String NAME = "Back Items";
    public static final String VERSION = "0.0.1";
    public static final String BACK_SLOT = "back";

    public static final Logger LOGGER = LogManager.getLogger();

    public BackItems() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::interModEnque);

        RegistryHandler.init();
    }

    private void interModEnque(InterModEnqueueEvent event) {
        // Register the back slot with curios
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage(BACK_SLOT)
                .setSize(1)
                .setEnabled(true)
                .setHidden(false)
        );
    }

}
