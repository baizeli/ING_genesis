package miku.united_as_one.genesis.common.effect.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GlazedFlowerRainEffect extends MobEffect {
    public GlazedFlowerRainEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".glazed_flower_rain";
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            if (entity.tickCount % 20 == 0) {
                Level level = player.level();
                BlockPos playerPos = player.blockPosition();

                int radius = 15;
                List<Entity> entities = level.getEntities(null, new AABB(
                    playerPos.getX() - radius, playerPos.getY() - 5, playerPos.getZ() - radius,
                    playerPos.getX() + radius, playerPos.getY() + 10, playerPos.getZ() + radius
                ));

                for (Entity targetEntity : entities) {
                    if (targetEntity instanceof LivingEntity livingEntity && targetEntity.isAlive()) {
                        if (targetEntity.distanceToSqr(player) <= radius * radius) {
                            // 所有玩家/友方[1s回复8点生命值]
                            if (livingEntity instanceof Player || livingEntity.getType().getCategory().isFriendly()) {
                                livingEntity.heal(8);
                            }
                            // 亡灵生物[1s12点伤害]
                            else if (livingEntity.getMobType() == MobType.UNDEAD) {
                                livingEntity.hurt(level.damageSources().magic(), 12);
                            }
                            // 已处理的亡灵/怪物
                            else if (livingEntity.getMobType() != MobType.UNDEAD && livingEntity.getType().getCategory() == MobCategory.MONSTER) {
                                // 缓慢
                                livingEntity.addEffect(
                                    new MobEffectInstance(
                                        MobEffects.MOVEMENT_SLOWDOWN,
                                        80,
                                        3
                                    )
                                );

                                // 虚弱
                                livingEntity.addEffect(
                                    new MobEffectInstance(
                                        MobEffects.WEAKNESS,
                                        80,
                                        3
                                    )
                                );

                                // 挖掘疲劳
                                livingEntity.addEffect(
                                    new MobEffectInstance(
                                        MobEffects.DIG_SLOWDOWN,
                                        80,
                                        3
                                    )
                                );
                            }
                        }
                    }
                }

                // 施法者自身[1s回复8点生命值]
                player.heal(8);
            }
        }

        super.applyEffectTick(entity, amplifier);
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}