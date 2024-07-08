package net.kapitencraft.kap_lib.item.bonus;

import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ExtraBonus implements Bonus {


    @Override
    public void onApply(LivingEntity living) {
    }

    @Override
    public void onRemove(LivingEntity living) {
    }

    @Override
    public void onTick(Level level, @NotNull LivingEntity entity) {
    }

    @Override
    public void onEntityKilled(LivingEntity killed, LivingEntity user, MiscHelper.DamageType type) {
    }

    @Override
    public List<Component> makeDisplay() {
        List<Component> list = new ArrayList<>();
        getDisplay().accept(list);
        return list;
    }

    @Override
    public String getName() {
        return "";
    }
}
