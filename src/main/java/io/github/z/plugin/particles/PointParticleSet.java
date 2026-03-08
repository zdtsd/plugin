package io.github.z.plugin.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;

public class PointParticleSet extends AbstractParticleSet<PointParticleSet>{

    private Location mLocation;

    public PointParticleSet setLocation(Location location){
        mLocation = location;
        return this;
    }
    @Override
    public void generateParticles() {
        ParticleBuilder builder = getDefaultBuilder().location(mLocation);
        builder.spawn();
    }
}
