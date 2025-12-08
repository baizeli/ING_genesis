package com.baizeli.eternisstarrysky.Util;

import net.minecraft.network.chat.Component;

import static com.baizeli.eternisstarrysky.EternisStarrySky.MODID;

public class i18nUtil {



    public static Component translatableContainerName(String name) {
        return Component.translatable("container."+MODID+"."+name);
    }
}
