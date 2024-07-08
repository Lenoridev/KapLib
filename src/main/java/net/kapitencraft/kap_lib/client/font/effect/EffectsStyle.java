package net.kapitencraft.kap_lib.client.font.effect;

import net.minecraft.network.chat.Style;

import java.util.List;

/**
 * interface (or <i>duck-class</i>) for {@link GlyphEffect}s
 */
public interface EffectsStyle {

    void addEffect(GlyphEffect effect);
    List<GlyphEffect> getEffects();

    static EffectsStyle of(Style style) {
        return (EffectsStyle) style;
    }
}
