package net.kapitencraft.kap_lib.cooldown;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record CompoundPath(String path, net.kapitencraft.kap_lib.cooldown.CompoundPath parent) {
    public static final CompoundPath ROOT = new CompoundPath("", null);
    public static final CompoundPath COOLDOWN = new CompoundPath("Cooldowns", ROOT);

    public @NotNull CompoundTag getOrCreateTag(Entity entity) {
        if (this == ROOT) return entity.getPersistentData();
        CompoundTag parentPath = parent.getOrCreateTag(entity);
        if (!parentPath.contains(this.path, 10)) {
            CompoundTag tag = new CompoundTag();
            parentPath.put(this.path, tag);
            return tag;
        }
        return parentPath.getCompound(this.path);
    }

    public @Nullable CompoundTag getTag(Entity entity) {
        if (this == ROOT) return entity.getPersistentData();
        CompoundTag parentPath = parent.getTag(entity);
        if (parentPath != null && parentPath.contains(this.path, 10)) {
            return parentPath.getCompound(this.path);
        } else {
            return null;
        }
    }

    public static Builder builder(String path) {
        return new Builder(path);
    }

    public static class Builder {
        private final String path;
        private CompoundPath parent;

        public Builder(String path) {
            this.path = path;
        }


        public Builder withParent(CompoundPath parent) {
            this.parent = parent;
            return this;
        }

        public Builder withParentIfNull(CompoundPath parent) {
            if (this.parent == null) this.parent = parent;
            return this;
        }

        public Builder withParent(String id, Consumer<Builder> consumer) {
            Builder builder = new Builder(id);
            consumer.accept(builder);
            this.parent = builder.build();
            return this;
        }

        public CompoundPath build() {
            return new CompoundPath(path, parent == null ? ROOT : parent);
        }
    }
}
