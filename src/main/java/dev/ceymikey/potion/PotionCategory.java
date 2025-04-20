package dev.ceymikey.potion;

import net.minecraft.text.Text;

public enum PotionCategory {
    BASE("Base Potions"),
    EFFECT("Effect Potions"),
    EXTENDED("Extended Duration"),
    ENHANCED("Enhanced Potency");

    private final String displayName;

    PotionCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Text getText() {
        return Text.of(this.displayName);
    }
}
