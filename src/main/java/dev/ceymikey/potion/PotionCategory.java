package dev.ceymikey.potion;

import lombok.Getter;

@Getter
public enum PotionCategory {
    BASE("Base Potions"),
    EFFECT("Effect Potions"),
    EXTENDED("Extended Duration"),
    UPGRADED("Upgraded Power");

    private final String displayName;
    PotionCategory(String displayName) {
        this.displayName = displayName;
    }
}
