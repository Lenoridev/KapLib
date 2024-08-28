package net.kapitencraft.kap_lib.item.bonus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.helpers.InventoryHelper;
import net.kapitencraft.kap_lib.io.JsonHelper;
import net.kapitencraft.kap_lib.io.serialization.DataGenSerializer;
import net.kapitencraft.kap_lib.registry.custom.core.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.*;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BonusManager extends SimpleJsonResourceReloadListener {
    public static BonusManager instance;

    public static final Codec<List<TagEntry>> TAG_ENTRY_LOADER_CODEC = TagEntry.CODEC.listOf();
    private final RegistryAccess access;
    private final Map<ResourceLocation, SetBonusElement> sets = new HashMap<>();
    private final Multimap<Item, BonusElement> itemBonuses = HashMultimap.create();

    public BonusManager(RegistryAccess access) {
        super(JsonHelper.GSON, "bonuses");
        this.access = access;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        pObject.forEach((location, element) -> {
            if (location.getPath().startsWith("set/")) readSetElement(new ResourceLocation(location.getNamespace(), location.getPath().substring(4)), element);
            else readItemElement(location, element);
        });
        pObject.forEach(this::readSetElement);
    }

    private void readItemElement(ResourceLocation location, JsonElement element) {
        try {
            JsonObject main = element.getAsJsonObject();
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item == null) throw new IllegalArgumentException("unknown Item: " + location);
            DataGenSerializer<? extends Bonus<?>> serializer = readFromString(GsonHelper.getAsString(main, "type"));


            Bonus<?> bonus = serializer.deserialize(GsonHelper.getAsJsonObject(main, "data"));
            boolean hidden = GsonHelper.getAsBoolean(main, "hidden");
            String translationKey = GsonHelper.getAsString(main, "translationKey");
            this.itemBonuses.put(item, new BonusElement(hidden, bonus, translationKey));
        } catch (Exception e) {
            KapLibMod.LOGGER.warn("error loading item bonus: {}", e.getMessage());
        }
    }

    private static DataGenSerializer<? extends Bonus<?>> readFromString(String string) {
        return ModRegistries.BONUS_SERIALIZER.getValue(new ResourceLocation(string));
    }

    private void readSetElement(ResourceLocation location, JsonElement jsonElement) {
        try {
            JsonObject main = jsonElement.getAsJsonObject();

            //read Item Tags
            Map<EquipmentSlot, TagKey<Item>> itemsForSlot = new HashMap<>();
            {
                JsonObject items = GsonHelper.getAsJsonObject(main, "items");
                Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagEntriesForBonus = new HashMap<>();
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (!items.has(slot.getName())) continue;
                    ResourceLocation locationForSlot = new ResourceLocation(location.getNamespace(), "set/" + location.getPath() + "/" + slot.getName());
                    JsonObject itemElements = GsonHelper.getAsJsonObject(items, slot.getName());
                    List<TagEntry> tagEntries = TAG_ENTRY_LOADER_CODEC.parse(JsonOps.INSTANCE, itemElements).getOrThrow(false, KapLibMod.LOGGER::error);
                    tagEntriesForBonus.put(locationForSlot,
                            tagEntries.stream()
                                    .map(tagEntry -> new TagLoader.EntryWithSource(tagEntry, locationForSlot.toString()))
                                    .toList()
                    );
                    itemsForSlot.put(slot, TagKey.create(Registries.ITEM, locationForSlot));
                }
                if (tagEntriesForBonus.size() < 2) throw new IllegalStateException("set bonuses require at least two used slots to work");
                TagLoader<Holder<Item>> loader = new TagLoader<>(ForgeRegistries.ITEMS::getHolder, "set_bonuses");
                Registry<Item> registry = this.access.registryOrThrow(Registries.ITEM);
                loader.build(tagEntriesForBonus).forEach((tagLocation, holders) -> {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagLocation);
                    registry.getOrCreateTag(tagKey).bind(List.copyOf(holders));
                });
            }
            //read Set Type and configuration
            DataGenSerializer<? extends Bonus<?>> serializer = readFromString(GsonHelper.getAsString(main, "type"));

            Bonus<?> bonus = serializer.deserialize(GsonHelper.getAsJsonObject(main, "bonus"));
            String translationKey = GsonHelper.getAsString(main, "translationKey");
            boolean hidden = GsonHelper.getAsBoolean(main, "hidden");
            this.sets.put(location, new SetBonusElement(hidden, bonus, itemsForSlot, translationKey));
        } catch (Exception e) {
            KapLibMod.LOGGER.warn("error loading bonus: {}", e.getMessage());
        }
    }

    public List<Bonus<?>> getAllBonuses(LivingEntity living, boolean ignoreHidden) {
        List<Bonus<?>> bonuses = new ArrayList<>();
        InventoryHelper.equipment(living).forEach((slot, stack) -> {
            bonuses.addAll(this.itemBonuses.get(stack.getItem()).stream().filter(bonusElement -> !bonusElement.hidden || ignoreHidden).map(BonusElement::getBonus).toList());
        });
        bonuses.addAll(getActiveSetBonuses(living, ignoreHidden));
        return bonuses;
    }

    public List<Bonus<?>> getActiveSetBonuses(LivingEntity living, boolean ignoreHidden) {
        Map<EquipmentSlot, ItemStack> equipment = InventoryHelper.equipment(living);
        List<Bonus<?>> bonuses = new ArrayList<>();
        sets.values().stream().filter(setBonusElement -> Arrays.stream(EquipmentSlot.values()).allMatch(slot ->
                        !setBonusElement.requiresSlot(slot) || setBonusElement.matchesItem(slot, equipment.get(slot)))
                ).filter(setBonusElement -> !setBonusElement.isHidden() || ignoreHidden)
                .map(SetBonusElement::getBonus).forEach(bonuses::add);
        return bonuses;
    }

    private static class SetBonusElement extends BonusElement {
        private final Map<EquipmentSlot, TagKey<Item>> itemsForSlot;

        private SetBonusElement(boolean hidden, Bonus<?> bonus, Map<EquipmentSlot, TagKey<Item>> itemsForSlot, String translationKey) {
            super(hidden, bonus, translationKey);
            this.itemsForSlot = itemsForSlot;
        }

        public boolean requiresSlot(EquipmentSlot slot) {
            return this.itemsForSlot.containsKey(slot);
        }

        public boolean matchesItem(EquipmentSlot slot, ItemStack stack) {
            return stack.is(itemsForSlot.get(slot));
        }

        /**
         * equality check for EquipmentsSlots, ItemStacks and other SetBonusElements
         */
        @Override
        public boolean equals(Object obj) {
            return obj instanceof EquipmentSlot slot && this.requiresSlot(slot) ||
                    obj instanceof ItemStack stack && itemsForSlot.values().stream().anyMatch(stack::is) ||
                    obj instanceof SetBonusElement element &&
                        element.isHidden() == this.isHidden() &&
                        element.getBonus().equals(this.getBonus()) &&
                        element.itemsForSlot.equals(this.itemsForSlot);
        }
    }

    private static class BonusElement {
        private final boolean hidden;
        private final Bonus<?> bonus;
        private final String translationKey;

        private BonusElement(boolean hidden, Bonus<?> bonus, String translationKey) {
            this.hidden = hidden;
            this.bonus = bonus;
            this.translationKey = translationKey;
        }

        public boolean isHidden() {
            return hidden;
        }


        public Bonus<?> getBonus() {
            return bonus;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }
}