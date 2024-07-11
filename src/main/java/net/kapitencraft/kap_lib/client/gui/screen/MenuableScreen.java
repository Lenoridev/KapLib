package net.kapitencraft.kap_lib.client.gui.screen;

import net.kapitencraft.kap_lib.client.gui.IMenuBuilder;
import net.kapitencraft.kap_lib.client.widget.menu.Menu;
import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuableScreen extends Screen {
    private Menu active;
    protected MenuableScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void render(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        MiscHelper.ifNonNull(this.active, menu -> menu.render(pPoseStack, pMouseX, pMouseY, pPartialTick));
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        MiscHelper.ifNonNull(this.active, menu -> menu.mouseMoved(pMouseX, pMouseY));
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        GuiEventListener listener = null;

        if (this.getFocused() instanceof Menu && this.getFocused().mouseClicked(pMouseX, pMouseY, pButton)) {
            listener = this.getFocused();
        } else for (GuiEventListener eventListener : List.copyOf(this.children()))
            if (eventListener.mouseClicked(pMouseX, pMouseY, pButton)) {
                if (eventListener instanceof IMenuBuilder builder && pButton == 1) {
                    Menu menu = builder.createMenu((int) pMouseX, (int) pMouseY);
                    if (menu != null) {
                        this.active = menu;
                        this.active.show();
                        listener = this.active;
                    }
                } else {
                    listener = eventListener;
                }
            }

        if (listener != this.active && this.active != null) {
            this.active.hide(this);
            this.active = null;
             return true;
        }
        if (listener != null) {
            this.setFocused(listener);
            if (pButton == 0) {
                this.setDragging(true);
            }

            return true;
        } else {
            return false;
        }
    }
}
