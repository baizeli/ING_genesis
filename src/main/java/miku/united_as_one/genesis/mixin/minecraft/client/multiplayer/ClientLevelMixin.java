package miku.united_as_one.genesis.mixin.minecraft.client.multiplayer;

import miku.united_as_one.genesis.api.mixinutil.ParticleSuppressionManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void genesis$onAddParticle(ParticleOptions particleData, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed,
                                       CallbackInfo ci) {
        if (ParticleSuppressionManager.isSuppressingEmbers(particleData)) {
            ci.cancel();
        }
    }
}