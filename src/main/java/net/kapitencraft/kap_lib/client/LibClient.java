package net.kapitencraft.kap_lib.client;

import net.kapitencraft.kap_lib.client.overlay.OverlayManager;
import net.kapitencraft.kap_lib.client.particle.animation.ParticleAnimationAcceptor;
import net.kapitencraft.kap_lib.io.network.ModMessages;
import net.kapitencraft.kap_lib.io.network.request.RequestHandler;
import net.kapitencraft.kap_lib.util.ShimmerShieldManager;

/**
 * client handler for much information
 */
public interface LibClient {


    /**
     * overlay controller; controls the given Overlays for the screen
     */
    OverlayManager controller = OverlayManager.load();


    /**
     * handles the server data requests
     */
    RequestHandler handler = new RequestHandler(ModMessages::sendToServer);


    /**
     * handles ParticleAnimations either clientside or synced from the server
     */
    ParticleAnimationAcceptor acceptor = new ParticleAnimationAcceptor();


    /**
     * shimmer shield manager; handles
     */
    ShimmerShieldManager manager = new ShimmerShieldManager();
}
