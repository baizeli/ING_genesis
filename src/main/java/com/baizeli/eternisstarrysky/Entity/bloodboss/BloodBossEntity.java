package com.baizeli.eternisstarrysky.Entity.bloodboss;

import com.baizeli.eternisstarrysky.util.EntityAttribute;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Supplier;

public class BloodBossEntity extends PathfinderMob implements GeoEntity {
	public static final String ENTITYID = "blood_boss";
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public BloodBossEntity(EntityType<? extends PathfinderMob > p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
		// Modify entity attributes
		EntityAttribute.require(this, Attributes.MAX_HEALTH).setBaseValue(950);
		EntityAttribute.require(this, Attributes.FOLLOW_RANGE).setBaseValue(50);
		EntityAttribute.require(this, AttributeRegistry.MAX_MANA.get()).setBaseValue(14000);


	}



	public static Supplier<? extends EntityType<BloodBossEntity>> supplier() {
		return () -> EntityType.Builder.of(BloodBossEntity::new,MobCategory.MONSTER )
				.sized(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight())
				.clientTrackingRange(64)
				.updateInterval(1)
				.build(ENTITYID);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

	}
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

	@Override
	public double getTick(Object o) {
		return 0;
	}
}
