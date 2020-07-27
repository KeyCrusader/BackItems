package mod.keycrusader.backitems.client.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KeybindHandler {
    public static final KeyBinding open = new KeyBinding("key.backitems.open", 66, "categories.backitems.backitems");
    public static final KeyBinding action = new KeyBinding("key.backitems.action", 82, "categories.backitems.backitems");;
}
