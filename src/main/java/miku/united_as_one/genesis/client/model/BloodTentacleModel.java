package miku.united_as_one.genesis.client.model;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacleModel;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.resources.ResourceLocation;

public class BloodTentacleModel extends VoidTentacleModel {
    public static final ResourceLocation textureResource = new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_tentacle/blood_tentacle.png");
    @Override
    public ResourceLocation getTextureResource(VoidTentacle mob) {
        return textureResource;
    }
}
