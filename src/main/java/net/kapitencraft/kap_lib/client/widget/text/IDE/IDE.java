package net.kapitencraft.kap_lib.client.widget.text.IDE;

import net.kapitencraft.kap_lib.client.widget.text.IFormatter;
import net.kapitencraft.kap_lib.client.widget.text.ITextCallback;
import net.kapitencraft.kap_lib.client.widget.text.MultiLineTextBox;

/**
 * wrapper for {@link IFormatter}, {@link ISuggestionProvider} and {@link ITextCallback}
 * used inside {@link MultiLineTextBox#setIDE(IDE)}
 */
public interface IDE extends IFormatter, ISuggestionProvider, ITextCallback {
}
