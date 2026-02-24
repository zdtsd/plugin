package io.github.z.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

public class DoubleJumpManager {
    //TODO: Deal fall damage to players with double jump tokens.

    //Manages tokens that allow players creative mode flight for the purposes of double jump detection.
    //CAUTION: Tokens MUST be cleared by the source.
    private static Map<Player, List<DoubleJumpToken>> mJumpTriggerTokens = new HashMap<>();
    private static DoubleJumpManager mManager;

    public DoubleJumpManager(){
        mManager = this;
    }

    public class DoubleJumpToken{
        private final String mTokenID;
        public DoubleJumpToken(String tokenID){
            mTokenID = tokenID;
        }
        public String getID(){
            return mTokenID;
        }
    }

    public static void addDoubleJumpToken(Player player, String tokenID){
        mManager.addDoubleJumpTokenLocal(player, tokenID);
    }
    public void addDoubleJumpTokenLocal(Player player, String tokenID){
        List<DoubleJumpToken> playerTokens = mJumpTriggerTokens.get(player);
        if(playerTokens == null){
            Bukkit.getLogger().info("PlayerTokens is null.");
            playerTokens = new ArrayList<>();
            playerTokens.add(new DoubleJumpToken(tokenID));
            mJumpTriggerTokens.put(player, playerTokens);
            player.setAllowFlight(true);
        }
        else{
            Bukkit.getLogger().info("PlayerTokens is NOT null.");
            //Only add new tokens when necessary.
            for(DoubleJumpToken token : playerTokens){
                if(Objects.equals(token.getID(), tokenID)){
                    Bukkit.getLogger().info("Matching token found, not adding new token to list.");
                    return;
                }
            }

            Bukkit.getLogger().info("Adding token");
            playerTokens.add(new DoubleJumpToken(tokenID));
        }
    }

    //Returns TRUE if the token is found, FALSE otherwise.
    public static boolean removeDoubleJumpToken(Player player, String tokenID){
        return mManager.removeDoubleJumpTokenLocal(player, tokenID);
    }

    public boolean removeDoubleJumpTokenLocal(Player player, String tokenID){
        List<DoubleJumpToken> playerTokens = mJumpTriggerTokens.get(player);
        if(playerTokens == null){
            Bukkit.getLogger().info("PlayerTokens is null.");
            return false;
        }
        else{
            for(int i=0; i < playerTokens.size(); i++){
                if(Objects.equals(playerTokens.get(i).getID(), tokenID)){
                    playerTokens.remove(i);
                    if(playerTokens.isEmpty()){
                        mJumpTriggerTokens.put(player, null);
                        if(player.getGameMode() != GameMode.CREATIVE){
                            player.setAllowFlight(false);
                        }
                    }
                    return true;
                }
            }
        }
        player.setAllowFlight(false);
        return false;

    }
}
