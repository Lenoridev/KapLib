package net.kapitencraft.kap_lib.client.chroma;

import net.minecraft.util.StringRepresentable;

public enum ChromaOrigin implements IShaderConfig, StringRepresentable {
    TOP_LEFT("top_left", 1),
    TOP_RIGHT("top_right", 3),
    BOTTOM_LEFT("bottom_left", 0),
    BOTTOM_RIGHT("bottom_right", 2);

    private final String name;
    private final int id;

    ChromaOrigin(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int getConfigId() {
        return id;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
