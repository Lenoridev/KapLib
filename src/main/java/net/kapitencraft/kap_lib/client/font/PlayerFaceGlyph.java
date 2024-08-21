package net.kapitencraft.kap_lib.client.font;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

public class PlayerFaceGlyph extends BakedGlyph {
    public static final Map<Player, PlayerFac>


    public PlayerFaceGlyph(GlyphRenderTypes pRenderTypes, float pU0, float pU1, float pV0, float pV1, float pLeft, float pRight, float pUp, float pDown) {
        super(pRenderTypes, 0, 0, 0, 0, pLeft, pRight, pUp, pDown);
    }

    @Override
    public void render(boolean pItalic, float pX, float pY, Matrix4f pMatrix, VertexConsumer pBuffer, float pRed, float pGreen, float pBlue, float pAlpha, int pPackedLight) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(pMatrix, pX, pY, 0f).uv(8, 8).endVertex();
        bufferbuilder.vertex(pMatrix, pX, (float)pY2, 0f).uv(8, 16).endVertex();
        bufferbuilder.vertex(pMatrix, (float)pX2, (float)pY2, 0f).uv(16, 16).endVertex();
        bufferbuilder.vertex(pMatrix, (float)pX2, pY, 0f).uv(16, 8).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
