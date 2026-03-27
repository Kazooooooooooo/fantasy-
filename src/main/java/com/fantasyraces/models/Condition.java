package com.fantasyraces.models;

import java.util.List;

public class Condition {

    public enum Trigger {
        BELOW_Y, ABOVE_Y,
        IS_DAYTIME, IS_NIGHTTIME,
        IN_WATER, ON_FIRE,
        IN_BIOME, WORLD_NAME,
        IN_NETHER,       // true when in the nether dimension
        IN_END,          // true when in the end dimension
        IN_OVERWORLD,    // true when in the overworld
        IN_COLD_BIOME    // true when in a cold/freezing biome
    }

    // Special effect actions that go beyond potion effects
    public enum SpecialEffect {
        NONE,
        SET_ON_FIRE,   // ignites the player
        FREEZE,        // applies freeze ticks (frostbite visual + damage)
        EXTINGUISH     // puts out fire
    }

    private final Trigger trigger;
    private final double yValue;
    private final List<String> biomes;
    private final String worldName;
    private final PassiveAbility effect;         // potion effect (may be null if specialEffect is used)
    private final SpecialEffect specialEffect;   // physical effect

    public Condition(Trigger trigger, double yValue, List<String> biomes,
                     String worldName, PassiveAbility effect, SpecialEffect specialEffect) {
        this.trigger = trigger;
        this.yValue = yValue;
        this.biomes = biomes;
        this.worldName = worldName;
        this.effect = effect;
        this.specialEffect = specialEffect != null ? specialEffect : SpecialEffect.NONE;
    }

    public Trigger getTrigger() { return trigger; }
    public double getYValue() { return yValue; }
    public List<String> getBiomes() { return biomes; }
    public String getWorldName() { return worldName; }
    public PassiveAbility getEffect() { return effect; }
    public SpecialEffect getSpecialEffect() { return specialEffect; }
    public boolean hasSpecialEffect() { return specialEffect != SpecialEffect.NONE; }
}
