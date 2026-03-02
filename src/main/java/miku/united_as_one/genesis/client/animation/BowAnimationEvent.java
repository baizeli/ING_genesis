package miku.united_as_one.genesis.client.animation;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.ItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.stream.Stream;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BowAnimationEvent {

    @SubscribeEvent
    public static void BowAnimation(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Stream.of(
                ItemRegistry.THUNDER_LONGBOW.get(),
                ItemRegistry.FROST_LONGBOW.get(),
                ItemRegistry.WITCHCRAFT_BOW.get(),
                ItemRegistry.FLAME_BOW.get()
            ).forEach(bow -> {
                ItemProperties.register(bow, new ResourceLocation("pull"), 
                    (stack, level, livingEntity, i) -> {
                        if (livingEntity != null)
                            return livingEntity.getUseItem() != stack ? 0 : (float) (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20;
                        return 0;
                    }
                );

                ItemProperties.register(bow, new ResourceLocation("pulling"),
                    (stack, level, livingEntity, i) -> livingEntity != null &&
                    livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1 : 0
                );
            });
        });
    }
}
