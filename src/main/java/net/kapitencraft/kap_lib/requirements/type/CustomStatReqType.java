package net.kapitencraft.kap_lib.requirements.type;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;

import java.util.function.Function;

/**
 * used for custom Stat Types to add custom display-translations
 */
public class CustomStatReqType extends StatReqType {
    private final Function<Integer, Component> display;
    public CustomStatReqType(Stat<ResourceLocation> stat, int level, Function<Integer, Component> display) {
        super(stat, level);
        this.display = display;
    }

    public CustomStatReqType(Stat<ResourceLocation> stat, int level, String translateKey) {
        this(stat, level, integer -> Component.translatable(translateKey, integer));
    }

    @Override
    public Component display() {
        return display.apply(minLevel);
    }
}
