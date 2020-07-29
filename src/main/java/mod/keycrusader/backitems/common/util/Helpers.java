package mod.keycrusader.backitems.common.util;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.capability.IPlayerStatus;
import mod.keycrusader.backitems.common.network.client.CSelectArrowIndexPacket;
import mod.keycrusader.backitems.common.network.server.SUsingQuiverPacket;
import mod.keycrusader.backitems.common.network.server.SUsingParachutePacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;

import java.util.UUID;

public class Helpers {
    /**
     * Used for the currently selected arrow in the quiver, for the overlay rendering and which slot to pull from in the
     * quiver item handler
     * @param playerEntity  The player using a quiver
     * @param direction     True for adding to slot index, false for subtracting
     */
    public static void setArrowSelectedIndex(PlayerEntity playerEntity, boolean direction) {
        IPlayerStatus playerStatus = Helpers.getPlayerStatus(playerEntity);
        int index = playerStatus.getArrowSelectedIndex();
        if (direction) {
            index++;
        }
        else {
            index--;
        }

        if (index > getBackInventory(getBackItem(playerEntity)).getSlots()-1) {
            index = 0;
        }
        else if (index < 0) {
            index = getBackInventory(getBackItem(playerEntity)).getSlots()-1;
        }

        if (playerEntity.world.isRemote) {
            PacketHandler.INSTANCE.sendToServer(new CSelectArrowIndexPacket(index));
        }
        playerStatus.setArrowSelectedIndex(index);
    }
    public static void setArrowSelectedIndex(PlayerEntity playerEntity, int value) {
        Helpers.getPlayerStatus(playerEntity).setArrowSelectedIndex(value);
    }
    /**
     * Getter for which slot is currently is use in the quiver
     * @param playerEntity  The player using a quiver
     * @return              The slot index in use
     */
    public static int getArrowSelectedIndex(PlayerEntity playerEntity) {
        return Helpers.getPlayerStatus(playerEntity).getArrowSelectedIndex();
    }


    /**
     * Used to keep track of if an arrow is drawn from the quiver or not, updated on bow right click, slot changed and
     * arrow loose events
     * @param playerEntity  The player using a quiver
     * @param hand          Which hand the drawn arrow is held in, null if not from quiver
     */
    public static void setArrowFromQuiver(PlayerEntity playerEntity, EquipmentSlotType hand) {
        Helpers.getPlayerStatus(playerEntity).setArrowFromQuiver(hand);

        // Update client if this is the server
        if (!playerEntity.world.isRemote) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity), new SUsingQuiverPacket(playerEntity, hand));
        }
    }
    /**
     * Getter for if an arrow is currently drawn from the quiver
     * @param playerEntity  The player using a quiver
     * @return              True if from quiver, false otherwise
     */
    public static EquipmentSlotType isArrowFromQuiver(PlayerEntity playerEntity) {
        return Helpers.getPlayerStatus(playerEntity).isArrowFromQuiver();
    }


    /**
     * Sets the status of parachute in use
     * @param playerEntity  Player to change
     * @param using         Boolean is using the parachute
     */
    public static void setUsingParachute(PlayerEntity playerEntity, boolean using) {
        Helpers.getPlayerStatus(playerEntity).setUsingParachute(using);

        if (!using) {
            ItemStack stackBack = Helpers.getBackItem(playerEntity);
            if (EnchantmentHelper.getEnchantmentLevel(RegistryHandler.PERPETUAL_ENCHANTMENT.get(), stackBack) == 0) {
                stackBack.setCount(0);
            }
        }

        // Update client if this is the server
        if (!playerEntity.world.isRemote) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity), new SUsingParachutePacket(playerEntity, using));
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> playerEntity), new SUsingParachutePacket(playerEntity, using));
        }
    }
    /**
     * @param playerEntity  The entity to check (player)
     * @return              True if using parachute, else false
     */
    public static boolean isUsingParachute(PlayerEntity playerEntity) {
        return Helpers.getPlayerStatus(playerEntity).isUsingParachute();
    }

    public static ItemStack getBackItem(LivingEntity livingEntity) {
        return getItem(livingEntity, BackItems.BACK_SLOT);
    }

    public static ItemStack getItem(LivingEntity livingEntity, String identifier) {
        ICurioItemHandler curioInventory = livingEntity.getCapability(CuriosCapability.INVENTORY).orElseThrow(() -> new IllegalArgumentException(livingEntity.getDisplayName()+" missing curio capability"));
        return curioInventory.getStackInSlot(identifier, 0);
    }

    public static IItemHandler getBackInventory(ItemStack stack) {
        return stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() -> new IllegalArgumentException(stack.getDisplayName()+" missing inventory capability"));
    }

    public static boolean doesInventoryHaveItems(ItemStack stack) {
        IItemHandler handlerInventory = Helpers.getBackInventory(stack);

        for (int slot = 0; slot < handlerInventory.getSlots(); slot++) {
            if (!(handlerInventory.getStackInSlot(slot).isEmpty())) {
                return true;
            }
        }

        return false;
    }

    public static IPlayerStatus getPlayerStatus(PlayerEntity playerEntity) {
        return playerEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElseThrow(() -> new IllegalArgumentException(playerEntity.getDisplayName()+" missing player status capability"));
    }
}
