package dev.ceymikey.mixin;

import dev.ceymikey.gui.RecipePanel;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenMixin extends HandledScreen<BrewingStandScreenHandler> {
    @Unique
    private RecipePanel recipePanel;

    public BrewingStandScreenMixin(BrewingStandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (recipePanel == null) {
            recipePanel = new RecipePanel(this.textRenderer, this.handler);
        }
        recipePanel.reposition(this.x + this.backgroundWidth, this.y, this.backgroundHeight);
        this.addSelectableChild(recipePanel.getSearchField());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        recipePanel.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return recipePanel.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return recipePanel.mouseDragged(mouseY) || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return recipePanel.mouseReleased() || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return recipePanel.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return recipePanel.charTyped(chr, modifiers) || super.charTyped(chr, modifiers);
    }

    //? if >=1.20.4 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return recipePanel.mouseScrolled(mouseX, mouseY, verticalAmount);
    }
    //?} elif =1.20.1 {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        super.mouseScrolled(mouseX, mouseY, amount);
        return recipePanel.mouseScrolled(mouseX, mouseY, amount);
    }
    *///?}
}