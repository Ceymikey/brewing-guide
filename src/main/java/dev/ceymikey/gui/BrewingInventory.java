package dev.ceymikey.gui;

import dev.ceymikey.potion.PotionRecipe;
//? if >=1.20.6 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
//?}
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
//? if <=1.20.4 {
/*import net.minecraft.potion.PotionUtil;
*///?}
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.function.Predicate;

/// This class is for the brewing stand screen
/// and also provides utility methods to check for fuel(blaze powder).
public class BrewingInventory {
    private static final int FIRST_POTION_SLOT = 0;
    private static final int LAST_POTION_SLOT = 2;
    private static final int INGREDIENT_SLOT = 3;
    private static final int FUEL_SLOT = 4;
    private static final int FIRST_PLAYER_SLOT = 5;

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final BrewingStandScreenHandler handler;

    public BrewingInventory(BrewingStandScreenHandler handler) {
        this.handler = handler;
    }

    public void fill(PotionRecipe recipe) {
        if (!handler.getCursorStack().isEmpty()) return;

        moveFromInventory(s -> sameStack(s, recipe.getIngredient()), INGREDIENT_SLOT);

        for (int slotId = FIRST_POTION_SLOT; slotId <= LAST_POTION_SLOT; slotId++) {
            if (handler.getSlot(slotId).hasStack()) continue;
            if (!moveFromInventory(s -> sameStack(s, recipe.getBasePotion()), slotId)) break;
        }

        if (!isFuelLoaded()) moveFromInventory(BrewingInventory::isBlazePowder, FUEL_SLOT);
    }

    public boolean has(ItemStack wanted) {
        return find(s -> sameStack(s, wanted)) != null;
    }

    public boolean hasBlazePowder() {
        return isFuelLoaded() || find(BrewingInventory::isBlazePowder) != null;
    }

    public boolean canBrew(PotionRecipe recipe) {
        return has(recipe.getBasePotion()) && has(recipe.getIngredient()) && hasBlazePowder();
    }

    private Slot find(Predicate<ItemStack> filter) {
        for (int i = FIRST_PLAYER_SLOT; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            if (slot.hasStack() && filter.test(slot.getStack())) return slot;
        }
        return null;
    }

    private boolean isFuelLoaded() {
        return isBlazePowder(handler.getSlot(FUEL_SLOT).getStack());
    }

    private static boolean sameStack(ItemStack have, ItemStack wanted) {
        if (have.getItem() != wanted.getItem()) return false;
        return !(wanted.getItem() instanceof PotionItem) || samePotion(have, wanted);
    }

    /// [...](https://fabricmc.net/2024/04/19/1205.html)
    //? if >=1.20.5 {
    private static boolean samePotion(ItemStack have, ItemStack wanted) {
        return potionContents(have).potion().equals(potionContents(wanted).potion());
    }

    private static PotionContentsComponent potionContents(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }
    //?} else {
    /*private static boolean samePotion(ItemStack have, ItemStack wanted) {
        return PotionUtil.getPotion(have) == PotionUtil.getPotion(wanted);
    }
    *///?}

    private static boolean isBlazePowder(ItemStack stack) {
        return stack.isOf(Items.BLAZE_POWDER);
    }

    private boolean moveFromInventory(Predicate<ItemStack> filter, int targetSlotId) {
        Slot from = find(filter);
        if (from == null) return false;

        click(from.id);
        click(targetSlotId);

        if (!handler.getCursorStack().isEmpty()) click(from.id);
        return true;
    }

    private void click(int slotId) {
        client.interactionManager.clickSlot(handler.syncId, slotId, 0, SlotActionType.PICKUP, client.player);
    }
}