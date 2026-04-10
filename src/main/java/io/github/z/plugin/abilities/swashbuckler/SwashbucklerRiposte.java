package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class SwashbucklerRiposte extends CooldownAbility {

    public static AbilityData<SwashbucklerRiposte> DATA = new AbilityData<>(SwashbucklerRiposte.class, "Riposte", SwashbucklerRiposte::new)
            .scoreboardID("SwshRip")
            .displayMaterial(Material.SHIELD)
            .cooldown(3 * 20);
    public SwashbucklerRiposte(Player mPlayer) {
        super(mPlayer);
    }


    private static final String riposteStunName = "riposteStun";
    //TODO: Remove block window
    private static final int blockWindow = 2000, stunDuration = 2 * 20;
    private static final double knockback = 0.6, bonusDamage = 10, radius = 3;

    private boolean isCharged = false;
    private double blockWindowElapsed = 0;

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }


    @Override
    public void onClick(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND){
            blockWindowElapsed = 0;
        }
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        if(player.getActiveItem().getType() == Material.SHIELD){
            blockWindowElapsed += 5;
        }
        else{
            blockWindowElapsed = 0;
        }
    }

    @Override
    public void onShieldBlockDamage(DamageEvent event) {
        if(blockWindowElapsed <= blockWindow){
            isCharged = true;
        }
    }

    @Override
    public void onDamage(DamageEvent event) {
        if(isCharged && event.isType(DamageEvent.DamageType.MELEE_ATTACK)){
            event.addBaseDamage(bonusDamage);
            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.3f, 0.8f);
            isCharged = false;
            for(Entity entity : EntityUtils.getEntitiesInSphere(event.getDamagee().getLocation(), radius)){
                if(entity instanceof LivingEntity le && !(entity instanceof Player) && entity != event.getDamagee()){
                    if(le != event.getDamagee()){
                        DamageUtils.damage(mPlayer, le, DamageEvent.DamageType.MELEE_SKILL, event.getBaseDamage(), DATA.getName(), true, false);
                    }
                    EffectManager.applyEffect(le, new StunEffect(stunDuration, 1), riposteStunName);
                    double xKnockback = le.getX() - mPlayer.getX();
                    double zKnockback = le.getZ() - mPlayer.getZ();
                    le.knockback(knockback, xKnockback, zKnockback);
                }
            }
        }
    }

    @Override
    public List<String> getSidebarLines() {
        List<String> sidebarLines = new ArrayList<>();
        if(isCharged){
            sidebarLines.add("#4874f7Riposte Charged!");
        }
        else{
            sidebarLines.add("#ff1919Riposte Uncharged!");
        }
        return sidebarLines;
    }
}
