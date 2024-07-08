package net.kapitencraft.kap_lib.client.overlay.box;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.glfw.GLFW;

public class ScreenDebugBox extends InteractiveBox {
    public ScreenDebugBox() {
        super(Vec2.ZERO, Vec2.ZERO, GLFW.GLFW_ARROW_CURSOR, new PoseStack(), 0xffffffff, null);
    }

    @Override
    public void render(GuiGraphics graphics, double mouseX, double mouseY) {
        float screenX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        float screenY = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        Font font = Minecraft.getInstance().font;

        graphics.drawString(font, "Mouse location: " + mouseX + ", " + mouseY, (float) mouseX, (float) mouseY, -1, false);
        graphics.drawString(font, "Screen Height: " + screenX + ", " + screenY, (float) mouseX, (float) (mouseY + 10), -1, false);
    }

    @Override
    public void move(Vec2 toAdd) {
    }
}
