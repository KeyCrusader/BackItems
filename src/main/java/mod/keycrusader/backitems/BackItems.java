package mod.keycrusader.backitems;

import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

@Mod(BackItems.MODID)
public class BackItems {
    public static final String MODID = "backitems";
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
