package net.kapitencraft.kap_lib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class BannerPatternRenderer {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final ModelPart FLAG;


    static {
        FLAG = MINECRAFT.getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
        FLAG.xRot = 0.0F;
        FLAG.y = -32.0F;
    }

    /**
     * @return the flag used by the renderer; do <i>not</i> modify
     */
    public static ModelPart getFlag() {
        return FLAG;
    }

    public static void renderBanner(GuiGraphics graphics, float x, float y, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, int height) {
        MultiBufferSource.BufferSource source = MINECRAFT.renderBuffers().bufferSource();
        graphics.pose().pushPose();
        graphics.pose().translate(x, y + height, 0.0F);
        float scale = height / 40f;
        graphics.pose().scale(24.0F * scale, -24.0F * scale, 1.0F);
        graphics.pose().translate(0.5F, 0.5F, 0);
        float f = 2 / 3f;
        graphics.pose().scale(f, -f, -f);
        BannerRenderer.renderPatterns(graphics.pose(), source, 15728880, OverlayTexture.NO_OVERLAY, FLAG, ModelBakery.BANNER_BASE, true, patterns);
        graphics.pose().popPose();
        source.endBatch();
    }

    public static void renderBannerFromStack(GuiGraphics graphics, int x, int y, ItemStack stack, int height) {
        renderBanner(graphics, x, y, fromStack(stack), height);
    }

    public static List<Pair<Holder<BannerPattern>, DyeColor>> fromStack(ItemStack bannerStack) {
        BannerItem banner = (BannerItem) bannerStack.getItem();
        return BannerBlockEntity.createPatterns(banner.getColor(), BannerBlockEntity.getItemPatterns(bannerStack));
    }
}
