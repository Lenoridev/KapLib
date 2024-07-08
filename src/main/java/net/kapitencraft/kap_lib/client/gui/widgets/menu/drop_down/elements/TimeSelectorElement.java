package net.kapitencraft.kap_lib.client.gui.widgets.menu.drop_down.elements;

import net.kapitencraft.kap_lib.client.gui.widgets.menu.drop_down.DropDownMenu;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.function.Consumer;

public class TimeSelectorElement extends ListElement {
    private static final List<Pair<String, Long>> times = List.of(
            new Pair<>("1min", 60000L),
            new Pair<>("2min", 120000L),
            new Pair<>("5min", 300000L),
            new Pair<>("10min", 600000L),
            new Pair<>("20min", 1200000L),
            new Pair<>("30min", 1800000L),
            new Pair<>("1h", 3600000L),
            new Pair<>("2h", 7200000L),
            new Pair<>("5h", 18000000L),
            new Pair<>("1d", 86400000L)
    );
    public TimeSelectorElement(@Nullable ListElement listElement, DropDownMenu menu, Component component, Consumer<Long> useTime) {
        super(listElement, menu, component);
        times.forEach(stringLongPair ->
                this.addElement(new ButtonElement(this, menu, Component.translatable("gui.time." + stringLongPair.getA()), () -> useTime.accept(Util.getMillis() + stringLongPair.getB()))));
    }

}
