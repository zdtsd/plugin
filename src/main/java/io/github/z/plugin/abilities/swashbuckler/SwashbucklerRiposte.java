package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.events.DamageEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SwashbucklerRiposte extends Ability {

    public static AbilityData<SwashbucklerRiposte> DATA = new AbilityData<>(SwashbucklerRiposte.class, "Riposte", SwashbucklerRiposte::new)
            .scoreboardID("SwshRiposte")
            .displayMaterial(Material.IRON_SWORD);

    public SwashbucklerRiposte(Player mPlayer) {
        super(mPlayer);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    private static final double COOLDOWN_REDUCTION_PER_DAMAGE = 0.05;

    @Override
    public void onShieldBlockDamage(DamageEvent event) {
        double reduction = event.getDamage() * COOLDOWN_REDUCTION_PER_DAMAGE;
        AbilityManager.reduceCooldownsByPercent(mPlayer, reduction);
    }
}
