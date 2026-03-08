package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public class SwashbuckerShieldBash extends CooldownAbility {
    public static AbilityData<SwashbuckerShieldBash> DATA = new AbilityData<>(SwashbuckerShieldBash.class, "Shield Bash", SwashbuckerShieldBash::new)
            .scoreboardID("SwshBash")
            .displayMaterial(Material.SHIELD)
            .cooldown(3 * 20);

    SwashbucklerStance playerStance;
    private static final double coneWidthDegrees = Math.toRadians(90), coneRange = 6;
    private static final double damageDealt = 6, knockbackDealt = 0.4;
    private static final int mStunDuration = 30;
    private SwashbucklerStance getOrFetchStance(){
        if(playerStance == null){
            playerStance = (SwashbucklerStance) AbilityManager.getAbility(mPlayer, SwashbucklerStance.class);
        }
        return playerStance;
    }

    public SwashbuckerShieldBash(Player mPlayer) {
        super(mPlayer);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        if(event.getAction().isRightClick()){
            if(!isCastable()){
                return;
            }

            //TODO: Dont cast on player, adjust hitbox for better hitting
            //Dont use with no enemies hit.
            Collection<Entity> entitiesHit = EntityUtils.getEntitiesInCone(mPlayer.getEyeLocation(), coneRange, coneWidthDegrees);
            entitiesHit.removeIf(entity -> !(entity instanceof LivingEntity) || entity instanceof Player);
            if(entitiesHit.isEmpty()){
                return;
            }

            spendCharge();

            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, 0.8f, 0.1f);

            SwashbucklerStance stance = getOrFetchStance();
            for(Entity entity : entitiesHit){
                if(entity instanceof LivingEntity le){
                    EffectManager.applyEffect(entity, new StunEffect(mStunDuration, 1), StunEffect.stunNamespace);
                    DamageUtils.damage(mPlayer, le, new DamageEvent.Metadata(DamageEvent.DamageType.MELEE_SKILL, DATA.getName()),
                            damageDealt, true, false, true);
                    le.knockback(knockbackDealt, -le.getLocation().getX() + mPlayer.getLocation().getX(),-le.getLocation().getZ() + mPlayer.getLocation().getZ() );
                }
            }
            if(stance.getStance() == SwashbucklerStance.SwashbucklerStanceType.DEFENSIVE){
                stance.setStance(SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE);
            }
        }
    }
}
