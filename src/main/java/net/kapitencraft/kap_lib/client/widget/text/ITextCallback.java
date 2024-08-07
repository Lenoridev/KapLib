package net.kapitencraft.kap_lib.client.widget.text;

public interface ITextCallback {
    void lineCreated(int index);
    void lineModified(int index, String content);
    void lineRemoved(int index);
}
