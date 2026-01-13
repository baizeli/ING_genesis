package com.baizeli.eternisstarrysky.Entity.bloodboss;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BloodBossModel extends DefaultedEntityGeoModel<BloodBossEntity> {
    public static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, "geo/blood_boss.geo.json");
    public static final ResourceLocation STAGE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, "textures/entity/blood_boss/stage1.png");
    public static final ResourceLocation STAGE1_TEXTURE_GLINT =
            ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, "textures/entity/blood_boss/steag1glint.png");



    public BloodBossModel() {
        super(new ResourceLocation(EternisStarrySky.MODID, "blood_boss"));
    }


    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

}
