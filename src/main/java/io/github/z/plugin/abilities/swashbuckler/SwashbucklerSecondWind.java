package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SwashbucklerSecondWind extends Ability {

    public static AbilityData<SwashbucklerSecondWind> DATA = new AbilityData<>(SwashbucklerSecondWind.class, "Second Wind", SwashbucklerSecondWind::new)
            .scoreboardID("Swsh2ndWnd")
            .displayMaterial(Material.COOKED_BEEF);

    public SwashbucklerSecondWind(Player mPlayer) {
        super(mPlayer);
    }
    @Override
    public AbilityData<?> getData() {
        return DATA;
    }
    SwashbucklerStance playerStance;
    private SwashbucklerStance getOrFetchStance(){
        if(playerStance == null){
            playerStance = (SwashbucklerStance) AbilityManager.getAbility(mPlayer, SwashbucklerStance.class);
        }
        return playerStance;
    }

    private static final double healAmount = 2;

    @Override
    public void onDamage(DamageEvent event) {
        if(event.isCrit() && getOrFetchStance().getStance() == SwashbucklerStance.SwashbucklerStanceType.DEFENSIVE){
            DamageUtils.healEntity(mPlayer, healAmount);
        }
    }
}
