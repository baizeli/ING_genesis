package miku.united_as_one.genesis.mixin.minecraftforge.event.entity.living;

import miku.united_as_one.genesis.api.mixin.LivingEventEC;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEvent.class)
public abstract class LivingEventMixin extends EntityEvent implements LivingEventEC {
    //源代码来自revelationfix，原作者mega32k
    @Unique
    private boolean revelationfix$unCancelable;

    public LivingEventMixin(Entity entity) {
        super(entity);
    }

    @Override
    public boolean ironSpellGenesis$isHackedUnCancelable() {
        return revelationfix$unCancelable;
    }

    @Override
    public void ironSpellGenesis$hackedUnCancelable(boolean target) {
        revelationfix$unCancelable = target;
    }

    @Override
    public boolean ironSpellGenesis$isHackedOnlyAmountUp() {
        return false;
    }

    @Override
    public void ironSpellGenesis$hackedOnlyAmountUp(boolean target) {
    }

    @Override
    public void setCanceled(boolean cancel) {
        if (this.revelationfix$unCancelable)
            return;
        super.setCanceled(cancel);
    }
}