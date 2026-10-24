package dev.ceymikey.potion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.ItemStack;

@Getter
@AllArgsConstructor
public class PotionRecipe {
    private final ItemStack basePotion;
    private final ItemStack ingredient;
    private final ItemStack result;
    private final PotionCategory category;
}
