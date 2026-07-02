package io.github.z.plugin.abilities.stormcaller;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.particles.ArcParticleSet;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class StormcallerStaticCharge extends Ability {

    public static AbilityData<StormcallerStaticCharge> DATA = new AbilityData<>(StormcallerStaticCharge.class, "Static Charge", StormcallerStaticCharge::new)
            .scoreboardID("StrmCharge")
            .displayMaterial(Material.LIGHTNING_ROD);

    private int mCharge = 0;
    private int mMaxCharge = 2;

    public StormcallerStaticCharge(Player player) {
        super(player);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    public int getCharge() {
        return mCharge;
    }

    public void incrementCharge() {
        mCharge++;
    }

    public void resetCharge() {
        mCharge = 0;
    }

    public boolean onCast(){
        if(mCharge >= mMaxCharge){
            resetCharge();
            return true;
        }
        else{
            incrementCharge();
            return false;
        }
    }

    public boolean onCastNoIncrement(){
        if(mCharge >= mMaxCharge){
            resetCharge();
            return true;
        }
        else{
            return false;
        }
    }


    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        String ohms= "&l&b";
        for(int i = 0; i < mMaxCharge; i++){
            if(i == mCharge){
                ohms += "&l&4";
            }
            ohms += "Ω";
        }
        String baseColor = mMaxCharge == mCharge ? "&b" : "&4";
        lines.add("&l" + baseColor + "Static Charge: " + ohms);
        return lines;
    }



}
