package com.baizeli.eternisstarrysky.Items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class InfinitySword extends SwordItem
{
	private static final double KILL_RADIUS = 5;

	public InfinitySword(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_)
	{
		super(p_43269_, p_43270_, p_43271_, p_43272_);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
	{
		if (!(player instanceof ServerPlayer))
			return false;

		if (!(entity instanceof LivingEntity))
			return false;

		Predicate<LivingEntity> predicate = (e) -> e.getId() != player.getId();
		List<LivingEntity> nearbyEntities = InfinitySword.getNearbyLivingEntities(entity, KILL_RADIUS, predicate);

		DamageSource source = new DamageSource(player.damageSources().fellOutOfWorld().typeHolder(), player);
		nearbyEntities.forEach(living -> {
			player.crit(living);
			living.hurt(source, this.getDamage());
		});
		return true;
	}

	public static List<LivingEntity> getNearbyLivingEntities(Entity entity, double radius, Predicate<LivingEntity> predicate)
	{
		Level level;
		if (entity == null || (level = entity.level()).isClientSide)
			return List.of();

		EntityTypeTest<Entity, LivingEntity> typeTest = EntityTypeTest.forClass(LivingEntity.class);
		Vec3 pos = entity.position();
		AABB aabb = new AABB(
			pos.x - radius, pos.y - radius, pos.z - radius,
			pos.x + radius, pos.y + radius, pos.z + radius
		);
		List<LivingEntity> nearbyEntities = new LinkedList<>();
		if (predicate == null)
			predicate = (e) -> true;
		level.getEntities(typeTest, aabb, predicate, nearbyEntities, Integer.MAX_VALUE);
		return nearbyEntities;
	}
}
