package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwashbucklerReposition extends CooldownAbility {

    public static AbilityData<SwashbucklerReposition> DATA = new AbilityData<>(SwashbucklerReposition.class, "Reposition", SwashbucklerReposition::new)
            .scoreboardID("SwshDash")
            .displayMaterial(Material.IRON_BOOTS)
            .cooldown(5 * 20)
            .maxCharges(3);

    SwashbucklerStance playerStance;
    private static final double dashSpeed = 1.5;

    private SwashbucklerStance getOrFetchStance(){
        if(playerStance == null){
            playerStance = (SwashbucklerStance) AbilityManager.getAbility(mPlayer, SwashbucklerStance.class);
        }
        return playerStance;
    }

    public SwashbucklerReposition(Player mPlayer) {
        super(mPlayer);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    @Override
    public void onDropKey() {
        if(mPlayer.isSneaking()){return;}
        if(!isCastable()){return;}
        spendCharge();

        SwashbucklerStance stance = getOrFetchStance();
        if(stance.getStance() == SwashbucklerStance.SwashbucklerStanceType.DEFENSIVE){
            stance.setStance(SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE);
        }
        else if(stance.getStance() == SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE){
            stance.setStance(SwashbucklerStance.SwashbucklerStanceType.DEFENSIVE);
        }
        EntityUtils.setForwardsVelocity(mPlayer, dashSpeed);
        mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.2f, 0.7f);
    }
}
