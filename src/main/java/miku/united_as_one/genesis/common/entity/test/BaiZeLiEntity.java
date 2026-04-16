package miku.united_as_one.genesis.common.entity.test;

import miku.united_as_one.genesis.init.registry.BaiZeEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class BaiZeLiEntity extends PathfinderMob implements IBaiZeLi {

    private BaiZeLiBehavior behavior;
    private boolean initialized = false;

    public BaiZeLiEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setCustomName(Component.literal("白泽利"));
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();
    }

    public BaiZeLiEntity(PlayMessages.SpawnEntity spawn, Level level) {
        this((EntityType<? extends PathfinderMob>) BaiZeEntities.BAI_ZE.get(), level);
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
}