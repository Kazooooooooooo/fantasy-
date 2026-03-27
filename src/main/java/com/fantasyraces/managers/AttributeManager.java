package com.fantasyraces.managers;

import com.fantasyraces.models.Race;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AttributeManager {

    private static final UUID HEALTH_MOD_UUID    = UUID.fromString("a0000001-0000-0000-0000-000000000001");
    private static final UUID SPEED_MOD_UUID     = UUID.fromString("a0000001-0000-0000-0000-000000000002");
    private static final UUID DAMAGE_MOD_UUID    = UUID.fromString("a0000001-0000-0000-0000-000000000003");
    private static final UUID KNOCKBACK_MOD_UUID = UUID.fromString("a0000001-0000-0000-0000-000000000004");
    private static final UUID SCALE_MOD_UUID     = UUID.fromString("a0000001-0000-0000-0000-000000000005");

    private static final String HEALTH_MOD_NAME    = "fantasyraces.health";
    private static final String SPEED_MOD_NAME     = "fantasyraces.speed";
    private static final String DAMAGE_MOD_NAME    = "fantasyraces.damage";
    private static final String KNOCKBACK_MOD_NAME = "fantasyraces.knockback";
    private static final String SCALE_MOD_NAME     = "fantasyraces.scale";

    public static void applyRace(Player player, Race race) {
        removeRaceModifiers(player);
        if (race == null) return;

        // Max Health
        AttributeInstance health = player.getAttribute(Attribute.MAX_HEALTH);
        if (health != null && race.getMaxHealthBonus() != 0) {
            health.addModifier(new AttributeModifier(
                    HEALTH_MOD_UUID, HEALTH_MOD_NAME,
                    race.getMaxHealthBonus() * 2,
                    AttributeModifier.Operation.ADD_NUMBER));
            if (player.getHealth() > health.getValue()) {
                player.setHealth(health.getValue());
            }
        }

        // Speed
        AttributeInstance speed = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speed != null && race.getSpeedModifier() != 0) {
            speed.addModifier(new AttributeModifier(
                    SPEED_MOD_UUID, SPEED_MOD_NAME,
                    race.getSpeedModifier(),
                    AttributeModifier.Operation.ADD_SCALAR));
        }

        // Attack Damage
        AttributeInstance damage = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (damage != null && race.getDamageModifier() != 0) {
            damage.addModifier(new AttributeModifier(
                    DAMAGE_MOD_UUID, DAMAGE_MOD_NAME,
                    race.getDamageModifier(),
                    AttributeModifier.Operation.ADD_SCALAR));
        }

        // Knockback Resistance
        AttributeInstance knockback = player.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        if (knockback != null && race.getKnockbackResistance() != 0) {
            knockback.addModifier(new AttributeModifier(
                    KNOCKBACK_MOD_UUID, KNOCKBACK_MOD_NAME,
                    race.getKnockbackResistance(),
                    AttributeModifier.Operation.ADD_NUMBER));
        }

        // Scale (1.21+ only) — 1.0 = normal size, 0.5 = half, 2.0 = double
        AttributeInstance scale = player.getAttribute(Attribute.SCALE);
        if (scale != null && race.getScale() != 1.0) {
            // Use MULTIPLY_SCALAR_1 so 2.0 = double size, 0.5 = half size
            // Formula: base * (1 + modifier), so modifier = scale - 1
            scale.addModifier(new AttributeModifier(
                    SCALE_MOD_UUID, SCALE_MOD_NAME,
                    race.getScale() - 1.0,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }
    }

    public static void removeRaceModifiers(Player player) {
        removeModifier(player.getAttribute(Attribute.MAX_HEALTH), HEALTH_MOD_UUID);
        removeModifier(player.getAttribute(Attribute.MOVEMENT_SPEED), SPEED_MOD_UUID);
        removeModifier(player.getAttribute(Attribute.ATTACK_DAMAGE), DAMAGE_MOD_UUID);
        removeModifier(player.getAttribute(Attribute.KNOCKBACK_RESISTANCE), KNOCKBACK_MOD_UUID);
        removeModifier(player.getAttribute(Attribute.SCALE), SCALE_MOD_UUID);
    }

    private static void removeModifier(AttributeInstance attr, UUID modUUID) {
        if (attr == null) return;
        attr.getModifiers().stream()
                .filter(m -> m.getUniqueId().equals(modUUID))
                .findFirst()
                .ifPresent(attr::removeModifier);
    }
}
