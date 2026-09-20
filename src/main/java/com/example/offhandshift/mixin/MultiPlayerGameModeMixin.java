package com.example.offhandshift.mixin;

import com.example.offhandshift.OffhandShiftClick;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {

    /** Inventory slot index of the off hand (also the "button" the swap-with-offhand click uses). */
    private static final int OFFHAND_SLOT = 40;

    @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
    private void offhandshiftclick$sendToOffhand(int containerId, int slotId, int button,
                                                 ContainerInput input, Player player,
                                                 CallbackInfo ci) {
        if (input != ContainerInput.QUICK_MOVE) return;

        AbstractContainerMenu menu = player.containerMenu;
        if (menu.containerId != containerId || slotId < 0 || slotId >= menu.slots.size()) return;

        // Only work in the player's own inventory screen (not chests, furnaces, etc.)
        if (menu != player.inventoryMenu) return;

        // Creative inventory uses its own click handling: leave it alone
        if (player.isCreative()) return;

        Slot slot = menu.getSlot(slotId);
        ItemStack stack = slot.getItem();
        if (stack.isEmpty()) return;

        // Shift-clicking the off-hand slot itself keeps its vanilla behaviour
        if (slot.container == player.getInventory() && slot.getContainerSlot() == OFFHAND_SLOT) return;

        // Skip output slots (crafting result etc.) so they still work normally
        if (!slot.mayPickup(player) || !slot.mayPlace(stack)) return;

        if (!OffhandShiftClick.config().allows(stack)) return;

        ((MultiPlayerGameMode) (Object) this)
                .handleContainerInput(containerId, slotId, OFFHAND_SLOT, ContainerInput.SWAP, player);
        ci.cancel();
    }
}
