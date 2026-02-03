package miku.united_as_one.genesis.common.entity.ai;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static miku.united_as_one.genesis.Genesis.MODID;


public class ModActivity {
    public static final DeferredRegister<Activity> MOD_ACTIVITY =
            DeferredRegister.create(ForgeRegistries.Keys.ACTIVITIES, MODID);

    public static final RegistryObject<Activity> TEST_ACTIVITY =
            MOD_ACTIVITY.register("test_activity", () -> new Activity("test_activity"));

    public static void register(IEventBus modEventBus) {
        MOD_ACTIVITY.register(modEventBus);
    }
}
