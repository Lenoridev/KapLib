package net.kapitencraft.kap_lib.client;

import net.kapitencraft.kap_lib.client.overlay.OverlayController;
import net.kapitencraft.kap_lib.client.particle.ShimmerShieldParticleOptions;
import net.kapitencraft.kap_lib.client.particle.animation.ParticleAnimationAcceptor;
import net.kapitencraft.kap_lib.io.network.ModMessages;
import net.kapitencraft.kap_lib.io.network.request.RequestHandler;
import net.kapitencraft.kap_lib.util.ShimmerShieldManager;
import org.checkerframework.checker.units.qual.A;

public class LibClient {
    public static final OverlayController controller = OverlayController.load();
    public static final RequestHandler handler = new RequestHandler(ModMessages::sendToServer);
    public static final ParticleAnimationAcceptor acceptor = new ParticleAnimationAcceptor();
    public static final ShimmerShieldManager shieldManager = new ShimmerShieldManager();
}
