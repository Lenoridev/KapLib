package net.kapitencraft.kap_lib.mixin.classes.client;

import net.kapitencraft.kap_lib.helpers.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", ordinal = 1))
    public void setScreen(Minecraft mc, Screen screen) {
        if (ClientHelper.postCommandScreen != null) {
            mc.setScreen(ClientHelper.postCommandScreen);
            ClientHelper.postCommandScreen = null;
        } else mc.setScreen(null);
    }
}