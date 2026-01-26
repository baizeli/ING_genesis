package miku.united_as_one.genesis.items;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ChaosCore extends Item {
    public ChaosCore() {
        super(new Properties()
            .stacksTo(1)
            .rarity(Rarity.EPIC)
        );
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext Context) {
        if(Context.getPlayer() != null) {
            ResourceKey<Level> currentDimension = Context.getPlayer().level().dimension();

            if(currentDimension == 
                ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "echo_of_decay")) || 
                currentDimension == Level.OVERWORLD) {

                if (!(Context.getPlayer().level() instanceof ServerLevel serverLevel)) return InteractionResult.FAIL;

                if(serverLevel.structureManager().getStructureWithPieceAt(
                    Context.getPlayer().blockPosition(), BuiltinStructures.ANCIENT_CITY).isValid()) {
                    BlockPos clickedPos = Context.getClickedPos().relative(Context.getClickedFace());

                    if(BlockRegistry.CHAOS_PORTAL.get().spawnPortal(Context.getLevel(), clickedPos)) {
                        Context.getLevel().playSound(
                            Context.getPlayer(), 
                            clickedPos, 
                            SoundEvents.PORTAL_TRIGGER, 
                            SoundSource.BLOCKS, 
                            6f, 
                            0.8f
                        );
                        /*if(!Context.getPlayer().isCreative()) Context.getPlayer().setItemInHand(Context.getHand(), ItemStack.EMPTY);*/
                        return InteractionResult.SUCCESS;
                    } else return InteractionResult.FAIL;
                } else {
                    return InteractionResult.FAIL;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack Stack, @NotNull Level Level, @NotNull Entity Entity, int SlotId, boolean IsSelected) {}

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player Player, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = Player.getItemInHand(pUsedHand);

        if(Player.level().dimension() == ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "echo_of_decay"))) {
            MinecraftServer server = pLevel.getServer();

            if(server != null) {
                ServerLevel overworld = server.getLevel(Level.OVERWORLD);
                if(overworld != null) {
                    Player.changeDimension(overworld, new ITeleporter() {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                            return repositionEntity.apply(false);
                        }
                    });
                    return InteractionResultHolder.consume(itemStack);
                }
            }
        }
        
        return InteractionResultHolder.pass(itemStack);
    }
}