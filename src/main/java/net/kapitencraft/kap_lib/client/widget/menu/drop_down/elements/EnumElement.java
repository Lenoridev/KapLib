package net.kapitencraft.kap_lib.client.widget.menu.drop_down.elements;

import net.kapitencraft.kap_lib.client.widget.menu.IValueModifierElement;
import net.kapitencraft.kap_lib.client.widget.menu.drop_down.DropDownMenu;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class EnumElement<T extends Enum<T>> extends ListElement implements IValueModifierElement<EnumElement<T>, T> {
    private T selected;
    private final Function<T, Component> nameMapper;
    private final Consumer<T> onChange;

    public EnumElement(ListElement parent, DropDownMenu menu, Component component, T[] elements, Function<T, Component> nameMapper, Consumer<T> onChange) {
        super(parent, menu, component);
        this.nameMapper = nameMapper;
        this.onChange = onChange;
        Arrays.stream(elements).map(ListItem::new).forEach(this::addElementInternal);
    }

    private void addElementInternal(Element element) {
        children.add(element);
    }

    @Override
    public void addElement(Element element) {
    }

    public EnumElement<T> value(T value) {
        this.selected = value;
        return this;
    }

    @Override
    public void setValue(T value) {
        this.selected = value;
        this.onChange.accept(this.selected);
    }

    private class ListItem extends BooleanElement {
        private final T id;

        protected ListItem(T id) {
            super(EnumElement.this, EnumElement.this.menu, EnumElement.this.nameMapper.apply(id), (flag) -> {
                if (flag) {
                    EnumElement.this.setValue(id);
                }
            });
            this.id = id;
        }

        @Override
        public boolean selected() {
            return EnumElement.this.selected == this.id;
        }
    }
}