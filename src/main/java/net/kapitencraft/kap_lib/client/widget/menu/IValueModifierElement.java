package net.kapitencraft.kap_lib.client.widget.menu;

public interface IValueModifierElement<K extends IValueModifierElement<K , T>, T> {

    void setValue(T value);
}
