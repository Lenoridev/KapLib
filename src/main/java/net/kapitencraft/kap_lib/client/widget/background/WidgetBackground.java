package net.kapitencraft.kap_lib.client.widget.background;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class WidgetBackground {

    public abstract void render(boolean scissorEnabled, GuiGraphics graphics, int x, int y, int width, int height, float offsetX, float offsetY);

    public static WidgetBackground texture(ResourceLocation texture, int texWidth, int texHeight) {
        return new TextureBackground(texture, texWidth, texHeight);
    }
}
