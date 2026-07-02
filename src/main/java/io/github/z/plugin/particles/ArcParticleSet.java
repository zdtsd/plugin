package io.github.z.plugin.particles;

import io.github.z.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ArcParticleSet extends AbstractParticleSet<ArcParticleSet> {

    private int mArcWidth;
    private int mArcLength;
    private int mDuration;
    private double mArcAngle;
    private Location mCenter;
    private Vector mAxis;
    private double mInnerRadius;
    private double mOuterRadius;

    public ArcParticleSet setArcWidth(int arcWidth) { mArcWidth = arcWidth; return this; }

    public ArcParticleSet setArcLength(int arcLength) { mArcLength = arcLength; return this; }

    public int getDuration() { return mDuration; }
    public ArcParticleSet setDuration(int duration) { mDuration = duration; return this; }

    public ArcParticleSet setArcAngle(double arcAngle) { mArcAngle = arcAngle; return this; }

    public ArcParticleSet setCenter(Location center) { mCenter = center; return this; }

    public ArcParticleSet setAxis(Vector axis) { mAxis = axis; return this; }

    public ArcParticleSet setInnerRadius(double innerRadius) { mInnerRadius = innerRadius; return this; }

    public ArcParticleSet setOuterRadius(double outerRadius) { mOuterRadius = outerRadius; return this; }

    @Override
    public void generateParticles() {
        for (int t = 0; t < mDuration; t++) {
            int startIndex = (int) Math.round((double) t * mArcLength / mDuration);
            int endIndex = (int) Math.round((double) (t + 1) * mArcLength / mDuration);
            int delay = t;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {
                for (int i = startIndex; i < endIndex; i++) {
                    spawnLine(i);
                }
            }, delay);
        }
    }

    private void spawnLine(int i) {
        double angleDeg = (mArcLength == 1) ? 0 : -mArcAngle / 2.0 + i * mArcAngle / (mArcLength - 1);
        double angleRad = Math.toRadians(angleDeg);

        Vector direction = mCenter.getDirection().clone();
        direction.rotateAroundNonUnitAxis(mAxis, angleRad);

        Location start = mCenter.clone().add(direction.clone().multiply(mInnerRadius));
        Location end = mCenter.clone().add(direction.clone().multiply(mOuterRadius));

        new LineParticleSet()
                .setCount(mArcWidth)
                .setParticle(mParticleType)
                .setX(mDeltaX)
                .setY(mDeltaY)
                .setZ(mDeltaZ)
                .setExtra(mExtra)
                .setStart(start)
                .setEnd(end)
                .setColor(mColor)
                .generateParticles();
    }
}
