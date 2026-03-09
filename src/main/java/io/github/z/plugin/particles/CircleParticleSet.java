package io.github.z.plugin.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CircleParticleSet extends AbstractParticleSet<CircleParticleSet>{

    private Location mCenter;
    private double mRadius;

    public CircleParticleSet setCenter(Location center){
        mCenter = center;
        return this;
    }

    public CircleParticleSet setRadius(double radius){
        mRadius = radius;
        return this;
    }
    @Override
    public void generateParticles() {
        ParticleBuilder builder = getDefaultBuilder().count(1);
        double angle = Math.toRadians(360.0/mCount);
        for(int i = 0; i < mCount; i++){
            builder.location(mCenter.clone().add(new Vector(Math.cos(angle * i) * mRadius, 0, Math.sin(angle * i) * mRadius))).spawn();
        }
    }
}
