package net.kapitencraft.kap_lib;

import org.slf4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.slf4j.MarkerFactory;

public class Markers {
    public static final Marker REQUESTS = getMarker("Requests");
    public static final Marker PARTICLE_ENGINE = getMarker("ParticleEngine");
    public static final Marker REQUIREMENTS_MANAGER = getMarker("RequirementManager");

    private static Marker getMarker(String name) {
        return MarkerFactory.getMarker(name);
    }
}
