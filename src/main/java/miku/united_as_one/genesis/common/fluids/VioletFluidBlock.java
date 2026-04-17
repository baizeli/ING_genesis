package miku.united_as_one.genesis.common.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class VioletFluidBlock extends LiquidBlock {
    public VioletFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            // 每秒 (20 tick) 触发一次
            if (livingEntity.tickCount % 20 == 0) {
                float maxHealth = livingEntity.getMaxHealth();
                // 扣除 20% 最大生命值，使用 magic 类型无视护甲
                livingEntity.hurt(level.damageSources().magic(), maxHealth * 0.20F);
            }
        }
    }
}