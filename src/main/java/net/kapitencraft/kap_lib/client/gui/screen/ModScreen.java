package net.kapitencraft.kap_lib.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kapitencraft.kap_lib.client.gui.screen.tooltip.HoverTooltip;
import net.kapitencraft.kap_lib.client.gui.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ModScreen<K extends ICapabilityProvider, T extends ModMenu<K>> extends AbstractContainerScreen<T> implements IModScreen {
    public ModScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }
    private final List<HoverTooltip> hoverTooltips = new ArrayList<>();

    protected abstract String getTextureName();

    @Override
    public void addHoverTooltip(HoverTooltip tooltip) {
        this.hoverTooltips.add(tooltip);
    }

    protected void addHoverTooltipAndImgButton(HoverTooltip tooltip, ResourceLocation location, Button.OnPress onPress) {
        this.hoverTooltips.add(tooltip);
        this.addRenderableWidget(tooltip.createButton(location, leftPos, topPos, onPress));
    }


    @Override
    protected void init() {
        super.init();
        this.hoverTooltips.clear(); //ensure emptying them to reload them from the init calls from above
    }

    @Override
    protected void containerTick() {
        tickRequirements();
    }

    public void tickRequirements() {
        if (this.hoverTooltips.stream()
                .filter(tooltip -> tooltip instanceof HoverScreenUpdatable<?>)
                .map(tooltip -> (HoverScreenUpdatable<?>) tooltip)
                .filter(HoverScreenUpdatable::changed)
                .peek(HoverScreenUpdatable::tick).findAny().isPresent()) {
            rebuildWidgets();
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.blit(getTexture(), this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        RenderSystem.disableBlend();
    }

    public abstract ResourceLocation getTexture();

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot == null) {
            hoverTooltips.forEach(tooltip -> {
                if (tooltip.matches(this.leftPos, this.topPos, mouseX, mouseY)) {
                    graphics.renderTooltip(Minecraft.getInstance().font, tooltip.getText(), Optional.empty(), mouseX, mouseY);
                }
            });
        }
    }
}