package net.kapitencraft.kap_lib.client.font.effect.effects;

import net.kapitencraft.kap_lib.client.font.effect.EffectSettings;
import net.kapitencraft.kap_lib.client.font.effect.GlyphEffect;
import net.kapitencraft.kap_lib.config.ClientModConfig;

public class RainbowEffect extends GlyphEffect {
    @Override
    public void apply(EffectSettings settings) {
        if (settings.isShadow) {
            return;
        }
        settings.r = makeLocationData() / 255f;
        settings.g = ClientModConfig.getChromaSpacing() / 255f;
        settings.b = ClientModConfig.getChromaSpeed() / 255f;
    }

    private static int makeLocationData() {
        int chromaType = ClientModConfig.getChromaType().getConfigId();
        int chromaOrigin = ClientModConfig.getChromaOrigin().getConfigId();
        return (chromaType << 2) | chromaOrigin;
    }

    @Override
    public char getKey() {
        return 'z';
    }
}
