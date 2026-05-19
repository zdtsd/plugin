package io.github.z.plugin.libraryofsouls;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum EntityDataKey {

    EXPLOSION_RADIUS("explosion_radius", "Explosion Radius", Material.TNT, "3",
            EntityType.CREEPER),

    FUSE_TICKS("fuse_ticks", "Fuse Ticks", Material.GUNPOWDER, "30",
            EntityType.CREEPER),

    POWERED("powered", "Powered", Material.LIGHTNING_ROD, "false",
            EntityType.CREEPER),

    SIZE("size", "Size", Material.SLIME_BALL, "1",
            EntityType.SLIME, EntityType.MAGMA_CUBE),

    EXPLOSION_POWER("explosion_power", "Explosion Power", Material.FIRE_CHARGE, "1",
            EntityType.GHAST),

    BABY("baby", "Baby", Material.EGG, "false",
            EntityType.SHEEP, EntityType.COW, EntityType.PIG, EntityType.CHICKEN,
            EntityType.RABBIT, EntityType.WOLF, EntityType.CAT, EntityType.OCELOT,
            EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA,
            EntityType.TRADER_LLAMA, EntityType.VILLAGER, EntityType.ZOMBIE,
            EntityType.HUSK, EntityType.DROWNED, EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.HOGLIN, EntityType.PIGLIN,
            EntityType.AXOLOTL, EntityType.GOAT, EntityType.CAMEL, EntityType.SNIFFER,
            EntityType.PANDA, EntityType.POLAR_BEAR, EntityType.FOX, EntityType.BEE,
            EntityType.TURTLE),

    SHEEP_COLOR("color", "Color", Material.WHITE_WOOL, "WHITE",
            EntityType.SHEEP),

    VILLAGER_PROFESSION("profession", "Profession", Material.EMERALD, "NONE",
            EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER),

    VILLAGER_LEVEL("level", "Villager Level", Material.EXPERIENCE_BOTTLE, "1",
            EntityType.VILLAGER),

    RABBIT_TYPE("rabbit_type", "Rabbit Type", Material.RABBIT_HIDE, "BROWN",
            EntityType.RABBIT);

    private final String mKey;
    private final String mDisplayName;
    private final Material mMaterial;
    private final String mDefaultValue;
    private final Set<EntityType> mApplicableTypes;

    EntityDataKey(String key, String displayName, Material material, String defaultValue, EntityType... types) {
        mKey = key;
        mDisplayName = displayName;
        mMaterial = material;
        mDefaultValue = defaultValue;
        mApplicableTypes = EnumSet.copyOf(Arrays.asList(types));
    }

    public String getKey() { return mKey; }
    public String getDisplayName() { return mDisplayName; }
    public Material getMaterial() { return mMaterial; }
    public String getDefaultValue() { return mDefaultValue; }

    public boolean appliesTo(EntityType type) {
        return mApplicableTypes.contains(type);
    }

    public static EntityDataKey fromKey(String key) {
        for (EntityDataKey k : values()) {
            if (k.mKey.equals(key)) return k;
        }
        return null;
    }

    public static EntityDataKey[] forType(EntityType type) {
        return Arrays.stream(values())
                .filter(k -> k.appliesTo(type))
                .toArray(EntityDataKey[]::new);
    }

    public void apply(LivingEntity entity, String value) {
        try {
            switch (this) {
                case EXPLOSION_RADIUS -> {
                    if (entity instanceof Creeper c) c.setExplosionRadius(Integer.parseInt(value));
                }
                case FUSE_TICKS -> {
                    if (entity instanceof Creeper c) c.setMaxFuseTicks(Integer.parseInt(value));
                }
                case POWERED -> {
                    if (entity instanceof Creeper c) c.setPowered(Boolean.parseBoolean(value));
                }
                case SIZE -> {
                    if (entity instanceof Slime s) s.setSize(Integer.parseInt(value));
                }
                case EXPLOSION_POWER -> {
                    if (entity instanceof Ghast g) g.setExplosionPower(Integer.parseInt(value));
                }
                case BABY -> {
                    boolean baby = Boolean.parseBoolean(value);
                    if (entity instanceof Ageable a) {
                        if (baby) a.setBaby(); else a.setAdult();
                    } else if (entity instanceof Zombie z) {
                        z.setBaby(baby);
                    } else if (entity instanceof Piglin p) {
                        p.setBaby(baby);
                    }
                }
                case SHEEP_COLOR -> {
                    if (entity instanceof Sheep s) s.setColor(DyeColor.valueOf(value));
                }
                case VILLAGER_PROFESSION -> {
                    if (entity instanceof Villager v)
                        v.setProfession(Villager.Profession.valueOf(value));
                    else if (entity instanceof ZombieVillager zv)
                        zv.setVillagerProfession(Villager.Profession.valueOf(value));
                }
                case VILLAGER_LEVEL -> {
                    if (entity instanceof Villager v) v.setVillagerLevel(Integer.parseInt(value));
                }
                case RABBIT_TYPE -> {
                    if (entity instanceof Rabbit r) r.setRabbitType(Rabbit.Type.valueOf(value));
                }
            }
        } catch (Exception ignored) {}
    }
}
