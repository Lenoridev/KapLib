package net.kapitencraft.kap_lib.client.overlay.holder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kapitencraft.kap_lib.client.overlay.PositionHolder;
import net.kapitencraft.kap_lib.helpers.TextHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Function;

public class MultiHolder extends RenderHolder {
    private final float yChange;
    private final List<Function<LocalPlayer, Component>> list;
    public MultiHolder(PositionHolder holder, float yChange, List<Function<LocalPlayer, Component>> allText) {
        super(holder);
        this.yChange = yChange;
        this.list = allText;
    }

    @Override
    public float getWidth(LocalPlayer player, Font font) {
        return TextHelper.getWidthFromMultiple(list.stream().map(func -> func.apply(player)).toList(), font);
    }

    @Override
    public float getHeight(LocalPlayer player, Font font) {
        return list.size() * -yChange;
    }

    @Override
    public void render(GuiGraphics stack, float width, float height, LocalPlayer player) {
        for (int i = 0; i < list.size(); i++) {
            Function<LocalPlayer, Component> mapper = list.get(i);
            renderString(stack, mapper.apply(player), -yChange * i);
        }
    }
}
