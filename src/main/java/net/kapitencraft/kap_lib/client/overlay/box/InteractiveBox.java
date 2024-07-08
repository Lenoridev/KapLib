package net.kapitencraft.kap_lib.client.overlay.box;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kapitencraft.kap_lib.client.overlay.holder.RenderHolder;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.world.phys.Vec2;

public class InteractiveBox extends RenderBox implements GuiEventListener {
    protected InteractiveBox(Vec2 start, Vec2 finish, int cursorType, PoseStack stack, int color, RenderHolder dedicatedHolder) {
        super(start, finish, cursorType, stack, color, dedicatedHolder);
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        return check(start.x, finish.x, x) && check(start.y, finish.y, y);
    }

    private static boolean check(float s, float f, double t) {
        return s < f ? s < t && t < f : s > t && t > f;
    }

    @Override
    public final boolean mouseDragged(double newX, double newY, int clickType, double changeX, double changeY) {
        double oldX = -changeX + newX;
        double oldY = -changeY + newY;
        return this.mouseDrag(newX, newY, clickType, changeX, changeY, oldX, oldY);
    }

    public boolean mouseDrag(double x, double y, int mouseType, double xChange, double yChange, double oldX, double oldY) {
        return false;
    }
    public void mouseClick(double x, double y) {
    }

    public void mouseMove(double x, double y) {
    }

    public void mouseRelease(double x, double y) {
    }

    @Override
    public void setFocused(boolean pFocused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
