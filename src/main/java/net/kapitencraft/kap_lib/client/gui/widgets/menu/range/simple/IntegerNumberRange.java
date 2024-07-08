package net.kapitencraft.kap_lib.client.gui.widgets.menu.range.simple;

public class IntegerNumberRange extends NumberRange<Integer> {
    private final int min;
    private final int max;

    public IntegerNumberRange(int min, int max) {
        this.min = min;
        this.max = max;
    }


    @Override
    public boolean test(Integer integer) {
        return min < integer && integer < max;
    }
}
