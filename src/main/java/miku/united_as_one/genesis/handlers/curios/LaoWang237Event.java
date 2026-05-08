package miku.united_as_one.genesis.handlers.curios;

import miku.united_as_one.genesis.registries.ItemRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

@Mod.EventBusSubscriber
public class LaoWang237Event {
    private static final Random RANDOM = new Random();

    // 猪排掉落老王237的概率
    private static final float DROP_CHANCE = 0.02f;

    // 佩戴老王237这个饰品击杀任意生物后掉落的猪排数量
    private static final int PORKCHOP_COUNT = 1;

    @SubscribeEvent
    public static void onLivingDropLoot(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getType() == EntityType.PIG && RANDOM.nextFloat() < DROP_CHANCE) {
            event.getDrops().add(new ItemEntity(
                entity.level(),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                new ItemStack(ItemRegistry.LAO_WANG_237.get())
            ));
        }

        if (event.getSource().getEntity() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.findFirstCurio(ItemRegistry.LAO_WANG_237.get()).ifPresent(result -> {
                    event.getDrops().add(new ItemEntity(
                        entity.level(),
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        new ItemStack(Items.PORKCHOP, PORKCHOP_COUNT)
                    ));
                });
            });
        }
    }
}