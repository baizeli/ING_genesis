package miku.united_as_one.genesis;

import dev.xkmc.l2library.base.L2Registrate;
import miku.united_as_one.genesis.config.ModConfigRegistration;
import miku.united_as_one.genesis.handlers.ModEventHandlers;
import miku.united_as_one.genesis.registries.ModRegistries;
import miku.united_as_one.genesis.registries.resource.ResourcePackRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(Genesis.MOD_ID)
public class Genesis {
    public static final String MOD_ID = "iron_spells_genesis";
    public static final String MODID = MOD_ID;
    public static final L2Registrate L2_REGISTRATE = new L2Registrate(MOD_ID);

    public Genesis(FMLJavaModLoadingContext context) {
        ResourcePackRegistry.registerOptionalTexturePack(Genesis.rl("genesis_old"), Component.literal("Genesis old"), false);
        ModRegistries.register(context.getModEventBus());
        ModEventHandlers.register();
        ModConfigRegistration.register(context);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String resource(String location) {
        return MOD_ID + ":" + location;
    }
}
