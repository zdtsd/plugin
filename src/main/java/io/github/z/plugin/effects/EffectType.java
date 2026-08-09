package io.github.z.plugin.effects;

import io.github.z.plugin.effects.effects.*;

import java.util.function.BiFunction;

public enum EffectType {
    WEAKNESS("weakness", WeaknessEffect::new),
    MOVE_SPEED("move_speed", BaseMoveSpeedModifyEffect::new),
    STUN("stun", StunEffect::new),
    SLOW_FALLING("slow_falling", FallSpeedEffect::new),
    AGILITY("agility", AgilityEffect::new),
    FEATHER_FALLING("feather_falling", FeatherFallingEffect::new),
    TRUE_FLIGHT("true_flight", TrueFlightEffect::new);

    private final String name;
    private final BiFunction<Integer, Integer, Effect> factory;

    EffectType(String name, BiFunction<Integer, Integer, Effect> factory) {
        this.name = name;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public Effect create(int duration, int strength) {
        return factory.apply(duration, strength);
    }
}
