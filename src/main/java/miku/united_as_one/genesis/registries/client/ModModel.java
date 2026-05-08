package miku.united_as_one.genesis.registries.client;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.BaiZeLiModel;
import miku.united_as_one.genesis.client.model.ThrowBloodAndWoundsModel;
import miku.united_as_one.genesis.client.model.WardenSpellcasterModel;
import miku.united_as_one.genesis.client.model.spell.celestial_source.DeadStarDecreeCometModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModel {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BaiZeLiModel.LAYER_LOCATION, BaiZeLiModel::createBodyLayer);
        event.registerLayerDefinition(DeadStarDecreeCometModel.LAYER_LOCATION, DeadStarDecreeCometModel::createBodyLayer);
        event.registerLayerDefinition(ThrowBloodAndWoundsModel.LAYER_LOCATION, ThrowBloodAndWoundsModel::createBodyLayer);
        event.registerLayerDefinition(WardenSpellcasterModel.LAYER_LOCATION, WardenSpellcasterModel::createBodyLayer);
    }
}
