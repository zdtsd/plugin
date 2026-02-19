package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.utils.ScoreboardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SwashbucklerStance extends Ability {
    public static AbilityData<SwashbucklerStance> DATA = new AbilityData<>(SwashbucklerStance.class, "Stance", SwashbucklerStance::new)
            .scoreboardID("SwshStance")
            .displayMaterial(Material.IRON_SWORD);

    private static final String stanceDataID = "CurSwshStance";

    public enum SwashbucklerStanceType {
        NEUTRAL("&fNeutral Stance", 0,1, 1),
        OFFENSIVE("#ff1919Offensive Stance", 1, 1.3, 1.3),
        DEFENSIVE("#4874f7Defensive Stance", 2,0.8, 0.8),
        MASTERFUL("#db18b8Masterful Stance", 3,1.3, 0.8);

        private final String mName;
        private final int mID;
        private final double mOutgoingMult;
        private final double mIncomingMult;

        SwashbucklerStanceType(String name, int scoreboardValue, double outgoingDamageMult, double incomingDamageMult){
            mName = name;
            mID = scoreboardValue;
            mOutgoingMult = outgoingDamageMult;
            mIncomingMult = incomingDamageMult;
        }

        public double getOutgoingMult(){
            return mOutgoingMult;
        }
        public double getIncomingMult(){
            return mIncomingMult;
        }

        public int getScoreboardID() {
            return mID;
        }

        public String getName() {
            return mName;
        }
    }

    private SwashbucklerStanceType currentStance = SwashbucklerStanceType.NEUTRAL;
    private final int mDuration = 8 * 20;
    private int mRemainingDuration = 0;

    public SwashbucklerStance(Player player) {
        super(player);
    }

    @Override
    public AbilityData getData() {
        return DATA;
    }

    @Override
    public void onDamage(DamageEvent event) {
        event.addMultiplicativeModifier(currentStance.mOutgoingMult);
    }
    @Override
    public void onHurt(DamageEvent event) {
        event.addMultiplicativeModifier(currentStance.mIncomingMult);
    }

    //TESTING, REMOVE THIS
    @Override
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        SwashbucklerStanceType newStance;
        switch(getStance()){
            case NEUTRAL -> newStance = SwashbucklerStanceType.OFFENSIVE;
            case OFFENSIVE -> newStance = SwashbucklerStanceType.DEFENSIVE;
            case DEFENSIVE -> newStance = SwashbucklerStanceType.MASTERFUL;
            case MASTERFUL -> newStance = SwashbucklerStanceType.NEUTRAL;
            default -> newStance = SwashbucklerStanceType.NEUTRAL;
        }
        setStance(newStance);
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        mRemainingDuration -= 5;

        //Check for duration expiration
        if(mRemainingDuration <= 0){
            setStance(SwashbucklerStanceType.NEUTRAL);
        }
    }


    public SwashbucklerStanceType getStance(){
        return currentStance;
    }

    //Returns if the stance was properly swapped.
    public boolean setStance(SwashbucklerStanceType stance){
        //Check for stance swaps
        if(stance!= currentStance){
            mRemainingDuration = mDuration;
            currentStance = stance;
            return true;
        }
        return false;
    }

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add(currentStance.getName());
        return lines;
    }
}
