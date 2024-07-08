package net.kapitencraft.kap_lib.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * use to indicate the fetch or loading of data
 * removed as soon as other screen is opened
 */
public class AwaitScreen extends DefaultBackgroundScreen {
    public AwaitScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        Component widthDef = ((MutableComponent) this.title).append("...");
        int x = this.leftPos + this.width / 2 - this.font.width(widthDef) / 2;
        int y = this.topPos + this.height / 2 - 5;
        Component actual = ((MutableComponent) this.title).append(".".repeat(((int) pPartialTick) % 3));
        graphics.drawString(this.font, actual, x, y, -1);
    }
}
