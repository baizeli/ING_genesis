package miku.united_as_one.genesis.handlers.item.weapon.bow;

import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.entity.spells.FireEruptionAoe;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber
public class FlameBowEvent {

    @SubscribeEvent
    public static void onFlameArrowImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof FireArrowProjectile fireArrow) {
            if (!fireArrow.getPersistentData().getBoolean("FlameBow_Arrow")) return;
            
            if (fireArrow.getOwner() instanceof LivingEntity owner && !fireArrow.level.isClientSide) {
                Vec3 impactLocation = event.getRayTraceResult().getLocation();

                FireEruptionAoe aoe = new FireEruptionAoe(fireArrow.level, 5);
                
                aoe.setOwner(owner);
                aoe.moveTo(impactLocation.x, impactLocation.y, impactLocation.z);
                fireArrow.level.addFreshEntity(aoe);

                CameraShakeManager.addCameraShake(new CameraShakeData(40, impactLocation, 12));
            }
        }
    }
}