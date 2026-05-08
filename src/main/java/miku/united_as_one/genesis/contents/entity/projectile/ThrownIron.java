package miku.united_as_one.genesis.contents.entity.projectile;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import miku.united_as_one.genesis.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.*;

public class ThrownIron extends ThrowableItemProjectile {
    private float damage = 6.0F;
    private int lifeTime = 100;
    private LivingEntity target;
    public final List<Vec3> trailPositions = new ArrayList<>();

    private List<Vec3> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    private int recalcCooldown = 0;
    private int stuckTicks = 0;
    private Vec3 lastPos = Vec3.ZERO;

    private static final int PATH_RECALC_INTERVAL = 5;    // 路径重算间隔(ticks)，A*寻路刷新频率
    private static final double WAYPOINT_REACH_DIST = 0.5;  // 路径点判定到达距离(格)，小于此值视为已到达
    private static final double STUCK_THRESHOLD = 0.05;  // 卡住检测阈值(格/tick)，移动小于此值判定为卡住
    private static final double ENTITY_RADIUS = 0.25;   // 实体碰撞箱半径(格)，用于宽度和碰撞检测，比实际高度小一点没事
    private static final double ENTITY_HEIGHT = 0.25;   // 实体碰撞箱高度(格)，影响垂直通过性
    private static final double PREDICTION_DIST = 2.5;  // 前方障碍预测距离(格)，避障探针长度
    private static final double AVOIDANCE_STRENGTH = 0.5; // 避障转向强度(0-1)，越大转向越急越敏感

