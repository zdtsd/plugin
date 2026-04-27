package io.github.z.plugin.mobspells.passives;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.mobspells.MobSpell;
import io.github.z.plugin.mobspells.MobSpellData;
import io.github.z.plugin.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitTask;

public class Gravedigger extends MobSpell {
    public static final MobSpellData<Gravedigger> DATA = new MobSpellData<>(
            Gravedigger.class, "Gravedigger", Gravedigger::new);

    private static final int DIG_RADIUS = 4, DIG_COUNT = 5;
    private static final float DIG_SOUND_VOLUME = 3.0f;
    private static final float DIG_SOUND_PITCH = 1.0f;
    private static final boolean BREAK_NATURALLY = false;

    public Gravedigger(LivingEntity entity) {
        super(entity);
    }

    @Override
    public MobSpellData<?> getData() {
        return DATA;
    }

    public void dig(Location center) {
        center.getWorld().playSound(center, Sound.BLOCK_GRAVEL_BREAK, DIG_SOUND_VOLUME, DIG_SOUND_PITCH);
        BlockUtils.breakCylinder(center, DIG_RADIUS, getLevel(), BREAK_NATURALLY);
    }

    @Override
    public void onCreeperExplode(EntityExplodeEvent event) {
        event.setCancelled(true);

        Location origin = event.getEntity().getLocation().toBlockLocation();
        int level = getLevel();
        int[] count = {0};

        BukkitTask[] task = new BukkitTask[1];
        task[0] = Bukkit.getScheduler().runTaskTimer(Plugin.getPlugin(), () -> {
            dig(origin.clone().subtract(0, count[0] * level, 0));
            if (++count[0] >= DIG_COUNT) {
                BlockUtils.replaceCircle(origin.clone().subtract(0, (DIG_COUNT - 1)* level + 1, 0), 4, Material.POINTED_DRIPSTONE, BREAK_NATURALLY);
                task[0].cancel();
            }
        }, 0L, 2L);
    }

    @Override
    public void onDamage(DamageEvent event) {
        event.setCancelled(true);
    }
}
