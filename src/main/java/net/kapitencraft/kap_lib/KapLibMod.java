package net.kapitencraft.kap_lib;

import com.mojang.logging.LogUtils;
import net.kapitencraft.kap_lib.config.ClientModConfig;
import net.kapitencraft.kap_lib.config.ServerModConfig;
import net.kapitencraft.kap_lib.helpers.CommandHelper;
import net.kapitencraft.kap_lib.registry.ModAttributes;
import net.kapitencraft.kap_lib.registry.ModParticleTypes;
import net.kapitencraft.kap_lib.registry.custom.ModSetBonusTypes;
import net.kapitencraft.kap_lib.registry.custom.core.ModRegistryBuilders;
import net.kapitencraft.kap_lib.registry.custom.ModRequirementTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import java.io.File;
import java.text.DecimalFormat;

@Mod(KapLibMod.MOD_ID)
public class KapLibMod
{
    public static final String MOD_ID = "kap_lib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation res(String sub) {
        return new ResourceLocation(MOD_ID, sub);
    }

    public static final File MAIN = new File("./kap_lib");
    public static final RandomSource RANDOM_SOURCE = RandomSource.create();

    public KapLibMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModRequirementTypes.REGISTRY.register(modEventBus);
        ModSetBonusTypes.REGISTRY.register(modEventBus);
        ModAttributes.REGISTRY.register(modEventBus);
        ModParticleTypes.REGISTRY.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientModConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerModConfig.SPEC);

        MinecraftForge.EVENT_BUS.addListener(CommandHelper::registerClient);

        StartupMessageManager.addModMessage("KapLib Mod loaded");
        LOGGER.info("KapLib loaded");
    }

    public static String doubleFormat(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static <T> DeferredRegister<T> registry(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, MOD_ID);
    }

    public static <T> DeferredRegister<T> registry(ResourceKey<Registry<T>> key) {
        return DeferredRegister.create(key, MOD_ID);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerRegistries(NewRegistryEvent event) {
            event.create(ModRegistryBuilders.REQUESTABLES_BUILDER);
            event.create(ModRegistryBuilders.REQUIREMENTS_BUILDER);
            event.create(ModRegistryBuilders.SET_BONUSES);
        }
    }
}
