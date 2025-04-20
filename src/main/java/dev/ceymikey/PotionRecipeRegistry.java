package dev.ceymikey;

import dev.ceymikey.potion.PotionCategory;
import dev.ceymikey.potion.PotionRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.potion.PotionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionRecipeRegistry {
    private static final List<PotionRecipe> allRecipes = new ArrayList<>();
    private static final Map<PotionCategory, List<PotionRecipe>> recipesByCategory = new HashMap<>();

    // Basically a glass bottle with water simplified...
    private static final ItemStack WATER_BOTTLE = PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);

    static {
        // Initialize category lists
        for (PotionCategory category : PotionCategory.values()) {
            recipesByCategory.put(category, new ArrayList<>());
        }
    }

    public static void registerVanillaRecipes() {
        // Base potions
        register(WATER_BOTTLE, new ItemStack(Items.NETHER_WART),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.THICK),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.GHAST_TEAR),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.RABBIT_FOOT),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.BLAZE_POWDER),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.GLISTERING_MELON_SLICE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.SUGAR),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.MAGMA_CREAM),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE),
                PotionCategory.BASE);

        // Regular potions
        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.GOLDEN_CARROT),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.MAGMA_CREAM),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.RABBIT_FOOT),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LEAPING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.SUGAR),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.PHANTOM_MEMBRANE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.BLAZE_POWDER),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.TURTLE_HELMET),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.TURTLE_MASTER),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.PUFFERFISH),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER_BREATHING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.GLISTERING_MELON_SLICE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HEALING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(Items.GHAST_TEAR),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS),
                PotionCategory.EFFECT);

        // Extended duration potions
        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LEAPING),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_LEAPING),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_FIRE_RESISTANCE),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_SWIFTNESS),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SLOWNESS),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOWNESS),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER_BREATHING),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_WATER_BREATHING),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_POISON),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_REGENERATION),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_STRENGTH),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_WEAKNESS),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOW_FALLING),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.TURTLE_MASTER),
                new ItemStack(Items.REDSTONE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_TURTLE_MASTER),
                PotionCategory.EXTENDED);

        // Enhanced potions
        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LEAPING),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_LEAPING),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_POISON),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_STRENGTH),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HEALING),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HARMING),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_SLOWNESS),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.TURTLE_MASTER),
                new ItemStack(Items.GLOWSTONE_DUST),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_TURTLE_MASTER),
                PotionCategory.ENHANCED);

        // Conversion recipes (using Fermented Spider Eye)
        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY),
                PotionCategory.EXTENDED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HEALING),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HARMING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING),
                PotionCategory.ENHANCED);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HARMING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_POISON),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HARMING),
                PotionCategory.EFFECT);

        register(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_POISON),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING),
                PotionCategory.ENHANCED);
    }

    /* Method to register a potion recipe */
    public static void register(ItemStack basePotion, ItemStack ingredient, ItemStack result, PotionCategory category) {
        PotionRecipe recipe = new PotionRecipe(basePotion, ingredient, result, category);
        allRecipes.add(recipe);
        recipesByCategory.get(category).add(recipe);
    }

    /* Returns all registered recipes. */
    public static List<PotionRecipe> getAllRecipes() {
        return Collections.unmodifiableList(allRecipes);
    }

    /* Method to get all registered potion recipes */
    public static List<PotionRecipe> getRecipesByCategory(PotionCategory category) {
        return Collections.unmodifiableList(recipesByCategory.getOrDefault(category, new ArrayList<>()));
    }

    /* Returns all categories of potion recipes */
    public static List<PotionCategory> getCategories() {
        return List.of(PotionCategory.values());
    }
}
