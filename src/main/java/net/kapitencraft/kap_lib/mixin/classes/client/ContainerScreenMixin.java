package net.kapitencraft.kap_lib.mixin.classes.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kapitencraft.kap_lib.item.LibItemProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Set;

@Mixin(AbstractContainerScreen.class)
public abstract class ContainerScreenMixin extends Screen {


    @Shadow protected int leftPos;

    @Shadow protected int topPos;

    protected ContainerScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Shadow protected abstract void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY);

    @Shadow @Nullable protected Slot hoveredSlot;

    @Shadow protected abstract void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot);

    @Shadow @Final protected AbstractContainerMenu menu;

    @Shadow protected abstract boolean isHovering(Slot pSlot, double pMouseX, double pMouseY);


    @Shadow public abstract int getSlotColor(int index);

    @Shadow
    public static void renderSlotHighlight(GuiGraphics pGuiGraphics, int pX, int pY, int pBlitOffset, int color) {
    }

    @Shadow protected abstract void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY);

    @Shadow private ItemStack draggingItem;

    @Shadow private boolean isSplittingStack;

    @Shadow @Final protected Set<Slot> quickCraftSlots;

    @Shadow private int quickCraftingRemainder;

    @Shadow protected boolean isQuickCrafting;

    @Shadow protected abstract void renderFloatingItem(GuiGraphics pGuiGraphics, ItemStack pStack, int pX, int pY, String pText);

    @Shadow private ItemStack snapbackItem;

    @Shadow private long snapbackTime;

    @Shadow @Nullable private Slot snapbackEnd;

    @Shadow private int snapbackStartX;

    @Shadow private int snapbackStartY;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int i = this.leftPos;
        int j = this.topPos;
        this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ContainerScreenEvent.Render.Background((AbstractContainerScreen<?>) (Object) this, pGuiGraphics, pMouseX, pMouseY));
        RenderSystem.disableDepthTest();
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((float)i, (float)j, 0.0F);
        this.hoveredSlot = null;

        for(int k = 0; k < this.menu.slots.size(); ++k) {
            Slot slot = this.menu.slots.get(k);
            if (slot.isActive()) {
                this.renderSlot(pGuiGraphics, slot);
            }

            if (this.isHovering(slot, pMouseX, pMouseY) && slot.isActive()) {
                this.hoveredSlot = slot;
                int l = slot.x;
                int i1 = slot.y;
                if (this.hoveredSlot.isHighlightable()) {
                    renderSlotHighlight(pGuiGraphics, l, i1, 0, getSlotColor(k));
                }
                CompoundTag tag = slot.getItem().getOrCreateTag();
                tag.putBoolean(LibItemProperties.HOVERED_TAG_ID, true);
            } else {
                CompoundTag tag = slot.getItem().getTag();
                if (tag != null) {
                    if (tag.contains(LibItemProperties.HOVERED_TAG_ID)) {
                        tag.remove(LibItemProperties.HOVERED_TAG_ID);
                    }
                }
            }
        }

        this.renderLabels(pGuiGraphics, pMouseX, pMouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ContainerScreenEvent.Render.Foreground((AbstractContainerScreen<?>) (Object) this, pGuiGraphics, pMouseX, pMouseY));
        ItemStack itemstack = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
        if (!itemstack.isEmpty()) {
            int l1 = 8;
            int i2 = this.draggingItem.isEmpty() ? 8 : 16;
            String s = null;
            if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
                itemstack = itemstack.copyWithCount(Mth.ceil((float)itemstack.getCount() / 2.0F));
            } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
                itemstack = itemstack.copyWithCount(this.quickCraftingRemainder);
                if (itemstack.isEmpty()) {
                    s = ChatFormatting.YELLOW + "0";
                }
            }

            this.renderFloatingItem(pGuiGraphics, itemstack, pMouseX - i - 8, pMouseY - j - i2, s);
        }

        if (!this.snapbackItem.isEmpty()) {
            float f = (float)(Util.getMillis() - this.snapbackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.snapbackItem = ItemStack.EMPTY;
            }

            int j2 = this.snapbackEnd.x - this.snapbackStartX;
            int k2 = this.snapbackEnd.y - this.snapbackStartY;
            int j1 = this.snapbackStartX + (int)((float)j2 * f);
            int k1 = this.snapbackStartY + (int)((float)k2 * f);
            this.renderFloatingItem(pGuiGraphics, this.snapbackItem, j1, k1, null);
        }

        pGuiGraphics.pose().popPose();
        RenderSystem.enableDepthTest();
    }
}
