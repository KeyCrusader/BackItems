package mod.keycrusader.backitems.common.util;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.containers.BackpackContainer;
import mod.keycrusader.backitems.common.containers.QuiverContainer;
import mod.keycrusader.backitems.common.enchantments.ConservationEnchantment;
import mod.keycrusader.backitems.common.enchantments.PerpetualEnchantment;
import mod.keycrusader.backitems.common.enchantments.QuickdrawEnchantment;
import mod.keycrusader.backitems.common.enchantments.StorageEnchantment;
import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.items.GliderItem;
import mod.keycrusader.backitems.common.items.ParachuteItem;
import mod.keycrusader.backitems.common.items.QuiverItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, BackItems.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, BackItems.MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = new DeferredRegister<>(ForgeRegistries.ENCHANTMENTS, BackItems.MODID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Items
    public static final RegistryObject<Item> BACKPACK = ITEMS.register("backpack", BackpackItem::new);
    public static final RegistryObject<Item> QUIVER = ITEMS.register("quiver", QuiverItem::new);
    public static final RegistryObject<Item> PARACHUTE = ITEMS.register("parachute", ParachuteItem::new);
    public static final RegistryObject<Item> GLIDER = ITEMS.register("glider", GliderItem::new);

    // Containers
    public static final RegistryObject<ContainerType<BackpackContainer>> BACKPACK_CONTAINER = CONTAINERS.register("backpack", () -> IForgeContainerType.create(BackpackContainer::new));
    public static final RegistryObject<ContainerType<QuiverContainer>> QUIVER_CONTAINER = CONTAINERS.register("quiver", () -> IForgeContainerType.create(QuiverContainer::new));

    // Enchantments
    public static final RegistryObject<Enchantment> STORAGE_ENCHANTMENT = ENCHANTMENTS.register("storage", StorageEnchantment::new);
    public static final RegistryObject<Enchantment> CONSERVATION_ENCHANTMENT = ENCHANTMENTS.register("conservation", ConservationEnchantment::new);
    public static final RegistryObject<Enchantment> PERPETUAL_ENCHANTMENT = ENCHANTMENTS.register("perpetual", PerpetualEnchantment::new);
    // TODO Decide if this should exist
    //public static final RegistryObject<Enchantment> QUICKDRAW_ENCHANTMENT = ENCHANTMENTS.register("quickdraw", QuickdrawEnchantment::new);
}
