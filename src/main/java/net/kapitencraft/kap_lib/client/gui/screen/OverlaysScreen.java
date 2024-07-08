package net.kapitencraft.kap_lib.client.gui.screen;

import net.kapitencraft.kap_lib.client.LibClient;
import net.kapitencraft.kap_lib.client.overlay.OverlayController;
import net.kapitencraft.kap_lib.client.overlay.box.InteractiveBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class OverlaysScreen extends MenuableScreen {
    private final OverlayController controller = LibClient.controller;
    private final List<InteractiveBox> boxes = new ArrayList<>();

    public OverlaysScreen() {
        super(Component.translatable("change_gui_locations.title"));
    }

    @Override
    protected void init() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        boxes.clear();
        controller.fillRenderBoxes(boxes::add, player, font, width, height);
        //boxes.add(new ScreenDebugBox());
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
        return list.size() > 0;
    }

    @Override
    public boolean mouseReleased(double x, double y, int z) {
        List<InteractiveBox> list = getHovering(x, y);
        list.forEach(interactiveBox -> interactiveBox.mouseRelease(x, y));
        return list.size() > 0;
    }

    private List<InteractiveBox> getHovering(double x, double y) {
        return boxes.stream().filter(interactiveBox -> interactiveBox.isMouseOver(x, y)).toList();
    }

    @Override
    public void mouseMoved(double x, double y) {
        super.mouseMoved(x, y);
        getHovering(x, y).forEach(interactiveBox -> interactiveBox.mouseMove(x, y));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        boxes.forEach(interactiveBox -> interactiveBox.render(graphics, mouseX, mouseY));
        super.render(graphics, mouseX, mouseY, delta);
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