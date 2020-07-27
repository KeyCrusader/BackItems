package mod.keycrusader.backitems.common.containers;

import mod.keycrusader.backitems.BackItems;
import mod.keycrusader.backitems.common.items.BackpackItem;
import mod.keycrusader.backitems.common.util.RegistryHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import top.theillusivec4.curios.api.CuriosAPI;

import javax.annotation.Nonnull;
import java.util.Objects;


public class BackpackContainer extends Container {
    private int rowCount;

    public BackpackContainer(int windowId, PlayerInventory inventoryPlayer, ItemStack stackBackpack) {
        super(RegistryHandler.BACKPACK_CONTAINER.get(), windowId);

        IItemHandler inventoryBackpack = stackBackpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);

        this.rowCount = EnchantmentHelper.getEnchantmentLevel(RegistryHandler.STORAGE_ENCHANTMENT.get(), stackBackpack)+1;
        int offsetX = 8;
        int offsetY = 18;

        // Backpack Inventory
        for (int y = 0; y < rowCount; ++y)
        {
            for (int x = 0; x < 9; ++x)
            {
                int slotIndex = x + (y * 9);
                int xPosition = offsetX + (x * 18);
                int yPosition = offsetY + (y * 18);

                this.addSlot(new SlotItemHandler(inventoryBackpack, slotIndex, xPosition, yPosition){
                    // Disable backpack nesting
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack) {
                        return (inventoryBackpack.isItemValid(slotIndex, stack));
                    }
                });
            }
        }

        // Player Inventory
        addPlayerInventorySlots(inventoryPlayer, offsetX, offsetY+(rowCount*18)+14);

        // Hotbar
        addPlayerHotbarSlots(inventoryPlayer, offsetX, offsetY+(rowCount*18)+72);
    }

    private void addPlayerInventorySlots(final PlayerInventory inventoryPlayer, final int offsetX, final int offsetY) {
        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9; ++x)
            {
                int positionX = offsetX + (x * 18);
                int positionY = offsetY + (y * 18);
                this.addSlot(new Slot(inventoryPlayer, x + y * 9 + 9, positionX, positionY));
            }
        }
    }

    private void addPlayerHotbarSlots(final PlayerInventory inventoryPlayer, final int offsetX, final int offsetY) {
        for (int x = 0; x < 9; ++x)
        {
            int positionX = offsetX + (x * 18);
            this.addSlot(new Slot(inventoryPlayer, x, positionX, offsetY));
        }
    }

    public BackpackContainer(int windowId, PlayerInventory inventoryPlayer, PacketBuffer data) {
        this(windowId, inventoryPlayer, stackFromPacket(inventoryPlayer, data));
    }

    private static ItemStack stackFromPacket(PlayerInventory inventoryPlayer, PacketBuffer data) {
        Objects.requireNonNull(inventoryPlayer, "Player Inventory cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");
        final ItemStack stackBackpack = data.readItemStack();
        if (stackBackpack.getItem() instanceof BackpackItem) {
            return stackBackpack;
        }
        throw new IllegalStateException("Tried to open not a backpack with backpack container");
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int fromSlotIndex) {
        ItemStack previous = ItemStack.EMPTY;
        Slot fromSlot = this.inventorySlots.get(fromSlotIndex);
        final int
                CONTAINER_START = 0,
                CONTAINER_END = (this.rowCount*9) - 1,
                PLAYER_INV_START = CONTAINER_END + 1,
                PLAYER_INV_END = PLAYER_INV_START + 26,
                HOT_BAR_START = PLAYER_INV_END + 1,
                HOT_BAR_END = HOT_BAR_START + 8;

        if (fromSlot != null && fromSlot.getHasStack())
        {
            ItemStack current = fromSlot.getStack();
            previous = current.copy();

            // Source item is in the container
            if (fromSlotIndex <= CONTAINER_END)
            {
                // Transfer from container inventory to player inventory
                if (!this.mergeItemStack(current, PLAYER_INV_START, HOT_BAR_END + 1, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                // Transfer from player inventory to container inventory
                if (!this.mergeItemStack(current, CONTAINER_START, CONTAINER_END + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (current.getCount() == 0)
            {
                fromSlot.putStack(ItemStack.EMPTY);
            }
            else
            {
                fromSlot.onSlotChanged();
            }

            if (current.getCount() == previous.getCount())
            {
                return ItemStack.EMPTY;
            }

            fromSlot.onTake(playerIn, current);
        }

        return previous;
    }
}
