package net.kapitencraft.kap_lib.util;

public class DamageCounter {
    private static double damage = 0;
    private static int attacked = 0;
    private static boolean shouldCount = false;

    public static void increaseDamage(double damageToAdd) {
        if (shouldCount) {
            damage += damageToAdd;
            attacked++;
        }
    }

    public static void activate() {
        shouldCount = true;
    }

    public static DamageHolder getDamage(boolean reset) {
        DamageHolder holder = new DamageHolder(attacked, damage);
        if (reset) {
            attacked = 0;
            damage = 0;
            shouldCount = false;
        }
        return holder;
    }

    public record DamageHolder(int hit, double damage) {

        public boolean hasDamage() {
            return hit > 0 && damage > 0;
        }
    }
}
