package com.baizeli.eternisstarrysky.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import com.baizeli.eternisstarrysky.mixinutil.EntityMarker;

@Mixin(Entity.class)
public class EntityMixin implements EntityMarker
{
	@Unique
	private long mark = 0;

	@Override
	public void mark(long mark)
	{
		this.mark = mark;
	}

	@Override
	public long mark()
	{
		return this.mark;
	}
}
