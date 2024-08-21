package net.kapitencraft.kap_lib.item.set;

import net.kapitencraft.kap_lib.io.JsonHelper;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FullSetManager implements PreparableReloadListener {

    public FullSetManager(String pDirectory) {
        super(JsonHelper.GSON, "set_bonuses");
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return null;
    }
}
