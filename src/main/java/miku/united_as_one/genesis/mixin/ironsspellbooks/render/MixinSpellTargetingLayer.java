package miku.united_as_one.genesis.mixin.ironsspellbooks.render;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.ClientSpellTargetingData;
import miku.bai_ze_li.genesis.api.render.RenderUtils;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.capabilities.magic.MultiTargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellTargetingLayer.class)
public class MixinSpellTargetingLayer {
    @Inject(method = "renderTargetLayer", at = @At("HEAD"), remap = false, cancellable = true)
    private static void injectRenderWireCube(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity, CallbackInfo ci) {
     /*
        if (SpellRegistry.getSpell(ClientMagicData.getTargetingData().spellId).getSchoolType() == SpellSchoolRegistry.CHAOS.get()
                //|| SpellRegistry.getSpell(ClientMagicData.getTargetingData().spellId).getSchoolType() == SpellSchoolRegistry.CELESTIAL_SOURCE.get()
        ) {
            Vector3f color = null;
            if (ClientMagicData.getRecasts().hasRecastsActive()) {
                for (RecastInstance recastInstance : ClientMagicData.getRecasts().getActiveRecasts()) {
                    ICastDataSerializable castData = recastInstance.getCastData();
                    if (castData instanceof MultiTargetEntityCastData multiTargetData) {
                        if (multiTargetData.isTargeted(entity)) {
                            color = SpellRegistry.getSpell(recastInstance.getSpellId()).getTargetingColor();
                            break;
                        }
                    }
                }
            }

            if (color == null) {
                color = SpellRegistry.getSpell(ClientMagicData.getTargetingData().spellId).getTargetingColor();
            }

            color.mul(0.4F);

            float height = (float) entity.getBoundingBox().getYsize();
            float magicYOffset = (float) (1.5 - height);

            int r = (int) (color.x() * 255);
            int g = (int) (color.y() * 255);
            int b = (int) (color.z() * 255);

            float angle = (System.currentTimeMillis() % 360000L) / 1000F * 90F;
            if (SpellRegistry.getSpell(ClientMagicData.getTargetingData().spellId).getSchoolType() == SpellSchoolRegistry.CHAOS.get()) {
                RenderUtils.renderWireCube(poseStack, bufferSource, 1.0F, angle, Axis.YP, r, g, b, 0, magicYOffset, 0);
                RenderUtils.renderWireCube(poseStack, bufferSource, 1.0F, angle, Axis.XP, r, g, b, 0, magicYOffset, 0);
                RenderUtils.renderWireCube(poseStack, bufferSource, 1.0F, angle, Axis.ZP, r, g, b, 0, magicYOffset, 0);
            }
//            else {
//                RenderUtils.render(poseStack, bufferSource, LightTexture.FULL_BRIGHT, entity, Minecraft.getInstance().realPartialTick);
//            }

            ci.cancel();
        }*/
    }
}
