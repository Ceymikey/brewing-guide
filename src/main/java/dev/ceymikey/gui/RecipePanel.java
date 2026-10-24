package dev.ceymikey.gui;

import dev.ceymikey.gui.RecipeList.Row;
import dev.ceymikey.potion.PotionRecipe;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static dev.ceymikey.gui.RecipePanelLayout.CONTENT_WIDTH;
import static dev.ceymikey.gui.RecipePanelLayout.ROW_HEIGHT;
import static dev.ceymikey.gui.RecipePanelLayout.TOP_PADDING;
import static dev.ceymikey.gui.RecipePanelLayout.VIEW_HEIGHT;
import static dev.ceymikey.gui.RecipePanelLayout.WIDTH;
import static dev.ceymikey.gui.RecipePanelLayout.within;

public class RecipePanel {
    private static final int BASE_X = 5;
    private static final int PLUS_X = 30;
    private static final int INGREDIENT_X = 45;
    private static final int ARROW_X = 70;
    private static final int RESULT_X = 85;
    private static final int ITEM_SIZE = 16;

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final TextRenderer textRenderer;
    private final BrewingInventory inventory;
    private final RecipeList list;
    private final ScrollBar scrollBar;
    @Getter private final TextFieldWidget searchField;

    private int x, y, height;

    public RecipePanel(TextRenderer textRenderer, BrewingStandScreenHandler handler) {
        this.textRenderer = textRenderer;
        this.inventory = new BrewingInventory(handler);
        this.list = new RecipeList();
        this.scrollBar = new ScrollBar(list);

        searchField = new TextFieldWidget(textRenderer, 0, 0, WIDTH - 10, 14, Text.literal("Search"));
        searchField.setMaxLength(50);
        searchField.setChangedListener(list::search);
    }

    public void reposition(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        searchField.setX(x + 5);
        searchField.setY(y + 5);
        scrollBar.reposition(x, top());
    }

    private int top() {
        return y + TOP_PADDING;
    }

    private int rowY(Row row) {
        return top() + row.y() - list.getScroll();
    }

    private boolean overView(double mouseX, double mouseY) {
        return within(mouseX, mouseY, x, top(), WIDTH, VIEW_HEIGHT);
    }

    private Row rowAt(double mouseX, double mouseY) {
        if (!within(mouseX, mouseY, x, top(), CONTENT_WIDTH, VIEW_HEIGHT)) return null;
        for (Row row : list.getRows()) {
            int contentY = rowY(row);
            if (mouseY >= contentY && mouseY < contentY + ROW_HEIGHT) return row;
        }
        return null;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(x, y, x + WIDTH, y + height, 0x80000000);
        ctx.drawText(textRenderer, "Potion Recipes", x + 4, y - 10, 0xFFFFFF, false);
        searchField.render(ctx, mouseX, mouseY, delta);

        if (list.getRows().isEmpty()) {
            ctx.drawText(textRenderer, "No matching recipes", x + 4, y + 80, 0xFFFFFF, false);
            return;
        }

        Row hovered = rowAt(mouseX, mouseY);
        boolean blazeOk = inventory.hasBlazePowder();

        ctx.enableScissor(x, top(), x + CONTENT_WIDTH, top() + VIEW_HEIGHT);
        for (Row row : list.getRows()) {
            int contentY = rowY(row);
            if (contentY + ROW_HEIGHT < top() || contentY > top() + VIEW_HEIGHT) continue;

            if (row.isHeader()) {
                ctx.drawText(textRenderer, row.category().getDisplayName(), x + 5, contentY + 5, 0xFFFFFF, false);
            } else {
                drawEntry(ctx, row.recipe(), contentY, blazeOk, row == hovered);
            }
        }
        ctx.disableScissor();

        scrollBar.render(ctx, mouseX, mouseY);
        if (hovered != null && !hovered.isHeader()) drawTooltip(ctx, hovered, mouseX, mouseY);
    }

    private void drawEntry(DrawContext ctx, PotionRecipe recipe, int contentY, boolean blazeOk, boolean hovered) {
        if (hovered) ctx.fill(x, contentY, x + CONTENT_WIDTH, contentY + ROW_HEIGHT, 0x80FFFFFF);

        drawIngredient(ctx, recipe.getBasePotion(), x + BASE_X, contentY + 2);
        ctx.drawText(textRenderer, "+", x + PLUS_X, contentY + 6, 0xFFFFFF, false);
        drawIngredient(ctx, recipe.getIngredient(), x + INGREDIENT_X, contentY + 2);
        ctx.drawText(textRenderer, "→", x + ARROW_X, contentY + 6, blazeOk ? 0xFFFFFF : 0xFF5555, false);
        ctx.drawItem(recipe.getResult(), x + RESULT_X, contentY + 2);
    }

    private void drawIngredient(DrawContext ctx, ItemStack stack, int itemX, int itemY) {
        ctx.drawItem(stack, itemX, itemY);
        if (!inventory.has(stack)) ctx.fill(itemX, itemY, itemX + ITEM_SIZE, itemY + ITEM_SIZE, 0x80FF0000);
    }

    private void drawTooltip(DrawContext ctx, Row row, int mouseX, int mouseY) {
        PotionRecipe recipe = row.recipe();
        int itemY = rowY(row) + 2;

        ItemStack hoveredItem = null;
        if (within(mouseX, mouseY, x + BASE_X, itemY, ITEM_SIZE, ITEM_SIZE)) {
            hoveredItem = recipe.getBasePotion();
        } else if (within(mouseX, mouseY, x + INGREDIENT_X, itemY, ITEM_SIZE, ITEM_SIZE)) {
            hoveredItem = recipe.getIngredient();
        } else if (within(mouseX, mouseY, x + RESULT_X, itemY, ITEM_SIZE, ITEM_SIZE)) {
            hoveredItem = recipe.getResult();
        }

        if (hoveredItem != null) ctx.drawItemTooltip(textRenderer, hoveredItem, mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (searchField.mouseClicked(mouseX, mouseY, button)) {
            searchField.setFocused(true);
            return true;
        }
        if (button != 0) return false;
        if (scrollBar.mouseClicked(mouseX, mouseY)) return true;

        Row row = rowAt(mouseX, mouseY);
        if (row == null || row.isHeader()) return false;

        PotionRecipe recipe = row.recipe();
        if (inventory.canBrew(recipe)) {
            playSound(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            inventory.fill(recipe);
        } else {
            playSound(PositionedSoundInstance.master(SoundEvents.ENTITY_VILLAGER_NO, 1.0F));
        }
        return true;
    }

    public boolean mouseDragged(double mouseY) {
        return scrollBar.mouseDragged(mouseY);
    }

    public boolean mouseReleased() {
        return scrollBar.mouseReleased();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!list.canScroll() || !overView(mouseX, mouseY)) return false;
        list.scrollBy(-(int) (amount * ROW_HEIGHT));
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!searchField.isFocused()) return false;

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            searchField.setFocused(false);
            return true;
        }
        searchField.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    public boolean charTyped(char chr, int modifiers) {
        return searchField.isFocused() && searchField.charTyped(chr, modifiers);
    }

    private void playSound(PositionedSoundInstance instance) {
        client.getSoundManager().play(instance);
    }
}