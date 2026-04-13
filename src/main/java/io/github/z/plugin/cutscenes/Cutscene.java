package io.github.z.plugin.cutscenes;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Figure out whatever the hell I actually did here
public class Cutscene {

    public static class Keyframe {
        private final Location mLocation;
        private final int mTick;

        public Keyframe(Location location, int tick) {
            mLocation = location;
            mTick = tick;
        }

        public Location getLocation() {
            return mLocation;
        }

        public int getTick() {
            return mTick;
        }
    }

    private final List<Player> mPlayers;
    private final List<Keyframe> mKeyframes;
    private int mCurrentTick = 0;
    private ItemDisplay mCamera;
    private int mNextKeyframeIndex = 1;
    private final Map<Player, Location> mStartingLocations = new HashMap<>();
    private final Map<Player, GameMode> mStartingGameModes = new HashMap<>();
    private Location mReturnLocationOverride = null;

    public Cutscene(List<Player> players, List<Keyframe> keyframes) {
        mPlayers = new ArrayList<>(players);
        mKeyframes = new ArrayList<>(keyframes);
    }

    public Cutscene addPlayer(Player player) {
        mPlayers.add(player);
        return this;
    }

    private static final int MAX_TELEPORT_DURATION = 59;

    public Cutscene addKeyframe(Location location, int tick) {
        if (mKeyframes.isEmpty()) {
            mKeyframes.add(new Keyframe(location, tick));
            mKeyframes.add(new Keyframe(location, tick + 1));
            mKeyframes.add(new Keyframe(location, tick + 5));
            return this;
        }
        if (!mKeyframes.isEmpty()) {
            Keyframe prev = mKeyframes.get(mKeyframes.size() - 1);
            int segmentLength = tick - prev.getTick();
            if (segmentLength > MAX_TELEPORT_DURATION) {
                int numSegments = (int) Math.ceil((double) segmentLength / MAX_TELEPORT_DURATION);
                for (int i = 1; i < numSegments; i++) {
                    double t = (double) i / numSegments;
                    int intermediateTick = prev.getTick() + (int) Math.round(t * segmentLength);
                    mKeyframes.add(new Keyframe(interpolateLocation(prev.getLocation(), location, t), intermediateTick));
                }
            }
        }
        mKeyframes.add(new Keyframe(location, tick));
        return this;
    }


    private static Location interpolateLocation(Location a, Location b, double t) {
        double x = a.getX() + (b.getX() - a.getX()) * t;
        double y = a.getY() + (b.getY() - a.getY()) * t;
        double z = a.getZ() + (b.getZ() - a.getZ()) * t;
        float yaw = a.getYaw() + lerpAngle(a.getYaw(), b.getYaw()) * (float) t;
        float pitch = a.getPitch() + (b.getPitch() - a.getPitch()) * (float) t;
        return new Location(a.getWorld(), x, y, z, yaw, pitch);
    }

    private static float lerpAngle(float a, float b) {
        return ((b - a) % 360 + 540) % 360 - 180;
    }

    public Cutscene setReturnLocation(Location location) {
        mReturnLocationOverride = location;
        return this;
    }

    void startCutscene() {
        if (mKeyframes.isEmpty()) return;

        Location spawnLocation = mKeyframes.get(0).getLocation();
        mCamera = (ItemDisplay) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ITEM_DISPLAY);
        mCamera.setInvisible(true);
        mCamera.setGravity(false);
        mCamera.setInvulnerable(true);

        // Teleport immediately with TeleportDuration=0 to register the starting position,
        // so the first interpolated teleport in tick() has a valid "from" point.
        mCamera.setTeleportDuration(0);
        mCamera.teleport(mKeyframes.get(0).getLocation());

        for (Player player : mPlayers) {
            mStartingLocations.put(player, player.getLocation());
            mStartingGameModes.put(player, player.getGameMode());
            if (player.getGameMode() != GameMode.SPECTATOR) {
                player.setGameMode(GameMode.SPECTATOR);
            }
            player.setSpectatorTarget(mCamera);
        }
    }

    void endCutscene() {
        if (mCamera != null) {
            mCamera.remove();
            mCamera = null;
        }

        for (Player player : mPlayers) {
            player.setGameMode(mStartingGameModes.getOrDefault(player, GameMode.SURVIVAL));
            Location returnLocation = mReturnLocationOverride != null
                    ? mReturnLocationOverride
                    : mStartingLocations.get(player);
            if (returnLocation != null) {
                player.teleport(returnLocation);
            }
        }
    }

    // Returns true when the cutscene is finished.
    public boolean tick() {
        if (isFinished()) {
            endCutscene();
            return true;
        }

        if (mCamera != null && mNextKeyframeIndex < mKeyframes.size()) {
            int nextTick = mKeyframes.get(mNextKeyframeIndex).getTick();

            // One tick before arriving: send the TeleportDuration metadata update so it
            // reaches clients before the teleport packet fires on the next tick.
            if (mCurrentTick == nextTick - 1 && mNextKeyframeIndex + 1 < mKeyframes.size()) {
                int duration = mKeyframes.get(mNextKeyframeIndex + 1).getTick() - nextTick;
                mCamera.setTeleportDuration(duration);
            }

            // On arrival: fire the teleport toward the following keyframe.
            if (mCurrentTick >= nextTick) {
                mNextKeyframeIndex++;
                if (mNextKeyframeIndex < mKeyframes.size()) {
                    mCamera.teleport(mKeyframes.get(mNextKeyframeIndex).getLocation());
                }
            }
        }

        mCurrentTick++;
        return false;
    }

    public boolean isFinished() {
        if (mKeyframes.isEmpty()) return true;
        return mCurrentTick >= mKeyframes.get(mKeyframes.size() - 1).getTick();
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public List<Keyframe> getKeyframes() {
        return mKeyframes;
    }

    public int getCurrentTick() {
        return mCurrentTick;
    }
}
