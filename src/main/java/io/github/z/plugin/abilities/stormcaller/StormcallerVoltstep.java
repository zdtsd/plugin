package io.github.z.plugin.abilities.stormcaller;

import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.effects.Effect;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.effects.TrueFlightEffect;
import io.github.z.plugin.utils.LocationUtils;
import io.github.z.plugin.utils.PlayerUtils;
import io.github.z.plugin.utils.TrueFlightManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import javax.xml.crypto.Data;

public class StormcallerVoltstep extends CooldownAbility {
    public static AbilityData<StormcallerVoltstep> DATA = new AbilityData<>(StormcallerVoltstep.class, "Voltstep", StormcallerVoltstep::new)
            .scoreboardID("StrmStp")
            .displayMaterial(Material.LIGHTNING_ROD)
            .cooldown(3 * 20)
            .maxCharges(2);

    public StormcallerVoltstep(Player mPlayer) {
        super(mPlayer);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    private static final int distance = 8, flightDuration = 5 * 20;
    private StormcallerStaticCharge mStaticCharge;

    private StormcallerStaticCharge getOrFetchStaticCharge() {
        if (mStaticCharge == null) {
            mStaticCharge = (StormcallerStaticCharge) AbilityManager.getAbility(mPlayer, StormcallerStaticCharge.class);
        }
        return mStaticCharge;
    }

    @Override
    public void onDropKey() {
        if(!isCastable()) return;
        spendCharge();
        boolean mIsEmpowered = getOrFetchStaticCharge().onCast();

        //Boxcast for the teleport destination
        BoundingBox boxCastBox = mPlayer.getBoundingBox();
        Location playerStartLocation = mPlayer.getLocation();
        Vector playerLookDir = playerStartLocation.getDirection();
        LocationUtils.travelTillObstructed(mPlayer.getWorld(), boxCastBox, distance, playerLookDir, 0.2f, true);
        Location endLoc = boxCastBox.getCenter().setY(boxCastBox.getMinY()).toLocation(mPlayer.getWorld()).setDirection(playerLookDir);

        //Teleport to the destination
        PlayerUtils.playerTeleport(mPlayer, endLoc);

        //Grant flight
        if(mIsEmpowered){
            Effect flightEffect = new TrueFlightEffect(flightDuration, 1, DATA.getScoreboardID());
            EffectManager.applyEffect(mPlayer, flightEffect, DATA.getScoreboardID());
            if(mPlayer.getAllowFlight()){
                mPlayer.setFlying(true);
            }
        }
    }
}
