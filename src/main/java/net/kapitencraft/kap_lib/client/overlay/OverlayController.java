package net.kapitencraft.kap_lib.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.client.LibClient;
import net.kapitencraft.kap_lib.client.overlay.box.InteractiveBox;
import net.kapitencraft.kap_lib.client.overlay.holder.MultiHolder;
import net.kapitencraft.kap_lib.client.overlay.holder.RenderHolder;
import net.kapitencraft.kap_lib.client.overlay.holder.SimpleHolder;
import net.kapitencraft.kap_lib.collection.MapStream;
import net.kapitencraft.kap_lib.event.ModEventFactory;
import net.kapitencraft.kap_lib.event.custom.client.RegisterOverlaysEvent;
import net.kapitencraft.kap_lib.helpers.ClientHelper;
import net.kapitencraft.kap_lib.helpers.CollectionHelper;
import net.kapitencraft.kap_lib.helpers.IOHelper;
import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.macosx.LibC;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OverlayController {
    private static final Codec<OverlayController> CODEC = RecordCodecBuilder.create(
            renderControllerInstance -> renderControllerInstance.group(
                    Codec.unboundedMap(IOHelper.UUID_CODEC, PositionHolder.CODEC).fieldOf("storage").forGetter(OverlayController::getLocations)
            ).apply(renderControllerInstance, OverlayController::fromCodec)
    );

    private static OverlayController fromCodec(Map<UUID, PositionHolder> map) {
        OverlayController controller = new OverlayController();
        controller.loadedPositions.putAll(map);
        return controller;
    }

    private Map<UUID, PositionHolder> getLocations() {
        return MapStream.of(map).mapValues(RenderHolder::getPos).mapKeys(OverlayLocation::getUUID).toMap();
    }

    private static File PERSISTENT_FILE;

    private static @NotNull File getPersistentFile() {
        if (PERSISTENT_FILE == null) {
            PERSISTENT_FILE = new File(KapLibMod.MAIN, "gui-locations.json");
        }
        return PERSISTENT_FILE;
    }

    public static OverlayController load() {
        return IOHelper.loadFile(getPersistentFile(), CODEC, OverlayController::new);
    }

    private final Map<OverlayLocation, Function<PositionHolder, RenderHolder>> constructors = new HashMap<>();
    public final Map<OverlayLocation, RenderHolder> map = new HashMap<>();
    private final Map<UUID, PositionHolder> loadedPositions = new HashMap<>();

    private OverlayController() {
        this.register();
    }

    private void register() {
        ModEventFactory.fireModEvent(new RegisterOverlaysEvent(this::createRenderer));
        construct();
    }

    private void createRenderer(OverlayLocation provider, Function<PositionHolder, RenderHolder> constructor) {
        if (this.constructors.containsKey(provider))
            throw new IllegalStateException("detected double registered Overlay with UUID '" + provider.getUUID() + "'");
        this.constructors.put(provider, constructor);
    }

    public static void save() {
        IOHelper.saveFile(getPersistentFile(), CODEC, LibClient.controller);
    }

    @SubscribeEvent
    public static void overlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("main", LibClient.controller::render);
    }

    public void fillRenderBoxes(Consumer<InteractiveBox> acceptor, LocalPlayer player, Font font, float width, float height) {
        this.map.values().stream().map(renderHolder -> renderHolder.newBox(width, height, player, font)).forEach(acceptor);
    }

    private void render(ForgeGui forgeGui, GuiGraphics graphics, float partialTicks, int screenWidth, int screenHeight) {
        LocalPlayer entity = Minecraft.getInstance().player;
        if (entity != null && !ClientHelper.hideGui()) {
            map.forEach((uuid, renderHolder) -> {
                graphics.pose().pushPose();
                PositionHolder holder = renderHolder.getPos();
                Vec2 renderLocation = renderHolder.getLoc(screenWidth, screenHeight);
                graphics.pose().translate(renderLocation.x, renderLocation.y, 0);
                graphics.pose().scale(holder.getXScale(), holder.getYScale(), 0);
                renderHolder.render(graphics, screenWidth, screenHeight, entity);
                graphics.pose().popPose();
            });
        }
    }

    private void construct() {
        this.constructors.forEach((location, constructor) -> {
            PositionHolder holder = MiscHelper.nonNullOr(this.loadedPositions.get(location.getUUID()), location.getDefault().createCopy());
            this.map.put(location, constructor.apply(holder));
        });
    }

    @SuppressWarnings("all")
    public void reset(RenderHolder dedicatedHolder) {
        if (this.map.containsValue(dedicatedHolder)) {
            OverlayLocation location = CollectionHelper.getKeyForValue(this.map, dedicatedHolder);
            dedicatedHolder.getPos().copy(location.getDefault());
            return;
        }
        throw new IllegalStateException("attempted to reset non-existing Holder");
    }

    public static void resetAll() {
        OverlayController controller = LibClient.controller;
        Collection<OverlayLocation> locations = controller.constructors.keySet();
        locations.forEach(location -> {
            RenderHolder holder = controller.map.get(location);
            holder.getPos().copy(location.getDefault());
        });
    }
}