package io.github.z.plugin.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LineParticleSet extends AbstractParticleSet<LineParticleSet> {

    private Location mStart;
    private Location mEnd;

    public LineParticleSet setStart(Location start) {
        mStart = start;
        return this;
    }

    public LineParticleSet setEnd(Location end) {
        mEnd = end;
        return this;
    }

    @Override
    public void generateParticles() {
        Vector diff = mEnd.toVector().subtract(mStart.toVector());
        double distance = diff.length();
        if (distance == 0) return;
        Vector step = diff.multiply(1.0 / mCount);
        ParticleBuilder builder = getDefaultBuilder().count(1);
        for (int i = 0; i < mCount; i++) {
            builder.location(mStart.clone().add(step.clone().multiply(i))).spawn();
        }
    }
}
