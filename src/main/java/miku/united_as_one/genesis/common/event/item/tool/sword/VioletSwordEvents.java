package miku.united_as_one.genesis.common.event.item.tool.sword;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.items.weapon.sword.VioletSword;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class VioletSwordEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // 1. 剑右键格挡减伤 (50%)
        if (event.getEntity() instanceof Player player && player.isUsingItem()) {
            if (player.getUseItem().getItem() instanceof VioletSword) {
                event.setAmount(event.getAmount() * 0.5F);
            }
        }

        // 2. 3x3 扩散攻击判定
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            if (weapon.getItem() instanceof VioletSword) {
                LivingEntity victim = event.getEntity();
                Level level = victim.level();

                // 判定受害者周围 3.0x2.0x3.0 的区域
                AABB area = victim.getBoundingBox().inflate(1.5D, 1.0D, 1.5D);
                level.getEntitiesOfClass(LivingEntity.class, area).forEach(target -> {
                    if (target != player && target != victim) {
                        // 扩散伤害设置为原伤害的 80%，防止递归死循环
                        target.hurt(level.damageSources().playerAttack(player), event.getAmount() * 0.8F);
                    }
                });
            }
        }
    }
}