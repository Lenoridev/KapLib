package net.kapitencraft.kap_lib.client.overlay.box;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kapitencraft.kap_lib.client.overlay.holder.RenderHolder;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.glfw.GLFW;

public class ResizeAccessBox extends InteractiveBox {
    private final ResizeBox.Type type;
    private final ResizeBox parent;
    protected ResizeAccessBox(Vec2 start, Vec2 finish, int cursorType, PoseStack stack, int color, RenderHolder dedicatedHolder, ResizeBox.Type type, ResizeBox parent) {
        super(start, finish, cursorType, stack, color, dedicatedHolder);
        this.type = type;
        this.parent = parent;
    }

    protected ResizeAccessBox(PoseStack stack, int color, RenderHolder dedicated, ResizeBox.Type type, ResizeBox box) {
        this(Vec2.ZERO, Vec2.ZERO, -1, stack, color, dedicated, type, box);
    }


    protected ResizeBox.Type getType() {
        return type;
    }

    protected int getCursorType() {
        return switch (type) {
            case E,W -> GLFW.GLFW_RESIZE_EW_CURSOR;
            case N,S -> GLFW.GLFW_RESIZE_NS_CURSOR;
            case NW,SE -> GLFW.GLFW_RESIZE_NWSE_CURSOR;
            case NE,SW -> GLFW.GLFW_RESIZE_NESW_CURSOR;
            case C -> GLFW.GLFW_RESIZE_ALL_CURSOR;
        };
    }

    @Override
    public int getCursorType(double mouseX, double mouseY) {
        return getCursorType();
    }

    private static final float lineWidth = 0.5f;
    private static final float squareSize = 1.5f;

    protected void reapplyPosition() {
        Vec2 start = this.parent.start;
        Vec2 finish = this.parent.finish;
        Vec2 bottomLeft = new Vec2(start.x, finish.y);
        Vec2 topRight = new Vec2(finish.x, start.y);
        switch (this.type) {
            case E -> this.applyLine(topRight, finish, lineWidth);
            case SE -> this.applySquare(finish, squareSize);
            case S -> this.applyLine(bottomLeft, finish, lineWidth);
            case SW -> this.applySquare(bottomLeft, squareSize);
            case W -> this.applyLine(start, bottomLeft, lineWidth);
            case NW -> this.applySquare(start, squareSize);
            case N -> this.applyLine(start, topRight, lineWidth);
            case NE -> this.applySquare(topRight, squareSize);
        }
    }

    public void applyLine(Vec2 start, Vec2 finish, float lineW) {
        boolean horizontal = start.x == finish.x;
        this.start = new Vec2(horizontal ? start.x - lineW : start.x, horizontal ? start.y : start.y - lineW);
        this.finish = new Vec2(horizontal ? finish.x + lineW : finish.x, horizontal ? finish.y : finish.y  + lineW);
    }

    public void applySquare(Vec2 center, float size) {
        this.start = center.add(new Vec2(-size, -size));
        this.finish = center.add(new Vec2(size, size));
    }
}