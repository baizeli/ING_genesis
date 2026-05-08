package miku.united_as_one.genesis.api;

import net.minecraft.network.chat.Component;

import static miku.united_as_one.genesis.Genesis.MODID;

public class i18nUtil {
    public static Component translatableContainerName(String name) {
        return Component.translatable("container."+MODID+"."+name);
    }
}