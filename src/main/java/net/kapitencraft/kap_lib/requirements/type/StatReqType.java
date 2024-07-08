package net.kapitencraft.kap_lib.requirements.type;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;

/**
 * only use when Stat is entry from block mined, item used, item picked up or entity killed <br>
 * otherwise use {@link CustomStatReqType}
 */
public class StatReqType extends RequirementType {
    private final Stat<?> stat;
    public StatReqType(Stat<?> stat, int level) {
        super(value -> value instanceof ServerPlayer player ? player.getStats().getValue(stat) : ((LocalPlayer) value).getStats().getValue(stat), level);
        this.stat = stat;
    }

    private <T> Component forStatType(Stat<T> stat) {
        StatType<T> type = stat.getType();
        if (type == Stats.BLOCK_MINED) {
            return Component.translatable("stat_req.blocks_mined", minLevel, stat.getValue());
        } else if (type == Stats.ITEM_USED) {
            return Component.translatable("stat_req.items_used", minLevel, stat.getValue());
        } else if (type == Stats.ITEM_PICKED_UP) {
            return Component.translatable("stat_req.items_picked_up", minLevel, stat.getValue());
        } else if (type == Stats.ENTITY_KILLED) {
            EntityType<?> type1 = (EntityType<?>) stat.getValue();
            return Component.translatable("stat_req.entities_killed", minLevel, type1.getDescription());
        }
        return Component.empty();
    }

    @Override
    public Component display() {
        return forStatType(this.stat);
    }
}