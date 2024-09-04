package net.kapitencraft.kap_lib.item.misc;

import net.kapitencraft.kap_lib.event.ModEventFactory;
import net.kapitencraft.kap_lib.event.custom.RegisterAnvilUsesEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Mod.EventBusSubscriber
public class AnvilUses {
    private static final List<AnvilUse> uses = new ArrayList<>();

    @SubscribeEvent
    public static void anvilEvent(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        for (AnvilUse use : uses) {
            if (use.bothPredicate.test(left, right)) {
                ItemStack output = left.copy();
                use.resultConsumer.accept(output, right);
                event.setOutput(output);
                event.setCost(use.xpCost);
            }
        }
    }

    public static void registerAnvilUse(BiPredicate<ItemStack, ItemStack> bothPredicate, BiConsumer<ItemStack, ItemStack> resultConsumer, int xpCost) {
        uses.add(new AnvilUse(bothPredicate, resultConsumer, xpCost));
    }

    public static void registerUses() {
        ModEventFactory.fireModEvent(new RegisterAnvilUsesEvent());
    }

    public static class AnvilUse {
        private final BiPredicate<ItemStack, ItemStack> bothPredicate;
        private final BiConsumer<ItemStack, ItemStack> resultConsumer;
        private final int xpCost;

        public AnvilUse(BiPredicate<ItemStack, ItemStack> bothPredicate, BiConsumer<ItemStack, ItemStack> resultConsumer, int xpCost) {
            this.bothPredicate = bothPredicate;
            this.resultConsumer = resultConsumer;
            this.xpCost = xpCost;
        }
    }

    private static BiPredicate<ItemStack, ItemStack> simple(Predicate<ItemStack> both) {
        return (stack, stack2) -> both.test(stack) && both.test(stack2);
    }

    private static BiPredicate<ItemStack, ItemStack> both(Predicate<ItemStack> first, Predicate<ItemStack> second) {
        return (stack, stack2) -> first.test(stack) && second.test(stack2);
    }
}
