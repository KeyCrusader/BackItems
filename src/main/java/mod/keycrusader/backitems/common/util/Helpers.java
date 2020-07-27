package mod.keycrusader.backitems.common.util;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.capability.IPlayerStatus;
import mod.keycrusader.backitems.common.network.client.CSelectArrowIndexPacket;
import mod.keycrusader.backitems.common.network.server.SUsingQuiverPacket;
import mod.keycrusader.backitems.common.network.server.SUsingParachutePacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
     * @param livingEntity  The player using a quiver
     * @param direction     True for adding to slot index, false for subtracting
     */
    public static void setArrowSelectedIndex(LivingEntity livingEntity, boolean direction) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            int index = playerStatus.getArrowSelectedIndex();
            if (direction) {
                index++;
            }
            else {
                index--;
            }

            if (index > getBackInventory(getBackItem(livingEntity)).getSlots()-1) {
                index = 0;
            }
            else if (index < 0) {
                index = getBackInventory(getBackItem(livingEntity)).getSlots()-1;
            }

            if (livingEntity.world.isRemote) {
                PacketHandler.INSTANCE.sendToServer(new CSelectArrowIndexPacket(index));
            }
            playerStatus.setArrowSelectedIndex(index);
        }
    }
    public static void setArrowSelectedIndex(LivingEntity livingEntity, int value) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            playerStatus.setArrowSelectedIndex(value);
        }
    }
    /**
     * Getter for which slot is currently is use in the quiver
     * @param livingEntity  The player using a quiver
     * @return              The slot index in use
     */
    public static int getArrowSelectedIndex(LivingEntity livingEntity) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            return playerStatus.getArrowSelectedIndex();
        }
        return 0;
    }


    /**
     * Used to keep track of if an arrow is drawn from the quiver or not, updated on bow right click, slot changed and
     * arrow loose events
     * @param livingEntity  The player using a quiver
     * @param hand          Which hand the drawn arrow is held in, null if not from quiver
     */
    public static void setArrowFromQuiver(LivingEntity livingEntity, EquipmentSlotType hand) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            playerStatus.setArrowFromQuiver(hand);

            // Update client if this is the server
            if (!livingEntity.world.isRemote) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) livingEntity), new SUsingQuiverPacket(livingEntity, hand));
            }
        }
    }
    /**
     * Getter for if an arrow is currently drawn from the quiver
     * @param livingEntity  The player using a quiver
     * @return              True if from quiver, false otherwise
     */
    public static EquipmentSlotType isArrowFromQuiver(LivingEntity livingEntity) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            return playerStatus.isArrowFromQuiver();
        }
        return null;
    }


    /**
     * Sets the status of parachute in use
     * @param livingEntity  Player to change
     * @param using         Boolean is using the parachute
     */
    public static void setUsingParachute(LivingEntity livingEntity, boolean using) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            playerStatus.setUsingParachute(using);

            if (!using) {
                ItemStack stackBack = Helpers.getBackItem(livingEntity);
                if (EnchantmentHelper.getEnchantmentLevel(RegistryHandler.PERPETUAL_ENCHANTMENT.get(), stackBack) == 0) {
                    stackBack.setCount(0);
                }
            }

            // Update client if this is the server
            if (!livingEntity.world.isRemote) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) livingEntity), new SUsingParachutePacket(livingEntity, using));
            }
        }
    }
    /**
     * @param livingEntity  The entity to check (player)
     * @return              True if using parachute, else false
     */
    public static boolean isUsingParachute(LivingEntity livingEntity) {
        IPlayerStatus playerStatus = livingEntity.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).orElse(null);
        if (playerStatus != null) {
            return playerStatus.isUsingParachute();
        }
        return false;
    }

    public static ItemStack getBackItem(LivingEntity livingEntity) {
        return getItem(livingEntity, BackItems.BACK_SLOT);
    }

    public static ItemStack getItem(LivingEntity livingEntity, String identifier) {
        ICurioItemHandler curioInventory = livingEntity.getCapability(CuriosCapability.INVENTORY).orElse(null);
        if (curioInventory != null) {
            return curioInventory.getStackInSlot(identifier, 0);
        }
        return ItemStack.EMPTY;
    }

    public static IItemHandler getBackInventory(ItemStack stack) {
        return stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    }

    public static boolean doesInventoryHaveItems(ItemStack stack) {
        IItemHandler handlerInventory = Helpers.getBackInventory(stack);

        if (handlerInventory == null) {
            return false;
        }
        for (int slot = 0; slot < handlerInventory.getSlots(); slot++) {
            if (!(handlerInventory.getStackInSlot(slot).isEmpty())) {
                return true;
            }
        }

        return false;
    }
}
