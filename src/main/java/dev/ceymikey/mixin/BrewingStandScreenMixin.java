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
package dev.ceymikey.mixin;

import dev.ceymikey.PotionRecipeRegistry;
import dev.ceymikey.potion.PotionCategory;
import dev.ceymikey.potion.PotionRecipe;
import dev.ceymikey.interfaces.ISearchFieldProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenMixin extends HandledScreen<BrewingStandScreenHandler> implements ISearchFieldProvider {
    private static final int RECIPE_PANEL_WIDTH = 120;
    private List<PotionRecipe> allRecipes;
    private float scrollOffset = 0.0F;
    private boolean isScrolling = false;
    private int visibleRecipes = 10;
    private boolean canScroll = false;

    // Search field
    private TextFieldWidget searchField;
    private String searchText = "";

    // Organized recipes by category
    private Map<PotionCategory, List<PotionRecipe>> recipesByCategory = new HashMap<>();

    public BrewingStandScreenMixin(BrewingStandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    /**
     * Inherits from {@link ISearchFieldProvider} to provide access to the search field.
     * @return returns if the user is focused on the search field AKA typing.
     */
    @Override
    public boolean isSearchFieldFocused() {
        return this.searchField != null && this.searchField.isFocused();
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // Loads all of our registered recipes
        this.allRecipes = PotionRecipeRegistry.getAllRecipes();

        // Organize recipes by category
        organizeRecipesByCat();

        // Creates search field
        int leftPos = (this.width - this.backgroundWidth) / 2;
        int topPos = (this.height - this.backgroundHeight) / 2;

        this.searchField = new TextFieldWidget(
                this.textRenderer,
                leftPos + 176 + 5,
                topPos + 5,
                RECIPE_PANEL_WIDTH - 10,
                14,
                Text.literal("Search")
        );

        this.searchField.setMaxLength(50);
        this.searchField.setVisible(true);
        this.searchField.setFocusUnlocked(true);
        this.searchField.setEditable(true);

        this.addSelectableChild(this.searchField);

        updateScrollState();
    }

    private void organizeRecipesByCat() {
        // Clear existing categories
        this.recipesByCategory.clear();

        // Initialize category lists
        for (PotionCategory category : PotionCategory.values()) {
            this.recipesByCategory.put(category, new ArrayList<>());
        }

        // Organize recipes by category
        for (PotionRecipe recipe : this.allRecipes) {
            PotionCategory category = recipe.getCategory();
            List<PotionRecipe> categoryRecipes = this.recipesByCategory.get(category);
            if (categoryRecipes != null) {
                categoryRecipes.add(recipe);
            }
        }
    }

    private void updateScrollState() {
        // Calculate total height needed for all recipes with category headers
        int totalHeight = calculateTotalContentHeight();

        // Calculate visible height
        int visibleHeight = visibleRecipes * 20;

        // Can scroll if total height exceeds visible height
        this.canScroll = totalHeight > visibleHeight;

        // Ensure scroll offset is within valid range
        if (!this.canScroll) {
            this.scrollOffset = 0.0F;
        } else {
            // Calculate maximum valid scroll offset
            float maxScrollOffset = (float)(totalHeight - visibleHeight) / totalHeight;
            this.scrollOffset = Math.min(this.scrollOffset, maxScrollOffset);
        }
    }

    private int calculateTotalContentHeight() {
        int height = 0;

        for (PotionCategory category : PotionCategory.values()) {
            List<PotionRecipe> recipes = getFilteredRecipesForCategory(category);
            if (!recipes.isEmpty()) {
                // Category header
                height += 20;
                // Recipes
                height += recipes.size() * 20;
                // Space after category
                height += 10;
            }
        }

        return height;
    }

    private List<PotionRecipe> getFilteredRecipesForCategory(PotionCategory category) {
        List<PotionRecipe> result = new ArrayList<>();
        List<PotionRecipe> categoryRecipes = this.recipesByCategory.get(category);

        if (categoryRecipes == null || categoryRecipes.isEmpty()) {
            return result;
        }

        // If the search field is empty we just return all of our potion recipe's
        if (this.searchText.isEmpty()) {
            return categoryRecipes;
        }

        // If the search field is NOT empty we handle the text to filter the recipe's.
        // Quick note: We can filter based of the result, ingredient or the base item name.
        // For example we can check for a fire res potion or magma cream (which is used to brew that potion).
        String searchLower = this.searchText.toLowerCase();
        for (PotionRecipe recipe : categoryRecipes) {
            String resultName = recipe.getResult().getName().getString().toLowerCase();
            String ingredientName = recipe.getIngredient().getName().getString().toLowerCase();
            String baseName = recipe.getBasePotion().getName().getString().toLowerCase();

            if (resultName.contains(searchLower) ||
                    ingredientName.contains(searchLower) ||
                    baseName.contains(searchLower)) {
                result.add(recipe);
            }
        }

        return result;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int leftPos = (this.width - this.backgroundWidth) / 2;
        int topPos = (this.height - this.backgroundHeight) / 2;

        // Draw guide background
        context.fill(leftPos + 176, topPos, leftPos + 176 + RECIPE_PANEL_WIDTH, topPos + this.backgroundHeight, 0x80000000);

        // Draw guide title
        context.drawText(this.textRenderer, "Potion Recipes", leftPos + 180, topPos - 10, 0xFFFFFF, false);

        if (this.searchField != null) {
            this.searchField.render(context, mouseX, mouseY, delta);

            String currentText = this.searchField.getText();
            if (!currentText.equals(this.searchText)) {
                this.searchText = currentText;
                updateScrollState();
            }
        }

        // Draw guide recipes by category
        drawRecipesByCat(context, leftPos + 176, topPos + 25, mouseX, mouseY);

        // Draw guide scrollbar if needed.
        if (this.canScroll) {
            drawScrollbar(context, leftPos + 176 + RECIPE_PANEL_WIDTH - 10, topPos + 25, mouseX, mouseY);
        }

        // Draw guide "No results" message if needed
        boolean hasAnyRecipes = false;
        for (PotionCategory category : PotionCategory.values()) {
            if (!getFilteredRecipesForCategory(category).isEmpty()) {
                hasAnyRecipes = true;
                break;
            }
        }

        if (!hasAnyRecipes) {
            context.drawText(this.textRenderer, "No matching recipes", leftPos + 180, topPos + 80, 0xFFFFFF, false);
        }
    }

    @Unique
    private void drawScrollbar(DrawContext context, int x, int y, int mouseX, int mouseY) {
        int scrollbarHeight = 140;
        int totalHeight = calculateTotalContentHeight();
        int visibleHeight = 140;

        // Only draw scrollbar if needed
        // If all the recipes fit in the visible view we return.
        if (totalHeight <= visibleHeight) {
            return;
        }

        // Calculate scroll handle size and position
        int scrollHandleHeight = Math.max(20, scrollbarHeight * visibleHeight / totalHeight);

        // Calculate maximum scroll range in pixels
        int maxScrollPixels = totalHeight - visibleHeight;

        // Calculate current scroll position as a fraction of the maximum
        float scrollFraction = (float)(this.scrollOffset * 20) / maxScrollPixels;

        // Clamp scroll fraction to valid range
        scrollFraction = Math.max(0.0F, Math.min(1.0F, scrollFraction));

        // Calculate scroll handle position
        int scrollHandleY = y + (int)(scrollFraction * (scrollbarHeight - scrollHandleHeight));

        // Draw guide scrollbar background
        context.fill(x, y, x + 6, y + scrollbarHeight, 0x40000000);

        // Draw guide scrollbar handle
        boolean isHovered = mouseX >= x && mouseX <= x + 6 &&
                mouseY >= scrollHandleY && mouseY <= scrollHandleY + scrollHandleHeight;
        context.fill(x, scrollHandleY, x + 6, scrollHandleY + scrollHandleHeight,
                isHovered ? 0xFFAAAAAA : 0xFF888888);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.searchField != null && this.searchField.isFocused()) {
            if (this.searchField.charTyped(chr, modifiers)) {
                // Update search text when character is typed
                this.searchText = this.searchField.getText();
                // Resets scroll position when search changes
                this.scrollOffset = 0;
                return true;
            }
        }
        return super.charTyped(chr, modifiers);
    }

    /* This method draws all recipes by category */
    @Unique
    private void drawRecipesByCat(DrawContext context, int x, int y, int mouseX, int mouseY) {
        int contentWidth = RECIPE_PANEL_WIDTH - 15;

        // Set a fixed height for the visible area
        int visibleHeight = 140;

        // Enable scissor to prevent drawing outside the visible area
        context.enableScissor(x, y, x + contentWidth, y + visibleHeight);

        // Calculate the scroll offset in pixels (simple linear scrolling)
        int scrollY = (int)(this.scrollOffset * 20);

        // Start drawing from the scrolled position
        int currentY = y - scrollY;

        // Loops through all categories and recipes
        for (PotionCategory category : PotionCategory.values()) {
            List<PotionRecipe> categoryRecipes = getFilteredRecipesForCategory(category);
            if (categoryRecipes.isEmpty()) {
                continue;
            }

            // Draw category header
            context.drawText(this.textRenderer, category.getDisplayName(), x + 5, currentY + 5, 0xFFFFFF, false);
            currentY += 20;

            // Draw recipes in this category
            for (PotionRecipe recipe : categoryRecipes) {
                // Check if this recipe is in the visible area
                if (currentY + 20 >= y && currentY <= y + visibleHeight) {
                    // Check if mouse is hovering over this recipe
                    boolean isHovered = mouseX >= x && mouseX <= x + contentWidth &&
                            mouseY >= currentY && mouseY <= currentY + 20;

                    // Draw highlight if hovered
                    // How else are we supposed to see where we are focusing...
                    if (isHovered) {
                        context.fill(x, currentY, x + contentWidth, currentY + 20, 0x80FFFFFF);
                    }

                    // Calculate better spacing to use the full width
                    int baseX = x + 5;
                    int plusX = x + 30;
                    int ingredientX = x + 45;
                    int arrowX = x + 70;
                    int resultX = x + 85;

                    boolean hasBasePotion = findRequirementsPlayerInv(recipe.getBasePotion()) != -1;
                    boolean hasIngredient = findRequirementsPlayerInv(recipe.getIngredient()) != -1;
                    boolean hasBlazePowder = hasFuel() || findRequirementsPlayerInv(new ItemStack(Items.BLAZE_POWDER)) != -1;

                    // Draw recipe items with better spacing and color based on availability
                    drawItemAvailability(context, recipe.getBasePotion(), baseX, currentY + 2, hasBasePotion);
                    context.drawText(this.textRenderer, "+", plusX, currentY + 6, 0xFFFFFF, false);
                    drawItemAvailability(context, recipe.getIngredient(), ingredientX, currentY + 2, hasIngredient);

                    // Draw arrow in red if missing blaze powder
                    int arrowColor = hasBlazePowder ? 0xFFFFFF : 0xFF5555;
                    context.drawText(this.textRenderer, "→", arrowX, currentY + 6, arrowColor, false);

                    // Always draw result in normal color
                    context.drawItem(recipe.getResult(), resultX, currentY + 2);
                }

                currentY += 20;
            }

            // Add some space after each category
            currentY += 10;
        }

        context.disableScissor();

        // Draw tooltips AFTER disabling scissor to allow them to render properly
        currentY = y - scrollY;

        for (PotionCategory category : PotionCategory.values()) {
            List<PotionRecipe> categoryRecipes = getFilteredRecipesForCategory(category);
            if (categoryRecipes.isEmpty()) {
                continue;
            }

            // Skip category header
            currentY += 20;

            // Draw tooltips for recipes in this category
            for (PotionRecipe recipe : categoryRecipes) {
                // Check if this recipe is in the visible area
                if (currentY + 20 >= y && currentY <= y + visibleHeight) {
                    // Calculate item positions
                    int baseX = x + 5;
                    int ingredientX = x + 45;
                    int resultX = x + 85;

                    // Draw tooltips for individual items if hovering over them
                    if (mouseX >= baseX && mouseX <= baseX + 16 && mouseY >= currentY + 2 && mouseY <= currentY + 18) {
                        context.drawItemTooltip(this.textRenderer, recipe.getBasePotion(), mouseX, mouseY);
                    } else if (mouseX >= ingredientX && mouseX <= ingredientX + 16 && mouseY >= currentY + 2 && mouseY <= currentY + 18) {
                        context.drawItemTooltip(this.textRenderer, recipe.getIngredient(), mouseX, mouseY);
                    } else if (mouseX >= resultX && mouseX <= resultX + 16 && mouseY >= currentY + 2 && mouseY <= currentY + 18) {
                        context.drawItemTooltip(this.textRenderer, recipe.getResult(), mouseX, mouseY);
                    }
                }

                currentY += 20;
            }

            // Add some space after each category
            currentY += 10;
        }
    }

    private void drawItemAvailability(DrawContext context, ItemStack stack, int x, int y, boolean available) {
        // Draw the item normally
        context.drawItem(stack, x, y);

        // If the item is not present in the players inventory,
        // draw a red overlay to show the player that they miss that item.
        if (!available) {
            context.fill(x, y, x + 16, y + 16, 0x80FF0000);
        }
    }

    /**
     * Checks if the player has blaze powder in their inventory
     * @return true if blaze powder is found
     */
    private boolean hasBlazePowder() {
        ItemStack blazePowder = new ItemStack(Items.BLAZE_POWDER);
        return findRequirementsPlayerInv(blazePowder) != -1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // If the search field is focused
        if (this.searchField != null && this.searchField.isFocused()) {
            // Handle Escape key to just unfocus the search field
            if (keyCode == 256) { // Escape key
                this.searchField.setFocused(false);
                return true;
            }

            // Let the search field try to handle the key
            boolean handled = this.searchField.keyPressed(keyCode, scanCode, modifiers);

            // If the key is E (inventory key), prevent it from closing the screen
            // even if the search field didn't handle it
            if (keyCode == 69) { // E key code
                return true;
            }

            return handled;
        }

        // If search field is not focused, use default behavior
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle search field clicks
        if (this.searchField != null && this.searchField.mouseClicked(mouseX, mouseY, button)) {
            // Force focus on the search field
            this.searchField.setFocused(true);
            return true;
        }

        int leftPos = (this.width - this.backgroundWidth) / 2;
        int topPos = (this.height - this.backgroundHeight) / 2;

        // Check if clicked on scrollbar
        // If so we scroll to that click point immediately.
        if (this.canScroll &&
                mouseX >= leftPos + 176 + RECIPE_PANEL_WIDTH - 10 &&
                mouseX <= leftPos + 176 + RECIPE_PANEL_WIDTH - 4 &&
                mouseY >= topPos + 25 && mouseY <= topPos + 25 + 140) {
            this.isScrolling = true;
            updateScrollPos(mouseY, topPos);
            return true;
        }

        // Check if clicked in recipe panel area
        if (mouseX >= leftPos + 176 && mouseX <= leftPos + 176 + RECIPE_PANEL_WIDTH - 15 &&
                mouseY >= topPos + 25 && mouseY <= topPos + 25 + 140) {
            // Find which recipe was clicked (if any)
            PotionRecipe clickedRecipe = getRecipeAtPosition(mouseX, mouseY, leftPos, topPos);

            if (clickedRecipe != null) {
                boolean hasBasePotion = findRequirementsPlayerInv(clickedRecipe.getBasePotion()) != -1;
                boolean hasIngredient = findRequirementsPlayerInv(clickedRecipe.getIngredient()) != -1;
                boolean hasBlazePowder = hasFuel() ||
                        findRequirementsPlayerInv(new ItemStack(Items.BLAZE_POWDER)) != -1;

                // Check if the player has all of the required items
                if (hasBasePotion && hasIngredient && hasBlazePowder) {
                    // Play a click sound when they do
                    playSound(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                    // Try to set up the brewing stand with this recipe
                    setupBrewingStand(clickedRecipe);
                    return true;
                } else {
                    // Player lacks items so we play the sound of depression.
                    playSound(PositionedSoundInstance.master(SoundEvents.ENTITY_VILLAGER_NO, 1.0F));
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    // This basically handles what recipes should be shown based on how far
    // the player has scrolled down or up.
    private PotionRecipe getRecipeAtPosition(double mouseX, double mouseY, int leftPos, int topPos) {
        int x = leftPos + 176;
        int y = topPos + 25;
        int contentWidth = RECIPE_PANEL_WIDTH - 15;

        // Calculate the scroll offset in pixels
        int scrollY = (int)(this.scrollOffset * 20);

        // Start from the scrolled position
        int currentY = y - scrollY;

        // Check each category and its recipes
        for (PotionCategory category : PotionCategory.values()) {
            List<PotionRecipe> categoryRecipes = getFilteredRecipesForCategory(category);
            if (categoryRecipes.isEmpty()) {
                continue;
            }

            // Skip category header if not clicked
            currentY += 20;

            // Check recipes in this category
            for (PotionRecipe recipe : categoryRecipes) {
                // Check if mouse is over this recipe
                if (mouseY >= currentY && mouseY <= currentY + 20 &&
                        mouseX >= x && mouseX <= x + contentWidth) {
                    return recipe;
                }

                currentY += 20;
            }

            // Skip space after category
            // You know, to keep it clean.
            currentY += 10;
        }

        return null;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isScrolling) {
            updateScrollPos(mouseY, (this.height - this.backgroundHeight) / 2);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateScrollPos(double mouseY, int topPos) {
        int scrollbarHeight = 140;
        int totalHeight = calculateTotalContentHeight();
        int visibleHeight = 140;

        if (totalHeight <= visibleHeight) {
            this.scrollOffset = 0;
            return;
        }

        // Calculate scrollbar handle height
        int scrollHandleHeight = Math.max(20, scrollbarHeight * visibleHeight / totalHeight);

        // Calculate usable scrollbar height
        int usableScrollbarHeight = scrollbarHeight - scrollHandleHeight;

        // Calculate relative mouse position in scrollbar
        float relativeY = (float)(mouseY - (topPos + 25));

        // Convert to scroll fraction
        float scrollFraction = relativeY / usableScrollbarHeight;
        scrollFraction = Math.max(0.0F, Math.min(1.0F, scrollFraction));

        // Calculate maximum scroll in items
        int maxScrollItems = (totalHeight - visibleHeight) / 20;

        // Set scroll offset in items
        this.scrollOffset = Math.round(scrollFraction * maxScrollItems);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.isScrolling) {
            this.isScrolling = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    //? if =1.20.4 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // Only handle scrolling when mouse is over the recipe panel
        int leftPos = (this.width - this.backgroundWidth) / 2;
        int topPos = (this.height - this.backgroundHeight) / 2;

        if (mouseX >= leftPos + 176 && mouseX <= leftPos + 176 + RECIPE_PANEL_WIDTH &&
                mouseY >= topPos + 25 && mouseY <= topPos + 25 + 140) {

            // Calculate total content height
            int totalHeight = calculateTotalContentHeight();
            int visibleHeight = 140;

            if (totalHeight > visibleHeight) {
                int maxScroll = (totalHeight - visibleHeight) / 20;

                this.scrollOffset = Math.max(0, Math.min(maxScroll, this.scrollOffset - (int)verticalAmount));
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
    //?} elif =1.20.1 {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // Only handle scrolling when mouse is over the recipe panel
        int leftPos = (this.width - this.backgroundWidth) / 2;
        int topPos = (this.height - this.backgroundHeight) / 2;

        if (mouseX >= leftPos + 176 && mouseX <= leftPos + 176 + RECIPE_PANEL_WIDTH &&
                mouseY >= topPos + 25 && mouseY <= topPos + 25 + 140) {

            // Calculate total content height
            int totalHeight = calculateTotalContentHeight();
            int visibleHeight = 140;

            if (totalHeight > visibleHeight) {
                int maxScroll = (totalHeight - visibleHeight) / 20;

                this.scrollOffset = Math.max(0, Math.min(maxScroll, this.scrollOffset - (int)amount));
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, amount);
    }
*///?}

    /**
     * Simple method to play sounds at distance
     * Mostly used for recipe clicks.
     */
    @Unique
    private void playSound(PositionedSoundInstance instance) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getSoundManager().play(instance);
    }

    // Sets up the brewing stand with the recipe
    // So moving all of the ingredients from the player's inv to the stand
    private void setupBrewingStand(PotionRecipe recipe) {
        // Get the slots from the brewing stand
        BrewingStandScreenHandler handler = this.handler;

        // Ingredient slot is at index 3
        Slot ingredientSlot = handler.slots.get(3);

        // Potion slots are at indices 0, 1, 2
        Slot[] potionSlots = new Slot[] {
                handler.slots.get(0),
                handler.slots.get(1),
                handler.slots.get(2)
        };

        ItemStack ingredientStack = recipe.getIngredient();
        int ingredientSlotId = findRequirementsPlayerInv(ingredientStack);

        if (ingredientSlotId != -1) {
            // Move the ingredient to the ingredient slot
            this.onMouseClick(ingredientSlot, 0, 0, SlotActionType.PICKUP);
            this.onMouseClick(handler.slots.get(ingredientSlotId), 0, 0, SlotActionType.PICKUP);
            this.onMouseClick(ingredientSlot, 0, 0, SlotActionType.PICKUP);
        }

        ItemStack basePotionStack = recipe.getBasePotion();
        int basePotionSlotId = findRequirementsPlayerInv(basePotionStack);

        if (basePotionSlotId != -1) {
            // Find an empty potion slot in the brewing stand
            for (Slot potionSlot : potionSlots) {
                if (!potionSlot.hasStack()) {
                    // Move the base potion to this potion slot.
                    // Only if the player has it in their inventory.
                    this.onMouseClick(handler.slots.get(basePotionSlotId), 0, 0, SlotActionType.PICKUP);
                    this.onMouseClick(potionSlot, 0, 0, SlotActionType.PICKUP);
                    break;
                }
            }
        }

        // Adds blaze powder from the players inventory if needed.
        // And only if the player has fuel in their inventory.
        if (!hasFuel()) {
            int blazePowderSlotId = findRequirementsPlayerInv(new ItemStack(Items.BLAZE_POWDER));
            if (blazePowderSlotId != -1) {
                Slot fuelSlot = handler.slots.get(4);
                this.onMouseClick(handler.slots.get(blazePowderSlotId), 0, 0, SlotActionType.PICKUP);
                this.onMouseClick(fuelSlot, 0, 0, SlotActionType.PICKUP);
                this.onMouseClick(handler.slots.get(blazePowderSlotId), 0, 0, SlotActionType.PICKUP);
            }
        }
    }

    /**
     * Simply checks if the brewing stand has blaze powder
     * in the fuel slot which is located at slot index 4 according
     * to {@link BrewingStandScreenHandler}.
     */
    @Unique
    private boolean hasFuel() {
        Slot fuelSlot = this.handler.slots.get(4);
        return fuelSlot.hasStack() && fuelSlot.getStack().getItem() == Items.BLAZE_POWDER;
    }

    /* Simply loops through players slots and checks for the required items to brew that potion */
    @Unique
    private int findRequirementsPlayerInv(ItemStack targetStack) {
        // Player inventory slots start at index 5 in the brewing stand screen handler
        for (int i = 5; i < this.handler.slots.size(); i++) {
            Slot slot = this.handler.slots.get(i);
            if (slot.hasStack()) {
                ItemStack stack = slot.getStack();

                // For potions, we need to check both the item and the potion type
                // This is to prevent adding the wrong potion type.
                // Because surprise surprise... this does matter much.
                if (targetStack.getItem() == Items.POTION ||
                        targetStack.getItem() == Items.SPLASH_POTION ||
                        targetStack.getItem() == Items.LINGERING_POTION) {

                    // Check if both are the same type of potion
                    if (stack.getItem() == targetStack.getItem() &&
                            PotionUtil.getPotion(stack) == PotionUtil.getPotion(targetStack)) {
                        return i;
                    }
                }
                // For non-potion items we just check normally.
                else if (ItemStack.areItemsEqual(stack, targetStack)) {
                    return i;
                }
            }
        }
        return -1; // Not found
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String currentSearchText = "";
        if (this.searchField != null) {
            currentSearchText = this.searchField.getText();
        }

        super.resize(client, width, height);

        int leftPos = (this.width - this.backgroundWidth) / 2;
        int topPos = (this.height - this.backgroundHeight) / 2;

        this.searchField = new TextFieldWidget(
                this.textRenderer,
                leftPos + 176 + 5,
                topPos + 5,
                RECIPE_PANEL_WIDTH - 10,
                14,
                Text.literal("Search")
        );

        this.searchField.setMaxLength(50);
        this.searchField.setVisible(true);
        this.searchField.setFocusUnlocked(true);
        this.searchField.setEditable(true);
        this.searchField.setText(currentSearchText);

        this.addSelectableChild(this.searchField);

        updateScrollState();
    }
}
