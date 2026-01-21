package miku.united_as_one.genesis.client.renderer.boss;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.render.ChargeSpellLayer;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import io.redspace.ironsspellbooks.render.GlowingEyesLayer;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.client.model.boss.BloodBossModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;

import java.util.ArrayList;
import java.util.List;

public class BloodBossRenderer extends AbstractSpellCastingMobRenderer{

    public BloodBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodBossModel());
        this.addRenderLayer(new BloodBossGlowLayer(this));
    }




}