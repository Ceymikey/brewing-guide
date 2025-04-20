package dev.ceymikey.mixin;

import dev.ceymikey.interfaces.ISearchFieldProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Screen self = (Screen)(Object)this;

        if (self instanceof BrewingStandScreen && self instanceof ISearchFieldProvider) {
            ISearchFieldProvider provider = (ISearchFieldProvider)self;

            if (provider.isSearchFieldFocused() && keyCode == 69) {
                cir.setReturnValue(true);
            }
        }
    }
}
