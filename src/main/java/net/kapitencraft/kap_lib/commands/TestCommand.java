package net.kapitencraft.kap_lib.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.kapitencraft.kap_lib.client.gui.screen.TestScreen;
import net.kapitencraft.kap_lib.helpers.ClientHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lib_test").executes(commandContext -> {
            ClientHelper.postCommandScreen = new TestScreen();
            return 1;
        }));
    }
}
