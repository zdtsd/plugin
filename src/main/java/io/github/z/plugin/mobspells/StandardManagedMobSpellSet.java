package io.github.z.plugin.mobspells;

public class StandardManagedMobSpellSet extends ManagedMobSpellSet {

    int spawnStartCastingDelay = 2 * 20;
    @Override
    public void tick() {
        if(spawnStartCastingDelay > 0){
            spawnStartCastingDelay -= 5;
            return;
        }
        super.tick();
        for (CooldownMobSpell spell : mCooldownSpells) {
            if (!spell.isChanneling() && spell.getCurrentCooldown() <= 0) {
                spell.startChannel();
            }
        }
    }
}
