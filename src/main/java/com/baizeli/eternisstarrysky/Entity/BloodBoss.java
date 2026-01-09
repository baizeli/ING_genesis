package com.baizeli.eternisstarrysky.Entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class BloodBoss extends Monster
{
	public static final String ENTITYID = "blood_boss";

	public BloodBoss(EntityType<? extends Monster> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
		AttributeInstance maxHealthAttr = this.getAttributes().getInstance(Attributes.MAX_HEALTH);
		if (maxHealthAttr == null)
			throw new NullPointerException("No attribute max_health");
		maxHealthAttr.setBaseValue(950);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static Supplier<EntityType<BloodBoss>> supplier()
	{
		return () -> EntityType.Builder.of(BloodBoss::new, MobCategory.MONSTER)
			.sized(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight())
			.clientTrackingRange(64)
			.updateInterval(1)
			.build(ENTITYID);
	}
}
