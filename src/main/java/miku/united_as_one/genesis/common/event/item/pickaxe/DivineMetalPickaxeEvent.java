package miku.united_as_one.genesis.common.event.item.pickaxe;

import miku.united_as_one.genesis.common.items.pickaxe.DivineMetalPickaxe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class DivineMetalPickaxeEvent {
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getMainHandItem();
        
        if (tool.getItem() instanceof DivineMetalPickaxe && !player.isCreative()) {
            BlockState state = event.getState();
            Level level = player.level();
            BlockPos pos = event.getPos();

            if (state.canHarvestBlock(level, pos, player)) {
                for (ItemStack drop : Block.getDrops(state, (ServerLevel) level, pos, level.getBlockEntity(pos), player, tool)) {
                    Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(
                        RecipeType.SMELTING, new SimpleContainer(drop), level
                    );
                    
                    if (recipe.isPresent()) {
                        ItemStack result = recipe.get().getResultItem(level.registryAccess());
                        if (!result.isEmpty()) {
                            level.addFreshEntity(
                                new ItemEntity(
                                    level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                    result.copyWithCount(drop.getCount())
                                )
                            );
                            level.addFreshEntity(
                                new ExperienceOrb(
                                    level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                    (int) (recipe.get().getExperience() * drop.getCount())
                                )
                            );
                            continue;
                        }
                    }

                    level.addFreshEntity(
                        new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop)
                    );

                    level.addFreshEntity(
                        new ExperienceOrb(
                            level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop.getCount()
                        )
                    );
                }
                level.destroyBlock(pos, false);
            }
        }
    }
}