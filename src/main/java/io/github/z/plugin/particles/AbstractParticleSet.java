package io.github.z.plugin.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Particle;

public abstract class AbstractParticleSet<T extends AbstractParticleSet<T>> {
    protected Particle mParticleType = Particle.BLOCK_MARKER;
    protected int mCount = 1;
    protected double mDeltaX = 0;
    protected double mDeltaY = 0;
    protected double mDeltaZ = 0;
    protected double mExtra = 0;
    protected Color mColor = Color.WHITE;


    private T getSpecificSelf(){
        return (T) this;
    }

    public T setParticle(Particle particle){
        mParticleType = particle;
        return getSpecificSelf();
    }
    public T setCount(int count){
        mCount = count;
        return getSpecificSelf();
    }

    public T setX(double deltaX){
        mDeltaX = deltaX;
        return getSpecificSelf();
    }
    public T setY(double deltaY){
        mDeltaY = deltaY;
        return getSpecificSelf();
    }
    public T setZ(double deltaZ){
        mDeltaZ = deltaZ;
        return getSpecificSelf();
    }

    public T setExtra(double extra){
        mExtra = extra;
        return getSpecificSelf();
    }

    public T setColor(Color color){
        mColor = color;
        return getSpecificSelf();
    }

    protected ParticleBuilder getDefaultBuilder(){
        return new ParticleBuilder(mParticleType)
                .count(mCount)
                .offset(mDeltaX, mDeltaY, mDeltaZ)
                .extra(mExtra);
    }

    public abstract void generateParticles();





}
