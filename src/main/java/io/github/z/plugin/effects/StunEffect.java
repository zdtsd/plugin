package io.github.z.plugin.effects;

import com.bergerkiller.bukkit.common.math.Vector3;
import io.github.z.plugin.Plugin;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

//TODO: Refactor stun away from NoAI to a custom noAI system.
public class StunEffect extends Effect{

    public static final String stunNamespace = "Stun";
    private NamespacedKey key = new NamespacedKey(Plugin.getPlugin(), "StnSpdMod");

    public StunVFX vfx = null;

    public StunEffect(int duration, int strength) {
        super(duration, strength);
    }

    @Override
    public String getName() {
        return "Stun";
    }

    @Override
    public void onEffectAdd(Effect oldEffect, Entity entity) {
        if(oldEffect != null){
            vfx = ((StunEffect)oldEffect).vfx;
        }
        else{
            vfx = new StunVFX(entity);
            if(entity instanceof LivingEntity livingEntity){
                EntityUtils.replaceAttribute(livingEntity, Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(key, -1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            }
        }
    }

    @Override
    public void onEffectRemove(Effect newEffect, Entity entity) {
        if(newEffect == null){
            vfx.clear();
            if(entity instanceof LivingEntity livingEntity){
                EntityUtils.removeAttribute(livingEntity, Attribute.GENERIC_MOVEMENT_SPEED, key);
            }
        }
    }

    @Override
    public void onDamage(Entity entity, DamageEvent event, double level) {
        event.setCancelled(true);
    }

    @Override
    public void onProjectileLaunch(Entity entity, ProjectileLaunchEvent event, double level) {
        event.setCancelled(true);
    }

    private class StunVFX{

        private final Entity mEntity;
        private static final int numDisplayItems = 2;
        private static final double rotationDegPerTick = Math.toRadians(20), yOffset = 0.2, xOffset = 0.3;
        private static final float sizeScalar = 0.2f;

        private List<BlockDisplay> blockDisplays = new ArrayList<>();
        private List<Vector> blockOffsets = new ArrayList<>();
        public StunVFX(Entity entity){
            mEntity = entity;
            Bukkit.getLogger().info("Stun VFX created!");

            createVisuals();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), this::trueTick, 1);
        }

        private void createVisuals(){
            Vector posOffset = new Vector(0, yOffset + mEntity.getHeight() / 2, xOffset);
            for(int i=0; i < numDisplayItems; i++){
                //Save offset
                blockOffsets.add(posOffset.clone());

                //Calculate position of block
                Location position = EntityUtils.getEntityCenter(mEntity).add(posOffset);
                //Location position = mEntity.getLocation();

                //Spawn block
                mEntity.getWorld().spawn(position, BlockDisplay.class, entity -> {
                    entity.setBlock(Material.YELLOW_GLAZED_TERRACOTTA.createBlockData());
                    entity.setTransformation(new Transformation(
                            new Vector3f(), // no translation
                            new AxisAngle4f(), // no left rotation
                            new Vector3f(sizeScalar, sizeScalar, sizeScalar), // scale up by a factor of 2 on all axes
                            new AxisAngle4f() // no right rotation
                    ));
                    blockDisplays.add(entity);
                });


                //Rotate vector for next block
                posOffset.rotateAroundY(2.0 * Math.PI / numDisplayItems);
            }
        }

        private boolean isActive = true;

        private void trueTick(){
            if(isActive){
                for(int i = 0; i < numDisplayItems; i++){
                    //Rotate the blocks
                    blockOffsets.get(i).rotateAroundY(rotationDegPerTick);
                    blockDisplays.get(i).setInterpolationDelay(0);
                    blockDisplays.get(i).setInterpolationDuration(1);
                    blockDisplays.get(i).teleport(EntityUtils.getEntityCenter(mEntity).add(blockOffsets.get(i)));
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), this::trueTick, 1);
            }
        }

        public void clear(){
            Bukkit.getLogger().info("Stun VFX removed!");
            isActive = false;
            for(BlockDisplay display : blockDisplays){
                display.remove();
            }
        }
    }
}
