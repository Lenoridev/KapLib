package net.kapitencraft.kap_lib.helpers;

import net.minecraft.Util;

/**
 * simple class to get passing time via code being run <br>
 * see {@link net.kapitencraft.mysticcraft.init.ModRegistryInit} for example
 */
public class Timer {
    private static long millis;
    private static boolean active;

    /**
     * starts the timer
     */
    public static void start() {
        millis = Util.getMillis();
        if (active) throw new IllegalStateException("tried starting timer whilst already active!");
        active = true;
    }

    /**
     * method to get the passing time
     * @return the time passed since the last {@link Timer#start()} call
     */
    public static long getPassedTime() {
        if (!active) throw new IllegalStateException("tried accessing passing time without Timer being active!");
        active = false;
        return Util.getMillis() - millis;
    }
}
