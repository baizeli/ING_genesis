package miku.united_as_one.genesis.common.event.item;

import miku.united_as_one.genesis.common.items.tool.axe.DivineMetalAxe;
import miku.united_as_one.genesis.common.items.weapon.sword.DivineMetalSword;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DivineMetalEvent {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof WitherSkeleton witherSkeleton) {
            if (event.getSource().getEntity() instanceof Player player) {
                ItemStack weapon = player.getItemBySlot(EquipmentSlot.MAINHAND);

                if (weapon.isEmpty()) weapon = player.getItemBySlot(EquipmentSlot.OFFHAND);
                
                if (!weapon.isEmpty() && weapon.getItem() instanceof DivineMetalSword || weapon.getItem() instanceof DivineMetalAxe) {
                    boolean hasSkull = false;

                    for (ItemEntity drop : event.getDrops()) {
                        if (drop.getItem().getItem() == Items.WITHER_SKELETON_SKULL) {
                            hasSkull = true;
                            drop.getItem().setCount(1);
                            break;
                        }
                    }

                    if (!hasSkull) {
                        event.getDrops().add(new ItemEntity(
                            witherSkeleton.level(),
                            witherSkeleton.getX(),
                            witherSkeleton.getY(),
                            witherSkeleton.getZ(),
                            new ItemStack(Items.WITHER_SKELETON_SKULL, 1)
                        ));
                    }
                }
            }
        }
    }
}