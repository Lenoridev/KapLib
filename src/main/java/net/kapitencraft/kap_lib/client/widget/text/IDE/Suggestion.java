package net.kapitencraft.kap_lib.client.widget.text.IDE;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class Suggestion {
    public final int insertIndex;
    public final String suggestionString;
    public String packageName;
    public String varType;
    public String extraInformation;
    private final Component renderSuggestion;

    public Suggestion(int insertIndex, String suggestionString) {
        this(insertIndex, suggestionString, Component.literal(
                suggestionString.substring(0, insertIndex)).withStyle(ChatFormatting.BLUE)
                        .append(suggestionString.substring(insertIndex))
        );
    }

    public Suggestion(int insertIndex, String suggestionString, Component renderSuggestion, String packageName, String varType, String extraInformation) {
        this.insertIndex = insertIndex;
        this.suggestionString = suggestionString;
        this.renderSuggestion = renderSuggestion;
        this.packageName = packageName;
        this.varType = varType;
        this.extraInformation = extraInformation;
    }

    public Suggestion(int insertIndex, String suggestionString, Component renderSuggestion) {
        this.insertIndex = insertIndex;
        this.suggestionString = suggestionString;
        this.renderSuggestion = renderSuggestion;
    }

    public void insertString(Consumer<String> toApply) {
        String toInsert = this.suggestionString.substring(insertIndex);
        toApply.accept(toInsert);
    }

    public Component getRenderable() {
        return renderSuggestion;
    }

    public int getWidth(Font font) {
        return font.width(this.renderSuggestion);
    }
}
