package miku.united_as_one.genesis.contents.items;

import miku.united_as_one.genesis.api.mixin.DamageSourceInterface;
import miku.united_as_one.genesis.api.mixin.LivingEventEC;
import miku.united_as_one.genesis.api.render.RainbowEffectHelper;
import miku.united_as_one.genesis.registries.block.BlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoodCake extends Item {
    private static final int ORE_SEARCH_RADIUS = 128;
    private static final int ORE_LOCATE_COOLDOWN_TICKS = 40;
    private static final int ENTITY_REMOVE_AREA_SIZE = 128;
    private static final double ENTITY_REMOVE_HALF_SIZE = ENTITY_REMOVE_AREA_SIZE / 2.0D;
    private static final String TRUE_DAMAGE_TAG = "GoodCakeTrueDamage";
    private static final float DEFAULT_TRUE_DAMAGE = 18.0F;
    private static final float TRUE_DAMAGE_MIN = 1.0F;
    private static final float TRUE_DAMAGE_MAX = 1000000.0F;
    private static final float TRUE_DAMAGE_STEP = 1.0F;

    public GoodCake() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .durability(0));
    }

    public static float getTrueDamage(ItemStack stack) {
        if (stack.hasTag() && stack.getOrCreateTag().contains(TRUE_DAMAGE_TAG)) {
            return Mth.clamp(stack.getOrCreateTag().getFloat(TRUE_DAMAGE_TAG), TRUE_DAMAGE_MIN, TRUE_DAMAGE_MAX);
        }
        return DEFAULT_TRUE_DAMAGE;
    }

    public static float setTrueDamage(ItemStack stack, float damage) {
        float clampedDamage = Mth.clamp(damage, TRUE_DAMAGE_MIN, TRUE_DAMAGE_MAX);
        stack.getOrCreateTag().putFloat(TRUE_DAMAGE_TAG, clampedDamage);
        return clampedDamage;
    }

    public static float adjustTrueDamage(ItemStack stack, int steps) {
        return setTrueDamage(stack, getTrueDamage(stack) + steps * TRUE_DAMAGE_STEP);
    }

    public static void adjustHeldCakeDamage(ServerPlayer player, int steps) {
        if (!player.isCrouching()) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GoodCake)) {
            return;
        }

        float damage = adjustTrueDamage(stack, steps);
        player.displayClientMessage(Component.literal("Good Cake 真伤已调整为 " + formatDamage(damage)).withStyle(ChatFormatting.RED), false);
    }

    public static String formatDamage(float damage) {
        if (damage == (int) damage) {
            return Integer.toString((int) damage);
        }
        return String.format(Locale.ROOT, "%.1f", damage);
    }

    public static boolean dealTrueDamage(ItemStack stack, Player player, LivingEntity target) {
        if (player.level().isClientSide()) {
            return false;
        }

        DamageSource damageSource = player.damageSources().playerAttack(player);
        makeGungnirLike(damageSource);
        target.invulnerableTime = 0;
        boolean damaged = target.hurt(damageSource, getTrueDamage(stack));
        target.invulnerableTime = 0;
        return damaged;
    }

    private static void makeGungnirLike(DamageSource damageSource) {
        ((DamageSourceInterface) damageSource).revelationfix$setBypassAll(true);
    }

    private static void makeGungnirLike(LivingEventEC event, DamageSource damageSource) {
        makeGungnirLike(damageSource);
        event.revelationfix$hackedUnCancelable(true);
        event.revelationfix$hackedOnlyAmountUp(true);
    }

    public void onAttack(ItemStack itemStack, LivingAttackEvent event) {
        makeGungnirLike((LivingEventEC) event, event.getSource());
    }

    public void onHurt(ItemStack itemStack, LivingHurtEvent event) {
        makeGungnirLike((LivingEventEC) event, event.getSource());
    }

    public void onDamage(ItemStack itemStack, LivingDamageEvent event) {
        LivingEventEC ec = (LivingEventEC) event;
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    public void onDeath(ItemStack itemStack, LivingDeathEvent event, EventPriority priority) {
        LivingEventEC ec = (LivingEventEC) event;
        ec.revelationfix$hackedUnCancelable(true);
        event.getEntity().setHealth(0F);
    }


    
    private void removeMobsAroundPlayer(ServerLevel level, Player player) {
        AABB worldBounds = new AABB(
            player.getX() - ENTITY_REMOVE_HALF_SIZE, level.getMinBuildHeight(), player.getZ() - ENTITY_REMOVE_HALF_SIZE,
            player.getX() + ENTITY_REMOVE_HALF_SIZE, level.getMaxBuildHeight(), player.getZ() + ENTITY_REMOVE_HALF_SIZE
        );
        
        // 获取世界中所有的生物实体
        List<LivingEntity> allEntities = level.getEntitiesOfClass(
            LivingEntity.class, 
            worldBounds
        );
        List<LivingEntity> entitiesToKill = new ArrayList<>();
        
        // 过滤出非玩家实体
        for (LivingEntity entity : allEntities) {
            if (!(entity instanceof Player)) {
                entitiesToKill.add(entity);
            }
        }
        
        // 然后统一杀死所有收集到的实体
        for (LivingEntity entity : entitiesToKill) {
            killEntity(entity);
        }
    }

    public static void killEntity(Entity entity) {
        try {
            if (entity == null || entity.level() == null) {
                return;
            }

            Level world = entity.level();
            entity.levelCallback = EntityInLevelCallback.NULL;

            if (world instanceof ServerLevel serverWorld) {
                serverWorld.getChunkSource().broadcast(entity, new ClientboundRemoveEntitiesPacket(entity.getId()));
                serverWorld.getChunkSource().chunkMap.entityMap.remove(entity.getId());
                serverWorld.entityTickList.active.remove(entity.getId());

                serverWorld.entityManager.visibleEntityStorage.byId.remove(entity.getId());
                serverWorld.entityManager.visibleEntityStorage.byUuid.remove(entity.getUUID());
                serverWorld.entityManager.knownUuids.remove(entity.getUUID());

                ((LevelEntityGetterAdapter<Entity>) serverWorld.entityManager.entityGetter).visibleEntities.byId.remove(entity.getId());
                ((LevelEntityGetterAdapter<Entity>) serverWorld.entityManager.entityGetter).visibleEntities.byUuid.remove(entity.getUUID());

                try {
                    EntitySection<Entity> section = serverWorld.entityManager.sectionStorage.getOrCreateSection(
                            SectionPos.asLong(entity.blockPosition())
                    );
                    section.remove(entity);
                } catch (Exception e) {
                    // 忽略异常
                }

                try {
                    var chunk = serverWorld.getChunkAt(entity.blockPosition());
                    if (chunk != null) {
                        chunk.setUnsaved(true);
                    }
                } catch (Exception e) {
                    // 忽略异常
                }

                System.out.println("[GoodCake] 服务端已清除实体: " + entity.getUUID());
            }
        } catch (Throwable ex) {
            System.err.println("[GoodCake] 清除实体失败: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if (locateOreIfCrouching(player)) {
            return true;
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (locateOreIfCrouching(player)) {
            return true;
        }
        if (entity instanceof LivingEntity livingEntity) {
            dealTrueDamage(stack, player, livingEntity);
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public static void locateHeldCakeOre(ServerPlayer player) {
        if (!player.isCrouching()) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GoodCake cake)) {
            return;
        }

        cake.locateOreIfCrouching(player);
    }

    private boolean locateOreIfCrouching(Player player) {
        if (!player.isCrouching()) {
            return false;
        }

        Level level = player.level();
        if (level.isClientSide()) {
            return true;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return true;
        }
        if (player.getCooldowns().isOnCooldown(this)) {
            return true;
        }

        player.getCooldowns().addCooldown(this, ORE_LOCATE_COOLDOWN_TICKS);
        BlockPos origin = player.blockPosition();
        BlockPos orePos = findNearestOre(serverLevel, origin);
        if (orePos == null) {
            player.displayClientMessage(Component.literal("附近 " + ORE_SEARCH_RADIUS + " 格内没有找到本模组矿物。"), false);
            return true;
        }

        int distance = (int) Math.round(Math.sqrt(distanceSqr(origin, orePos)));
        player.displayClientMessage(Component.literal("最近的本模组矿物: X " + orePos.getX()
                + ", Y " + orePos.getY()
                + ", Z " + orePos.getZ()
                + "，距离约 " + distance + " 格。"), false);
        return true;
    }

    private static BlockPos findNearestOre(ServerLevel level, BlockPos origin) {
        int chunkRadius = (ORE_SEARCH_RADIUS >> 4) + 1;
        int horizontalRadiusSqr = ORE_SEARCH_RADIUS * ORE_SEARCH_RADIUS;
        ChunkPos centerChunk = new ChunkPos(origin);
        BlockPos nearest = null;
        double nearestDistanceSqr = Double.MAX_VALUE;

        for (int chunkX = centerChunk.x - chunkRadius; chunkX <= centerChunk.x + chunkRadius; chunkX++) {
            for (int chunkZ = centerChunk.z - chunkRadius; chunkZ <= centerChunk.z + chunkRadius; chunkZ++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }

                LevelChunkSection[] sections = chunk.getSections();
                for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                    LevelChunkSection section = sections[sectionIndex];
                    if (section.hasOnlyAir() || !section.maybeHas(GoodCake::isGenesisOre)) {
                        continue;
                    }

                    int sectionBottom = level.getSectionYFromSectionIndex(sectionIndex) << 4;
                    for (int localY = 0; localY < 16; localY++) {
                        int blockY = sectionBottom + localY;
                        if (level.isOutsideBuildHeight(blockY)) {
                            continue;
                        }

                        for (int localX = 0; localX < 16; localX++) {
                            int blockX = (chunkX << 4) + localX;
                            int dx = blockX - origin.getX();
                            if (dx * dx > horizontalRadiusSqr) {
                                continue;
                            }

                            for (int localZ = 0; localZ < 16; localZ++) {
                                int blockZ = (chunkZ << 4) + localZ;
                                int dz = blockZ - origin.getZ();
                                if (dx * dx + dz * dz > horizontalRadiusSqr) {
                                    continue;
                                }

                                BlockState state = section.getBlockState(localX, localY, localZ);
                                if (!isGenesisOre(state)) {
                                    continue;
                                }

                                double distanceSqr = distanceSqr(origin, blockX, blockY, blockZ);
                                if (distanceSqr < nearestDistanceSqr) {
                                    nearestDistanceSqr = distanceSqr;
                                    nearest = new BlockPos(blockX, blockY, blockZ);
                                }
                            }
                        }
                    }
                }
            }
        }

        return nearest;
    }

    private static boolean isGenesisOre(BlockState state) {
        return state.is(BlockRegistry.ARCANE_CRYSTAL_ORE.get())
                || state.is(BlockRegistry.ARCANE_CRYSTAL_ORE_DEEPSLATE.get())
                || state.is(BlockRegistry.NETHER_ARCANE_CRYSTAL_ORE.get())
                || state.is(BlockRegistry.END_ARCANE_CRYSTAL_ORE.get());
    }

    private static double distanceSqr(BlockPos origin, BlockPos target) {
        return distanceSqr(origin, target.getX(), target.getY(), target.getZ());
    }

    private static double distanceSqr(BlockPos origin, int x, int y, int z) {
        double dx = x - origin.getX();
        double dy = y - origin.getY();
        double dz = z - origin.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if(level instanceof ServerLevel slv){
          removeMobsAroundPlayer(slv, player);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("真伤: " + formatDamage(getTrueDamage(stack))).withStyle(ChatFormatting.RED));
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
