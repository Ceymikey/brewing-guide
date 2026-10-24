package dev.ceymikey.potion;

//? if 1.20.6 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
//?}
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
//? if <=1.20.4 {
/*import net.minecraft.potion.PotionUtil;
*///?}

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// https://minecraft.wiki/w/Potion#Variants
public class PotionRecipeRegistry {
    private static PotionRecipeRegistry instance;
    private final List<PotionRecipe> allRecipes = new ArrayList<>();
    private final Map<PotionCategory, List<PotionRecipe>> recipesByCategory = new HashMap<>();

    // Basically a glass bottle with water simplified...
    private final ItemStack WATER_BOTTLE = potion(Items.POTION, Potions.WATER);

    private PotionRecipeRegistry() {
        for (PotionCategory category : PotionCategory.values()) {
            recipesByCategory.put(category, new ArrayList<>());
        }
    }

    public void registerVanillaRecipes() {
        // Base potions
        register(WATER_BOTTLE, new ItemStack(Items.NETHER_WART),
                potion(Items.POTION, Potions.AWKWARD),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.THICK),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.SPIDER_EYE),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.GHAST_TEAR),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.RABBIT_FOOT),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.BLAZE_POWDER),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.GLISTERING_MELON_SLICE),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.SUGAR),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        register(WATER_BOTTLE, new ItemStack(Items.MAGMA_CREAM),
                potion(Items.POTION, Potions.MUNDANE),
                PotionCategory.BASE);

        // Regular potions
        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.GOLDEN_CARROT),
                potion(Items.POTION, Potions.NIGHT_VISION),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.MAGMA_CREAM),
                potion(Items.POTION, Potions.FIRE_RESISTANCE),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.RABBIT_FOOT),
                potion(Items.POTION, Potions.LEAPING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.SUGAR),
                potion(Items.POTION, Potions.SWIFTNESS),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.PHANTOM_MEMBRANE),
                potion(Items.POTION, Potions.SLOW_FALLING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.BLAZE_POWDER),
                potion(Items.POTION, Potions.STRENGTH),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.TURTLE_HELMET),
                potion(Items.POTION, Potions.TURTLE_MASTER),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.PUFFERFISH),
                potion(Items.POTION, Potions.WATER_BREATHING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.GLISTERING_MELON_SLICE),
                potion(Items.POTION, Potions.HEALING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.SPIDER_EYE),
                potion(Items.POTION, Potions.POISON),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.AWKWARD),
                new ItemStack(Items.GHAST_TEAR),
                potion(Items.POTION, Potions.REGENERATION),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.WATER),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.WEAKNESS),
                PotionCategory.EFFECT);

        // Extended duration potions
        register(potion(Items.POTION, Potions.NIGHT_VISION),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_NIGHT_VISION),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.INVISIBILITY),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_INVISIBILITY),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.LEAPING),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_LEAPING),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.FIRE_RESISTANCE),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_FIRE_RESISTANCE),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.SWIFTNESS),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_SWIFTNESS),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.SLOWNESS),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_SLOWNESS),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.WATER_BREATHING),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_WATER_BREATHING),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.POISON),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_POISON),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.REGENERATION),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_REGENERATION),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.STRENGTH),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_STRENGTH),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.WEAKNESS),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_WEAKNESS),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.SLOW_FALLING),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_SLOW_FALLING),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.TURTLE_MASTER),
                new ItemStack(Items.REDSTONE),
                potion(Items.POTION, Potions.LONG_TURTLE_MASTER),
                PotionCategory.EXTENDED);

        // Upgraded power
        register(potion(Items.POTION, Potions.LEAPING),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_LEAPING),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.SWIFTNESS),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_SWIFTNESS),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.POISON),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_POISON),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.REGENERATION),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_REGENERATION),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.STRENGTH),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_STRENGTH),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.HEALING),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_HEALING),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.HARMING),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_SLOWNESS),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.TURTLE_MASTER),
                new ItemStack(Items.GLOWSTONE_DUST),
                potion(Items.POTION, Potions.STRONG_TURTLE_MASTER),
                PotionCategory.UPGRADED);

        // Conversion recipes (using Fermented Spider Eye)
        register(potion(Items.POTION, Potions.NIGHT_VISION),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.INVISIBILITY),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.LONG_NIGHT_VISION),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.LONG_INVISIBILITY),
                PotionCategory.EXTENDED);

        register(potion(Items.POTION, Potions.HEALING),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.HARMING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.STRONG_HEALING),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.STRONG_HARMING),
                PotionCategory.UPGRADED);

        register(potion(Items.POTION, Potions.POISON),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.HARMING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.LONG_POISON),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.HARMING),
                PotionCategory.EFFECT);

        register(potion(Items.POTION, Potions.STRONG_POISON),
                new ItemStack(Items.FERMENTED_SPIDER_EYE),
                potion(Items.POTION, Potions.STRONG_HARMING),
                PotionCategory.UPGRADED);
    }

    /* Method to register a potion recipe */
    public void register(ItemStack basePotion, ItemStack ingredient, ItemStack result, PotionCategory category) {
        PotionRecipe recipe = new PotionRecipe(basePotion, ingredient, result, category);
        allRecipes.add(recipe);
        recipesByCategory.get(category).add(recipe);
    }

    public List<PotionRecipe> getAllRecipes() {
        return Collections.unmodifiableList(allRecipes);
    }

    //? if >=1.20.5 {
    private static ItemStack potion(Item bottle, RegistryEntry<Potion> potion) {
        ItemStack stack = new ItemStack(bottle);
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        return stack;
    }
    //?} else {
    /*private static ItemStack potion(Item bottle, Potion potion) {
        return PotionUtil.setPotion(new ItemStack(bottle), potion);
    }
    *///?}

    public static PotionRecipeRegistry getInstance() {
        if (instance == null) {
            instance = new PotionRecipeRegistry();
        }
        return instance;
    }
}
