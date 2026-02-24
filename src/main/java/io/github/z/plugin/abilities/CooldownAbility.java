package io.github.z.plugin.abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CooldownAbility extends Ability{
    private int currentCharges = 0;
    private int currentCooldownProgress = 0;
    private int mMaxCharges;
    private int mCooldown;
    public CooldownAbility(Player mPlayer) {
        super(mPlayer);
        mMaxCharges = getData().getMaxCharges();
        mCooldown = getData().getCooldown();
    }

    private String sidebarLine = "";

    //4Hz
    public void tickCooldown(){
        progressCooldown(5);
    }

    public void progressCooldown(int ticks){
        int maxCharges = getData().getMaxCharges();
        if(currentCharges >= maxCharges){
            return;
        }

        currentCooldownProgress -= ticks;
        if(currentCooldownProgress <= 0){
            currentCharges++;
            if(currentCharges == maxCharges){
                currentCooldownProgress = getData().getCooldown();
            }
            else{
                currentCooldownProgress += getData().getCooldown();
            }
        }

        updateSidebarLine();
    }


    private void updateSidebarLine(){
        sidebarLine = "&l" + getData().getName() + "||";
        String cooldownBar = "▯▯▯▯▯▯▯▯▯▯▯▯";
        int ticksPerCharge = 12;
        switch(mMaxCharges){
            case 2 -> {cooldownBar = "▯▯▯▯▯▯|▯▯▯▯▯▯";
                        ticksPerCharge = 7;}
            case 3 -> {cooldownBar = "▯▯▯▯|▯▯▯▯|▯▯▯▯";
                ticksPerCharge = 5;}
            case 4 -> {cooldownBar = "▯▯▯|▯▯▯|▯▯▯|▯▯▯";
                ticksPerCharge = 4;}
            case 5 -> {cooldownBar = "▯▯|▯▯|▯▯|▯▯|▯▯";
                ticksPerCharge = 3;}
            case 6 -> {cooldownBar = "▯▯|▯▯|▯▯|▯▯|▯▯|▯▯";
                ticksPerCharge = 3;}
        }

        if(currentCharges == 0){
            sidebarLine = "&4" + sidebarLine + "&b&l";
        }
        else{
            sidebarLine = "&b" + sidebarLine + "&b&l";
        }

        if(mMaxCharges == currentCharges){
            sidebarLine = sidebarLine.concat(cooldownBar);
        }
        else{
            int redStart = ticksPerCharge * currentCharges;
            redStart += ((mCooldown - currentCooldownProgress) * ticksPerCharge) / (mCooldown);
            cooldownBar = cooldownBar.substring(0, redStart) + "&4&l" + cooldownBar.substring(redStart);
            sidebarLine = sidebarLine.concat(cooldownBar);
        }


    }

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();

        String line = sidebarLine;

        lines.add(line);
        return lines;
    }

    protected boolean isCastable(){
        return currentCharges >= 1;
    }

    protected void spendCharge(){
        currentCharges -= 1;
    }
}
