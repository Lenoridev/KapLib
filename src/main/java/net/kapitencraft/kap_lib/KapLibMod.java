package net.kapitencraft.kap_lib;

import com.mojang.logging.LogUtils;
import net.kapitencraft.kap_lib.commands.TestCommand;
import net.kapitencraft.kap_lib.config.ClientModConfig;
import net.kapitencraft.kap_lib.registry.ModAttributes;
import net.kapitencraft.kap_lib.registry.ModParticleTypes;
import net.kapitencraft.kap_lib.registry.custom.ModRegistryBuilders;
import net.kapitencraft.kap_lib.registry.custom.ModRequirementTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.io.File;
import java.text.DecimalFormat;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KapLibMod.MOD_ID)
public class KapLibMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "kap_lib";
    // Directly reference a slf4j logger
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
        ModAttributes.REGISTRY.register(modEventBus);
        ModParticleTypes.REGISTRY.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientModConfig.SPEC);
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
        }
    }
}
