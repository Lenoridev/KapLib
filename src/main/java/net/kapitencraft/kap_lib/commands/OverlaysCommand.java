package net.kapitencraft.kap_lib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.kapitencraft.kap_lib.helpers.ClientHelper;
import net.kapitencraft.kap_lib.client.overlay.OverlayController;
import net.kapitencraft.kap_lib.client.gui.screen.ChangeOverlayLocationsScreen;
import net.kapitencraft.kap_lib.helpers.CommandHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class OverlaysCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("overlays")
                .then(Commands.literal("change_locs")
                        .executes(OverlaysCommand::execute))
                .then(Commands.literal("cl")
                        .executes(OverlaysCommand::execute))
                .then(Commands.literal("reset")
                        .executes(OverlaysCommand::reset)
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        ClientHelper.postCommandScreen = new ChangeOverlayLocationsScreen();
        return 1;
    }

    private static int reset(CommandContext<CommandSourceStack> context) {
        OverlayController.resetAll();
        CommandHelper.sendSuccess(context.getSource(), "command.overlays.reset.success");
        return 1;
    }
}
