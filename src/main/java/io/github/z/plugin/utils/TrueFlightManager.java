package io.github.z.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

public class TrueFlightManager {

    //Manages tokens that grant a player uncancelled flight, as opposed to DoubleJumpManager's flight-for-detection tokens.
    //CAUTION: Tokens MUST be cleared by the source.
    private static Map<Player, List<TrueFlightToken>> mTrueFlightTokens = new HashMap<>();
    private static TrueFlightManager mManager;

    public TrueFlightManager(){
        mManager = this;
    }

    public class TrueFlightToken{
        private final String mTokenID;
        public TrueFlightToken(String tokenID){
            mTokenID = tokenID;
        }
        public String getID(){
            return mTokenID;
        }
    }

    public static void addTrueFlightToken(Player player, String tokenID){
        mManager.addTrueFlightTokenLocal(player, tokenID);
    }
    public void addTrueFlightTokenLocal(Player player, String tokenID){
        List<TrueFlightToken> playerTokens = mTrueFlightTokens.get(player);
        player.setAllowFlight(true);
        if(playerTokens == null){
            playerTokens = new ArrayList<>();
            playerTokens.add(new TrueFlightToken(tokenID));
            mTrueFlightTokens.put(player, playerTokens);
        }
        else{
            //Only add new tokens when necessary.
            for(TrueFlightToken token : playerTokens){
                if(Objects.equals(token.getID(), tokenID)){
                    return;
                }
            }
            playerTokens.add(new TrueFlightToken(tokenID));
        }
    }

    //Returns TRUE if the token is found, FALSE otherwise.
    public static boolean removeTrueFlightToken(Player player, String tokenID){
        return mManager.removeTrueFlightTokenLocal(player, tokenID);
    }

    public boolean removeTrueFlightTokenLocal(Player player, String tokenID){
        List<TrueFlightToken> playerTokens = mTrueFlightTokens.get(player);
        if(playerTokens == null){
            Bukkit.getLogger().info("PlayerTokens is null.");
            return false;
        }
        else{
            for(int i=0; i < playerTokens.size(); i++){
                if(Objects.equals(playerTokens.get(i).getID(), tokenID)){
                    playerTokens.remove(i);
                    if(playerTokens.isEmpty()){
                        mTrueFlightTokens.put(player, null);
                        if(player.getGameMode() != GameMode.CREATIVE){
                            player.setFlying(false);
                            if(!DoubleJumpManager.hasDoubleJumpToken(player)){
                                player.setAllowFlight(false);
                            }
                        }
                    }
                    return true;
                }
            }
        }
        if(player.getGameMode() != GameMode.CREATIVE && !DoubleJumpManager.hasDoubleJumpToken(player)){
            player.setAllowFlight(false);
        }
        return false;

    }

    //Returns TRUE if the player currently holds at least one true flight token.
    public static boolean hasTrueFlightToken(Player player){
        List<TrueFlightToken> playerTokens = mTrueFlightTokens.get(player);
        return playerTokens != null && !playerTokens.isEmpty();
    }
}
