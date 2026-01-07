//很美味的蛋糕🍰，太美味了🥰🥰🥰，太好吃了😋😋😋，太棒了👍🏻👍🏻👍🏻，我非常喜欢吃😋😋😋
package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.Util.RainbowEffectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class GoodCake extends Item {

    public GoodCake() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .durability(0));
    }


    
    private void killAllMobsInWorld(ServerLevel level) {
        List<LivingEntity> entitiesToKill = new ArrayList<>();
        AABB worldBounds = new AABB(
            -30000000, -256, -30000000, 
            30000000, 256, 30000000
        );
        
        // 获取世界中所有的生物实体
        List<LivingEntity> allEntities = level.getEntitiesOfClass(
            LivingEntity.class, 
            worldBounds
        );
        
        // 过滤出非玩家实体
        for (LivingEntity entity : allEntities) {
            if (!(entity instanceof Player)) {
                entitiesToKill.add(entity);
            }
        }
        
        // 然后统一杀死所有收集到的实体
        for (LivingEntity entity : entitiesToKill) {
            entity.kill();
            entity.discard();
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if(level instanceof ServerLevel slv){
          killAllMobsInWorld(slv);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack stack) {
        Component originalName = super.getName(stack);
        if (stack.hasCustomHoverName()) {
            return originalName;
        }
        stack.getOrCreateTag().putInt("HideFlags", 2);
        return RainbowEffectHelper.createCustomGradientText(originalName.getString(), RainbowEffectHelper.BLUE, 3, 1, 0.03F, 1F);
    }
}