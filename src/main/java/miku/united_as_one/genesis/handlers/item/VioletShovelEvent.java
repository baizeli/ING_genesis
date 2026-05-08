package miku.united_as_one.genesis.handlers.item;

import miku.united_as_one.genesis.contents.items.tool.axe.*;
import miku.united_as_one.genesis.contents.items.tool.hoe.*;
import miku.united_as_one.genesis.contents.items.tool.pickaxe.*;
import miku.united_as_one.genesis.contents.items.tool.shovel.*;
import miku.united_as_one.genesis.contents.items.weapon.sword.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VioletShovelEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();

        if (level.isClientSide && player.getAbilities().instabuild) return;

        if (isVioletTool(stack)) {
            for (ItemStack drop : Block.getDrops(event.getState(), level, pos, level.getBlockEntity(pos), player, stack))
                if (!drop.isEmpty()) player.getInventory().add(drop);
            level.removeBlock(pos, false);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (isVioletTool(player.getMainHandItem())) {
                for (var entity : event.getDrops()) {
                    ItemStack drop = entity.getItem();
                    if (!drop.isEmpty()) player.getInventory().add(drop);
                }
                event.getDrops().clear();
            }
        }
    }

    private static boolean isVioletTool(ItemStack stack) {
        return stack.getItem() instanceof VioletPickaxe ||
               stack.getItem() instanceof VioletAxe ||
               stack.getItem() instanceof VioletShovel ||
               stack.getItem() instanceof VioletHoe || 
               stack.getItem() instanceof VioletSword;
    }
}