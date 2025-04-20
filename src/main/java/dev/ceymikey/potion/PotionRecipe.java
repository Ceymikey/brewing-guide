package dev.ceymikey.potion;

import net.minecraft.item.ItemStack;

public class PotionRecipe {
    private final ItemStack basePotion;
    private final ItemStack ingredient;
    private final ItemStack result;
    private final PotionCategory category;

    public PotionRecipe(ItemStack basePotion, ItemStack ingredient, ItemStack result, PotionCategory category) {
        this.basePotion = basePotion;
        this.ingredient = ingredient;
        this.result = result;
        this.category = category;
    }

    public PotionRecipe(ItemStack basePotion, ItemStack ingredient, ItemStack result) {
        this(basePotion, ingredient, result, PotionCategory.BASE);
    }

    public ItemStack getBasePotion() {
        return basePotion;
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public PotionCategory getCategory() {
        return category;
    }
}
