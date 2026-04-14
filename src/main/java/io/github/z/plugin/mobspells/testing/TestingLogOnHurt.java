package io.github.z.plugin.mobspells.testing;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.mobspells.MobSpell;
import io.github.z.plugin.mobspells.MobSpellData;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class TestingLogOnHurt extends MobSpell {
    public static final MobSpellData<TestingLogOnHurt> DATA = new MobSpellData<>(
            TestingLogOnHurt.class, "TestingLogOnHurt", TestingLogOnHurt::new);

    public TestingLogOnHurt(LivingEntity entity) {
        super(entity);
    }

    @Override
    public MobSpellData<?> getData() {
        return DATA;
    }

    @Override
    public void onHurt(DamageEvent event) {
        Bukkit.getLogger().info(mEntity.getName() + " took damage!");
    }
}
