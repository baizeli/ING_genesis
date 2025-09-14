package com.baizeli.eternisstarrysky.Mixin;

import com.baizeli.eternisstarrysky.Items.InfinitySwordTrue;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SynchedEntityData.class)
public class synchedDataMixin {

    @Inject(method = "get",at =@At("RETURN"),cancellable = true)
    public <T> void get(EntityDataAccessor<T> key, CallbackInfoReturnable<T> cir){
        //if (key.id()
        if (InfinitySwordTrue.datas.contains(this)) {
            if (cir.getReturnValue() instanceof Float)
                cir.setReturnValue((T) (Object) 0.0F);
        }
    }
}