    public ThrownIron(EntityType<? extends ThrownIron> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public ThrownIron(Level level, LivingEntity shooter) {
        super(EntityRegistry.THROWN_IRON.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(20.0D);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 2048.0D;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; i++) trailPositions.add(this.position());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.tickCount % 5 == 0 && (target == null || !target.isAlive())) {
                findNewTarget();
            }

            if (target != null && target.isAlive()) {
                navigate();
            }

            if (this.tickCount >= this.lifeTime) this.discard(); // 达到生命周期后消失

            checkIfStuck();
        } else {
            trailPositions.add(this.position());
            if (trailPositions.size() > 20) trailPositions.remove(0);  // 保持轨迹点数量，控制拖尾长度
        }
    }

    private void findNewTarget() {
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(20.0D),
                e -> e != this.getOwner() && e.isAlive() && e.isAttackable());

        if (!entities.isEmpty()) {
            entities.sort(Comparator.comparingDouble(e -> e.distanceToSqr(this)));
            this.target = entities.get(0);
            this.currentPath.clear();
            this.recalcCooldown = 0;
            this.stuckTicks = 0;
        }
    }

    private void navigate() {
        Vec3 currentPos = this.position();

        // 定期重算或路径耗尽时更新
        if (--recalcCooldown <= 0 || pathIndex >= currentPath.size()) {
            recalculatePath();
        }

        if (!currentPath.isEmpty() && pathIndex < currentPath.size()) {
            Vec3 nextWaypoint = currentPath.get(pathIndex);
            Vec3 dirToNext = nextWaypoint.subtract(currentPos);
            double distToNext = dirToNext.length();

            // 到达路径点
            if (distToNext < WAYPOINT_REACH_DIST) {
                pathIndex++;
                if (pathIndex < currentPath.size()) {
                    nextWaypoint = currentPath.get(pathIndex);
                    dirToNext = nextWaypoint.subtract(currentPos);
                }
            }

            // 预测性避障转向
            Vec3 desiredVelocity = calculateSteering(dirToNext.normalize(), currentPos);

            // 速度混合：保留部分惯性+新方向
            Vec3 currentVelocity = this.getDeltaMovement();
            Vec3 newVelocity = currentVelocity.scale(0.2D).add(desiredVelocity.scale(0.5D));

            // 确保最小速度
            if (newVelocity.lengthSqr() < 0.04) {
                newVelocity = dirToNext.normalize().scale(0.3D);
            }

            // 碰撞预测与处理
            Vec3 nextPos = currentPos.add(newVelocity);
            if (wouldCollideAt(nextPos)) {
                // 尝试向上越障（台阶/方块）
                Vec3 upAttempt = currentPos.add(newVelocity.x, Math.abs(newVelocity.y) + 0.25, newVelocity.z);
                if (!wouldCollideAt(upAttempt) && !wouldCollideAt(currentPos.add(0, 0.25, 0))) {
                    newVelocity = new Vec3(newVelocity.x, 0.25, newVelocity.z);
                } else {
                    // 滑墙逻辑：沿碰撞法线切向滑动
                    Vec3 normal = findWallNormal(currentPos, newVelocity);
                    Vec3 slide = newVelocity.subtract(normal.scale(newVelocity.dot(normal)));
                    if (slide.lengthSqr() > 0.01) {
                        slide = slide.normalize().scale(0.25); // 减速滑行
                        newVelocity = slide;
                    } else {
                        // 完全卡住时尝试垂直逃逸
                        newVelocity = new Vec3(0, 0.25, 0);
                    }
                }
            }

            this.setDeltaMovement(newVelocity);
        } else {
            directPursuit();
        }
    }

    private Vec3 calculateSteering(Vec3 direction, Vec3 currentPos) {
        Vec3 steering = direction.scale(0.3D);

        // 预测未来位置
        Vec3 futurePos = currentPos.add(this.getDeltaMovement().scale(2.0));

        // 射线检测前方障碍物（不仅是当前位置）
        Vec3[] probes = {
                direction.scale(PREDICTION_DIST),                                    // 正前
                direction.yRot((float)(Math.PI / 6)).scale(PREDICTION_DIST * 0.8),   // 右前30度
                direction.yRot((float)(-Math.PI / 6)).scale(PREDICTION_DIST * 0.8), // 左前30度
                direction.yRot((float)(Math.PI / 3)).scale(PREDICTION_DIST * 0.5),  // 右前60度
                direction.yRot((float)(-Math.PI / 3)).scale(PREDICTION_DIST * 0.5)   // 左前60度
        };

        Vec3 avoidance = Vec3.ZERO;
        int hitCount = 0;

        for (Vec3 probe : probes) {
            Vec3 checkPos = futurePos.add(probe);
            if (isSolidAt(checkPos)) {
                double dist = futurePos.distanceTo(checkPos);
                double force = (PREDICTION_DIST - dist) / PREDICTION_DIST; // 越近越强
                Vec3 repulse = futurePos.subtract(checkPos).normalize().scale(force * AVOIDANCE_STRENGTH);
                avoidance = avoidance.add(repulse);
                hitCount++;
            }
        }

        // 如果前方被堵，增加垂直分量尝试越过
        if (hitCount >= 2) {
            avoidance = avoidance.add(0, 0.3, 0);
        }

        // 长时间卡住时加入随机扰动（逃离局部最优）
        if (stuckTicks > 3) {
            avoidance = avoidance.add(
                    (random.nextDouble() - 0.5) * 0.4,
                    0.2,
                    (random.nextDouble() - 0.5) * 0.4
            );
        }

        Vec3 result = steering.add(avoidance);
        return result.lengthSqr() > 0.0001 ? result.normalize().scale(0.2D) : direction.scale(0.2D);
    }

    private void recalculatePath() {
        BlockPos start = this.blockPosition();
        BlockPos end = target.blockPosition();

        // A*计算
        List<BlockPos> blockPath = calculateAStarNoDiagonal(start, end);

        if (!blockPath.isEmpty()) {
            currentPath = simplifyPath(blockPath);
            pathIndex = 0;
        }

        recalcCooldown = PATH_RECALC_INTERVAL;
    }

    private List<BlockPos> calculateAStarNoDiagonal(BlockPos start, BlockPos end) {
        PriorityQueue<AStarNode> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Set<BlockPos> closed = new HashSet<>();
        Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
        Map<BlockPos, Double> gScore = new HashMap<>();

        open.add(new AStarNode(start, 0, heuristic(start, end)));
        gScore.put(start, 0.0);

        int iterations = 0;
        int maxIter = 300;

        while (!open.isEmpty() && iterations++ < maxIter) {
            AStarNode current = open.poll();
            BlockPos cur = current.pos;

            if (cur.distSqr(end) <= 1) {
                return reconstructPath(cameFrom, cur);
            }

            closed.add(cur);

            // 6方向邻居（无对角线）
            BlockPos[] neighbors = {
                    cur.north(), cur.south(), cur.east(), cur.west(),
                    cur.above(), cur.below()
            };

            for (BlockPos neighbor : neighbors) {
                if (closed.contains(neighbor)) continue;

                // 更严格的可通行检查
                if (!isPassableWithExpansion(neighbor)) continue;
                if (isPathBlocked(cur, neighbor)) continue;

                double g = gScore.get(cur) + cur.distSqr(neighbor); // 使用真实距离成本
                if (g < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, cur);
                    gScore.put(neighbor, g);
                    double f = g + heuristic(neighbor, end);
                    open.add(new AStarNode(neighbor, g, f));
                }
            }
        }

        return Collections.emptyList();
    }

    private boolean isPathBlocked(BlockPos from, BlockPos to) {
        Vec3 start = Vec3.atCenterOf(from);
        Vec3 end = Vec3.atCenterOf(to);

        // 更精细的步进检测
        Vec3 step = end.subtract(start).normalize().scale(0.2);
        Vec3 check = start;
        double dist = start.distanceTo(end);
        double traveled = 0;

        while (traveled < dist) {
            if (isSolidAt(check)) return true;
            check = check.add(step);
            traveled += 0.2;

            // 检查对角线夹缝（仅水平移动时）
            if (step.y == 0) {
                // 检查周围四个对角线位置是否同时阻挡
                Vec3[] diagonals = {
                        check.add(0.3, 0, 0.3), check.add(-0.3, 0, 0.3),
                        check.add(0.3, 0, -0.3), check.add(-0.3, 0, -0.3)
                };
                for (Vec3 d : diagonals) {
                    if (isSolidAt(d) && isSolidAt(d.add(0, 0.5, 0))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPassableWithExpansion(BlockPos pos) {
        AABB entityBox = new AABB(
                pos.getX() + 0.5 - ENTITY_RADIUS,
                pos.getY() + 0.5 - ENTITY_HEIGHT / 2,
                pos.getZ() + 0.5 - ENTITY_RADIUS,
                pos.getX() + 0.5 + ENTITY_RADIUS,
                pos.getY() + 0.5 + ENTITY_HEIGHT / 2,
                pos.getZ() + 0.5 + ENTITY_RADIUS
        );

        if (!this.level().noCollision(entityBox)) return false;

        Vec3 center = Vec3.atCenterOf(pos);
        Vec3[] corners = {
                center.add(0.3, 0, 0.3),
                center.add(-0.3, 0, 0.3),
                center.add(0.3, 0, -0.3),
                center.add(-0.3, 0, -0.3),
        };
        for (Vec3 c : corners) {
            if (isSolidAt(c) && isSolidAt(c.add(0, ENTITY_HEIGHT, 0))) {
                return false; // 角落被堵
            }
        }
        return true;
    }

    private boolean wouldCollideAt(Vec3 pos) {
        AABB box = new AABB(
                pos.x - ENTITY_RADIUS, pos.y - ENTITY_HEIGHT / 2, pos.z - ENTITY_RADIUS,
                pos.x + ENTITY_RADIUS, pos.y + ENTITY_HEIGHT / 2, pos.z + ENTITY_RADIUS
        );
        return !this.level().noCollision(box);
    }

    private Vec3 findWallNormal(Vec3 pos, Vec3 velocity) {
        Vec3[] dirs = {
                new Vec3(1, 0, 0), new Vec3(-1, 0, 0),
                new Vec3(0, 0, 1), new Vec3(0, 0, -1),
                new Vec3(0, 1, 0), new Vec3(0, -1, 0)
        };

        for (Vec3 dir : dirs) {
            if (isSolidAt(pos.add(dir.scale(ENTITY_RADIUS + 0.05)))) {
                return dir.normalize();
            }
        }
        return velocity.normalize().scale(-1);
    }

    private List<Vec3> simplifyPath(List<BlockPos> blockPath) {
        if (blockPath.size() <= 2) {
            return blockPath.stream().map(Vec3::atCenterOf).toList();
        }

        List<Vec3> waypoints = new ArrayList<>();
        waypoints.add(Vec3.atCenterOf(blockPath.get(0)));

        int i = 0;
        while (i < blockPath.size() - 1) {
            // 保守的最大步长，防止跳过狭窄拐角
            int maxLookahead = Math.min(i + 2, blockPath.size() - 1);
            int furthest = i + 1;

            for (int j = maxLookahead; j > i; j--) {
                Vec3 from = Vec3.atCenterOf(blockPath.get(i));
                Vec3 to = Vec3.atCenterOf(blockPath.get(j));

                // 不仅检查视线，还要确保路径宽度足够
                if (hasLineOfSight(from, to) && hasSufficientWidth(from, to)) {
                    furthest = j;
                    break;
                }
            }

            waypoints.add(Vec3.atCenterOf(blockPath.get(furthest)));
            i = furthest;
        }

        return waypoints;
    }

    private boolean hasSufficientWidth(Vec3 from, Vec3 to) {
        Vec3 dir = to.subtract(from).normalize();
        Vec3 perp = new Vec3(-dir.z, 0, dir.x).scale(ENTITY_RADIUS * 1.25); // 1.25倍半径

        // 检查路径左右两侧是否都通畅
        Vec3 leftFrom = from.add(perp);
        Vec3 leftTo = to.add(perp);
        Vec3 rightFrom = from.subtract(perp);
        Vec3 rightTo = to.subtract(perp);

        return hasLineOfSight(leftFrom, leftTo) && hasLineOfSight(rightFrom, rightTo);
    }

    private boolean hasLineOfSight(Vec3 from, Vec3 to) {
        var clip = this.level().clip(new net.minecraft.world.level.ClipContext(
                from, to,
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                this
        ));
        return clip.getType() == HitResult.Type.MISS;
    }

    private boolean isSolidAt(Vec3 pos) {
        BlockPos bp = BlockPos.containing(pos);
        BlockState state = this.level().getBlockState(bp);
        return state.isSolid() && state.blocksMotion();
    }

    private void checkIfStuck() {
        Vec3 current = this.position();
        if (lastPos != Vec3.ZERO) {
            double moved = current.distanceToSqr(lastPos);
            if (moved < STUCK_THRESHOLD * STUCK_THRESHOLD) {
                stuckTicks++;
                if (stuckTicks > 5) {
                    // 优先尝试垂直方向逃逸，而非随机
                    Vec3 up = new Vec3(0, 0.6, 0);
                    if (!wouldCollideAt(current.add(up))) {
                        this.setDeltaMovement(up.scale(0.5));
                    } else {
                        // 水平随机方向
                        Vec3 escape = new Vec3(
                                random.nextDouble() - 0.5,
                                0.1,
                                random.nextDouble() - 0.5
                        ).normalize().scale(0.4);
                        this.setDeltaMovement(escape);
                    }
                    currentPath.clear();
                    recalcCooldown = 0;
                }
            } else {
                stuckTicks = Math.max(0, stuckTicks - 2); // 更快恢复
            }
        }
        lastPos = current;
    }

    private void directPursuit() {
        Vec3 dir = target.position().subtract(this.position()).normalize();
        dir = dir.add((random.nextDouble() - 0.5) * 0.1, 0, (random.nextDouble() - 0.5) * 0.1).normalize();
        this.setDeltaMovement(this.getDeltaMovement().scale(0.7D).add(dir.scale(0.15D)));
    }

    private double heuristic(BlockPos a, BlockPos b) {
        double dx = Math.abs(a.getX() - b.getX());
        double dz = Math.abs(a.getZ() - b.getZ());
        double dy = Math.abs(a.getY() - b.getY()) * 1.2;
        return dx + dz + dy;
    }

    private List<BlockPos> reconstructPath(Map<BlockPos, BlockPos> from, BlockPos cur) {
        List<BlockPos> path = new ArrayList<>();
        while (from.containsKey(cur)) {
            path.add(0, cur);
            cur = from.get(cur);
        }
        path.add(0, cur);
        if (path.get(0).equals(this.blockPosition())) {
            path.remove(0);
        }
        return path;
    }

    private static class AStarNode {
        BlockPos pos;
        double g, f;
        AStarNode(BlockPos p, double g, double f) {
            this.pos = p; this.g = g; this.f = f;
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), this.damage)) {
            result.getEntity().invulnerableTime = 0;
        }
        Vec3 center = result.getEntity().getBoundingBox().getCenter();
        if (!this.level().isClientSide()) {
            MagicManager.spawnParticles(this.level(), new BlastwaveParticleOptions(new Vector3f(1, 1, 1),
                            result.getEntity().getBbWidth() * 1.5F + 1.5F),
                    center.x, center.y, center.z, 1, 0.0, 0.F, 0.0, 0.0, true);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ANVIL_LAND, this.getSoundSource(), 1, 1);
            if (result.getEntity() instanceof LivingEntity livingEntity)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
            this.discard();
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() { return Items.IRON_INGOT; }

    public void setDamage(float damage) { this.damage = damage; }
    public void setLifeTime(int lifeTime) { this.lifeTime = lifeTime; }
    public float getDamage() { return this.damage; }

    public void setTarget(LivingEntity target) {
        this.target = target;
        this.currentPath.clear();
        this.recalcCooldown = 0;
    }
}