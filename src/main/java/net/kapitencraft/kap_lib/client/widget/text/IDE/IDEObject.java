package net.kapitencraft.kap_lib.client.widget.text.IDE;

public abstract class IDEObject {
    public String name;
    public String packageName;
    public boolean isPrivate;

    public String getName() {
        return this.name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }
}
