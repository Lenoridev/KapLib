package net.kapitencraft.kap_lib.client.gui.screen;

import net.kapitencraft.kap_lib.client.LibClient;
import net.kapitencraft.kap_lib.client.overlay.OverlayController;
import net.kapitencraft.kap_lib.client.overlay.box.InteractiveBox;
import net.kapitencraft.kap_lib.client.overlay.holder.Overlay;
import net.kapitencraft.kap_lib.client.widget.menu.drop_down.DropDownMenu;
import net.kapitencraft.kap_lib.client.widget.menu.drop_down.elements.MultiElementSelectorElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * change GUI locations screen
 */
public class ChangeOverlayLocationsScreen extends MenuableScreen {
    private final OverlayController controller = LibClient.controller;
    private final List<InteractiveBox> boxes = new ArrayList<>();

    public ChangeOverlayLocationsScreen() {
        super(Component.translatable("change_overlay_locations.title"));
        this.setDefaultMenuBuilder((x, y) -> {
            DropDownMenu menu = new DropDownMenu(x, y, this);
            menu.addElement(
                    MultiElementSelectorElement.builder(Overlay.class)
                            .setElements(controller.map.values())
                            .setStatusMapper(Overlay::isVisible)
                            .setNameMapper(Overlay::getName)
                            .setOnChange(Overlay::setVisible)
            );
            return menu;
        });
    }

    @Override
    protected void init() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        boxes.clear();
        controller.fillRenderBoxes(boxes::add, player, font, width, height);
        super.init();
    }

    @Override
    public @NotNull List<InteractiveBox> children() {
        return boxes;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        List<InteractiveBox> list = getHovering(pMouseX, pMouseY);
        list.forEach(interactiveBox -> interactiveBox.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY));
        return !list.isEmpty();
    }

    @Override
    public boolean mouseReleased(double x, double y, int z) {
        List<InteractiveBox> list = getHovering(x, y);
        list.forEach(interactiveBox -> interactiveBox.mouseRelease(x, y));
        return !list.isEmpty();
    }

    private List<InteractiveBox> getHovering(double x, double y) {
        return boxes.stream().filter(interactiveBox -> interactiveBox.isMouseOver(x, y)).toList();
    }

    @Override
    public void mouseMoved(double x, double y) {
        super.mouseMoved(x, y);
        getHovering(x, y).forEach(interactiveBox -> interactiveBox.mouseMove(x, y));
    }

    /**
     * renders the screen and updates the Cursor Visuals to match size modifiers
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        boxes.forEach(interactiveBox -> interactiveBox.render(graphics, mouseX, mouseY));
        super.render(graphics, mouseX, mouseY, pPartialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFF4F4F4F);
        if (minecraft == null) return;
        int arrowId = boxes.stream().map(box -> box.getCursorType(mouseX, mouseY))
                .filter(i -> i != GLFW.GLFW_ARROW_CURSOR) //ensure to only scan for non-default cursors
                .findFirst().orElse(GLFW.GLFW_ARROW_CURSOR);
        long windowId = minecraft.getWindow().getWindow();
        minecraft.execute(()-> GLFW.glfwSetCursor(windowId, GLFW.glfwCreateStandardCursor(arrowId)));
    }

    @Override
    public void onClose() {
        OverlayController.save();
        super.onClose();
    }
}