package miku.united_as_one.genesis.common.event.entity;
import miku.united_as_one.genesis.common.entity.warlock.WardenMageEntity;
import miku.united_as_one.genesis.init.registry.EntityRegistry; // 你的实体注册类
import miku.united_as_one.genesis.init.registry.ItemRegistry;    // 你的物品注册类
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "iron_spells_genesis")
public class WardenWarlockEvents {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != net.minecraft.world.InteractionHand.MAIN_HAND) {
            return;
        }
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack itemStack = event.getItemStack();
        if (itemStack.is(ItemRegistry.ELDRITCH_UPGRADE_ORB.get())) {
            if (level.isClientSide) {
                return;
            }
            BlockPos playerPos = player.blockPosition();
            int radius = 10;
            BlockPos targetSensorPos = null;
            Iterable<BlockPos> area = BlockPos.betweenClosed(
                    playerPos.offset(-radius, -3, -radius),
                    playerPos.offset(radius, 3, radius)
            );
            for (BlockPos pos : area) {
                if (level.getBlockState(pos).is(Blocks.SCULK_SENSOR)) {
                    targetSensorPos = pos.immutable();
                    break;
                }
            }
            if (targetSensorPos != null) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                player.getCooldowns().addCooldown(ItemRegistry.ELDRITCH_UPGRADE_ORB.get(), 100);

                double spawnX = targetSensorPos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 4;
                double spawnZ = targetSensorPos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 4;
                double spawnY = targetSensorPos.getY() + 1.0;

                WardenMageEntity boss = new WardenMageEntity(EntityRegistry.WARDEN_MANCER.get(), level);
                boss.moveTo(spawnX, spawnY, spawnZ, level.random.nextFloat() * 360F, 0);
                level.addFreshEntity(boss);
                level.playSound(null, boss.getX(), boss.getY(), boss.getZ(),
                        SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE, 1.0F, 1.0F);

                event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                event.setCanceled(true);
            } else {
                player.displayClientMessage(Component.translatable("chat.iron_spells_genesis.no_sensor_found"), true);
            }
        }
    }
}