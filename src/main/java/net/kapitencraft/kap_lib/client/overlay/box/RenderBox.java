package net.kapitencraft.kap_lib.client.overlay.box;

import net.kapitencraft.kap_lib.client.overlay.holder.Overlay;
import net.kapitencraft.kap_lib.helpers.ClientHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public class RenderBox {
    /**
     * the top-left position of this RenderBox relative to the top left of the screen
     */
    public Vec2 start;
    /**
     * the bottom-right position of this RenderBox relative to the top left of the screen
     */
    protected Vec2 finish;
    /**
     * the cursorType which should be swapped to when hovered
     * @see org.lwjgl.glfw.GLFW#GLFW_ARROW_CURSOR Arrow Cursor and others (below)
     */
    private final int cursorType;
    /**
     * the color this box should be rendered in
     */
    private final int color;
    /**
     * contains
     */
    @Nullable
    public final Overlay dedicatedHolder;
    public RenderBox(Vec2 start, Vec2 finish, int cursorType, int color, @Nullable Overlay dedicatedHolder) {
        this.start = start;
        this.finish = finish;
        this.cursorType = cursorType;
        this.color = color;
        this.dedicatedHolder = dedicatedHolder;
    }

    /**
     * @return the width of this Box
     */
    protected float width() {
        return Math.abs(this.finish.x - this.start.x);
    }

    /**
     * @return the height of this box
     */
    protected float height() {
        return Math.abs(this.finish.y - this.start.y);
    }

    /**
     * applies a scaling to this box (increasing or decreasing it in size)
     * @param x the x scale factor
     * @param y the y scale factor
     */
    public void scale(float x, float y) {
        this.dedicatedHolder.getProperties().scale(x, y);
    }

    /**
     * @param mouseX the mouse X position
     * @param mouseY the mouse Y position
     * @return the id of the Cursor type that should be rendered
     */
    public int getCursorType(double mouseX, double mouseY) {
        return cursorType;
    }

    /**
     * moves this box around (change X and Y coordinates)
     * @param toAdd the offset applied to this Box
     */
    public void move(Vec2 toAdd) {
        this.start = this.start.add(toAdd);
        this.finish = this.finish.add(toAdd);
    }

    /**
     * render this Box
     */
    public void render(GuiGraphics graphics, double mouseX, double mouseY) {
        ClientHelper.fill(graphics, start.x, start.y, finish.x, finish.y, color, -1);
    }
}
