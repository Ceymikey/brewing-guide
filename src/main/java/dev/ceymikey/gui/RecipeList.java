package dev.ceymikey.gui;

import dev.ceymikey.potion.PotionRecipeRegistry;
import dev.ceymikey.potion.PotionCategory;
import dev.ceymikey.potion.PotionRecipe;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static dev.ceymikey.gui.RecipePanelLayout.CATEGORY_GAP;
import static dev.ceymikey.gui.RecipePanelLayout.ROW_HEIGHT;
import static dev.ceymikey.gui.RecipePanelLayout.VIEW_HEIGHT;

public class RecipeList {
    public record Row(PotionCategory category, PotionRecipe recipe, int y) {
        boolean isHeader() {
            return recipe == null;
        }
    }

    private final List<PotionRecipe> recipes = PotionRecipeRegistry.getInstance().getAllRecipes();
    private final Map<PotionRecipe, String> searchKeys = new HashMap<>();
    @Getter private final List<Row> rows = new ArrayList<>();
    @Getter private int contentHeight;
    @Getter private int scroll;

    public RecipeList() {
        for (PotionRecipe recipe : recipes) {
            searchKeys.put(recipe, String.join("\n",
                    recipe.getResult().getName().getString(),
                    recipe.getIngredient().getName().getString(),
                    recipe.getBasePotion().getName().getString()).toLowerCase(Locale.ROOT));
        }
        rebuildRows("");
    }

    public void search(String text) {
        rebuildRows(text);
        scroll = 0;
    }

    private void rebuildRows(String query) {
        String query_root = query.toLowerCase(Locale.ROOT);
        rows.clear();

        int current_y = 0;
        for (PotionCategory category : PotionCategory.values()) {
            List<PotionRecipe> matching = recipes.stream()
                    .filter(r -> r.getCategory() == category && searchKeys.get(r).contains(query_root))
                    .toList();
            if (matching.isEmpty()) continue;

            rows.add(new Row(category, null, current_y));
            current_y += ROW_HEIGHT;
            for (PotionRecipe recipe : matching) {
                rows.add(new Row(category, recipe, current_y));
                current_y += ROW_HEIGHT;
            }
            current_y += CATEGORY_GAP;
        }

        contentHeight = rows.isEmpty() ? 0 : current_y - CATEGORY_GAP;
        setScroll(scroll);
    }

    public int maxScroll() {
        return Math.max(0, contentHeight - VIEW_HEIGHT);
    }

    public boolean canScroll() {
        return maxScroll() > 0;
    }

    public void setScroll(int value) {
        scroll = Math.max(0, Math.min(maxScroll(), value));
    }

    public void scrollBy(int pixels) {
        setScroll(scroll + pixels);
    }
}