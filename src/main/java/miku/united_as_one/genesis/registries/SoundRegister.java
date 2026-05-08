package miku.united_as_one.genesis.registries;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class SoundRegister {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Genesis.MOD_ID);

    public static final RegistryObject<SoundEvent> GIRL_A = SOUND_EVENTS.register("girl_a",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "girl_a")));

    public static final RegistryObject<SoundEvent> CHAOS_CAST = SOUND_EVENTS.register("chaos_cast",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "chaos_cast")));

    public static final RegistryObject<SoundEvent> CELESTIAL_SOURCE_CAST = SOUND_EVENTS.register("celestia_source",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "celestia_source")));
            
    public static final RegistryObject<SoundEvent> CULINARY_CAST = SOUND_EVENTS.register("culinary",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "culinary")));

    public static final RegistryObject<SoundEvent> EVASION = SOUND_EVENTS.register("evasion",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "evasion")));

    public static final RegistryObject<SoundEvent> VAN_SH_HIT = SOUND_EVENTS.register("van_sh_hit",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "van_sh_hit")));


    public static final RegistryObject<SoundEvent> CUTE_HIT = SOUND_EVENTS.register("cute_hit",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "cute_hit")));


    public static final RegistryObject<SoundEvent> BLOOD_BOSS_MUSIC = SOUND_EVENTS.register("battle_sanctuary",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Genesis.MOD_ID, "battle_sanctuary")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
