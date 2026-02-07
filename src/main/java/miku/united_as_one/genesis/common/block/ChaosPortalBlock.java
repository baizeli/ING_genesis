package miku.united_as_one.genesis.common.block;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.BlockRegistry;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

@SuppressWarnings({"deprecation", "NullableProblems"})
public class ChaosPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0, 0, 6, 16, 16, 10);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6, 0, 0, 10, 16, 16);

    public ChaosPortalBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(pState.getValue(AXIS) == Direction.Axis.X) return X_AXIS_AABB;
        return Z_AXIS_AABB;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        Direction.Axis facingAxis = pDirection.getAxis();
        Direction.Axis axis = pState.getValue(AXIS);
        boolean flag = axis != facingAxis && facingAxis.isHorizontal();
        return !flag && !pNeighborState.is(this) && !(new ChaosPortalShape(pLevel, pPos, axis, this)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if(pRandom.nextFloat() < 0.0007) {
            pLevel.playLocalSound(pPos.getX() + 0.5d, pPos.getY() + 0.5d, pPos.getZ() + 0.5d, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.2f, pRandom.nextFloat() * 0.2f + 0.9f, false);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if(!pEntity.isPassenger() && !pEntity.isVehicle() && pEntity.canChangeDimensions()) {
            if(pEntity.isOnPortalCooldown()) {
                pEntity.setPortalCooldown();
            } else {
                if(!pLevel.isClientSide) {
                    pEntity.handleInsidePortal(pPos);
                }

                MinecraftServer server = pLevel.getServer();
                if(server == null) return;
                
                ResourceKey<Level> echoDecayLevel = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "echo_of_decay"));
                if(pLevel.dimension() != echoDecayLevel && pLevel.dimension() != Level.OVERWORLD) return;

                ResourceKey<Level> destination = pLevel.dimension() == echoDecayLevel ? Level.OVERWORLD : echoDecayLevel;
                ServerLevel destWorld = server.getLevel(destination);
                if(destWorld != null && !pEntity.isPassenger()) {
                    pLevel.getProfiler().push("portal");
                    pEntity.setPortalCooldown();
                    pEntity.changeDimension(destWorld, new ITeleporter() {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                            return repositionEntity.apply(false);
                        }
                    });
                    pLevel.getProfiler().pop();
                }
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return switch (direction) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ItemStack.EMPTY;
    }

    public boolean spawnPortal(LevelAccessor worldIn, BlockPos pos) {
        ChaosPortalShape portal = this.isPortal(worldIn, pos);
        if(portal != null && !trySpawningPortal(worldIn, pos, portal)) {
            portal.createPortalBlocks();
            return true;
        } else return false;
    }

    public static boolean trySpawningPortal(LevelAccessor world, BlockPos pos, ChaosPortalShape portal) {
        return MinecraftForge.EVENT_BUS.post(new PortalSpawnEvent(world, pos, world.getBlockState(pos), portal));
    }

    public ChaosPortalShape isPortal(LevelAccessor level, BlockPos pos) {
        ChaosPortalShape portalX = new ChaosPortalShape(level, pos, Direction.Axis.X, this);
        if(portalX.isValid() && portalX.numPortalBlocks == 0) {
            return portalX;
        } else {
            ChaosPortalShape portalZ = new ChaosPortalShape(level, pos, Direction.Axis.Z, this);
            return portalZ.isValid() && portalZ.numPortalBlocks == 0 ? portalZ : null;
        }
    }

    public static class PortalSpawnEvent extends BlockEvent {
        private final ChaosPortalShape size;

        public PortalSpawnEvent(LevelAccessor level, BlockPos pos, BlockState state, ChaosPortalShape size) {
            super(level, pos, state);
            this.size = size;
        }

        public ChaosPortalShape getSize() {
            return size;
        }
    }

    public static class ChaosPortalShape {
        public static final int MIN_WIDTH = 2;
        public static final int MIN_HEIGHT = 2;
        public static final int MAX_WIDTH = 21;
        public static final int MAX_HEIGHT = 21;

        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final ChaosPortalBlock portalBlock;
        private BlockPos bottomLeft;
        private int numPortalBlocks;
        private int height;
        private final int width;

        public ChaosPortalShape(LevelAccessor level, BlockPos bottomLeft, Direction.Axis axis, ChaosPortalBlock portalBlock) {
            this.level = level;
            this.axis = axis;
            this.portalBlock = portalBlock;
            this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
            this.bottomLeft = this.calculateBottomLeft(bottomLeft);

            if(this.bottomLeft == null) {
                this.bottomLeft = bottomLeft;
                this.width = 1;
                this.height = 1;
            } else {
                this.width = this.calculateWidth();
                if(this.width > 0) {
                    this.height = this.calculateHeight();
                }
            }
        }

        private BlockPos calculateBottomLeft(BlockPos pos) {
            int height = Math.max(this.level.getMinBuildHeight(), pos.getY() - 21);
            while(pos.getY() > height && isEmpty(this.level.getBlockState(pos.below()))) pos = pos.below();

            Direction direction = this.rightDir.getOpposite();
            int j = this.getFrameWidth(pos, direction) - 1;
            return j < 0 ? null : pos.relative(direction, j);
        }

        private int calculateHeight() {
            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
            int i = this.getFrameHeight(blockPos);
            return i >= MIN_HEIGHT && i <= MAX_HEIGHT && this.hasTopFrame(blockPos, i) ? i : 0;
        }

        private int calculateWidth() {
            int i = this.getFrameWidth(this.bottomLeft, this.rightDir);
            return i >= MIN_WIDTH && i <= MAX_WIDTH ? i : 0;
        }

        private int getFrameHeight(BlockPos.MutableBlockPos pos) {
            for(int i = 0; i < MAX_HEIGHT; i++) {
                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
                if(!this.level.getBlockState(pos).is(Blocks.REINFORCED_DEEPSLATE)) return i;

                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
                if(!this.level.getBlockState(pos).is(Blocks.REINFORCED_DEEPSLATE)) return i;

                for(int j = 0; j < this.width; j++) {
                    pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                    BlockState blockState = this.level.getBlockState(pos);
                    if(!isEmpty(blockState)) return i;

                    if(blockState.is(portalBlock)) this.numPortalBlocks++;
                }
            }

            return MAX_HEIGHT;
        }

        private int getFrameWidth(BlockPos pos, Direction direction) {
            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

            for(int i = 0; i <= MAX_HEIGHT; i++) {
                blockPos.set(pos).move(direction, i);
                BlockState blockState = this.level.getBlockState(blockPos);
                if(!isEmpty(blockState)) {
                    if(blockState.is(Blocks.REINFORCED_DEEPSLATE)) return i;
                    break;
                }

                BlockState blockStateDown = this.level.getBlockState(blockPos.move(Direction.DOWN));
                if(!blockStateDown.is(Blocks.REINFORCED_DEEPSLATE)) break;
            }

            return 0;
        }

        private boolean hasTopFrame(BlockPos.MutableBlockPos pos, int n) {
            for(int i = 0; i < this.width; i++) {
                BlockPos.MutableBlockPos blockPos = pos.set(this.bottomLeft).move(Direction.UP, n).move(this.rightDir, i);
                if(!this.level.getBlockState(blockPos).is(Blocks.REINFORCED_DEEPSLATE)) {
                    return false;
                }
            }

            return true;
        }

        public void createPortalBlocks() {
            BlockState blockstate = portalBlock.defaultBlockState().setValue(ChaosPortalBlock.AXIS, this.axis);
            BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach((blockPos) -> this.level.setBlock(blockPos, blockstate, 18));
        }

        public boolean isComplete() {
            return this.isValid() && this.numPortalBlocks == this.width * this.height;
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT;
        }

        private static boolean isEmpty(BlockState state) {
            return state.isAir() || state.is(BlockRegistry.CHAOS_PORTAL.get());
        }
    }
}