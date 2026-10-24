package dev.ceymikey.gui;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RecipePanelLayout {
    static final int WIDTH = 120;
    static final int CONTENT_WIDTH = WIDTH - 15;
    static final int TOP_PADDING = 25;
    static final int VIEW_HEIGHT = 140;
    static final int ROW_HEIGHT = 20;
    static final int CATEGORY_GAP = 10;

    public static boolean within(double pointX, double pointY, int left, int top, int width, int height) {
        return pointX >= left && pointX <= left + width
                && pointY >= top && pointY <= top + height;
    }
}