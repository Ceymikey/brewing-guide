package dev.ceymikey.gui;

import net.minecraft.client.gui.DrawContext;

import static dev.ceymikey.gui.RecipePanelLayout.VIEW_HEIGHT;
import static dev.ceymikey.gui.RecipePanelLayout.WIDTH;
import static dev.ceymikey.gui.RecipePanelLayout.within;

public class ScrollBar {
    private static final int SCROLLBAR_X = WIDTH - 10;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int MIN_HANDLE_HEIGHT = 20;

    private final RecipeList list;
    private int barX, barY;
    private boolean dragging;

    public ScrollBar(RecipeList list) {
        this.list = list;
    }

    public void reposition(int panelX, int viewTop) {
        this.barX = panelX + SCROLLBAR_X;
        this.barY = viewTop;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY) {
        if (!list.canScroll()) return;

        int handleY = handleY();
        int handleHeight = handleHeight();
        boolean hovered = within(mouseX, mouseY, barX, handleY, SCROLLBAR_WIDTH, handleHeight);

        ctx.fill(barX, barY, barX + SCROLLBAR_WIDTH, barY + VIEW_HEIGHT, 0x40000000);
        ctx.fill(barX, handleY, barX + SCROLLBAR_WIDTH, handleY + handleHeight,
                hovered ? 0xFFAAAAAA : 0xFF888888);
    }

    public boolean mouseClicked(double mouseX, double mouseY) {
        if (!list.canScroll() || !within(mouseX, mouseY, barX, barY, SCROLLBAR_WIDTH, VIEW_HEIGHT)) return false;
        dragging = true;
        scrollToMouse(mouseY);
        return true;
    }

    public boolean mouseDragged(double mouseY) {
        if (!dragging) return false;
        scrollToMouse(mouseY);
        return true;
    }

    public boolean mouseReleased() {
        boolean was = dragging;
        dragging = false;
        return was;
    }

    private int handleHeight() {
        return Math.max(MIN_HANDLE_HEIGHT, VIEW_HEIGHT * VIEW_HEIGHT / list.getContentHeight());
    }

    private int handleY() {
        return barY + (VIEW_HEIGHT - handleHeight()) * list.getScroll() / list.maxScroll();
    }

    private void scrollToMouse(double mouseY) {
        int travel = VIEW_HEIGHT - handleHeight();
        if (travel <= 0) return;
        double fraction = (mouseY - barY - handleHeight() / 2.0) / travel;
        fraction = Math.max(0, Math.min(1, fraction));
        list.setScroll((int) Math.round(fraction * list.maxScroll()));
    }
}