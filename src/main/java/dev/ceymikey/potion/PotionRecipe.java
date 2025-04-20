/*
 * This file is part of BrewingGuide (https://github.com/Ceymikey/brewing-guide)
 *
 * Copyright © 2025 Ceymikey.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
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
