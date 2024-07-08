package net.kapitencraft.kap_lib.client.font.effect.effects;

import net.kapitencraft.kap_lib.client.font.effect.EffectSettings;
import net.kapitencraft.kap_lib.client.font.effect.GlyphEffect;
import net.minecraft.Util;
import net.minecraft.util.Mth;

public class ShakeEffect extends GlyphEffect {
    @Override
    public void apply(EffectSettings settings) {
        settings.x += Mth.sin(Util.getMillis() * 0.01F + settings.index) * 2;
    }

    @Override
    public char getKey() {
        return 's';
    }
}
