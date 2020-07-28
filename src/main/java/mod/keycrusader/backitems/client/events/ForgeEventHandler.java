package mod.keycrusader.backitems.client.events;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.client.screens.BackpackScreen;
import mod.keycrusader.backitems.client.screens.QuiverOverlayScreen;
import mod.keycrusader.backitems.client.screens.QuiverScreen;
import mod.keycrusader.backitems.client.screens.StatusOverlayScreen;
import mod.keycrusader.backitems.client.util.KeybindHandler;
import mod.keycrusader.backitems.common.items.QuiverItem;
import mod.keycrusader.backitems.common.network.client.COpenGui;
import mod.keycrusader.backitems.common.network.client.CUsingParachutePacket;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = BackItems.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
    private static final QuiverOverlayScreen QUIVER_OVERLAY_SCREEN = new QuiverOverlayScreen();
    private static final StatusOverlayScreen STATUS_OVERLAY_SCREEN = new StatusOverlayScreen();

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Entity player = mc.getRenderViewEntity();
        if (!(player instanceof PlayerEntity)) return;
        if (KeybindHandler.open.isKeyDown()) {
            PacketHandler.INSTANCE.sendToServer(new COpenGui());
        }
        if (Helpers.getBackItem((LivingEntity) player).getItem() instanceof QuiverItem && KeybindHandler.action.isKeyDown()) {
            boolean flag = true;
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                flag = false;
            }
            Helpers.setArrowSelectedIndex(mc.player, flag);
        }
        if (player instanceof  PlayerEntity && Helpers.isArrowFromQuiver((PlayerEntity) player) != null && mc.gameSettings.keyBindSwapHands.isPressed()) {
            // Disable hand swapping whilst using an arrow from the quiver
            // Without this swapping hands with the bow drawn loses your bow
            // and swaps it for the offhand, gaining an arrow in the process
            // Leave as isPressed to override default Minecraft behaviour
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            PacketHandler.INSTANCE.sendToServer(new CUsingParachutePacket());
        }

        if(mc.currentScreen instanceof BackpackScreen || mc.currentScreen instanceof QuiverScreen)
        {
            if(event.getAction() == GLFW.GLFW_PRESS && (event.getKey() == KeybindHandler.open.getKey().getKeyCode() || event.getKey() == mc.gameSettings.keyBindInventory.getKey().getKeyCode())) {
                mc.player.closeScreen();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().getRenderViewEntity();

            STATUS_OVERLAY_SCREEN.render();

            if (Helpers.getBackItem(player).getItem() instanceof QuiverItem) {
                QUIVER_OVERLAY_SCREEN.render();
            }
        }
    }
}
