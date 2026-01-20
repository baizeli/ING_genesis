package miku.united_as_one.genesis.Entity.bloodboss;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class BloodBossLookControl extends LookControl
{
	public BloodBossLookControl(Mob mob)
	{
		super(mob);
	}

	@Override
	public void tick()
	{
		double dx = wantedX - this.mob.getX();
		double dy = wantedY - this.mob.getY();
		double dz = wantedZ - this.mob.getZ();

		double lengthXZ = Math.sqrt((dx * dx) + (dz * dz));
		double cos = -Math.toDegrees(Math.acos(dz / lengthXZ));
		if (dx < 0)
			cos = -cos;
		this.mob.setYBodyRot((float) cos);
		this.mob.setYHeadRot((float) cos);
		this.mob.setYRot((float) cos);

		double lengthXYZ = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		cos = -Math.toDegrees(Math.acos(lengthXZ / lengthXYZ));
		if (dy < 0)
			cos = -cos;
		this.mob.setXRot((float) cos);
	}
}
