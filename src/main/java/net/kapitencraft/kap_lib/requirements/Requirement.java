package net.kapitencraft.kap_lib.requirements;

import net.kapitencraft.kap_lib.registry.custom.ModRegistries;
import net.kapitencraft.kap_lib.requirements.type.RequirementType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Requirement<T> {

    private final RequirementType type;
    private final Supplier<? extends T> target;

    public Requirement(RequirementType type, Supplier<? extends T> target) {
        this.type = type;
        this.target = target;
    }

    public Component display() {
        return type.display();
    }

    public <K> boolean related(K t) {
        return target.get() == t;
    }

    public boolean matches(Player player) {
        return player != null && this.type.matchesPlayer(player);
    }

    public static <T> boolean doesntMeetRequirements(Player player, T obj) {
        List<Requirement<T>> list = (List<Requirement<T>>) getReqs(obj);
        return !list.stream().allMatch(requirement -> requirement.matches(player));
    }

    public static boolean doesntMeetReqsFromEvent(PlayerEvent event) {
        return doesntMeetRequirements(event.getEntity(), event.getEntity().getMainHandItem().getItem());
    }

    public static <T> List<? extends Requirement<?>> getReqs(T target) {
        return ModRegistries.REQUIREMENT_REGISTRY.getEntries().stream().map(Map.Entry::getValue).filter(requirement -> requirement.related(target)).toList();
    }
}