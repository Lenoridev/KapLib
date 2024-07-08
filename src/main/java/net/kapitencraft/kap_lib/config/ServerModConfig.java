package net.kapitencraft.kap_lib.config;

import net.kapitencraft.kap_lib.KapLibMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = KapLibMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerModConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_SOCIAL_COMMANDS = BUILDER
            .comment("determines if social commands (/show etc.) should be available")
            .define("enable_social", true);
    private static final ForgeConfigSpec.IntValue MAX_ITERATION_BROKEN_BLOCKS = BUILDER
            .comment("determines how many blocks per tick should be broken by the multi-break enchantments")
            .defineInRange("iter_max_broken", 20, 1, 200);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableSocial = true;
    public static int iterationMaxBroken = 20;

    @SubscribeEvent
    public static void registerConfig(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            KapLibMod.LOGGER.info("loading server config...");
            enableSocial = ENABLE_SOCIAL_COMMANDS.get();
            iterationMaxBroken = MAX_ITERATION_BROKEN_BLOCKS.get();
        }
    }
}
