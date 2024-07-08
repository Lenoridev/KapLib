package net.kapitencraft.kap_lib.client.overlay.holder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kapitencraft.kap_lib.client.overlay.PositionHolder;
import net.kapitencraft.kap_lib.client.overlay.box.ResizeBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

public abstract class RenderHolder {
    private final PositionHolder pos;

    public RenderHolder(PositionHolder holder) {
        this.pos = holder;
    }

    public Vec2 getLoc(float screenWidth, float screenHeight) {
        return pos.getLoc(screenWidth, screenHeight);
    }

    public void move(Vec2 toAdd) {
        this.pos.add(toAdd);
    }

    protected void renderString(GuiGraphics graphics, Component toWrite, float y) {
        graphics.drawString(Minecraft.getInstance().font, toWrite, 0, (int) y, -1);
    }

    public ResizeBox newBox(float screenWidth, float screenHeight, LocalPlayer player, Font font) {
        Vec2 loc = this.getLoc(screenWidth, screenHeight);
        float width = this.getWidth(player, font) * this.pos.getXScale();
        float height = this.getHeight(player, font) * this.pos.getYScale();
        return new ResizeBox(loc.add(new Vec2(-1, -2)), loc.add(new Vec2(width + 1, height)), this);
    }

    public abstract float getWidth(LocalPlayer player, Font font);
    public abstract float getHeight(LocalPlayer player, Font font);


    public abstract void render(GuiGraphics stack, float posX, float posY, LocalPlayer player);

    public PositionHolder getPos() {
        return pos;
    }
}
