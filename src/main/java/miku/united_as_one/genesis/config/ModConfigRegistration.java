package miku.united_as_one.genesis.config;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
public final class ModConfigRegistration {
    private ModConfigRegistration() {
    }

    public static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.SERVER, Configuration.SERVER_SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
    }
}
