package net.kapitencraft.kap_lib.client.gui.screen.tooltip;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class HoverTooltip {
    private final int xOffsetStart;
    private final int yOffsetStart;
    private final int xSize;
    private final int ySize;
    protected List<Component> text;

    public HoverTooltip(int xOffsetStart, int yOffsetStart, int xSize, int ySize, List<Component> text) {
        this.xOffsetStart = xOffsetStart;
        this.yOffsetStart = yOffsetStart;
        this.xSize = xSize;
        this.ySize = ySize;
        this.text = text;
    }

    public boolean matches(int xPos, int yPos, int xMousePos, int yMousePos) {
        return matchesX(xPos, xMousePos) && matchesY(yPos, yMousePos);
    }

    public ImageButton createButton(ResourceLocation imageLocation, int leftPos, int topPos, Button.OnPress task) {
        return new ImageButton(leftPos + xOffsetStart, topPos + yOffsetStart, 16, 16, 0, 0, 16, imageLocation, 16, 16, task);
    }

    public List<Component> getText() {
        return text;
    }

    private boolean matchesX(int xPos, int xMousePos) {
        return xMousePos >= xPos + xOffsetStart && xMousePos <= xPos + xOffsetStart + xSize;
    }
    private boolean matchesY(int yPos, int yMousePos) {
        return yMousePos >= yPos + yOffsetStart && yMousePos <= yPos + yOffsetStart + ySize;
    }

}
