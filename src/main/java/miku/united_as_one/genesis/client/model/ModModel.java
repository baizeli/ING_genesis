package miku.united_as_one.genesis.client.model;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.client.model.spell.celestial_source.DeadStarDecreeCometModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModel {
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DeadStarDecreeCometModel.LAYER_LOCATION, DeadStarDecreeCometModel::createBodyLayer);
    }
}