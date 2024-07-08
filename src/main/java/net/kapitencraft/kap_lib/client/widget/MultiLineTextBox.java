package net.kapitencraft.kap_lib.client.widget;

import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.client.gui.widgets.background.WidgetBackground;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import net.kapitencraft.kap_lib.util.Vec2i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class MultiLineTextBox extends AbstractWidget implements Renderable {
    public static final int BACKWARDS = -1;
    public static final int FORWARDS = 1;
    private static final int CURSOR_INSERT_WIDTH = 1;
    private static final int CURSOR_INSERT_COLOR = -3092272;
    private static final String CURSOR_APPEND_CHARACTER = "_";
    public static final int DEFAULT_TEXT_COLOR = 14737632;
    private static final int BACKGROUND_COLOR = -16777216;
    private int renderXStart, renderYStart;
    private final Font font;
    /** Has the current text being edited on the textbox. */
    private final List<String> lineValues = new ArrayList<>();
    private String value = "";
    private int frame;
    /** if true the textbox can lose focus by clicking elsewhere on the screen */
    private boolean canLoseFocus = true;
    /** If this value is true along with isFocused, keyTyped will process the keys. */
    private boolean isEditable = true;
    private boolean shiftPressed;
    /** The current character index that should be used as start of the rendered text. */
    private Vec2i cursorPos2d = new Vec2i(0);
    private Vec2i highlightPos2d = new Vec2i(0);
    private int displayPos;
    private int cursorPos;
    /** other selection position, maybe the same as the cursor */
    private int highlightPos;
    private int textColor = 14737632;
    private int textColorUneditable = 7368816;
    private WidgetBackground background;
    @Nullable
    private String suggestion;
    @Nullable
    private Consumer<String> responder;
    /** Called to check if the text is valid */
    private Predicate<String> filter = Objects::nonNull;
    private BiFunction<String, Integer, FormattedCharSequence> formatter = (string, i) -> FormattedCharSequence.forward(string, Style.EMPTY);
    @Nullable
    private Component hint;
    private float scrollX, scrollY;

    public MultiLineTextBox(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        this(pFont, pX, pY, pWidth, pHeight, null, pMessage);
    }

    public MultiLineTextBox(Font pFont, int pX, int pY, int pWidth, int pHeight, @Nullable MultiLineTextBox pEditBox, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.font = pFont;
        if (pEditBox != null) {
            this.value = pEditBox.value;
            this.cursorPos2d = pEditBox.cursorPos2d;
            this.highlightPos2d = pEditBox.highlightPos2d;
            this.highlightPos = pEditBox.highlightPos;
            this.cursorPos = pEditBox.cursorPos;
        }
        this.applyText2d();
    }

    public void setFormatter(BiFunction<String, Integer, FormattedCharSequence> pTextFormatter) {
        this.formatter = pTextFormatter;
    }

    /**
     * Increments the cursor counter
     */
    public void tick() {
        ++this.frame;
    }

    protected MutableComponent createNarrationMessage() {
        Component component = this.getMessage();
        return Component.translatable("gui.narrate.editBox", component, this.value);
    }

    /**
     * Sets the text of the textbox, and moves the cursor to the end.
     */
    public void setValue(String pText) {
        if (this.filter.test(pText)) {
            this.value = pText;

            this.moveCursorToEnd();
            this.moveHighlightToCursor();
            this.onValueChange(pText);
        }
    }

    /**
     * Returns the contents of the textbox
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the text between the cursor and selectionEnd.
     */
    public String getHighlighted() {
        int i = Math.min(this.cursorPos, this.highlightPos);
        int j = Math.max(this.cursorPos, this.highlightPos);
        return this.value.substring(i, j);
    }

    /**
     * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
     */
    public void insertText(String pTextToWrite) {
        int selectionStart = Math.min(this.cursorPos, this.highlightPos);
        int selectionEnd = Math.max(this.cursorPos, this.highlightPos);
        int space = Integer.MAX_VALUE - this.value.length() - (selectionStart - selectionEnd);
        String insert = SharedConstants.filterText(pTextToWrite, true);
        int insertLength = insert.length();
        if (space < insertLength) {
            insert = insert.substring(0, space);
            insertLength = space;
        }

        updateText(insert, selectionStart, selectionEnd, insertLength);
    }

    private void updateText(String insert, int selectionStart, int selectionEnd, int insertLength) {
        String s1 = (new StringBuilder(this.value)).replace(selectionStart, selectionEnd, insert).toString();
        if (this.filter.test(s1)) {
            this.value = s1;
            applyText2d();
            this.setCursorPosition(selectionStart + Math.max(0, insertLength));
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(this.value);
        }
    }

    private void applyText2d() {
        this.lineValues.clear();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.value.length(); i++) {
            char c = this.value.charAt(i);
            if (c == '\n') {
                lineValues.add(builder.toString());
                builder = new StringBuilder();
            }
            else builder.append(c);
        }
        lineValues.add(builder.toString());
    }

    private void onValueChange(String pNewText) {
        if (this.responder != null) {
            this.responder.accept(pNewText);
        }

    }

    private void deleteText(int pCount) {
        if (Screen.hasControlDown()) {
            this.deleteWords(pCount);
        } else {
            this.deleteChars(pCount);
        }

    }

    /**
     * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
     * which case the selection is deleted instead.
     */
    public void deleteWords(int pNum) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteChars(this.getWordPosition(pNum) - this.cursorPos);
            }
        }
    }

    /**
     * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
     * in which case the selection is deleted instead.
     */
    public void deleteChars(int pNum) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int i = this.getCursorPos(pNum);
                int j = Math.min(i, this.cursorPos);
                int k = Math.max(i, this.cursorPos);
                if (j != k) {
                    updateText("", j, k, pNum);
                }
            }
        }
    }

    /**
     * Gets the starting index of the word at the specified number of words away from the cursor position.
     */
    public int getWordPosition(int pNumWords) {
        return this.getWordPosition(pNumWords, this.getCursorPosition());
    }

    /**
     * Gets the starting index of the word at a distance of the specified number of words away from the given position.
     */
    private int getWordPosition(int pN, int pPos) {
        return this.getWordPosition(pN, pPos, true);
    }

    /**
     * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
     */
    private int getWordPosition(int pN, int pPos, boolean pSkipWs) {
        int i = pPos;
        boolean flag = pN < 0;
        int j = Math.abs(pN);

        for(int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.value.length();
                i = this.value.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while(pSkipWs && i < l && this.value.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while(pSkipWs && i > 0 && this.value.charAt(i - 1) == ' ') {
                    --i;
                }

                while(i > 0 && this.value.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursor(int pDelta) {
        this.moveCursorTo(this.getCursorPos(pDelta));
    }

    public void moveCursorVertical(int pDelta) {
        int curXWidth = this.font.width(this.lineValues.get(this.cursorPos2d.y).substring(0, this.cursorPos2d.x));
        int newY = Math.min(this.lineValues.size() - 1, this.cursorPos2d.y + pDelta);
        String subs = this.font.plainSubstrByWidth(this.lineValues.get(newY), curXWidth);
        this.moveCursorTo(subs.length(), newY);
    }

    private int getCursorPos(int pDelta) {
        return Util.offsetByCodepoints(this.value, this.cursorPos, pDelta);
    }

    /**
     * Sets the current position of the cursor.
     */
    public void moveCursorTo(int pPos) {
        this.setCursorPosition(pPos);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }

        this.onValueChange(this.value);
    }

    private void moveCursorTo(int xPos, int yPos) {
        this.cursorPos2d = new Vec2i(xPos, yPos);
        int pos = 0;
        int line = 0;
        for (String s : lineValues) {
            if (line == yPos) break;
            pos += s.length();
            pos++; //line break
            line++;
        }
        this.cursorPos = pos + xPos;
        if (!this.shiftPressed) this.moveHighlightToCursor();
    }

    public void setCursorPosition(int pPos) {
        this.cursorPos = Mth.clamp(pPos, 0, this.value.length());
        reapplyCursor2d();
    }

    private void reapplyCursor2d() {
        int loc = 0;
        for (int i = 0; i < lineValues.size(); i++) {
            if (cursorPos <= loc + lineValues.get(i).length()) {
                this.cursorPos2d = new Vec2i(cursorPos - loc, i);
                if (cursorPos - loc < 0 || cursorPos - loc > this.lineValues.get(i).length()) {
                    KapLibMod.LOGGER.error("cursor index set to illegal x state: {}", cursorPos - loc);
                }
                return;
            }
            loc += lineValues.get(i).length();
            loc++;
        }
    }

    private void reapplyHighlight2d() {
        int loc = 0;
        for (int i = 0; i < lineValues.size(); i++) {
            if (highlightPos <= loc + lineValues.get(i).length()) {
                this.highlightPos2d = new Vec2i(highlightPos - loc, i);
                if (highlightPos - loc < 0 || highlightPos - loc > this.lineValues.get(i).length()) {
                    KapLibMod.LOGGER.error("highlight index set to illegal x state: {}", highlightPos - loc);
                }
                return;
            }
            loc += lineValues.get(i).length();
            loc++;
        }
    }

    /**
     * Moves the cursor to the very start of this text box.
     */
    public void moveCursorToStart() {
        this.moveCursorTo(0);
    }

    /**
     * Moves the cursor to the very end of this text box.
     */
    public void moveCursorToEnd() {
        this.moveCursorTo(this.value.length());
    }

    /**
     * Called when a keyboard key is pressed within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pKeyCode the key code of the pressed key.
     * @param pScanCode the scan code of the pressed key.
     * @param pModifiers the keyboard modifiers.
     */
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else {
            this.shiftPressed = Screen.hasShiftDown();
            if (Screen.isSelectAll(pKeyCode)) {
                this.moveCursorToEnd();
                this.setHighlightPos(0);
                return true;
            } else if (Screen.isCopy(pKeyCode)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                return true;
            } else if (Screen.isPaste(pKeyCode)) {
                if (this.isEditable) {
                    this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                }

                return true;
            } else if (Screen.isCut(pKeyCode)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                if (this.isEditable) {
                    this.insertText("");
                }

                return true;
            } else {
                return switch (pKeyCode) {
                    case 257 -> {
                        this.insertText("\n");
                        yield true;
                    }
                    case 258 -> {
                        this.insertText("\t");
                        yield true;
                    }
                    case 259 -> {
                        if (this.isEditable) {
                            this.shiftPressed = false;
                            this.deleteText(-1);
                            this.shiftPressed = Screen.hasShiftDown();
                        }
                        yield true;
                    }
                    default -> false;
                    case 261 -> {
                        //delete one
                        if (this.isEditable) {
                            this.shiftPressed = false;
                            this.deleteText(1);
                            this.shiftPressed = Screen.hasShiftDown();
                        }
                        yield true;
                    }
                    case 262 -> {
                        //move right
                        if (Screen.hasControlDown()) {
                            this.moveCursorTo(this.getWordPosition(1));
                        } else {
                            this.moveCursor(1);
                        }
                        yield true;
                    }
                    case 263 -> {
                        //move left
                        if (Screen.hasControlDown()) {
                            this.moveCursorTo(this.getWordPosition(-1));
                        } else {
                            this.moveCursor(-1);
                        }
                        yield true;
                    }
                    case 264 -> {
                        //move down
                        moveCursorVertical(1);
                        yield true;
                    }
                    case 265 -> {
                        //move up
                        moveCursorVertical(-1);
                        yield true;
                    }
                    case 268 -> {
                        this.moveCursorToStart();
                        yield true;
                    }
                    case 269 -> {
                        this.moveCursorToEnd();
                        yield true;
                    }
                };
            }
        }
    }

    public boolean canConsumeInput() {
        return this.isVisible() && this.isFocused() && this.isEditable();
    }

    /**
     * Called when a character is typed within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pCodePoint the code point of the typed character.
     * @param pModifiers the keyboard modifiers.
     */
    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (SharedConstants.isAllowedChatCharacter(pCodePoint)) {
            if (this.isEditable) {
                this.insertText(Character.toString(pCodePoint));
            }


            return true;
        } else {
            return false;
        }
    }

    public void onClick(double pMouseX, double pMouseY) {
        if (this.value.isEmpty()) {
            this.moveCursorTo(0);
            return;
        }
        int xOffset = Mth.floor(pMouseX) - this.getX();
        int yOffset = Mth.floor(pMouseY) - this.getY();
        int line = Math.min(this.lineValues.size() - 1, yOffset / 10);

        this.moveCursorTo(this.font.plainSubstrByWidth(this.lineValues.get(line), xOffset).length(), line);
    }

    private void moveHighlightToCursor() {
        this.highlightPos = this.cursorPos;
        this.highlightPos2d = this.cursorPos2d;
    }

    public void playDownSound(@NotNull SoundManager pHandler) {
    }

    public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.enableScissor(getX(), getY(), getX() + this.width, getY() + this.height);

        //background
        this.background.render(true, pGuiGraphics, getX(), getY(), this.width, this.height, this.scrollX, this.scrollY);

        //actual text
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((float)getX(), (float)getY(), 0.0F);
        int textColor = this.isEditable ? this.textColor : this.textColorUneditable;
        for (int lineIndex = 0; lineIndex < lineValues.size(); lineIndex++) {

            String line = lineValues.get(lineIndex);
            boolean cursorInLine = cursorPos2d.y == lineIndex;
            boolean showCursor = this.isFocused() && this.frame / 6 % 2 == 0 && cursorInLine;
            int x = 2;
            int y = 2 + lineIndex * 10;
            int j1 = x;

            if (!line.isEmpty()) {
                pGuiGraphics.drawString(this.font, this.formatter.apply(line, line.length()), x, y, textColor);
                if (cursorInLine) {
                    j1 += this.font.width(line.substring(0, cursorPos2d.x));
                }
            }

            if (this.hint != null && line.isEmpty() && !this.isFocused()) {
                pGuiGraphics.drawString(this.font, this.hint, j1, y, textColor);
            }

            if (false&& this.suggestion != null) {
                pGuiGraphics.drawString(this.font, this.suggestion, j1 - 1, y, -8355712);
            }

            if (showCursor) {
                if (!this.value.isEmpty()) {
                    pGuiGraphics.fill(RenderType.guiOverlay(), j1, y - 1, j1 + 1, y + 1 + 9, -3092272);
                } else {
                    pGuiGraphics.drawString(this.font, "_", j1, y, textColor);
                }
            }

            if (highlightPos != cursorPos) {
                String highlighted = getHighlighted(lineIndex, line);
                if (!highlighted.isEmpty()) {
                    int l1 = this.font.width(highlighted);
                    this.renderHighlight(pGuiGraphics, j1, y - 1, l1 - 1, y + 1 + 9);
                }
            }
        }
        String s = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.width);

        pGuiGraphics.pose().popPose();
        pGuiGraphics.disableScissor();
    }

    private String getHighlighted(int lineIndex, String line) {
        Vec2i highlightStart = getHighlightPos(true);
        Vec2i highlightEnd = getHighlightPos(false);
        String highlighted;
        if (highlightStart.y < lineIndex && highlightEnd.y > lineIndex) { //it neither starts nor ends here
            highlighted = line;
        } else if (highlightStart.y < lineIndex) { //it starts above this line but ends at this one
            highlighted = line.substring(0, highlightEnd.x);
        } else { //it must end below this line but start at this one
            highlighted = line.substring(highlightStart.x);
        }
        return highlighted;
    }

    private Vec2i getHighlightPos(boolean start) {
        return highlightPos < cursorPos == start ? highlightPos2d : cursorPos2d;
    }

    private void renderHighlight(GuiGraphics pGuiGraphics, int pMinX, int pMinY, int pMaxX, int pMaxY) {
        if (pMinX < pMaxX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }

        if (pMinY < pMaxY) {
            int j = pMinY;
            pMinY = pMaxY;
            pMaxY = j;
        }

        if (pMaxX > this.getX() + this.width) {
            pMaxX = this.getX() + this.width;
        }

        if (pMinX > this.getX() + this.width) {
            pMinX = this.getX() + this.width;
        }

        pGuiGraphics.fill(RenderType.guiTextHighlight(), pMinX, pMinY, pMaxX, pMaxY, -16776961);
    }
    /**
     * Returns the current position of the cursor.
     */
    public int getCursorPosition() {
        return this.cursorPos;
    }

    /**
     * Retrieves the next focus path based on the given focus navigation event.
     * <p>
     * @return the next focus path as a ComponentPath, or {@code null} if there is no next focus path.
     * @param pEvent the focus navigation event.
     */
    @Nullable
    public ComponentPath nextFocusPath(FocusNavigationEvent pEvent) {
        return this.visible && this.isEditable ? super.nextFocusPath(pEvent) : null;
    }

    /**
     * Checks if the given mouse coordinates are over the GUI element.
     * <p>
     * @return {@code true} if the mouse is over the GUI element, {@code false} otherwise.
     * @param pMouseX the X coordinate of the mouse.
     * @param pMouseY the Y coordinate of the mouse.
     */
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return this.visible && pMouseX >= (double)this.getX() && pMouseX < (double)(this.getX() + this.width) && pMouseY >= (double)this.getY() && pMouseY < (double)(this.getY() + this.height);
    }

    /**
     * Sets the focus state of the GUI element.
     * @param pFocused {@code true} to apply focus, {@code false} to remove focus
     */
    public void setFocused(boolean pFocused) {
        if (this.canLoseFocus || pFocused) {
            super.setFocused(pFocused);
            if (pFocused) {
                this.frame = 0;
            }
        }
    }

    private boolean isEditable() {
        return this.isEditable;
    }

    /**
     * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
     */
    public void setEditable(boolean pEnabled) {
        this.isEditable = pEnabled;
    }

    /**
     * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
     * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
     */
    public void setHighlightPos(int pPosition) {
        int i = this.value.length();
        this.highlightPos = Mth.clamp(pPosition, 0, i);
        if (this.font != null) {
            if (this.displayPos > i) {
                this.displayPos = i;
            }

            int j = this.width;
            String s = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), j);
            int k = s.length() + this.displayPos;
            if (this.highlightPos == this.displayPos) {
                this.displayPos -= this.font.plainSubstrByWidth(this.value, j, true).length();
            }

            if (this.highlightPos > k) {
                this.displayPos += this.highlightPos - k;
            } else if (this.highlightPos <= this.displayPos) {
                this.displayPos -= this.displayPos - this.highlightPos;
            }

            this.displayPos = Mth.clamp(this.displayPos, 0, i);
        }
        this.reapplyHighlight2d();
    }

    /**
     * Returns {@code true} if this textbox is visible.
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets whether this textbox is visible.
     */
    public void setVisible(boolean pIsVisible) {
        this.visible = pIsVisible;
    }

    public void setSuggestion(@Nullable String pSuggestion) {
        this.suggestion = pSuggestion;
    }

    public int getScreenX(int pCharNum) {
        return pCharNum > this.value.length() ? this.getX() : this.getX() + this.font.width(this.value.substring(0, pCharNum));
    }

    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    public void setTextureBackground(ResourceLocation backgroundLocation) {
        this.background = WidgetBackground.texture(backgroundLocation, 16, 16);
    }
}
