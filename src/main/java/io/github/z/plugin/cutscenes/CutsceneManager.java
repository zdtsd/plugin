package io.github.z.plugin.cutscenes;

import java.util.ArrayList;
import java.util.List;

public class CutsceneManager {

    private static final List<Cutscene> mActiveCutscenes = new ArrayList<>();

    public static void startCutscene(Cutscene cutscene) {
        cutscene.startCutscene();
        mActiveCutscenes.add(cutscene);
    }

    public static void tick() {
        mActiveCutscenes.removeIf(Cutscene::tick);
    }
}
