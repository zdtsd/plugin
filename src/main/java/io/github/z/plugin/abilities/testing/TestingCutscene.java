package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.cutscenes.Cutscene;
import io.github.z.plugin.cutscenes.CutsceneManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class TestingCutscene extends Ability {

    public static AbilityData<TestingCutscene> DATA = new AbilityData<>(TestingCutscene.class, "testing_cutscene", TestingCutscene::new)
            .scoreboardID("testCutscene")
            .displayMaterial(Material.LIGHT);

    public TestingCutscene(Player player) {
        super(player);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    @Override
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Location base = mPlayer.getLocation();

        Cutscene cutscene = new Cutscene(List.of(), List.of())
                .addPlayer(mPlayer)
                .addKeyframe(base.clone().add(0, 2, 0), 0)
                .addKeyframe(base.clone().add(0, 5, 0).setDirection(new Vector(0, 1, 0)), 60)
                .addKeyframe(base.clone().add(0, 2, 0), 100);

        CutsceneManager.startCutscene(cutscene);
    }
}
