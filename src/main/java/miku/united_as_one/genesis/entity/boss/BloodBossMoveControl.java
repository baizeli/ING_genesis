package miku.united_as_one.genesis.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BloodBossMoveControl extends MoveControl {

    private final BloodBoss boss;

    public BloodBossMoveControl(BloodBoss mob) {
        super(mob);
        this.boss = mob;
    }

    @Override
    public void tick() {
        if (!boss.isCasting()) {
            super.tick();
            return;
        }

        if (this.operation != Operation.MOVE_TO) {
            super.tick();
            return;
        }

        this.operation = Operation.WAIT;

        double dx = this.wantedX - this.mob.getX();
        double dz = this.wantedZ - this.mob.getZ();
        double dy = this.wantedY - this.mob.getY();

        double distSq = dx * dx + dz * dz + dy * dy;
        if (distSq < MIN_SPEED_SQR) {
            this.mob.setZza(0.0F);
            this.mob.setXxa(0.0F); 
            return;
        }

        
        BlockPos blockpos = this.mob.blockPosition();
        BlockState blockstate = this.mob.level().getBlockState(blockpos);
        VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level(), blockpos);

        boolean shouldJump =
                (dy > (double)this.mob.getStepHeight()
                        && dx * dx + dz * dz < (double)Math.max(1.0F, this.mob.getBbWidth()))
                        || (!voxelshape.isEmpty()
                        && this.mob.getY() < voxelshape.max(Axis.Y) + (double)blockpos.getY()
                        && !blockstate.is(BlockTags.DOORS)
                        && !blockstate.is(BlockTags.FENCES));

        if (shouldJump) {
            this.mob.getJumpControl().jump();
            this.operation = Operation.JUMPING;
        }

        
        float baseSpeed = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float speed = (float)this.speedModifier * baseSpeed;

        
        double dist = Math.sqrt(dx * dx + dz * dz);

        
        
        double worldNormX = dx / dist;
        double worldNormZ = dz / dist;

        
        
        float checkDist = 1.5F;
        double checkX = worldNormX * checkDist;
        double checkZ = worldNormZ * checkDist;

        
        if (!isSafe(checkX, checkZ)) {
            
            this.mob.setZza(0.0F);
            this.mob.setXxa(0.0F);
            return;
        }

        
        
        float yawRad = this.mob.getYRot() * Mth.DEG_TO_RAD;

        
        
        double localX = worldNormX * Mth.cos(-yawRad) - worldNormZ * Mth.sin(-yawRad);
        double localZ = worldNormX * Mth.sin(-yawRad) + worldNormZ * Mth.cos(-yawRad);

        float strafe = (float)localX;
        float forward = (float)localZ;

        
        float len = Mth.sqrt(strafe * strafe + forward * forward);
        if (len < 1.0F) {
            len = 1.0F;
        }

        
        
        float scale = speed / len;
        strafe *= scale;
        forward *= scale;

        
        this.mob.setSpeed(speed);
        this.mob.setZza(forward);
        this.mob.setXxa(strafe);
    }

    
    private boolean isSafe(double worldRelX, double worldRelZ) {
        
        if (!this.isWalkable((float)worldRelX, (float)worldRelZ)) {
            return false;
        }

        
        double nextX = this.mob.getX() + worldRelX;
        double nextY = this.mob.getY();
        double nextZ = this.mob.getZ() + worldRelZ;

        
        
        BlockPos groundPos = new BlockPos(
                Mth.floor(nextX),
                Mth.floor(nextY - 1.0), 
                Mth.floor(nextZ)
        );

        
        
        if (this.mob.level().getBlockState(groundPos).isAir()) {
            return false;
        }

        return true;
    }
}