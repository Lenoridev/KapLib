package net.kapitencraft.kap_lib.item.bonus;

import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IItemBonusItem {

    @Nullable
    PieceBonus getBonus();
    @Nullable
    ExtraBonus getExtraBonus();

    default void addDisplay(List<Component> toolTip, EquipmentSlot slot) {
        toolTip.addAll(MiscHelper.ifNonNullOrDefault(this.getBonus(), Bonus::makeDisplay, List::of));
        toolTip.addAll(MiscHelper.ifNonNullOrDefault(this.getExtraBonus(), ExtraBonus::makeDisplay, List::of));
    }
}
