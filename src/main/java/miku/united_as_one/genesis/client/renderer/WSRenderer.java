
package miku.united_as_one.genesis.client.renderer;

import miku.united_as_one.genesis.client.model.WardenSpellcasterModel;
import miku.united_as_one.genesis.common.entity.WardenSpellcaster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WSRenderer<E extends WardenSpellcaster>
extends MobRenderer<E, WardenSpellcasterModel<E>>
{
    public WSRenderer(EntityRendererProvider.Context context)
    {
        super(context, new WardenSpellcasterModel<>(context.bakeLayer(WardenSpellcasterModel.LAYER_LOCATION)),
                1.0F);
    }

    public ResourceLocation getTextureLocation(E e)
    {
        return new ResourceLocation("iron_spells_genesis:textures/entity/ws.png");
    }
}
