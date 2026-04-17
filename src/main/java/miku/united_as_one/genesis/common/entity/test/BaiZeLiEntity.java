package miku.united_as_one.genesis.common.entity.test;

import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class BaiZeLiEntity extends PathfinderMob implements IBaiZeLi, IFlagMob {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    public AnimationState animation = new AnimationState();
    public AnimationState animation2 = new AnimationState();
    private BaiZeLiBehavior behavior;
    private boolean initialized = false;

    public BaiZeLiEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setCustomName(Component.literal("白泽利"));
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();
    }

    public BaiZeLiEntity(PlayMessages.SpawnEntity spawn, Level level) {
        this(EntityRegistry.BAI_ZE_LI.get(), level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(DATA_FLAGS)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 1 -> {
                        this.animation2.stop();
                        this.animation.startIfStopped(tickCount);
                    }
                    case 2 -> {
                        this.animation.stop();
                        this.animation2.startIfStopped(tickCount);
                    }
                    default -> {
                    }
                }
            } else {
                if (this.getFlag() < 0 || this.getFlag() > 2) {
                    this.setFlag(0);
                }
            }
        }
        super.onSyncedDataUpdated(key);
    }

    private void ensureInit() {
        if (!initialized) {
            this.behavior = new BaiZeLiBehavior(this);
            initialized = true;
            if (behavior != null) {
                behavior.registerGoals();
            }
        }
    }

    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getMainHandItem().isEmpty()) {
            this.setFlag(this.getFlag() + 1);
        }
        return super.mobInteract(player, hand);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void tick() {
        ensureInit();
        if (behavior != null) {
            behavior.tick();
        }
        super.tick();
    }

    @Override
    public void tickLogic() {
        ensureInit();
        if (behavior != null) behavior.tick();
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i) {
        this.entityData.set(DATA_FLAGS, i);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(BaiZeLiEntity.class, EntityDataSerializers.INT);
    }
}
