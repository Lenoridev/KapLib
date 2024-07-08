package net.kapitencraft.kap_lib.client.gui.screen;

import net.kapitencraft.kap_lib.client.widget.MultiLineTextBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TestScreen extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/block/diamond_block.png");
    private MultiLineTextBox textBox;

    public TestScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        final int widgetWidth = 100, widgetHeight = 100;
        textBox = new MultiLineTextBox(this.font, width / 2 - widgetWidth / 2, height / 2 - widgetHeight / 2, widgetWidth, widgetHeight, textBox, Component.empty());
        textBox.setTextureBackground(BACKGROUND);
        this.addRenderableWidget(textBox);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(0, 0, width, height, 0x408F8F8F);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void tick() {
        this.textBox.tick();
    }
}
