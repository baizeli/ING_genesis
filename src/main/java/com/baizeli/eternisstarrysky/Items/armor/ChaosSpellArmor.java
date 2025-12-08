package com.baizeli.eternisstarrysky.Items.armor;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChaosSpellArmor extends ExtendedArmorItem {
    public ChaosSpellArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.EPIC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new GenericArmorModel<>(EternisStarrySky.MOD_ID, "chaos_spell"));
    }
}