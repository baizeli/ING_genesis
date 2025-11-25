package com.baizeli.eternisstarrysky.event.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.client.renderer.AfterImageManager;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.client.player.Input;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID)
public class UnparalleledEvent {
    private static final Map<Player, Boolean> hasUsedSpecialJump = new HashMap<>();
    private static final Map<Player, Boolean> wasJumpPressed = new HashMap<>();
    
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5D0");
    private static final UUID SPELL_POWER_MODIFIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5D1");

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModEffect.UNPARALLELED.get())) {
                // 速度
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_UUID);
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(
                    new AttributeModifier(
                        SPEED_MODIFIER_UUID, 
                        "Unparalleled speed boost", 
                        1.0,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                    )
                );

                // 伤害
                player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_MODIFIER_UUID);
                player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(
                    new AttributeModifier(
                        DAMAGE_MODIFIER_UUID, 
                        "Unparalleled damage boost", 
                        1.0,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                    )
                );

                // 法术强度
                player.getAttribute(AttributeRegistry.SPELL_POWER.get()).removeModifier(SPELL_POWER_MODIFIER_UUID);
                player.getAttribute(AttributeRegistry.SPELL_POWER.get()).addTransientModifier(
                    new AttributeModifier(
                        SPELL_POWER_MODIFIER_UUID, 
                        "Unparalleled spell power boost", 
                        0.5,
                        AttributeModifier.Operation.ADDITION
                    )
                );

                // 在地上false
                if (player.onGround()) {
                    hasUsedSpecialJump.put(player, false);
                }
            } else {
                // 修饰符/二段跳/残影清理
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_UUID);
                player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_MODIFIER_UUID);
                player.getAttribute(AttributeRegistry.SPELL_POWER.get()).removeModifier(SPELL_POWER_MODIFIER_UUID);
                
                hasUsedSpecialJump.remove(player);
                wasJumpPressed.remove(player);

                AfterImageManager.clear();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            // 摔落伤害肘开
            if (player.hasEffect(ModEffect.UNPARALLELED.get()) && event.getSource().is(DamageTypeTags.IS_FALL)) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        Input input = event.getInput();

        if (player.hasEffect(ModEffect.UNPARALLELED.get())) {
            Boolean lastJumpPressed = wasJumpPressed.get(player);
            if (lastJumpPressed == null) lastJumpPressed = false;

            wasJumpPressed.put(player, input.jumping);
            
            // 是否按下了跳跃键 之前有没有按下 是否在空中且没有使用过二段跳
            if (input.jumping && !lastJumpPressed && !player.onGround()) {
                Boolean usedSpecialJump = hasUsedSpecialJump.get(player);
                if (usedSpecialJump == null || !usedSpecialJump) {
                    Vec3 motion = player.getDeltaMovement();
                    float yaw = player.getYRot();
                    double forwardX = -Math.sin(Math.toRadians(yaw));
                    double forwardZ = Math.cos(Math.toRadians(yaw));

                    // 二段跳
                    player.setDeltaMovement(
                        motion.x + forwardX * 2,
                        motion.y + 0.7,
                        motion.z + forwardZ * 2
                    );

                    // 跳跃的标记
                    hasUsedSpecialJump.put(player, true);
                }
            }
        }
    }
}