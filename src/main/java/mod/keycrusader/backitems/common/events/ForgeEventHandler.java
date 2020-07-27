package mod.keycrusader.backitems.common.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.client.models.SashimonoModel;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.capability.CapabilityHandler;
import mod.keycrusader.backitems.common.capability.CurioSashimono;
import mod.keycrusader.backitems.common.capability.IPlayerStatus;
import mod.keycrusader.backitems.common.capability.PlayerStatusHandler;
import mod.keycrusader.backitems.common.items.ParachuteItem;
import mod.keycrusader.backitems.common.items.QuiverItem;
import mod.keycrusader.backitems.common.network.server.SPlayerInfoPacket;
import mod.keycrusader.backitems.common.util.Helpers;
import mod.keycrusader.backitems.common.util.PacketHandler;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.tileentity.BannerTileEntity.func_230138_a_;

@Mod.EventBusSubscriber(modid = BackItems.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onItemAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof BannerItem) {
            if (!event.getObject().getCapability(CuriosCapability.ITEM).isPresent()) {
                event.addCapability(new ResourceLocation(BackItems.MODID, "curios"), new ICapabilityProvider() {
                    private final ICurio curio = new CurioSashimono();
                    private final LazyOptional<ICurio> curioCapability = LazyOptional.of(() -> curio);

                    @Nonnull
                    @Override
                    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
                        if (capability == CuriosCapability.ITEM)
                            return (LazyOptional<T>) curioCapability;
                        else
                            return LazyOptional.empty();
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getObject();
            if (!player.getCapability(CapabilityHandler.PLAYER_STATUS_CAPABILITY).isPresent()) {
                event.addCapability(
                        new ResourceLocation(BackItems.MODID, "status"),
                        new ICapabilitySerializable<CompoundNBT>() {
                            private final PlayerStatusHandler playerStatus = new PlayerStatusHandler();
                            private final LazyOptional<IPlayerStatus> playerStatusCapability = LazyOptional.of(() -> playerStatus);

                            @Nonnull
                            @Override
                            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
                                if (capability == CapabilityHandler.PLAYER_STATUS_CAPABILITY) {
                                    return (LazyOptional<T>) playerStatusCapability;
                                }
                                else {
                                    return LazyOptional.empty();
                                }
                            }


                            @Override
                            public CompoundNBT serializeNBT() {
                                return (CompoundNBT) CapabilityHandler.PLAYER_STATUS_CAPABILITY.writeNBT(this.playerStatus, null);
                            }

                            @Override
                            public void deserializeNBT(CompoundNBT nbt) {
                                CapabilityHandler.PLAYER_STATUS_CAPABILITY.readNBT(this.playerStatus, null, nbt);
                            }
                        });
            }
        }
    }

    // Sync player capability events
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getPlayer().world.isRemote) return;
        sendPlayerInfo(event.getPlayer(), event.getPlayer());
    }
    @SubscribeEvent
    public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (event.getPlayer().world.isRemote) return;
        sendPlayerInfo(event.getPlayer(), event.getPlayer());
    }
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        if (event.getPlayer().world.isRemote) return;
        sendPlayerInfo(event.getPlayer(), event.getPlayer());
    }
    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        if (event.getPlayer().world.isRemote) return;
        sendPlayerInfo(event.getTarget(), event.getPlayer());
    }
    private static void sendPlayerInfo(Entity carrier, PlayerEntity player)
    {
        if (carrier instanceof PlayerEntity) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SPlayerInfoPacket((LivingEntity) carrier, Helpers.getArrowSelectedIndex((LivingEntity) carrier), Helpers.isUsingParachute((LivingEntity) carrier)));
        }
    }

    /**
     * These methods draw an arrow from the quiver if one is equipped and has arrows in the selected slot
     */
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        if (event.getPlayer().world.isRemote) return;
        if (player.isCreative()) return;
        // If used item is a bow or a crossbow
        if (event.getItemStack().getItem() instanceof ShootableItem) {
            if (event.getItemStack().getItem() instanceof CrossbowItem && CrossbowItem.isCharged(event.getItemStack())) {
                return;
            }

            EquipmentSlotType oppositeHand = EquipmentSlotType.OFFHAND;
            if (event.getHand() == Hand.OFF_HAND) {
                oppositeHand = EquipmentSlotType.MAINHAND;
            }

            // If already holding an arrow in the offhand use that instead as normal
            if (player.getItemStackFromSlot(oppositeHand).getItem() instanceof ArrowItem) return;
            // If not wearing a quiver do nothing
            if (!(Helpers.getBackItem(player).getItem() instanceof QuiverItem)) return;
            // Opposite hand must be empty (was too cheaty otherwise)
            if (!player.getItemStackFromSlot(oppositeHand).isEmpty()) return;

            drawFromQuiver(player, oppositeHand);
        }
    }
    private static void drawFromQuiver(PlayerEntity player, EquipmentSlotType oppositeHand) {
        ItemStack stackQuiver = Helpers.getBackItem(player);
        int selectedIndex = Helpers.getArrowSelectedIndex(player);

        IItemHandler inventoryQuiver = Helpers.getBackInventory(stackQuiver);
        // If the selected slot in the quiver is empty do nothing
        if (inventoryQuiver.getStackInSlot(selectedIndex).isEmpty()) return;


        ItemStack stackArrowFromQuiver = inventoryQuiver.getStackInSlot(selectedIndex).copy();
        stackArrowFromQuiver.setCount(1);

        player.setItemStackToSlot(oppositeHand, stackArrowFromQuiver);
        Helpers.setArrowFromQuiver(player, oppositeHand);
    }

    /**
     * Called when the current slot is changed, usually mouse wheel or number to change hotbar selection. Removes arrow
     * from offhand if it was drawn from the quiver
     */
    @SubscribeEvent
    public static void onSlotChanged(LivingEquipmentChangeEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;

        EquipmentSlotType changedSlot = Helpers.isArrowFromQuiver(event.getEntityLiving());

        if (changedSlot == null) return;
        else if (changedSlot == event.getSlot()) return;

        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        // TODO current problem when switching from crossbow that isn't loaded to one that is will use the arrow
        // need to somehow get the previous and current selected slot index (unsure how rn)

        if (event.getFrom().getItem() instanceof CrossbowItem && event.getTo().getItem() instanceof CrossbowItem) {
            if (!CrossbowItem.isCharged(event.getFrom()) && CrossbowItem.isCharged(event.getTo())) {
                replaceInQuiver(player, event.getFrom(), true);
                return;
            }
        }

        replaceInQuiver(player, event.getFrom(), false);
    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;

        PlayerEntity player = event.getPlayer();
        if (event.getPlayer().world.isRemote) return;
        if (Helpers.isArrowFromQuiver(player) == null) return;

        replaceInQuiver(player, event.getBow(), true);
    }

    private static void replaceInQuiver(PlayerEntity player, ItemStack stackBow, boolean arrowFired) {
        boolean infinityEnchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stackBow) > 0;

        int selectedIndex = Helpers.getArrowSelectedIndex(player);
        ItemStack stackQuiver = Helpers.getBackItem(player);

        if (arrowFired && !infinityEnchantment) {
            // Remove arrow from the quiver
            int level = EnchantmentHelper.getEnchantmentLevel(RegistryHandler.CONSERVATION_ENCHANTMENT.get(), stackQuiver);
            int chanceToSave = level*15;
            int use = (int) Math.round(Math.random()*100);
            boolean useArrow = use > chanceToSave;

            Helpers.getBackInventory(stackQuiver).extractItem(selectedIndex, useArrow ? 1 : 0, false);
        }
        else {
            // Destroy the copied arrow
            player.getItemStackFromSlot(Helpers.isArrowFromQuiver(player)).setCount(player.getHeldItemOffhand().getCount()-1);
        }

        Helpers.setArrowFromQuiver(player, null);
    }

    /**
     * Events for parachutes
     */
    // Landing event
    @SubscribeEvent
    public static void onLanded(LivingFallEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();

        if (!Helpers.isUsingParachute(playerEntity)) return;

        ItemStack stackBack = Helpers.getBackItem(playerEntity);
        if (!(stackBack.getItem() instanceof ParachuteItem)) return;

        Helpers.setUsingParachute(playerEntity, false);
    }

    @SubscribeEvent
    public static void onPlayerMoved(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();

        if (Helpers.getBackItem(playerEntity).getItem() instanceof ParachuteItem && Helpers.isUsingParachute(playerEntity)) {
            if (playerEntity.isOnLadder() || playerEntity.isOnePlayerRiding() || playerEntity.isElytraFlying() || playerEntity.abilities.isFlying) {
                Helpers.setUsingParachute(playerEntity, false);
                return;
            }

            double motionY = playerEntity.getMotion().y;
            motionY += 0.1;
            if (motionY > -0.2) {
                motionY = -0.2;
                playerEntity.fallDistance = 0.1F;
            }
            else {
                playerEntity.fallDistance -= 0.1F;
            }
            playerEntity.setMotion(playerEntity.getMotion().x, motionY, playerEntity.getMotion().z);
        }
    }
}
