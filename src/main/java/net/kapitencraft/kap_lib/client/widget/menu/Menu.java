package net.kapitencraft.kap_lib.client.widget.menu;

import net.kapitencraft.kap_lib.client.widget.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

/**
 * basic implementation and subclass of all menus
 */
public abstract class Menu extends Widget {
    protected final int x, y;
    private final GuiEventListener parent;

    public Menu(int x, int y, GuiEventListener parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    public abstract void show();

    public void hide(Screen screen) {
        screen.setFocused(parent);
    }

    protected int height() {
        return 0;
    }
}
