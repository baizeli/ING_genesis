package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class ChaosMaterial extends Item {
    
    public ChaosMaterial(Properties properties) {
        super(properties);
    }
    
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @Nullable Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
                if (context == IClientItemExtensions.FontContext.SELECTED_ITEM_NAME || context == IClientItemExtensions.FontContext.TOOLTIP) {
                    String twistedchaos = I18n.get("item." + EternisStarrySky.MOD_ID + ".twisted_chaos");
                    return TextUtil.createFloatingGradientFont(
                        Minecraft.getInstance().font.fonts, 
                        true,
                        new int[]{0xFFFF0000, 0xFF8B0000/*, 0xFF0000FF*/},
                        2.0f,
                        0.1f,
                        0.1f,
                        twistedchaos
                        /*TextUtil.ALL_TEXTS*/
                    );
                }

                return null;
            }
        });

        super.initializeClient(consumer);
    }
}