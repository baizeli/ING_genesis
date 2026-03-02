package miku.united_as_one.genesis.common.event.item.tool;

import miku.united_as_one.genesis.common.items.tool.VioletGalaxyingotTool;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "united_as_one") // 确保这里的 modid 与你的一致
public class VioletSwordEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            // 判断玩家是否正在右键使用紫极剑
            if (player.isUsingItem() && player.getUseItem().getItem() instanceof VioletGalaxyingotTool.Sword) {

                // 1. 减免 50% 伤害
                event.setAmount(event.getAmount() * 0.5F);

                // 2. 播放格挡音效
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + player.level().random.nextFloat() * 0.4F);

                // 3. 产生碰撞颗粒感 (hurtMarked)
                player.hurtMarked = true;
            }
        }
    }
}